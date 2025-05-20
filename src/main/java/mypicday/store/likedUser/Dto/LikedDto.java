package mypicday.store.likedUser.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikedDto {

    private Long diaryId;  // 현재 mock 데이터는 String 으로 되어있어 사용 x
    private boolean liked;
}
