package mypicday.store.like.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id ;

    private long count ;

    public LikeEntity(long count) {
        this.count = count;
    }

    public static LikeEntity create() {
        return new LikeEntity(0);
    }

    public void increaseCount() {
        this.count++;
    }

    public void decreaseCount() {
        this.count--;
    }
}
