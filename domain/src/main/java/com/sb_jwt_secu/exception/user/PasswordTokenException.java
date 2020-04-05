package com.sb_jwt_secu.exception.user;

import com.sb_jwt_secu.exception.BadRequestException;

public class PasswordTokenException extends BadRequestException {

    public PasswordTokenException(String message) {
        super(message);
    }

    public PasswordTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}
