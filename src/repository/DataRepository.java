package repository;

import model.Booking;
import model.Room;
import model.RoomType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataRepository {
    private final Map<Integer, Room> rooms = new LinkedHashMap<>();
    private final Map<Integer, Booking> bookings = new LinkedHashMap<>();
    private int nextBookingId = 1001;

    public DataRepository() {
        seedInventory();
    }

    private void seedInventory() {
        // Seed exactly 10 initial rooms with realistic numbers and room configurations
        rooms.put(101, new Room(101, RoomType.SINGLE));
        rooms.put(102, new Room(102, RoomType.SINGLE));
        rooms.put(103, new Room(103, RoomType.SINGLE));
        
        rooms.put(201, new Room(201, RoomType.DOUBLE));
        rooms.put(202, new Room(202, RoomType.DOUBLE));
        rooms.put(203, new Room(203, RoomType.DOUBLE));
        
        rooms.put(301, new Room(301, RoomType.DELUXE));
        rooms.put(302, new Room(302, RoomType.DELUXE));
        
        rooms.put(401, new Room(401, RoomType.SUITE));
        rooms.put(402, new Room(402, RoomType.SUITE));
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public Room getRoomByNumber(int roomNumber) {
        return rooms.get(roomNumber);
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }

    public Booking getBookingById(int id) {
        return bookings.get(id);
    }

    public synchronized int addBooking(Booking booking) {
        bookings.put(booking.getId(), booking);
        return booking.getId();
    }

    public synchronized int getAndIncrementNextBookingId() {
        return nextBookingId++;
    }
}

