package mypicday.store.character.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mypicday.store.character.enums.CharacterCreationType;
import mypicday.store.global.entity.BaseEntity;
import mypicday.store.user.entity.User;

@Getter
@Entity
@Table(name = "characters")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)

public class Character extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private CharacterCreationType type;

    public Character(User user, String imageUrl) {
        this.user = user;
        this.imageUrl = imageUrl;
        this.type = CharacterCreationType.AI_GENERATED;
    }

    public Character(String imageUrl, CharacterCreationType type) {
        this.imageUrl = imageUrl;
        this.type = type;
    }
}
