import java.util.ArrayList;
import java.util.Map;

public class Room {
    private int roomId;
    private String roomType;
    private int capacity;
    private long price;

    public Room(int roomId, String roomType, int capacity, long price) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.capacity = capacity;
        this.price = price;
    }

    public int getRoomId() {
        return roomId;
    }


    public String getRoomType() {
        return roomType;
    }


    public int getCapacity() {
        return capacity;
    }


    public long getPrice() {
        return price;
    }


    public void printRoomInfo() {
        System.out.println("Room Number : " + this.roomId + " | Room Type : " + this.roomType + " | Room Capacity : " + this.capacity + " | Room Price : " + this.price);
    }
}