import java.util.ArrayList;

public class Reservation {
    private int roomId;
    private Guest guest;

    // 숙박 기간
    private ArrayList<String> periodOfStay;

    public Reservation(int roomId, Guest guest, ArrayList<String> periodOfStay) {
        this.roomId = roomId;
        this.guest = guest;
        this.periodOfStay = periodOfStay;
    }

    public ArrayList<String> getPeriodOfStay() {
        return periodOfStay;
    }

    public void setPeriodOfStay(ArrayList<String> periodOfStay) {
        this.periodOfStay = periodOfStay;
    }

}
