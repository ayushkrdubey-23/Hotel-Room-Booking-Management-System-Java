package utility;

import model.Booking;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class FileManager {
    private static final String FILE_PATH = "data/bookings.txt";
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Appends a newly finalized or closed booking record directly to a persistent system flat-file log.
     */
    public static void appendBookingRecord(Booking booking) {
        // Ensure data directory exists
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String record = String.format("ID: %d | Room: %d | Guest: %s | Status: %s | Period: %s to %s | Total: ₹%s%n",
                    booking.getId(),
                    booking.getRoomNumber(),
                    booking.getGuest().getFullName(),
                    booking.getStatus(),
                    booking.getCheckIn().format(DTF),
                    booking.getCheckOut().format(DTF),
                    booking.getTotal().toPlainString()
            );
            writer.write(record);
        } catch (IOException e) {
            System.err.println("⚠️ Warning: Could not write record safely to data log file: " + e.getMessage());
        }
    }
}

