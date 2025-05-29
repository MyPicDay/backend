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

    private String email;

    //@Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255) COMMENT '암호화'")
    private String password;

   // @Column(name = "nickname", nullable = false, columnDefinition = "VARCHAR(255) COMMENT '암호화(복호화 가능)'")
    private String nickname;

    private String avatar;

    public User(String email, String password, String nickname, String avatar) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void changeAvatar(String avatar) {
        this.avatar = avatar;
    }
}
