package mypicday.store.comment.dto.reponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ResponseReplyDto {
    private Long id ;
    private Long parentId;
    private String text ;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}
