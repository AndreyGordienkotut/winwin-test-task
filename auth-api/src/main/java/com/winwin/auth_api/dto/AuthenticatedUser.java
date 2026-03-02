package com.winwin.auth_api.dto;

import java.util.UUID;

public record AuthenticatedUser(
        UUID userId,
        String email
) {}