package fit.fitspring.domain.account;

public enum AccountType {
    TRAINER("트레이너"),
    CUSTOMER("고객");

    private String krName;
    AccountType(String krName) {
        this.krName = krName;
    }

    public String getKrName() {
        return this.krName;
    }
}
