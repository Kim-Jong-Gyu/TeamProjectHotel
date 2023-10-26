public class Guest {
    //gittest1

    private String name;
    private String phone;
    private long cash;
    private boolean isNew;


    public Guest(String name, String phone, long cash) {
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

    public long getCash() {
        return cash;
    }

    public void setCash(long cash) {
        this.cash = cash;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isNew() {
        return isNew;
    }
}
