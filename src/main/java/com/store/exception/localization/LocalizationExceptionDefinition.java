package com.store.exception.localization;

import com.store.security.entity.User;

public class LocalizationExceptionDefinition {
    public static final String APP = "ElectronicStore";
    public static final String USER = User.class.getSimpleName();

    public static final LocalizationException APP_ENTITY_ID_EMPTY_EXCEPTION = new LocalizationException(APP + ".Id.01", "Entity Id is empty.");
    public static final LocalizationException APP_ENTITY_NOT_FOUND_EXCEPTION = new LocalizationException(APP + ".02", "Entity not found.");
    public static final LocalizationException USER_NOT_FOUND_EXCEPTION = new LocalizationException(USER + ".01", "Invalid username or password.");

}
