package com.store.model.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserRequest {
    String username;
    String password;
    String role;
}
