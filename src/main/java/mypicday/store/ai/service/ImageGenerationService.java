package mypicday.store.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.file.FileUtil;
import mypicday.store.global.dto.RequestMetaInfo;
import mypicday.store.global.util.ImagePathToUrlConverter;
import mypicday.store.user.entity.User;
import mypicday.store.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageGenerationService {

    private final ImagePathToUrlConverter imagePathToUrlConverter;
    private final GeminiApiClient geminiApiClient;
    private final DiaryImageGenerationService diaryImageGenerationService;
    private final UserService userService;
    private final FileUtil fileUtil;

    public String diaryImage(String content, String userId, RequestMetaInfo requestMetaInfo) {
        log.info("사용자 ID '{}' 일기 이미지 생성 요청", userId);

        User user = userService.getUserById(userId);
        String avatarPath = user.getAvatar();

        String characterImageBase64 = fileUtil.getImageAsBase64(avatarPath);
        if (characterImageBase64 == null || characterImageBase64.trim().isEmpty()) {
            log.error("사용자 '{}'의 아바타 이미지 파일을 읽을 수 없습니다. 경로: {}", userId, avatarPath);
            throw new IllegalArgumentException("사용자 아바타 이미지 파일을 읽을 수 없습니다. 경로: " + avatarPath);
        }
        log.info("사용자 '{}'의 아바타 이미지 로드 완료. Base64 길이: {}", userId, characterImageBase64.length());


        // 멀티모달 프롬프트 생성
        String prompt = buildActionPrompt(content);
        log.info("일기 프롬프트 생성 완료: {}", prompt);

        String generatedImageRelativePath = generateCharacterActionImage(prompt, characterImageBase64, userId);
        if (generatedImageRelativePath == null) {
            throw new RuntimeException("Gemini API에서 이미지 생성을 실패했거나 파일 저장에 실패했습니다.");
        }

        // CalendarController
        String imageUrl = imagePathToUrlConverter.diaryImageUrl(generatedImageRelativePath, requestMetaInfo);
        diaryImageGenerationService.save(userId, prompt, generatedImageRelativePath);
        log.info("생성된 이미지 URL: {}", imageUrl);
        return imageUrl;
    }

    /**
     * 캐릭터 행위 이미지 생성 (사용자 아바타 기반 + 텍스트)
     * 사용자의 아바타 이미지를 기반으로 요청된 행위를 하는 이미지를 생성합니다.
     * @param content 일기 내용
     * @param userId 사용자 ID
     * @return 생성된 이미지 URL
     */
    public String generateCharacterAction(String content, String userId) {
        log.info("사용자 ID '{}'일기 사진 요청", userId);

        User user = userService.getUserById(userId);
        String avatarPath = user.getAvatar();

        String characterImageBase64 = fileUtil.getImageAsBase64(avatarPath);
        if (characterImageBase64 == null || characterImageBase64.trim().isEmpty()) {
            log.error("사용자 '{}'의 아바타 이미지 파일을 읽을 수 없습니다. 경로: {}", userId, avatarPath);
            throw new IllegalArgumentException("사용자 아바타 이미지 파일을 읽을 수 없습니다. 경로: " + avatarPath);
         }
        log.info("사용자 '{}'의 아바타 이미지 로드 완료. Base64 길이: {}", userId, characterImageBase64.length());

        String generatedImageRelativePath = generateCharacterActionImage(content, characterImageBase64, userId);
        if (generatedImageRelativePath == null) {
            throw new RuntimeException("Gemini API에서 이미지 생성을 실패했거나 파일 저장에 실패했습니다.");
        }
        return generatedImageRelativePath; // "userId/filename.png" 형태의 경로 반환
    }

    /**
     * MultipartFile을 Base64로 변환 (기존 메서드, 참고용으로 남겨두나 직접 사용 X)
     * FileUtil.getImageAsBase64 또는 직접 파일 바이트 읽기로 대체
     */
    private String convertImageToBase64(MultipartFile image) throws IOException {
        // 이 메서드는 이제 직접 사용되지 않음. FileUtil.getImageAsBase64 또는 파일 직접 읽기로 대체.
        // 만약 MultipartFile을 직접 받아 처리해야 하는 경우가 다시 생긴다면 FileUtil에 유사 기능 구현 고려.
        byte[] imageBytes = image.getBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    /**
     * 캐릭터 행위 이미지 생성 (Gemini API 멀티모달)
     * 생성된 이미지의 저장 경로 (userId/filename.png)를 반환합니다.
     */
    private String generateCharacterActionImage(String prompt, String characterImageBase64, String userId) {
        log.info("Gemini API 호출 (멀티모달): 사용자 ID: {}", userId);

        try {
            // Gemini API 멀티모달 호출 (이미지 + 텍스트)
            String base64ImageData = geminiApiClient.editImage(characterImageBase64, prompt).block();

            if (base64ImageData != null && !base64ImageData.isEmpty()) {
                // FileUtil을 사용하여 Base64 데이터를 파일로 저장
                // fileUtil.saveBase64AsFile은 "userId/filename.ext" 형태의 경로 반환
                String savedFilePathSuffix = fileUtil.saveBase64AsFile(base64ImageData, userId, "png");
                log.info("Gemini API 행위 이미지 생성 및 저장 성공: 사용자 ID: {}, 경로: {}", userId, savedFilePathSuffix);
                return savedFilePathSuffix; // "userId/filename.png" 반환
            } else {
                log.error("Gemini API에서 이미지 데이터를 받지 못했습니다. userId: {}", userId);
                throw new RuntimeException("Gemini API에서 이미지 데이터를 받지 못했습니다.");
            }

        } catch (Exception e) {
            log.error("Gemini API 일기 이미지 생성 중 오류 발생: 사용자 ID: {},", userId, e);
            throw new RuntimeException("일기 이미지 생성 실패: " + e.getMessage());
        }
    }

    /**
     * 액션 프롬프트 구성
     */
    private String buildActionPrompt(String content) {
        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder.append("Based on the provided character image, ");
        promptBuilder.append("generate an image where the character is performing the following action: ");
        promptBuilder.append(content);


        promptBuilder.append(". Additional instructions: ");
        promptBuilder.append(". Please generate it as an artistic and visually appealing scene.");
        promptBuilder.append(". Maintain consistency with the character's appearance and style, and generate the image with high quality.");
        return promptBuilder.toString();
    }
}

