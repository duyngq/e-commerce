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
import com.store.product.entity.Discount;
import com.store.product.entity.Product;
import com.store.product.entity.ProductDiscount;
import com.store.product.repository.DiscountRepository;
import com.store.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartServiceImpl implements CartService {
//    @Autowired
//    private CartRepository cartRepository;
//
//    @Autowired
//    private ProductRepository productRepository;

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final CartMapper cartMapper; // Use MapStruct to map DTOs

    private final UserRepository userRepository;
    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository,
                           DiscountRepository discountRepository, CartMapper cartMapper, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.cartMapper = cartMapper;
        this.userRepository = userRepository;
    }

    @Override
    public CartResponse addToCart(CartItemRequest request) {
        Cart cart = cartRepository.findByUser(getCurrentUser()).orElse(new Cart());
        cart.setUser(getCurrentUser());

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElse(new CartItem());

        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        cart.getItems().add(cartItem);
        cartRepository.save(cart);

        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse removeFromCart(CartItemRequest request) {
        Cart cart = cartRepository.findByUser(getCurrentUser())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(request.getProductId()));

        cartRepository.save(cart);
        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse getCart() {
        Cart cart = cartRepository.findByUser(getCurrentUser())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse checkout() {
        Cart cart = cartRepository.findByUser(getCurrentUser())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            // Apply discount if exists
            if (!product.getDiscounts().isEmpty()) {
                for (ProductDiscount pd : product.getDiscounts()) {
                    itemTotal = applyDiscount(itemTotal, pd.getDiscount());
                }
            }

//            item.setTotalPrice(itemTotal);
            total = total.add(itemTotal);
        }

        cart.setTotalPrice(total);
        cartRepository.save(cart);

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
