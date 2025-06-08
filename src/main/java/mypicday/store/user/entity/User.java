package mypicday.store.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mypicday.store.diary.entity.Diary;
import mypicday.store.follow.entity.Follow;
import mypicday.store.global.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String nickname;

    private String avatar;

    @OneToMany(mappedBy = "user")
    private List<Diary> diaries = new ArrayList<>();

    //내가 팔로잉 하는 유저
    @OneToMany(mappedBy = "follower")
    private List<Follow> following;

    // 나를 팔로우 하고 있는 유저
    @OneToMany(mappedBy = "following")
    private List<Follow> followers;

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


    public User(List<Follow> following, List<Follow> followers) {
        this.following = following;
        this.followers = followers;
    }

    public void changeAvatar(String avatar) {
        this.avatar = avatar;

    }
}
