package ru.javawebinar.topjava.util.exception;

import ua.training.top.util.exception.ApplicationException;
import ua.training.top.util.exception.ErrorType;

public class UpdateRestrictionException extends ApplicationException {
    public static final String EXCEPTION_UPDATE_RESTRICTION = "exception.user.updateRestriction";

    public UpdateRestrictionException() {
        super(EXCEPTION_UPDATE_RESTRICTION, ErrorType.VALIDATION_ERROR);
    }
}
