import java.util.ArrayList;
import java.util.Map;

public class Room {
    private int roomId;
    private String roomType;
    private int capacity;
    private int price;

    public Room(int roomId, String roomType, int capacity, int price) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.capacity = capacity;
        this.price = price;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}