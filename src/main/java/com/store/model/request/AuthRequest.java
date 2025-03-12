package com.store.model.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AuthRequest {
    private String username;
    private String password;
}
