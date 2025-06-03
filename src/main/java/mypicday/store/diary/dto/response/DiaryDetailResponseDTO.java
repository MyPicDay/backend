package mypicday.store.diary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mypicday.store.user.dto.response.UserInfoDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryDetailResponseDTO {
/*    private Long id;
    private String title;

    private Visibility visibility;*/
    private String content;
    private long likeCount ;
    private boolean liked;

    private List<String> imageUrls;
    private int commentCount;
    private LocalDate createdAt;
    private UserInfoDTO author;
    private List<CommentDto> comments;
}
