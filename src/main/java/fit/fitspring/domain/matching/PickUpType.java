package fit.fitspring.domain.matching;

public enum PickUpType {
    TRAINER_GO("트레이너가 갈게요"),
    CUSTOMER_GO("고객이 갈게요");

    private String krName;
    PickUpType(String krName) {
        this.krName = krName;
    }

    public String getKrName() {
        return this.krName;
    }
}
