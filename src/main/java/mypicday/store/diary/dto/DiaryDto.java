package mypicday.store.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DiaryDto {

    private String title;
    private String content ;
    private String visibility;
    private List<MultipartFile> images ;
    private List<String> allImages;

}
