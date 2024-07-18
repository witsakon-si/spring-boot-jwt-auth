package com.jwtauth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.jwtauth.utils.ApplicationConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final HazelcastInstance hz;
    private final ApplicationConstant appConstant;
    private final ObjectMapper objectMapper;

    @GetMapping("/tokenCache")
    public ResponseEntity<String> getTokenCache() throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(hz.getMap(appConstant.TOKEN_CACHE)));
    }
}
