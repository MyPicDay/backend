package mypicday.store.ai.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.ai.service.ImageGenerationService;
import mypicday.store.global.config.CustomUserDetails;
import mypicday.store.global.dto.RequestMetaInfo;
import mypicday.store.global.util.RequestMetaMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AiGeneratorController {

    private final ImageGenerationService imageGenerationService;
    private final RequestMetaMapper requestMetaMapper;

    @PostMapping("/diary/ai/generate")
    public ResponseEntity<String> generateDiaryImage(@RequestBody Map<String, String> body, HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String content = body.get("content");
        String userId = customUserDetails.getId();


        RequestMetaInfo requestMetaInfo = requestMetaMapper.extractMetaInfo(request);
        String imageUrl = imageGenerationService.diaryImage(content, userId, requestMetaInfo);
        log.info("Generated image URL: {}", imageUrl);
        return ResponseEntity.ok(imageUrl);
    }
}
