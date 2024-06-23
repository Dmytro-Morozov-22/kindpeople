package com.dmtrmrzv.kindpeople.controllers;

import com.dmtrmrzv.kindpeople.dto.UserDTO;
import com.dmtrmrzv.kindpeople.entities.User;
import com.dmtrmrzv.kindpeople.facade.UserFacade;
import com.dmtrmrzv.kindpeople.services.UserService;
import com.dmtrmrzv.kindpeople.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {

    private final UserService userService;

    private final UserFacade userFacade;

    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public UserController(UserService userService, UserFacade userFacade, ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId) {
        User user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        ResponseEntity<Object> response;
        if (!ObjectUtils.isEmpty(errors)) {
            response = errors;
        } else {
            User user = userService.updateUser(userDTO, principal);
            UserDTO userUpdated = userFacade.userToUserDTO(user);
            response = new ResponseEntity<>(userUpdated, HttpStatus.OK);
        }
        return response;
    }





}
