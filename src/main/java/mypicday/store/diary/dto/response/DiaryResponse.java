package mypicday.store.diary.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mypicday.store.diary.dto.UserDTO;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.entity.Visibility;
import mypicday.store.file.FileUtil;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiaryResponse {
    private Long diaryId;
    private String title;
    private Visibility visibility;
    private String content;
    private UserDTO author;
    private List<String> imageUrls;
    private int likeCount;
    private int commentCount;
    private String createdAt;

    private DiaryResponse(final Diary diary) {
        this.diaryId = diary.getId();
        this.visibility = diary.getStatus();
        this.content = diary.getContent();
        this.title = diary.getTitle();
        this.author = UserDTO.from(diary.getUser());
        this.imageUrls = diary.getImageList().stream()
                .map(FileUtil::getImageUrls)
                .toList();
        this.likeCount = 0;
        this.commentCount = 0;
        this.createdAt = diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static DiaryResponse from(final Diary diary) {
        return new DiaryResponse(diary);
    }
}
