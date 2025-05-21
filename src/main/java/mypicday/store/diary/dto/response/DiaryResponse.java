package mypicday.store.diary.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mypicday.store.diary.dto.FileDTO;
import mypicday.store.diary.dto.UserDTO;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.entity.Visibility;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiaryResponse {
    private Long diaryId;
    private String title;
    private Visibility visibility;
    private String content;
    // NOTE 유저 정보를 담는 DTO를 가정
    private UserDTO author;
    // NOTE 임의로 이미지 파일 목록이 존재한다고 가정 후에 개발
    private List<FileDTO> imageUrls;
    private int likeCount;
    private int commentCount;
    private String createdAt;

    private DiaryResponse(final Diary diary) {
        this.diaryId = diary.getId();
        this.visibility = diary.getStatus();
        this.content = diary.getContent();
        this.title = diary.getTitle();
        // TODO UserDTO, 썸네일 이미지 목록, 좋아요 수, 댓글 수 하드코딩
        this.author = new UserDTO(1L, "박지은", "");
        this.imageUrls = List.of();
        this.likeCount = 0;
        this.commentCount = 0;
        this.createdAt = diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static DiaryResponse from(final Diary diary) {
        return new DiaryResponse(diary);
    }
}
