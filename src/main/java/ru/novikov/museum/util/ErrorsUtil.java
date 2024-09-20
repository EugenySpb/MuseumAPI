package ru.novikov.museum.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorsUtil {
    private static String buildErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMsg.append(error.getField())
                    .append(" - ").append(error.getDefaultMessage() == null ? error.getCode() : error.getDefaultMessage())
                    .append(";");
        }
        return errorMsg.toString();
    }

    public static void returnErrorsEventToClient(BindingResult bindingResult) {
        String errorMsg = buildErrorMessage(bindingResult);
        throw new EventNotCreatedException(errorMsg);
    }

    public static void returnErrorsBookingToClient(BindingResult bindingResult) {
        String errorMsg = buildErrorMessage(bindingResult);
        throw new BookingNotCreatedException(errorMsg);
    }

    public static void returnErrorsAdminToClient(BindingResult bindingResult) {
        String errorMsg = buildErrorMessage(bindingResult);
        throw new AdminNotCreatedException(errorMsg);
    }
}