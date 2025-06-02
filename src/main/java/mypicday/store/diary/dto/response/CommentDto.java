package mypicday.store.diary.dto.response;



import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.Setter;
import java.time.LocalDateTime;

@Data
@Setter
@AllArgsConstructor
public class CommentDto {

    private long commentId ;

    private Long parentCommentId;

    private String name ;

    private String avatar ;

    private String text ;

    private LocalDateTime createdAt ;
}
