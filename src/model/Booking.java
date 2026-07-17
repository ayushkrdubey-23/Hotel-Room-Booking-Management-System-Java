package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking {
    private final int id;
    private final int roomNumber;
    private final Guest guest;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private BookingStatus status;

    // Financial components computed at instantiation
    private final BigDecimal nightlyRate;
    private final long nights;
    private final BigDecimal roomCharge;
    private final BigDecimal tax;
    private final BigDecimal total;

    public Booking(int id, int roomNumber, Guest guest, LocalDate checkIn, 
                   LocalDate checkOut, BigDecimal nightlyRate, BigDecimal taxRate) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.guest = guest;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = BookingStatus.RESERVED;
        
        this.nightlyRate = nightlyRate.setScale(2, RoundingMode.HALF_UP);
        this.nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        
        this.roomCharge = this.nightlyRate.multiply(new BigDecimal(this.nights))
                .setScale(2, RoundingMode.HALF_UP);
        this.tax = this.roomCharge.multiply(taxRate)
                .setScale(2, RoundingMode.HALF_UP);
        this.total = this.roomCharge.add(this.tax)
                .setScale(2, RoundingMode.HALF_UP);
    }

    // Getters and Status Setter
    public int getId() { return id; }
    public int getRoomNumber() { return roomNumber; }
    public Guest getGuest() { return guest; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    
    public BigDecimal getNightlyRate() { return nightlyRate; }
    public long getNights() { return nights; }
    public BigDecimal getRoomCharge() { return roomCharge; }
    public BigDecimal getTax() { return tax; }
    public BigDecimal getTotal() { return total; }
}
