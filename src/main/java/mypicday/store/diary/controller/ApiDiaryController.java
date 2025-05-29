package mypicday.store.diary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.comment.dto.reponse.ResponseCommentDto;
import mypicday.store.comment.dto.reponse.UserCommentsDto;
import mypicday.store.comment.dto.request.CommentDto;
import mypicday.store.comment.entity.Comment;
import mypicday.store.diary.dto.DiaryDto;
import mypicday.store.diary.dto.response.DiaryResponse;
import mypicday.store.diary.dto.response.UserDiaryDto;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.service.DiaryService;
import mypicday.store.global.config.CustomUserDetails;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ApiDiaryController {

    private final DiaryService diaryService;

    @PostMapping("/diary")
    public ResponseEntity<Map<String ,String>> Diary(@ModelAttribute DiaryDto diaryDto,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        String userId = customUserDetails.getId();
        Optional<Diary> diary = diaryService.updateDiary(userId, diaryDto);
        if (diary.isPresent()) {
            return ResponseEntity.ok(Map.of("id", userId));
        }

        
        List<String> images = new ArrayList<>();

        // uploads 폴더 존재 확인 및 생성
        Path uploadDir = Paths.get("uploads");
        if (Files.notExists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        if (diaryDto.getImages() != null) {
            for (MultipartFile file : diaryDto.getImages()) {
                if (!file.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = uploadDir.resolve(fileName);

                    Files.copy(file.getInputStream(), filePath);  // 서버 로컬에 저장
                    images.add("/uploads/" + fileName);
                }
            }
        }

        diaryDto.setAllImages(images);


        diaryService.save(userId, diaryDto);
        return ResponseEntity.ok(Map.of("id", userId));
    }


    @GetMapping("/diary")
    public ResponseEntity<UserDiaryDto> editDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date ,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("editDiary: date: {}", date);
        String userId = customUserDetails.getId();
        UserDiaryDto userDiary = diaryService.findUserDiary(userId, date);
        log.info("userDiary: {}", userDiary);
        return ResponseEntity.ok(userDiary);

    }


    @GetMapping("/diaries")
    public ResponseEntity<List<DiaryResponse>> findAllDiaries() {
        List<Diary> allDiaries = diaryService.findAllDiaries();

        log.info("findAllDiaries: allDiaries = {}", allDiaries.size());
        List<DiaryResponse> diaries = allDiaries.stream().map(diary ->
                new DiaryResponse(diary.getTitle(), diary.getId(), diary.getStatus(), diary.getContent(), diary.getUser().getNickname(), diary.getImageList(),
                        diary.getComments().size() , diary.getCreatedAt().toLocalDate())
        ).collect(toList());
        return diaries.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(diaries);
    }


    @GetMapping("/comments/{diaryId}")
    public ResponseEntity<UserCommentsDto> getComments(@PathVariable Long diaryId) {
        log.info("getComments: diaryId = {}", diaryId);
        List<Diary> allDiaries = diaryService.findAllComments(diaryId);
        UserCommentsDto userCommentsDto = new UserCommentsDto();
        for (Diary allDiary : allDiaries) {
            log.info("getComments: allDiary = {}", allDiary.getId());
            List<Comment> comments = allDiary.getComments();
            List<ResponseCommentDto> collect = comments.stream().map(comment -> new ResponseCommentDto(comment.getId(), comment.getContext(), comment.getCreatedAt().toLocalDate()))
                    .collect(toList());
            userCommentsDto.setTitle(allDiary.getTitle());
            userCommentsDto.setName(allDiary.getUser().getNickname());
            userCommentsDto.setContent(allDiary.getContent());
            userCommentsDto.setAvatar(allDiary.getUser().getAvatar());
            userCommentsDto.setComments(collect);
            log.info("getComments: userCommentsDto = {}", userCommentsDto);
        }
        return new ResponseEntity<>(userCommentsDto, HttpStatus.OK);
    }

    @GetMapping("/replies/{diaryId}")
    public ResponseEntity<UserCommentsDto> getRepliesByDiaryId(@PathVariable Long diaryId) {
        List<Diary> allReplies = diaryService.findAllReplies(diaryId);

        UserCommentsDto userCommentsDto = new UserCommentsDto();
        for (Diary allDiary : allReplies) {
            log.info("getComments: allDiary = {}", allDiary.getId());
            List<Comment> comments = allDiary.getComments();
            List<ResponseCommentDto> collect = comments.stream().map(comment -> new ResponseCommentDto(comment.getId(), comment.getContext(), comment.getCreatedAt().toLocalDate()))
                    .collect(toList());
            userCommentsDto.setTitle(allDiary.getTitle());
            userCommentsDto.setName(allDiary.getUser().getNickname());
            userCommentsDto.setContent(allDiary.getContent());
            userCommentsDto.setAvatar(allDiary.getUser().getAvatar());
            userCommentsDto.setComments(collect);
            log.info("getComments: userCommentsDto = {}", userCommentsDto);
        }
        return new ResponseEntity<>(userCommentsDto, HttpStatus.OK);
    }


}

