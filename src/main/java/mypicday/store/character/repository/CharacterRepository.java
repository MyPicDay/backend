package mypicday.store.character.repository;

import mypicday.store.character.entity.Character;
import mypicday.store.character.enums.CharacterCreationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    List<Character> findByType(CharacterCreationType type);
} 