package mypicday.store.follow.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

//복합 키 클래스
@Getter
@Setter
//다른 Entity의 키로 삽입될 수 있음
@Embeddable
// 구현 필수 -> Hibernate 내부에서 객체를 직렬화 해 사용함
public class FollowId implements Serializable {
    private String followerId;
    private String followingId;

    public FollowId(String followingId, String followerId) {
        this.followingId = followingId;
        this.followerId = followerId;
    }
    //구현 필수 복합 키의 동일성 비교와 해시를 위해 꼭 필요
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowId)) return false;
        FollowId that = (FollowId) o;
        return Objects.equals(followerId, that.followerId) &&
                Objects.equals(followingId, that.followingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followerId, followingId);
    }

    protected FollowId() {

    }
}