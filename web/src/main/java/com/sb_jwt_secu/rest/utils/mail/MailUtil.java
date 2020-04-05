package com.sb_jwt_secu.rest.utils.mail;

import com.sb_jwt_secu.model.user.CustomUser;
import org.springframework.mail.SimpleMailMessage;

public enum MailUtil {

    INSTANCE;
     public static final String RESET_MAIL_URL= "%s?id=%s&token=%s";

    public SimpleMailMessage constructResetTokenEmail(String clientResetUrl,String token, CustomUser user) {
        String url = String.format(RESET_MAIL_URL,clientResetUrl,user.getId(),token);
        String message = "Change password";
        return constructEmail("Reset Password", message + " \r\n " + url, user);
    }

    public SimpleMailMessage constructEmail(String subject, String body,
                                             CustomUser user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        return email;

    }
}
