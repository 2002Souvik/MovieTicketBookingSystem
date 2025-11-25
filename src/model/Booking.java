package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    private String bookingId;
    private int movieId;
    private int show; // 1..4
    private SeatType seatType;
    private int quantity;
    private double totalPaid;
    private LocalDateTime createdAt;

    public Booking(String bookingId, int movieId, int show, SeatType seatType, int quantity, double totalPaid) {
        this.bookingId = bookingId;
        this.movieId = movieId;
        this.show = show;
        this.seatType = seatType;
        this.quantity = quantity;
        this.totalPaid = totalPaid;
        this.createdAt = LocalDateTime.now();
    }

    public String getBookingId() { return bookingId; }
    public int getMovieId() { return movieId; }
    public int getShow() { return show; }
    public SeatType getSeatType() { return seatType; }
    public int getQuantity() { return quantity; }
    public double getTotalPaid() { return totalPaid; }
    public String getCreatedAt() { return createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME); }

    public String toCsv() {
        // BookingID,MovieId,Show,SeatType,Quantity,TotalPaid,CreatedAt
        return String.format("%s,%d,%d,%s,%d,%.2f,%s", bookingId, movieId, show, seatType.name(), quantity, totalPaid, getCreatedAt());
    }

    public static Booking fromCsv(String line) {
        try {
            String[] p = line.split(",", -1);
            String id = p[0];
            int movieId = Integer.parseInt(p[1]);
            int show = Integer.parseInt(p[2]);
            SeatType st = SeatType.valueOf(p[3]);
            int qty = Integer.parseInt(p[4]);
            double total = Double.parseDouble(p[5]);
            // createdAt ignored for object reconstruction simplicity
            return new Booking(id, movieId, show, st, qty, total);
        } catch (Exception e) {
            return null;
        }
    }
}
