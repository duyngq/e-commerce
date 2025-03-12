package com.store.exception;

import com.store.exception.localization.LocalizationException;

public class EntityNotFoundException extends ApplicationException {

    public EntityNotFoundException(LocalizationException exception) {
        super(exception);
    }
}
