package com.example.normalizer.controller;

import com.example.normalizer.model.input.ProviderAlphaRequest;
import com.example.normalizer.model.input.ProviderBetaRequest;
import com.example.normalizer.service.MessageNormalizerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FeedController {

    private final MessageNormalizerService messageNormalizerService;

    public FeedController(MessageNormalizerService messageNormalizerService) {
        this.messageNormalizerService = messageNormalizerService;
    }

    @PostMapping("/provider-alpha/feed")
    public ResponseEntity<Map<String, String>> handleProviderAlpha(@RequestBody ProviderAlphaRequest body) {
        String msgType = body.getMsgType();
        String eventId = body.getEventId();

        if (msgType == null || eventId == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Missing required fields: msg_type or event_id"));
        }

        try {
            messageNormalizerService.processProviderAlpha(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("odds", "Invalid or missing fields: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(500)
                    .body(Map.of("error", "Internal error: " + e.getMessage()));
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/provider-beta/feed")
    public ResponseEntity<Map<String, String>> handleProviderBeta(@RequestBody ProviderBetaRequest body) {
        String type = body.getMsgType();
        String eventId = body.getEventId();

        if (type == null || eventId == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Missing required fields: msg_type or event_id"));
        }

        try {
            messageNormalizerService.processProviderBeta(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("odds", "Invalid or missing fields: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(500)
                    .body(Map.of("error", "Internal error: " + e.getMessage()));
        }

        return ResponseEntity.ok().build();


    }
}
