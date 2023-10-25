import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class Main {

    private static Hotel hotel;

    public static void main(String[] args) throws Exception {
        hotel = new Hotel();
        createRoomList();

        while (true) {
            displayMenu();
        }
    }

    private static void displayMenu() throws Exception {
        System.out.println("서비스를 선택해주세요.");
        System.out.println("1. 예약하기");
        System.out.println("2. 예약 조회하기");
        System.out.println("3. 예약 취소하기");

        handleMenuInput();
    }

    private static void handleMenuInput() throws Exception {
        Scanner sc = new Scanner(System.in);

        int menu = sc.nextInt();
        switch (menu) {
            case 1 -> makeReservation();
            case 2 -> callReservation();
            case 3 -> cancelReservation(hotel.getReservations());
        }
    }

    private static void makeReservation() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("이름을 입력해주세요: ");
        String name = sc.next();

        System.out.print("전화번호를 입력해주세요: ");
        String phone = sc.next();

        System.out.print("소지 금액을 입력해주세요: ");
        int cash = sc.nextInt();

        Guest guest = hotel.findOrCreateGuest(name, phone, cash);

        System.out.print("예약하실 날짜를 입력해주세요(예시, 2023/4/29): ");
        String input = sc.next();
        System.out.print("예약하실 기간을 입력해주세요: ");
        int duration = sc.nextInt();

        String[] split = input.split("/");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);

        ArrayList<String> date = new ArrayList<>();
        for (int i = 0; i < duration; i++) {
            date.add(ZonedDateTime.of(LocalDateTime.of(year, month, day, 15, 0), ZoneId.of("Asia/Seoul")).plusDays(i).toString().substring(0, 22));
        }

        hotel.printAvailableRooms(date);

        System.out.print("예약할 방 번호를 입력해주세요: ");
        int roomIndex = sc.nextInt();

        Room room = hotel.getRoom(roomIndex);
        if (room == null) throw new CustomException("존재하지 않는 방 번호입니다.");
        if (room.getPrice() > cash) throw new CustomException("소지금이 부족합니다.");

        System.out.print("이용 인원을 입력해주세요: ");
        int personnel = sc.nextInt();
        if (room.getCapacity() < personnel) throw new CustomException("숙박 가능한 인원을 초과했습니다.");

        String rId = UUID.randomUUID().toString();
        Reservation reservation = new Reservation(roomIndex, guest, date);

        hotel.addReservationList(rId, reservation);
        hotel.addImpossibleList(roomIndex,date);

        if (guest.isNew()) hotel.addGuest(guest);
        System.out.println("예약이 완료되었습니다.");
    }

    private static void createRoomList(){
        Room single = new Room(1,"싱글", 1, 100000);
        Room Double = new Room(2,"더블", 2, 105000);
        Room Deluxe = new Room(3,"디럭스", 2, 110000);
        Room Twin = new Room(4,"트윈", 2, 120000);
        Room Sweet = new Room(5,"스위트", 4, 200000);

        hotel.addRoomList(single);
        hotel.addRoomList(Double);
        hotel.addRoomList(Deluxe);
        hotel.addRoomList(Twin);
        hotel.addRoomList(Sweet);
    }

    private static Reservation getReservation(Map<String, Reservation> reservations, String UUID) throws Exception{
        Reservation reservation = reservations.get(UUID);

        if (reservation == null) {
            throw new CustomException("Invalid UUID.");
        }

        return reservation;
    }


    ///예약 가져오는 함수
    private static void callReservation() throws CustomException {
        Scanner sc = new Scanner(System.in);
        System.out.print("예약번호를 입력해주세요: ");

        String reservationNum = sc.next();
        if (hotel.getReservations().containsKey(reservationNum)){
            Reservation reservation =hotel.getReservations().get(reservationNum);

            Guest guest = reservation.getGuest();
            System.out.println("[예약 정보]");
            System.out.println("예약번호: "+reservationNum);
            System.out.println("이름: " +guest.getName());
            for (String s : reservation.getPeriodOfStay()) {
                System.out.println(s);
            }

        }else {
         throw new CustomException("해당 예약번호가 없습니다.");
        }

    }


    private static void cancelReservation(Map<String, Reservation> reservations) throws CustomException {

        Scanner sc = new Scanner(System.in);

        System.out.println("취소하고 싶은 예약 번호를 입력해 주세요");
        String UUID = sc.next();

        if (!reservations.containsKey(UUID)) {
            throw new CustomException("Invalid UUID");
        }

        reservations.remove(UUID);
        System.out.println("요청하신 취소가 완료 되었습니다.");
    }

}