package com.winwin.auth_api.service;

import com.winwin.auth_api.dto.TransformResponse;
import com.winwin.auth_api.model.ProcessingLog;
import com.winwin.auth_api.model.User;
import com.winwin.auth_api.repository.ProcessingLogRepository;
import com.winwin.auth_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProcessService {
    private final RestTemplate restTemplate;
    private final ProcessingLogRepository processingLogRepository;
    private final UserRepository userRepository;

    @Value("${internal.token}")
    private String internalToken;

    public String process(String email, String text) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("text", text);

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<TransformResponse> response =
                restTemplate.postForEntity(
                        "http://data-api:8081/api/transform",
                        request,
                        TransformResponse.class
                );

        String result = response.getBody().result();

        ProcessingLog log = new ProcessingLog();
        log.setUser(user);
        log.setInputText(text);
        log.setOutputText(result);
        log.setCreatedAt(LocalDateTime.now());

        processingLogRepository.save(log);

        return result;
    }
}
