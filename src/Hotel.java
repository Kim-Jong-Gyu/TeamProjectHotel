import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hotel {
    private Map<String, Reservation> reservations; // key : 예약 번호, value : 예약 내용
    private ArrayList<Guest> guestsList;
    private ArrayList<Room> roomsList;

    // checkList -> 날짜, 방 번호 -> 예약 가능한지 아닌지 체크하기 위해
    private Map<Integer, ArrayList<String>> impossibleList;


    public Hotel() {
        this.reservations = new HashMap<>();
        this.guestsList = new ArrayList<>();
        this.roomsList = new ArrayList<>();
        this.impossibleList = new HashMap<>();
    }

    public Map<String, Reservation> getReservations() {
        return reservations;
    }

    public ArrayList<Room> getRoomsList() {
        return roomsList;
    }

    public void addRoomList(Room room){
        this.roomsList.add(room);
    }


    public void addReservationList(String rId, Reservation reservation){
        reservations.put(rId, reservation);
    }

    public void addGuest(Guest guest) {
        this.guestsList.add(guest);
    }

    // impossibleList 에 값 추가
    public void addImpossibleList(int roomId, ArrayList<String> date) {
        ArrayList<String> tmp = new ArrayList<>();
        if (impossibleList.containsKey(roomId)) {
            tmp = impossibleList.get(roomId);
            for (int i = 0; i < date.size(); i++) {
                tmp.add(date.get(i));
            }
            impossibleList.put(roomId, tmp);
        }else impossibleList.put(roomId, date);
    }

    public Map<Integer, ArrayList<String>> getImpossibleList() {
        return impossibleList;
    }

    public Guest findOrCreateGuest(String name, String phone, int cash) {
        for(Guest guest : guestsList){
            if(guest.getName().equals(name)){
                if(guest.getPhone().equals(phone)){
                    guest.setCash(cash);
                    guest.setNew(false);
                    return guest;
                }
            }
        }
        return new Guest(name,phone,cash);
    }

    // impossibleList에 해당 roomId 값이 포함되는지 체크
    private boolean checkImpossible(int roomId, ArrayList<String> date){
        if(impossibleList.containsKey(roomId)){
            for(String check : impossibleList.get(roomId)){
                if(date.contains(check)){
                    return false;
                }
            }
        }
        return true;
    }

    // 예약 가능한 방을 출력
    public ArrayList<Integer> printAvailableRooms(ArrayList<String> date) {
        ArrayList<Integer> availableRooms = new ArrayList<>();
        for(Room room : roomsList){
            int roomId = room.getRoomId();
            if(checkImpossible(roomId,date)){
                availableRooms.add(roomId);
                System.out.printf("%s | %-14s| %s | %s %d\n", "Room Number " + room.getRoomId(), "Room Type " + room.getRoomType(), "Room Capacity " + room.getCapacity(), "Room Price ", room.getPrice());
//                System.out.println("Room Number " + room.getRoomId() + " | Room Type " + room.getRoomType() + " | Room Capacity " + room.getCapacity() + " | Room Price " + room.getPrice());
            }
        }
        return availableRooms;
    }

    public Room getRoom(int roomIndex) {
        for(Room room : roomsList){
            if (room.getRoomId() == roomIndex) {
                return room;
            }
        }
        return null;
    }

    // 날짜 하나하나 비교하기 위해 만듬
    public boolean checkDate(int roomId, String date) {
        for (String str : this.impossibleList.get(roomId)) {
            if (str.equals(date)) return false;
        }
        return true;
    }

    public void removeReservationDate(int roomId, String date) {
        for (String str : this.impossibleList.get(roomId)) {
            if (str.equals(date)) {
                impossibleList.get(roomId).remove(str);
                return;
            }
        }
    }
}
