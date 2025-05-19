package mypicday.store.diary.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum Status {
    PUBLIC,        // 전체공개
    FRIENDS_ONLY,  // 친구공개
    PRIVATE        // 비공개
}


