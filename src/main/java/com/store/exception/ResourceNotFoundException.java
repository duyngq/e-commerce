package com.store.exception;

import com.store.exception.localization.LocalizationException;

import java.util.List;

public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(List<LocalizationException> exceptions) {
        super(exceptions);
    }
}
