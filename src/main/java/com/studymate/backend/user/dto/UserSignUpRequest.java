package com.studymate.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 10)
    private String password;

    @NotBlank
    private String name;
}
