package mypicday.store.diary.repository;

import mypicday.store.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface DiaryRepository extends JpaRepository<Diary, Long> {

    // TODO 친구 목록으로 인한 쿼리 및 전체 목록 나중에 하고 일단 공통된 반환 값만 나오게 설정
    @Query("""
    select d
    from Diary d
    where   d.user.id = :loginUserId
        or d.status <> 'PRIVATE'
    order by d.createdAt desc
    """)
    //or (d.user.id <> :loginUserId and d.status <> 'PRIVATE')
    Page<Diary> findAllByAuthorId(
            @Param("loginUserId") String loginUserId,
            Pageable pageable
    );

    @Query("SELECT d.like.count FROM Diary d WHERE d.id = :diaryId")
    long LikeCountByDiaryId(@Param("diaryId") Long diaryId);

    @Query("select d from Diary d where d.user.id = :userId and d.createdAt between :startOfDay and :endOfDay")
    Optional<Diary> findUserIdAndCreatedAt(@Param("userId") String userId,
                                          @Param("startOfDay") LocalDateTime startOfDay,
                                          @Param("endOfDay") LocalDateTime endOfDay);


    @Query("select d from Diary d join fetch d.user left join fetch d.comments where d.status != 'PRIVATE' order by d.createdAt desc")
    List<Diary> findAllDiaries();


    @Query("select d from Diary d join fetch d.user left join fetch d.comments where d.id = :diaryId")
    List<Diary> findAllComments(@Param("diaryId") Long diaryId);

    @Query("select d from Diary d join fetch d.user left join fetch d.comments c where d.id = :diaryId and c.parent is not null")
    List<Diary> findAllReplies(Long diaryId);

    List<Diary> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(String userId,
                                                                    LocalDateTime startOfMonth,
                                                                    LocalDateTime endOfMonth);

    // @Query("select d from Diary d left join fetch d.imageList where d.user.id = :userId and d.createdAt between :startOfMonth and :endOfMonth order by d.createdAt desc")
    // List<Diary> findByUserIdAndCreatedAtBetweenWithImages(@Param("userId") String userId,
    //                                                      @Param("startOfMonth") LocalDateTime startOfMonth,
    //                                                      @Param("endOfMonth") LocalDateTime endOfMonth);

}
