package com.sb_jwt_secu.rest.controller.user;

import com.sb_jwt_secu.model.user.CustomUser;
import com.sb_jwt_secu.rest.apiResponse.ApiResponse;
import com.sb_jwt_secu.rest.apiResponse.user.ApiResponsePassword;
import com.sb_jwt_secu.rest.dto.user.ResetPasswordDTO;
import com.sb_jwt_secu.rest.dto.user.ResetPasswordWithTokenDTO;
import com.sb_jwt_secu.rest.dto.user.TokenResetPasswordDTO;
import com.sb_jwt_secu.rest.utils.mail.MailUtil;
import com.sb_jwt_secu.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/password")
@Slf4j
public class PasswordResetController {


    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private JavaMailSender javaMailSender;

    public PasswordResetController(
            PasswordEncoder passwordEncoder,
            UserService userService,
            JavaMailSender javaMailSender
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.javaMailSender = javaMailSender;
    }

    @PostMapping(value = "/resetPassword")
    @ResponseBody
    public ApiResponse resetPassword(
            HttpServletRequest request,
            @Valid @RequestBody ResetPasswordWithTokenDTO resetPasswordWithTokenDTO
    ) {
        CustomUser user = userService.loadUserByUsername(resetPasswordWithTokenDTO.getEmail());

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        javaMailSender.send(
                MailUtil.INSTANCE.constructResetTokenEmail(
                    resetPasswordWithTokenDTO.getClientResetPasswordUrl(),
                    token,
                    user
                )
        );

        return ApiResponsePassword.builder()
                .success(true)
                .message(
                       "well send"
                ).build();
    }

    @GetMapping("/checkToken")
    @ResponseBody
    public ApiResponse showChangePasswordPage(
            @RequestParam("id") long id,
            @RequestParam("token") String token) {
        userService.validatePasswordResetToken(id, token,false);
        return new ApiResponse(true, "Valid Token");
    }


    @PostMapping("/changePasswordWithToken")
    @ResponseBody
    public ApiResponse showChangePasswordPage(
            @Valid @RequestBody TokenResetPasswordDTO tokenResetPasswordDTO
    ) {
        userService.validatePasswordResetToken(
                tokenResetPasswordDTO.getId(),
                tokenResetPasswordDTO.getToken(),
                true
        );
        savePassword(tokenResetPasswordDTO.getNewPassword());
        return new ApiResponse(true, "Password Changed Successfully");
    }

    @PostMapping("/savePassword")
    @ResponseBody
    public ApiResponsePassword savePassword(@Valid ResetPasswordDTO passwordDto) {
        savePassword(passwordDto);
        return ApiResponsePassword.builder().success(true).message("Password changed Successfully").build();
    }

    private void savePassword(String password){
        CustomUser user = userService.getLoggedUser();
        user.setPassword( passwordEncoder.encode( password ) );
        userService.saveAndDeleteToken(user);

    }

}
