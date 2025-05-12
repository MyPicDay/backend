package mypicday.store.user.entity;

import jakarta.persistence.Entity;
import lombok.Getter;

import jakarta.persistence.*;
import lombok.*;
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
}
