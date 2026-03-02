package com.winwin.auth_api.dto;

import jakarta.validation.constraints.NotBlank;

public record ProcessRequest(
        @NotBlank(message = "Text must not be blank")
        String text
) {}