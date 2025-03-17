package com.store.cart.service.impl;

import com.store.auth.entity.User;
import com.store.auth.repository.UserRepository;
import com.store.cart.entity.Cart;
import com.store.cart.entity.CartItem;
import com.store.cart.model.mapper.CartMapper;
import com.store.cart.model.request.CartItemRequest;
import com.store.cart.model.response.CartResponse;
import com.store.cart.repository.CartRepository;
import com.store.cart.service.CartService;
import com.store.product.discount.DiscountRuleEngine;
import com.store.product.entity.Discount;
import com.store.product.entity.Product;
import com.store.product.repository.DiscountRepository;
import com.store.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final CartMapper cartMapper; // Use MapStruct to map DTOs

    private final UserRepository userRepository;

    private final DiscountRuleEngine discountRuleEngine;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository,
                           DiscountRepository discountRepository, CartMapper cartMapper, UserRepository userRepository,
                           DiscountRuleEngine discountRuleEngine) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.cartMapper = cartMapper;
        this.userRepository = userRepository;
        this.discountRuleEngine = discountRuleEngine;
    }

    @Override
    public CartResponse getCart() {
        Cart cart = cartRepository.findByUser(getCurrentUser())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse checkout(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            /*BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            // Apply discount if exists
            if (!product.getDiscounts().isEmpty()) {
                for (ProductDiscount pd : product.getDiscounts()) {
                    itemTotal = applyDiscount(itemTotal, pd.getDiscount());
                }
            }

//            item.setTotalPrice(itemTotal);
            total = total.add(itemTotal);*/


            BigDecimal itemBasePrice = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            // Let the rule engine apply the correct discount
            BigDecimal discountedTotal = discountRuleEngine.applyDiscounts(item, itemBasePrice);

            total = total.add(discountedTotal);
        }

        cart.setTotalPrice(total);
        cartRepository.save(cart);

        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse addProductsToCart(List<CartItemRequest> items) {
        Cart cart =Cart.builder().user(getCurrentUser()).totalPrice(BigDecimal.ZERO).build();

        for (CartItemRequest itemRequest : items) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.getProductId()));

            List<CartItem> cartItemList = cart.getItems();
            if (cartItemList == null) {
                cartItemList = new ArrayList<>();
                cart.setItems(cartItemList);
            }
            // Find existing item or create a new one
            CartItem cartItem = cartItemList.stream()
                    .filter(ci -> ci.getProduct() != null && ci.getProduct().equals(product))
                    .findFirst()
                    .orElse(CartItem.builder()
                            .product(product)
                            .quantity(0)
                            .cart(cart)
                            .build());

            // Update quantity
            cartItem.setQuantity(cartItem.getQuantity() + itemRequest.getQuantity());

            // Ensure item is in cart’s item list
            if (!cartItemList.contains(cartItem)) {
                cartItemList.add(cartItem);
            }
        }

        cartRepository.save(cart);

        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse removeProductsFromCart(Long cartId, List<CartItemRequest> items) {
        // 1) Fetch user's cart or throw an exception if not found
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // 2) For each item in the list, reduce quantity or remove completely
        for (CartItemRequest itemReq : items) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));

            // Find the cart item that matches the product
            CartItem existingItem = cart.getItems().stream()
                    .filter(ci -> ci.getProduct().equals(product))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                int newQuantity = existingItem.getQuantity() - itemReq.getQuantity();

                if (newQuantity <= 0) {
                    // If new quantity is zero or negative, remove the item entirely
                    cart.getItems().remove(existingItem);
                } else {
                    // Otherwise, update the quantity
                    existingItem.setQuantity(newQuantity);
                }
            }
        }
        cart.setTotalPrice(BigDecimal.ZERO);
        // 3) Save the updated cart
        cartRepository.save(cart);

        // 4) Return the updated cart as a response
        return cartMapper.toResponse(cart);
    }

    private BigDecimal applyDiscount(BigDecimal price, Discount discount) {
        if (discount.getPercentage() > 0) {
            return price.multiply(BigDecimal.valueOf((100 - discount.getPercentage()) / 100.0));
        }
        return price;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    /*@Override
    public void addToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(null, new ArrayList<>(), new User()));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        cart.getCartItems().add(item);

        cartRepository.save(cart);
    }

    @Override
    public Receipt calculateReceipt(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem item : cart.getCartItems()) {
            BigDecimal itemPrice = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            // Apply discounts if available
            for (ProductDiscount discount : item.getProduct().getDiscounts()) {
                if (item.getQuantity() >= discount.getDiscount().getQuantityRequired()) {
                    BigDecimal discountAmount = item.getProduct().getPrice()
                            .multiply(discount.getDiscount().getDiscountPercentage());
                    itemPrice = itemPrice.subtract(discountAmount);
                }
            }

            totalPrice = totalPrice.add(itemPrice);
        }

        return new Receipt(cart.getCartItems(), totalPrice);
    }*/
}
