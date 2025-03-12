package com.store.exception;

import com.store.exception.localization.LocalizationException;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class ApplicationException extends RuntimeException {

    @Getter
    private final List<LocalizationException> exceptions;

    public ApplicationException(List<LocalizationException> exceptions) {
        super(exceptions.toString());
        this.exceptions = exceptions;
    }

    public ApplicationException(LocalizationException exception) {
        this(Arrays.asList(exception));
    }
}
