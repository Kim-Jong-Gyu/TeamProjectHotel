import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    public int getRoomId() {
        return this.roomId;
    }

    public Guest getGuest() {
        return guest;
    }

    public ArrayList<String> getPeriodOfStay() {
        return periodOfStay;
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
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmXXX");

        try {
            Date startDate = inputFormat.parse(periodOfStay.get(0));
            Date endDate = inputFormat.parse(periodOfStay.get(periodOfStay.size() - 1));

            // 날짜 간 차이를 밀리초 단위로 계산
            long timeDifference = endDate.getTime() - startDate.getTime();

            // 밀리초를 일로 변환 (1일 = 24시간 * 60분 * 60초 * 1000밀리초)
            int stayDays = (int) (timeDifference / (24 * 60 * 60 * 1000));

            return stayDays;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // 오류 발생 시 -1 반환 또는 예외 처리 방법을 선택
        }
    }

}
