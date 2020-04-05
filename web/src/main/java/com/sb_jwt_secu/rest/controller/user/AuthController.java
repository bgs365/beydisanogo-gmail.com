package com.sb_jwt_secu.rest.controller.user;

import com.config.spring.JwtTokenProvider;
import com.sb_jwt_secu.model.user.CustomUser;
import com.sb_jwt_secu.model.user.Role;
import com.sb_jwt_secu.model.user.RoleName;
import com.sb_jwt_secu.rest.apiResponse.ApiResponse;
import com.sb_jwt_secu.rest.apiResponse.user.*;
import com.sb_jwt_secu.rest.dto.user.*;
import com.sb_jwt_secu.service.user.RoleService;
import com.sb_jwt_secu.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
                          RoleService roleService, PasswordEncoder passwordEncoder,
                          JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/authUser")
    public ResponseEntity<CustomUser> getUserData() {
        return ResponseEntity.ok(userService.getLoggedUser());
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        JwtAuthenticationResponse response = new JwtAuthenticationResponse(jwt);
        if(!StringUtils.isEmpty(jwt)){
            response.setUser(userService.loadUserByUsername(loginRequest.getUsernameOrEmail()));
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CustomRegisterUserDTO customRegisterUserDTO) {

        ApiUserConnectedDataResponse apireponse = verifUsernameOrEmailExistance(customRegisterUserDTO, Optional.empty() );
        if(apireponse.getSuccess() == false ){
            return new ResponseEntity( apireponse,HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        CustomUser customUser = new CustomUser(
                customRegisterUserDTO.getUsername(), customRegisterUserDTO.getPassword(),
                customRegisterUserDTO.getEmail(), customRegisterUserDTO.getFirstName(),
                customRegisterUserDTO.getLastName(),
                Collections.singleton(roleService.findRole(RoleName.ROLE_USER).get())
        );

        customUser.setPassword(passwordEncoder.encode(customUser.getPassword()));

        customUser.setSubscriptionDate(LocalDateTime.now());

        CustomUser result = userService.save(customUser);

        return ResponseEntity.created(createLocation(result)).body(
                apireponse.builder()
                        .success(true)
                        .message("User registered successfully")
                        .emailTaken(false)
                        .usernameTaken(false)
                        .build()
        );
    }

    @PutMapping("/updateAuthUserData")
    public ResponseEntity<?> updateUser(@Valid @RequestBody CustomUpdateUserDTO customUpdateUserDTO){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user =  userService.loadUserByUsername(authentication.getName());

        updateUserData(user, customUpdateUserDTO);

        CustomUser result = userService.save(user);

        return ResponseEntity.created(createLocation(result)).body(
                ApiUserConnectedDataResponse.builder()
                        .success(true)
                        .message("User Update successfully")
                        .emailTaken(false)
                        .usernameTaken(false)
                        .build()
        );
    }

    @PutMapping("/updateAuthUserPassword")
    public ResponseEntity<ApiResponsePassword> updateAuthUserPassword(@Valid @RequestBody AuthUserPasswordDTO authUserPasswordDTO){

        if(authUserPasswordDTO.getOldPassword().equals(authUserPasswordDTO.getNewPassword())){
            return ResponseEntity.badRequest().body( ApiResponsePassword.builder()
                    .success(false)
                    .message("Old and new password are same")
                    .old_and_new_password_are_same(true)
                    .build()
            );
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user =  userService.loadUserByUsername(authentication.getName());

        try {
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    user.getUsername(),
                                    authUserPasswordDTO.getOldPassword()
                            )
                    );

            user.setPassword(passwordEncoder.encode(authUserPasswordDTO.getNewPassword()));

            CustomUser result = userService.save(user);

            return ResponseEntity.created(createLocation(result)).body(
                    ApiResponsePassword.builder()
                            .success(true)
                            .message("User password updated successfully")
                            .build()
            );

        }catch(Exception e) {
            return ResponseEntity.badRequest().body( ApiResponsePassword.builder()
                    .success(false)
                    .message("Old password is wrong")
                    .wrong_old_password(true)
                    .build()
            );
        }

    }

    @PutMapping("/updateAuthUserUsername")
    public ResponseEntity<ApiResponse> updateAuthUserUsername(@Valid @RequestBody AuthUserUsername authUserUsername){

        if(userService.existsByUsername(authUserUsername.getUsername())){
            return ResponseEntity.badRequest().body(
                    ApiResponseUserUsername.builder()
                    .success(false)
                    .message("Username is already taken")
                    .build()
            );
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user =  userService.loadUserByUsername(authentication.getName());

        user.setUsername(authUserUsername.getUsername());

        CustomUser result = userService.save(user);

        return ResponseEntity.created(createLocation(result)).body(
                ApiResponseUserUsername.builder()
                        .success(true)
                        .message("User username updated successfully")
                        .username(user.getUsername())
                        .build()
        );

    }

    @PutMapping("/updateAuthUserEmail")
    public ResponseEntity<ApiResponse> updateAuthUserEmail(@Valid @RequestBody AuthUserEmail authUserEmail){


        if(userService.existsByEmail(authUserEmail.getEmail())){
            return ResponseEntity.badRequest().body(
            ApiResponseUserEmail.builder()
                    .success(false)
                    .message("Email is already taken")
                    .build()
            );
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user =  userService.loadUserByUsername(authentication.getName());

        user.setEmail(authUserEmail.getEmail());

        CustomUser result = userService.save(user);

        return ResponseEntity.created(createLocation(result)).body(
                ApiResponseUserEmail.builder()
                        .success(true)
                        .message("User password updated successfully")
                        .email(user.getEmail())
                        .build()
        );

    }


    private URI createLocation(CustomUser customUser){
        return ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(customUser.getUsername()).toUri();
    }

    private void updateUserData(CustomUser oldData, CustomUpdateUserDTO newData){
        oldData.setLastName( newData.getLastName() );
        oldData.setFirstName( newData.getFirstName() );
        oldData.setBirthDate( newData.getBirthDate() );

        oldData.setGender( newData.getGender() );

        if( newData.getRoles()!= null && !newData.getRoles().isEmpty() ){
            Set<Role> roles = newData.getRoles().stream()
                    .map( roleName ->  roleService.findRole(roleName).get() )
                    .collect(Collectors.toSet());
            oldData.setRoles( roles );
        }

    }

    private ApiUserConnectedDataResponse verifUsernameOrEmailExistance(CustomRegisterUserDTO customRegisterUserDTO, Optional<CustomUser> user){
        ApiUserConnectedDataResponse apiResponse = ApiUserConnectedDataResponse.builder()
                .success(true)
                .emailTaken(false)
                .usernameTaken(false)
                .build();

        if(user.isPresent()){
            apiResponse.setMessage("Update fail : ");

            if( ! customRegisterUserDTO.getUsername().equals(user.get().getUsername())){
                verifUserName(customRegisterUserDTO.getUsername(),apiResponse);
            }

            if(! customRegisterUserDTO.getEmail().equals( user.get().getEmail() )){
                verifEmail(customRegisterUserDTO.getEmail(),apiResponse);
            }

        }else{
            apiResponse.setMessage("Creation fail : ");

            verifUserName(customRegisterUserDTO.getUsername(),apiResponse);
            verifEmail(customRegisterUserDTO.getEmail(),apiResponse);
        }


        return apiResponse;
    }

    private void verifUserName(String username,ApiUserConnectedDataResponse apiResponse){
        if(userService.existsByUsername(username)) {
            apiResponse.setUsernameTaken(true);
            apiResponse.setMessage(apiResponse.getMessage() + (" Username is already taken "));
            apiResponse.setSuccess(false);
        }
    }

    private void verifEmail(String email,ApiUserConnectedDataResponse apiResponse){
        if(userService.existsByEmail(email)) {
            apiResponse.setEmailTaken(true);
            apiResponse.setMessage(apiResponse.getMessage() + (" Email is already taken "));
            apiResponse.setSuccess(false);
        }
    }


}