package main;

import service.BookingSystem;
import model.SeatType;
import model.Movie;
import model.Booking;
import exceptions.BookingNotFoundException;
import exceptions.InvalidSeatSelectionException;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.File;

public class MovieTicketBooking {
    public static void main(String[] args) {
        // data folder and bookings csv path
        String dataDir = "data";
        new File(dataDir).mkdirs();
        String bookingsCsv = dataDir + File.separator + "bookings.csv";

        BookingSystem bs = new BookingSystem(bookingsCsv);
        Scanner in = new Scanner(System.in);

        System.out.println("Welcome to IMAX - Movie Ticket Booking");
        System.out.println("Chitkara University  |  Batch: 2022-2026");
        System.out.println("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM uuuu")));

        while (true) {
            System.out.println("Select option:");
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Your Ticket");
            System.out.println("3. Check Availability");
            System.out.println("4. Upcoming Movies");
            System.out.println("5. Show My Bookings");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            String line = in.nextLine().trim();
            int action = -1;
            try { action = Integer.parseInt(line); } catch (Exception e) { System.out.println("Please enter a valid number."); continue; }

            try {
                if (action == 1) {
                    // show movies
                    for (Movie m : bs.getMovies()) {
                        System.out.println(m);
                    }
                    System.out.print("Enter movie id: ");
                    int mid = Integer.parseInt(in.nextLine().trim());
                    if (mid < 1 || mid > bs.getMovies().size()) { System.out.println("Invalid movie id."); continue; }
                    System.out.println("Choose Show: 1. Morning 11:00  2. Noon 02:30  3. First 06:20  4. Second 10:00");
                    System.out.print("Enter show (1-4): ");
                    int show = Integer.parseInt(in.nextLine().trim());
                    System.out.println("Select Seat Type: 1.PREMIUM 2.LUXURY 3.GENERAL");
                    System.out.print("Seat option: ");
                    int st = Integer.parseInt(in.nextLine().trim());
                    SeatType seatType = switch (st) {
                        case 1 -> SeatType.PREMIUM;
                        case 2 -> SeatType.LUXURY;
                        case 3 -> SeatType.GENERAL;
                        default -> null;
                    };
                    if (seatType == null) { System.out.println("Invalid seat type."); continue; }
                    System.out.print("Enter number of tickets (max 8): ");
                    int qty = Integer.parseInt(in.nextLine().trim());

                    // simulate processing with a small loading thread
                    Thread loader = new Thread(() -> {
                        try {
                            System.out.print("Processing");
                            for (int i = 0; i < 5; i++) { Thread.sleep(200); System.out.print("."); }
                            System.out.println();
                        } catch (InterruptedException ignored) {}
                    });
                    loader.start();
                    loader.join();

                    try {
                        String bid = bs.book(mid, show, seatType, qty);
                        java.util.Optional<Booking> b = java.util.Optional.ofNullable(bs.getBooking(bid));
                        b.ifPresent(booking -> {
                            System.out.println("*** Ticket Invoice ***");
                            System.out.println("Booking ID: " + booking.getBookingId());
                            System.out.println("Movie: " + bs.getMovies().stream().filter(m -> m.getId() == booking.getMovieId()).findFirst().map(Movie::getTitle).orElse("Unknown"));
                            System.out.println("Show: " + booking.getShow());
                            System.out.println("Seat Type: " + booking.getSeatType());
                            System.out.println("Tickets: " + booking.getQuantity());
                            System.out.println("Total Paid: Rs. " + String.format("%.2f", booking.getTotalPaid()));
                        });
                    } catch (InvalidSeatSelectionException ise) {
                        System.out.println("Booking failed: " + ise.getMessage());
                    }

                } else if (action == 2) {
                    System.out.print("Enter your Booking ID: ");
                    String id = in.nextLine().trim();
                    try {
                        double refund = bs.cancel(id);
                        System.out.println("Refund amount: Rs. " + String.format("%.2f", refund));
                    } catch (BookingNotFoundException bnfe) {
                        System.out.println("Cancellation failed: " + bnfe.getMessage());
                    }
                } else if (action == 3) {
                    for (Movie m : bs.getMovies()) System.out.println(m);
                    System.out.print("Enter movie id to check availability: ");
                    int mid = Integer.parseInt(in.nextLine().trim());
                    bs.printAvailability(mid);
                } else if (action == 4) {
                    System.out.println("Upcoming Movies(2025):");
                    String[] upcoming = {
                        " Jan  Hanuman 2",
                        " Feb  Stree 2",
                        " Mar  War 2",
                        " Apr  Pushpa 2",
                        " May  Bhool Bhulaiyaa 3"
                };
                    for (String s : upcoming) { System.out.println(s); Thread.sleep(300); }
                } else if (action == 5) {
                    System.out.println("Your saved bookings (from CSV):");
                    bs.getAllBookings().forEach(b -> System.out.println(b.toCsv()));
                } else if (action == 6) {
                    System.out.println("Goodbye!");
                    break;
                } else {
                    System.out.println("Choose a valid option (1-6).");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        in.close();
    }
}
