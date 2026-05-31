package com.studymate.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserCreateRequest {

    @NotBlank // NotEmpty -> null과 ""은 막히는데 " "은 허용 가능 반면에 NotBlank는 모든것이 다 막힘
    private String name;

    @Email
    @NotBlank
    private String email;



}
