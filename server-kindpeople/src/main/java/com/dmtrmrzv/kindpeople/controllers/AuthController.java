package com.dmtrmrzv.kindpeople.controllers;

import com.dmtrmrzv.kindpeople.payload.request.LoginRequest;
import com.dmtrmrzv.kindpeople.payload.request.SignupRequest;
import com.dmtrmrzv.kindpeople.payload.response.JWTTokenSuccessResponse;
import com.dmtrmrzv.kindpeople.payload.response.MessageResponse;
import com.dmtrmrzv.kindpeople.security.JWTTokenProvider;
import com.dmtrmrzv.kindpeople.security.SecurityConstants;
import com.dmtrmrzv.kindpeople.services.UserService;
import com.dmtrmrzv.kindpeople.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        //FIXME in this method is used two 'return' statements. It is needed to update the method to use one 'return statement'
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
              loginRequest.getUsername(),
              loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        //FIXME in this method is used two 'return' statements. It is needed to update the method to use one 'return statement'
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return  errors;

        userService.createUser(signupRequest);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

}
