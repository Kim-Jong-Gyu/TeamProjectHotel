import java.util.ArrayList;
import java.util.List;

public class ScheduledDate {
    private int roomId;
    private List<String> dates;

    public ScheduledDate(int roomId) {
        this.roomId = roomId;
        this.dates = new ArrayList<>();
    }

    public void addDate(String date) {
        this.dates.add(date);
    }

    public boolean isEmptyDate(String date){
        if (dates.contains(date)) return false;
        return true;
    }
}
