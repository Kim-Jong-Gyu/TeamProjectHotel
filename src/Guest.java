public class Guest {
    //gittest1

    private String name;
    private String phone;
    private int cash;
    private boolean isNew;


    public Guest(String name, String phone, int cash) {
        this.name = name;
        this.phone = phone;
        this.cash = cash;
        this.isNew = true;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isNew() {
        return isNew;
    }
}
