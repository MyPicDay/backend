package mypicday.store.diary.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.ai.service.DiaryImageGenerationService;
import mypicday.store.comment.dto.reponse.ResponseCommentDto;
import mypicday.store.comment.dto.reponse.UserCommentsDto;
import mypicday.store.comment.entity.Comment;
import mypicday.store.comment.service.CommentService;
import mypicday.store.diary.dto.DiaryDto;
import mypicday.store.diary.dto.response.CommentDto;
import mypicday.store.diary.dto.response.DiaryDetailResponseDTO;
import mypicday.store.diary.dto.response.DiaryResponse;
import mypicday.store.diary.dto.response.UserDiaryDto;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.service.DiaryService;
import mypicday.store.file.FileUtil;
import mypicday.store.global.config.CustomUserDetails;
import mypicday.store.global.dto.RequestMetaInfo;
import mypicday.store.global.util.ImagePathToUrlConverter;
import mypicday.store.global.util.RequestMetaMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


import static java.util.stream.Collectors.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ApiDiaryController {

    private final DiaryService diaryService;
    private final FileUtil fileUtil;
    private final RequestMetaMapper requestMetaMapper;
    private final CommentService commentService;
    private final DiaryImageGenerationService diaryImageGenerationService;
    private final ImagePathToUrlConverter converter;

    @PostMapping("/diary")
    public ResponseEntity<Map<String ,String>> Diary(@ModelAttribute DiaryDto diaryDto,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                     HttpServletRequest request) throws IOException {
        String userId = customUserDetails.getId();

        List<String> images = fileUtil.saveFiles(diaryDto.getImages(), userId);

        // diartyDto.getAiGeneratedImage 가 null이 아닐 경우, 이미지 저장 url 제거 후 저장
        if (diaryDto.getAiGeneratedImage() != null) {
            String aiImagePath = diaryImageGenerationService.getPathFromUrl(diaryDto.getAiGeneratedImage(), request);
            if (!aiImagePath.isBlank()){
                images.add(0, aiImagePath);
            }
        }

        diaryDto.setAllImages(images);
        Diary diaryInfo = diaryService.save(userId, diaryDto);
        if (diaryDto.getAiGeneratedImage() != null) {
            diaryImageGenerationService.diaryUpdate(customUserDetails, diaryInfo.getId(), diaryDto.getAllImages().get(0));
        diaryDto.setAllImages(images);
        boolean bol = diaryService.updateDiary(userId, diaryDto);

        if (!bol) {
            return ResponseEntity.ok(Map.of("id", userId));
        }

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
    public ResponseEntity<List<DiaryResponse>> findAllDiaries(HttpServletRequest request) {

        List<Diary> allDiaries = diaryService.findAllDiaries();

        log.info("findAllDiaries: allDiaries = {}", allDiaries.size());
        RequestMetaInfo requestMetaInfo = requestMetaMapper.extractMetaInfo(request);

        List<DiaryResponse> diaries = allDiaries.stream().map(diary ->
                new DiaryResponse(diary.getTitle(), diary.getId(), diary.getStatus(), diary.getContent(), diary.getUser().getNickname(), diary.getImageList(),
                        diary.getComments().size() , diary.getCreatedAt().toLocalDate() , converter.userAvatarImageUrl(diary.getUser().getAvatar() , requestMetaInfo))
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

    @GetMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryDetailResponseDTO> getDiaryDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails ,HttpServletRequest request, @PathVariable Long diaryId) {
        String userId  = customUserDetails.getId();
        RequestMetaInfo requestMetaInfo = requestMetaMapper.extractMetaInfo(request);
        DiaryDetailResponseDTO detail = diaryService.getDiaryDetail(userId ,diaryId, requestMetaInfo);
        List<Comment> comments = commentService.findAllByDiaryId(diaryId);
        if (comments == null) {
            return ResponseEntity.ok(detail) ;
        }

        return ResponseEntity.ok(detail);
    }

}

