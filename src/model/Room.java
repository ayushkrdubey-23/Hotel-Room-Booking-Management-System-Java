package model;

public class Room {
    private final int number;
    private final RoomType type;
    private RoomStatus status;

    public Room(int number, RoomType type) {
        this.number = number;
        this.type = type;
        this.status = RoomStatus.VACANT;
    }

    // Getters and Setters
    public int getNumber() { return number; }
    public RoomType getType() { return type; }
    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
}
