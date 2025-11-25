package model;

public enum SeatType {
    PREMIUM(200, "PRM"),
    LUXURY(120, "LUX"),
    GENERAL(80, "GEN");

    public final int price;
    public final String code;
    SeatType(int price, String code) {
        this.price = price;
        this.code = code;
    }
}
