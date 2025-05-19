package mypicday.store.diary.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.diary.dto.DiaryDto;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.entity.Visibility;
import mypicday.store.diary.repository.DiaryRepository;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final UserRepository userRepository;

    public Diary save(String userID , DiaryDto diaryDto) {
        Optional<User> user = userRepository.findById(userID);
        log.info("Visibility.valueOf(diaryDto.getVisibility().toUpperCase()) ={}", Visibility.valueOf(diaryDto.getVisibility().toUpperCase()));
        Diary save = diaryRepository.save(Diary.crateDiary(user.get(), diaryDto.getTitle(), diaryDto.getContent(), Visibility.valueOf(diaryDto.getVisibility().toUpperCase()), diaryDto.getAllImages()));
        return save;
    }


}
