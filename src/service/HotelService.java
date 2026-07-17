package service;

import model.*;
import repository.DataRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HotelService {
    private final DataRepository repository;
    private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); // Constant 10% room tax

    public HotelService(DataRepository repository) {
        this.repository = repository;
    }

    /**
     * Business Logic: Finds rooms matching the category that do not have an active, 
     * overlapping reservation for the requested date window.
     */
    public List<Room> findAvailableRooms(RoomType type, LocalDate checkIn, LocalDate checkOut) {
        List<Room> allRooms = repository.getAllRooms();
        List<Room> matchingCategory = new ArrayList<>();
        
        // Filter out by room category and service state first
        for (Room r : allRooms) {
            if (r.getType() == type && r.getStatus() != RoomStatus.OUT_OF_SERVICE) {
                matchingCategory.add(r);
            }
        }

        List<Room> availableRooms = new ArrayList<>();
        List<Booking> allBookings = repository.getAllBookings();

        // Check each room against current bookings to protect against double allocation
        for (Room room : matchingCategory) {
            boolean isOverlapping = false;
            for (Booking booking : allBookings) {
                // Ignore bookings that are cancelled or already checked out (completed)
                if (booking.getRoomNumber() == room.getNumber() && 
                    booking.getStatus() != BookingStatus.CANCELLED && 
                    booking.getStatus() != BookingStatus.COMPLETED) {
                    
                    if (isDateRangeOverlapping(checkIn, checkOut, booking.getCheckIn(), booking.getCheckOut())) {
                        isOverlapping = true;
                        break; // Room has a direct scheduling collision, exit inner validation loop
                    }
                }
            }
            if (!isOverlapping) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    /**
     * Core Algorithm: Evaluates if two time periods overlap.
     * Formula: (StartA < EndB) AND (StartB < EndA)
     */
    private boolean isDateRangeOverlapping(LocalDate startA, LocalDate endA, LocalDate startB, LocalDate endB) {
        return startA.isBefore(endB) && startB.isBefore(endA);
    }

    public int bookRoom(int roomNumber, Guest guest, LocalDate checkIn, LocalDate checkOut) {
        Room room = repository.getRoomByNumber(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Target room allocation does not exist.");
        }

        int bookingId = repository.getAndIncrementNextBookingId();
        Booking booking = new Booking(
                bookingId, 
                roomNumber, 
                guest, 
                checkIn, 
                checkOut, 
                room.getType().getNightlyRate(), 
                TAX_RATE
        );

        repository.addBooking(booking);
        return bookingId;
    }

    public List<Room> displayAllRoomsInventory() {
        return repository.getAllRooms();
    }

    public Booking getBookingDetails(int id) {
        return repository.getBookingById(id);
    }

    public boolean executeCheckIn(int bookingId) {
        Booking booking = repository.getBookingById(bookingId);
        if (booking == null) {
            System.out.println("❌ Error: Reservation trace ID not found.");
            return false;
        }

        if (booking.getStatus() != BookingStatus.RESERVED) {
            System.out.println("❌ Processing Failure: Room can only transition to check-in if state is RESERVED. Current state: " + booking.getStatus());
            return false;
        }

        Room room = repository.getRoomByNumber(booking.getRoomNumber());
        // If the physical room is currently occupied by someone else, block the check-in
        if (room.getStatus() != RoomStatus.VACANT) {
            System.out.println("❌ Operations Conflict: The physical room is still marked as " + room.getStatus() + ". Previous occupant must clear first.");
            return false;
        }

        // Atomically update transaction and environmental states
        booking.setStatus(BookingStatus.IN_HOUSE);
        room.setStatus(RoomStatus.OCCUPIED);
        return true;
    }

    public boolean executeCheckOut(int bookingId) {
        Booking booking = repository.getBookingById(bookingId);
        if (booking == null) {
            System.out.println("❌ Error: Reservation trace ID not found.");
            return false;
        }

        if (booking.getStatus() != BookingStatus.IN_HOUSE) {
            System.out.println("❌ Processing Failure: Checkout actions require guest state to be IN_HOUSE. Current state: " + booking.getStatus());
            return false;
        }

        Room room = repository.getRoomByNumber(booking.getRoomNumber());
        
        // Finalize billing and lifecycle state updates
        booking.setStatus(BookingStatus.COMPLETED);
        room.setStatus(RoomStatus.VACANT);
        return true;
    }

    public boolean cancelReservation(int bookingId) {
        Booking booking = repository.getBookingById(bookingId);
        if (booking == null) {
            System.out.println("❌ Error: Reservation trace ID not found.");
            return false;
        }

        if (booking.getStatus() != BookingStatus.RESERVED) {
            System.out.println("❌ Policy Block: Only explicitly RESERVED book items can be cancelled. Current status: " + booking.getStatus());
            return false;
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return true;
    }
}

