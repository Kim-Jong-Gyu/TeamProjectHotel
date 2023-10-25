public class Guest {
    //gittest1

    String name;
    String phone;
    int cash;

    public Guest(String name, String phone, int cash) {
        this.name = name;
        this.phone = phone;
        this.cash = cash;
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
}
