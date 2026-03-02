package com.winwin.data_api.service;

import org.springframework.stereotype.Service;

@Service
public class TransformService {
    public String transform(String input) {
        return input.toUpperCase();
    }
}
