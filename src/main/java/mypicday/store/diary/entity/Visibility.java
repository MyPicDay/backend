package mypicday.store.diary.entity;

public enum Visibility {
    PUBLIC("전체공개"),
    FRIENDS("친구공개"),
    PRIVATE("비공개");

    private final String label;

    Visibility(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }


}
