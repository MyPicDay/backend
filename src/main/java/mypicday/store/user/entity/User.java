package mypicday.store.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mypicday.store.global.entity.BaseEntity;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @Column(nullable = false, updatable = false, length = 50)
    private String nickname;

    // TODO (삭제 예정) 테스트를 위해 임시
    public User(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
