package com.winwin.auth_api.controller;


import com.winwin.auth_api.dto.AuthenticatedUser;
import com.winwin.auth_api.dto.ProcessRequest;
import com.winwin.auth_api.dto.ProcessResponse;
import com.winwin.auth_api.service.ProcessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProcessController {

    private final ProcessService processService;

    @PostMapping("/process")
    public ResponseEntity<ProcessResponse> process(
            @Valid @RequestBody ProcessRequest request,
            Authentication authentication
    ) {

        AuthenticatedUser principal = (AuthenticatedUser) authentication.getPrincipal();

        String result = processService.process(principal.email(), request.text());

        return ResponseEntity.ok(new ProcessResponse(result));
    }
}