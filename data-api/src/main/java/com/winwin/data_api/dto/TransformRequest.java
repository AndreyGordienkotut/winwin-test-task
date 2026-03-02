package com.winwin.data_api.dto;

import jakarta.validation.constraints.NotBlank;

public record TransformRequest(
        @NotBlank(message = "Text must not be blank")
        String text
) {
}