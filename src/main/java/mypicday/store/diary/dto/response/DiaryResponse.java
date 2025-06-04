package mypicday.store.diary.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import mypicday.store.diary.entity.Visibility;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class DiaryResponse {
    private String title;
    private Long diaryId;
    private Visibility visibility;
    private String content;
    private String username ;
    private List<String> imageUrls;
    private int commentCount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    private String avatar ;

    public DiaryResponse(String title, Long diaryId, Visibility visibility, String content, String username, List<String> imageUrls, int commentCount, LocalDate createdAt) {
        this.title = title;
        this.diaryId = diaryId;
        this.visibility = visibility;
        this.content = content;
        this.username = username;
        this.imageUrls = imageUrls;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }
}
