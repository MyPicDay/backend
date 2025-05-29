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

}
