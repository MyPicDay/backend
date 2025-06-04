package mypicday.store.comment.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCommentUser {

    private Long id ;
    private String name ;
    private String avatar ;

    private LocalDateTime date ;
}
