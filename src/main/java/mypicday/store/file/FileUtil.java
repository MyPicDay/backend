package mypicday.store.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import mypicday.store.character.enums.CharacterCreationType;

@Slf4j
@Component
public class FileUtil {
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    /**
     * 원본 파일명을 기반으로 유니크한 파일명을 생성합니다.
     * 형식: yyyyMMdd_HHmmss_원본파일명(확장자제외)_UUID8자리.확장자
     * @param originalFilename 원본 파일명
     * @return 생성된 유니크한 파일명
     */
    private String generateUniqueFileName(String originalFilename) {
        String cleanOriginalFilename = StringUtils.cleanPath(originalFilename);
        String timeStamp = LocalDateTime.now().format(TIME_FORMATTER);
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        String nameWithoutExt = cleanOriginalFilename;
        String extension = "";
        int lastDotIndex = cleanOriginalFilename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            nameWithoutExt = cleanOriginalFilename.substring(0, lastDotIndex);
            extension = cleanOriginalFilename.substring(lastDotIndex);
        }
        return timeStamp + "_" + nameWithoutExt + "_" + uuid + extension;
    }
    
    /**
     * 사용자별 업로드 디렉토리 경로 생성
     * @param userId 사용자 ID
     * @return 사용자별 업로드 디렉토리 경로
     */
    private Path getUserUploadDir(String userId) {
        return Paths.get(FileConstants.UPLOADS_BASE_DIR_NAME).resolve(userId);
    }
    
    /**
     * 파일을 사용자별 폴더에 저장하고 "userId/실제파일명" 형태로 반환
     * @param file 저장할 파일
     * @param userId 사용자 ID
     * @return "userId/실제파일명" 형태의 저장된 파일 경로
     */
    public String saveFile(MultipartFile file, String userId) {
        try {
            Path userUploadDir = getUserUploadDir(userId);
            if (Files.notExists(userUploadDir)) {
                Files.createDirectories(userUploadDir);
            }
            
            if (file.isEmpty()) {
                throw new IllegalArgumentException("빈 파일은 저장할 수 없습니다.");
            }
            
            String actualFileName = generateUniqueFileName(Objects.requireNonNull(file.getOriginalFilename()));
            Path filePath = userUploadDir.resolve(actualFileName);
            
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("파일 저장 완료 - 사용자: {}, 파일: {}", userId, actualFileName);
            return userId + "/" + actualFileName; 
            
        } catch (IOException e) {
            log.error("파일 저장 실패 - 사용자: {}, 오류: {}", userId, e.getMessage());
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }
    }
    
    /**
     * 여러 파일을 한번에 저장하고 "userId/실제파일명" 리스트로 반환
     * @param files 저장할 파일들
     * @param userId 사용자 ID
     * @return "userId/실제파일명" 형태의 저장된 파일 경로 리스트
     */
    public List<String> saveFiles(List<MultipartFile> files, String userId) {
        List<String> savedFilePaths = new ArrayList<>();
        
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String savedFilePath = saveFile(file, userId); // "userId/실제파일명" 반환
                    savedFilePaths.add(savedFilePath); 
                }
            }
        }
        return savedFilePaths;
    }
    
    /**
     * 파일을 Resource로 로드
     * @param filePathWithUser "userId/실제파일명" 형태의 파일 경로
     * @return Resource 객체
     */
    public Resource loadFileAsResource(String filePathWithUser) {
        try {
            // filePathWithUser는 "userId/filename.ext" 형태
            // 또는 "fixed/character_name.png" (고정 캐릭터) 또는 "ai_user_id/character_name.png" (AI 생성 캐릭터) 형태가 될 수 있음
            // 따라서 어떤 base_dir를 사용할지 결정하는 로직이 필요
            // 여기서는 우선 uploads 폴더만 가정하고, 추후 이 메소드 확장 필요
            Path filePath = Paths.get(FileConstants.UPLOADS_BASE_DIR_NAME).resolve(filePathWithUser).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                log.warn("파일을 찾을 수 없거나 읽을 수 없습니다: {}", filePathWithUser);
                throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다: " + filePathWithUser);
            }
        } catch (MalformedURLException e) {
            log.error("파일 로드 실패 - 경로: {}, 오류: {}", filePathWithUser, e.getMessage());
            throw new RuntimeException("파일 로드에 실패했습니다: " + filePathWithUser, e);
        }
    }
    
    /**
     * 파일 삭제
     * @param filePathWithUser "userId/실제파일명" 형태의 파일 경로
     * @return 삭제 성공 여부
     */
    public boolean deleteFile(String filePathWithUser) {
        try {
            // 여기도 loadFileAsResource와 마찬가지로 base_dir 결정 로직 필요
            Path filePath = Paths.get(FileConstants.UPLOADS_BASE_DIR_NAME).resolve(filePathWithUser);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("파일 삭제 완료 - 경로: {}", filePathWithUser);
                return true;
            } else {
                log.warn("삭제하려는 파일이 존재하지 않습니다 - 경로: {}", filePathWithUser);
                return false;
            }
            
        } catch (IOException e) {
            log.error("파일 삭제 실패 - 경로: {}, 오류: {}", filePathWithUser, e.getMessage());
            return false;
        }
    }
    
    /**
     * 파일 존재 여부 확인
     * @param filePathWithUser "userId/실제파일명" 형태의 파일 경로
     * @return 존재 여부
     */
    public boolean fileExists(String filePathWithUser) {
        Path filePath = Paths.get(FileConstants.UPLOADS_BASE_DIR_NAME).resolve(filePathWithUser);
        return Files.exists(filePath);
    }
    
    /**
     * 이미지를 Base64로 인코딩하여 반환
     * @param filePathWithUser "userId/실제파일명" 형태의 파일 경로
     * @return Base64 인코딩된 문자열
     */
    public String getImageAsBase64(String filePathWithUser) {
        try {
            Path path = Paths.get(FileConstants.UPLOADS_BASE_DIR_NAME).resolve(filePathWithUser);
            if (!Files.exists(path)) {
                log.warn("파일이 존재하지 않습니다 - 경로: {}", filePathWithUser);
                return null;
            }
            
            byte[] bytes = Files.readAllBytes(path);
            return java.util.Base64.getEncoder().encodeToString(bytes);
            
        } catch (IOException e) {
            log.error("파일 읽기 실패 - 경로: {}, 오류: {}", filePathWithUser, e.getMessage());
            return null;
        }
    }
    
    /**
     * 파일 크기 반환
     * @param filePathWithUser "userId/실제파일명" 형태의 파일 경로
     * @return 파일 크기 (bytes)
     */
    public long getFileSize(String filePathWithUser) {
        try {
            Path filePath = Paths.get(FileConstants.UPLOADS_BASE_DIR_NAME).resolve(filePathWithUser);
            if (Files.exists(filePath)) {
                return Files.size(filePath);
            }
            return 0;
        } catch (IOException e) {
            log.error("파일 크기 확인 실패 - 경로: {}, 오류: {}", filePathWithUser, e.getMessage());
            return 0;
        }
    }

    /**
     * 파일 확장자에 따른 Content-Type 반환
     * @param filename 파일명
     * @return Content-Type
     */
    public String getContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            case "bmp":
                return "image/bmp";
            case "svg":
                return "image/svg+xml";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            case "json":
                return "application/json";
            case "xml":
                return "application/xml";
            case "mp4":
                return "video/mp4";
            case "mp3":
                return "audio/mpeg";
            case "wav":
                return "audio/wav";
            case "zip":
                return "application/zip";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * 캐릭터 타입별 업로드 디렉토리 경로 생성.
     * FIXED 경우: characters/fixed
     * AI_GENERATED 경우: characters/{userId}
     * @param type 캐릭터 생성 타입
     * @param userId 사용자 ID (AI_GENERATED 타입에 필요)
     * @return 캐릭터 타입별 업로드 디렉토리 경로
     */
    private Path getCharacterUploadDir(CharacterCreationType type, String userId) {
        String subDir;
        if (type == CharacterCreationType.FIXED) {
            subDir = FileConstants.FIXED_CHARACTER_SUB_DIR_NAME;
        } else if (type == CharacterCreationType.AI_GENERATED) {
            if (userId == null || userId.trim().isEmpty()) {
                throw new IllegalArgumentException("AI 생성 캐릭터 저장을 위한 사용자 ID가 필요합니다.");
            }
            subDir = userId; // userId를 하위 디렉토리명으로 사용
        } else {
            throw new IllegalArgumentException("지원하지 않는 캐릭터 생성 타입입니다: " + type);
        }
        return Paths.get(FileConstants.CHARACTERS_BASE_DIR_NAME).resolve(subDir);
    }

    /**
     * 캐릭터 이미지를 저장하고 실제 저장된 파일명(DB에 기록될 순수 파일명)을 반환합니다.
     * @param file 저장할 파일
     * @param type 캐릭터 생성 타입
     * @param userId 사용자 ID (AI_GENERATED 타입에 필요)
     * @param desiredName 저장 시 사용할 파일명 (확장자 포함). null일 경우 원본 파일명 기반으로 유니크하게 생성.
     * @return 실제 저장된 파일명 (순수 파일명, 예: "image.png")
     */
    public String saveCharacterImage(MultipartFile file, CharacterCreationType type, String userId, String desiredName) {
        try {
            Path characterStorageDir = getCharacterUploadDir(type, userId);
            if (Files.notExists(characterStorageDir)) {
                Files.createDirectories(characterStorageDir);
            }

            if (file.isEmpty()) {
                throw new IllegalArgumentException("빈 파일은 저장할 수 없습니다.");
            }

            String actualFileName;
            if (desiredName != null && !desiredName.trim().isEmpty()) {
                actualFileName = StringUtils.cleanPath(desiredName);
            } else {
                actualFileName = generateUniqueFileName(Objects.requireNonNull(file.getOriginalFilename()));
            }

            Path filePath = characterStorageDir.resolve(actualFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("캐릭터 이미지 저장 완료 - 타입: {}, 사용자ID: {}, 파일: {}, 저장경로: {}", type, userId, actualFileName, characterStorageDir);
            return actualFileName; // 이제 순수 파일명 반환

        } catch (IOException e) {
            log.error("캐릭터 이미지 저장 실패 - 타입: {}, 사용자ID: {}, 오류: {}", type, userId, e.getMessage());
            throw new RuntimeException("캐릭터 이미지 저장에 실패했습니다.", e);
        }
    }

    /**
     * 캐릭터 이미지 파일을 Resource로 로드합니다.
     * @param type 캐릭터 생성 타입
     * @param userId 사용자 ID (AI_GENERATED 타입에 필요, FIXED 타입이면 null 또는 무시)
     * @param fileName 로드할 순수 파일명 (예: "image.png")
     * @return Resource 객체
     */
    public Resource loadCharacterImageAsResource(CharacterCreationType type, String userId, String fileName) {
        try {
            Path characterStorageDir = getCharacterUploadDir(type, userId);
            Path filePath = characterStorageDir.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                log.warn("캐릭터 이미지를 찾을 수 없거나 읽을 수 없습니다: {}", filePath);
                throw new RuntimeException("캐릭터 이미지를 찾을 수 없거나 읽을 수 없습니다: " + filePath);
            }
        } catch (MalformedURLException e) {
            log.error("캐릭터 이미지 로드 실패 - 경로 조합 중 오류: type={}, userId={}, fileName={}, 오류: {}", type, userId, fileName, e.getMessage());
            throw new RuntimeException("캐릭터 이미지 로드에 실패했습니다: " + fileName, e);
        }
    }
}
