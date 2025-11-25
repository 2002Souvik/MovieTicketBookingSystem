package service;

import model.Movie;
import model.SeatType;
import model.Booking;
import util.FileUtil;
import exceptions.BookingNotFoundException;
import exceptions.InvalidSeatSelectionException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
// import java.text.DecimalFormat;

public class BookingSystem {
    private List<Movie> movies = new ArrayList<>();
    private Map<String, Booking> bookings = new HashMap<>();
    private Map<String, Integer> availability = new HashMap<>();
    private Random random = new Random();
    private String bookingsFilePath;
    private AtomicInteger idCounter = new AtomicInteger(100);

    public BookingSystem(String bookingsFilePath) {
        this.bookingsFilePath = bookingsFilePath;
        // initialize movies
        movies.add(new Movie(1, "ANIMAL"));
        movies.add(new Movie(2, "KGF: CHAPTER 2"));
        movies.add(new Movie(3, "3 IDIOTS"));
        movies.add(new Movie(4, "GADAR 2"));
        movies.add(new Movie(5, "WAR"));
        movies.add(new Movie(6, "SOORYAVANSHI"));
        movies.add(new Movie(7, "CHHICHHORE"));
        movies.add(new Movie(8, "PATHAAN"));
        movies.add(new Movie(9, "JAWAN"));
        movies.add(new Movie(10, "DRISHYAM 2"));


        // initialize availability and load from CSV if exists
        for (Movie m : movies) {
            for (int s = 1; s <= 4; s++) {
                for (SeatType st : SeatType.values()) {
                    int base = switch (st) {
                        case PREMIUM -> 40;
                        case LUXURY -> 120;
                        default -> 80;
                    };
                    int min = Math.max(1, (int)Math.round(base * 0.3));
                    int avail = random.nextInt(base - min + 1) + min;
                    availability.put(key(m.getId(), s, st), avail);
                }
            }
        }

        // load earlier bookings from file
        loadBookingsFromFile();
        // start a background thread to occasionally refresh availability (simulation)
        Thread refresher = new Thread(this::availabilityRefresher, "AvailabilityRefresher");
        refresher.setDaemon(true);
        refresher.start();
    }

    private String key(int movieId, int show, SeatType st) {
        return movieId + "-" + show + "-" + st.name();
    }

    public List<Movie> getMovies() { return movies; }

    public void printAvailability(int movieId) {
        System.out.println("Availability for movie id " + movieId + ":");
        for (int s = 1; s <= 4; s++) {
            System.out.printf("Show %d: ", s);
            for (SeatType st : SeatType.values()) {
                int avail = availability.getOrDefault(key(movieId, s, st), 0);
                System.out.printf("%s[%d] ", st.name(), avail);
            }
            System.out.println();
        }
    }

    // Book a number of seats, write to CSV, return bookingId
    public synchronized String book(int movieId, int show, SeatType st, int qty) throws InvalidSeatSelectionException {
        if (qty <= 0 || qty > 8) throw new InvalidSeatSelectionException("Ticket quantity must be between 1 and 8.");
        String k = key(movieId, show, st);
        int avail = availability.getOrDefault(k, 0);
        if (qty > avail) throw new InvalidSeatSelectionException("Only " + avail + " seats are available for selected category.");
        // compute price + gst
        double gstPer = (st.price * 18.0) / 100.0;
        double total = (st.price + gstPer) * qty;
        String bookingId = st.code + (idCounter.getAndIncrement());
        Booking b = new Booking(bookingId, movieId, show, st, qty, round2(total));
        bookings.put(bookingId, b);
        availability.put(k, avail - qty);
        // persist to CSV
        FileUtil.appendLine(bookingsFilePath, b.toCsv());
        return bookingId;
    }

    public synchronized double cancel(String bookingId) throws BookingNotFoundException {
        Booking b = bookings.get(bookingId);
        if (b == null) throw new BookingNotFoundException("Booking ID not found: " + bookingId);
        double finePercent = switch (b.getSeatType()) {
            case PREMIUM -> 0.45;
            case LUXURY -> 0.45;
            case GENERAL -> 0.40;
        };
        double refundPerTicket = (b.getTotalPaid() / b.getQuantity()) * (1 - finePercent);
        double refundTotal = refundPerTicket * b.getQuantity();
        // restore seats
        String k = key(b.getMovieId(), b.getShow(), b.getSeatType());
        int avail = availability.getOrDefault(k, 0);
        availability.put(k, avail + b.getQuantity());
        // remove booking and rewrite file (simple approach)
        bookings.remove(bookingId);
        rewriteBookingsToFile();
        return round2(refundTotal);
    }

    private void rewriteBookingsToFile() {
        List<String> lines = new ArrayList<>();
        // header optional - we will not include header as earlier code reads lines into Booking objects
        for (Booking b : bookings.values()) {
            lines.add(b.toCsv());
        }
        FileUtil.writeAllLines(bookingsFilePath, lines);
    }

    private void loadBookingsFromFile() {
        List<String> lines = FileUtil.readAllLines(bookingsFilePath);
        for (String l : lines) {
            Booking b = Booking.fromCsv(l);
            if (b != null) {
                bookings.put(b.getBookingId(), b);
            }
        }
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private void availabilityRefresher() {
        try {
            while (true) {
                Thread.sleep(10000); // every 10 seconds (daemon)
                // small random adjustments to availability to simulate concurrent bookings elsewhere
                for (String k : new ArrayList<>(availability.keySet())) {
                    int cur = availability.get(k);
                    int change = random.nextInt(3) - 1; // -1,0,1
                    int updated = Math.max(0, cur + change);
                    availability.put(k, updated);
                }
            }
        } catch (InterruptedException ignored) {}
    }

    public Booking getBooking(String id) {
        return bookings.get(id);
    }

    public Collection<Booking> getAllBookings() {
        return bookings.values();
    }
}
