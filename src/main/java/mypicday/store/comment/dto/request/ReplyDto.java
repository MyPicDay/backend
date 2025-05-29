package mypicday.store.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDto {
    private Long diaryId;
    private Long parentCommentId;
    private String comment ;




}
