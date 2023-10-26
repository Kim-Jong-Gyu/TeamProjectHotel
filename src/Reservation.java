import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Reservation {
    private int roomId;
    private Guest guest;
    private int totalPrice;

    // 숙박 기간
    private ArrayList<String> periodOfStay;

    public Reservation(int roomId, Guest guest, int totalPrice, ArrayList<String> periodOfStay) {
        this.roomId = roomId;
        this.guest = guest;
        this.totalPrice = totalPrice;
        this.periodOfStay = periodOfStay;
    }

    public int getRoomId() {
        return this.roomId;
    }

    public Guest getGuest() {
        return guest;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public ArrayList<String> getPeriodOfStay() {
        return periodOfStay;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPeriodOfStay(ArrayList<String> periodOfStay) {
        this.periodOfStay = periodOfStay;
    }

    public void printPeriodOfStay() {
        ArrayList<String> period = this.periodOfStay;

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmXXX");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd");

        try {
            Date startDate = inputFormat.parse(period.get(0));
            Date endDate = inputFormat.parse(period.get(period.size() - 1));

            String formattedStartDate = outputFormat.format(startDate);
            String formattedEndDate = outputFormat.format(endDate);

            System.out.println(formattedStartDate + " ~ " + formattedEndDate);
        } catch (ParseException e) {
            // parser 예외처리 해야 한다고 해서 추가
            e.printStackTrace();
        }
    }

    public int getStayDays() {
        return periodOfStay.size() - 1;
    }

}
