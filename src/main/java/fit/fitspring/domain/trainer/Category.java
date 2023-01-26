package fit.fitspring.domain.trainer;

public enum Category {

    PERSONAL_PT("개인 PT"),
    DIET("다이어트"),
    FOOD_CHECK("식단관리"),
    REHAB("재활치료"),
    FIT_MATE("운동친구");

    private String krName;
    Category(String krName) {
        this.krName = krName;
    }

    public String getKrName() {
        return this.krName;
    }
}
