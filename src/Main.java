import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        System.out.println("4. 예약 변경하기");

        handleMenuInput();
    }

    private static void handleMenuInput() throws Exception {
        Scanner sc = new Scanner(System.in);

        int menu = sc.nextInt();
        // 0번은 호텔입장에서의 예약조회를 하는겁니다.
        switch (menu) {
            case 0 -> hotelInquireReservations();
            case 1 -> makeReservation();
            case 2 -> callReservation();
            case 3 -> cancelReservation(hotel.getReservations());
            case 4 -> changeReservation();
        }
    }

    private static void changeReservation() throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("1. 날짜 변경하기");
        System.out.println("2. 방 변경하기");

        int input = sc.nextInt();
        if (input != 1 && input != 2) throw new CustomException("잘못된 입력입니다.");

        switch (input) {
            case 1 -> changeDateOfReservation();
//            case 2 -> changeRoomTypeOfReservation();
        }
    }

    private static void changeDateOfReservation() throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("예약 번호를 입력해주세요: ");
        String rId = sc.next();

        Map<String, Reservation> reservations = hotel.getReservations();
        if (!reservations.containsKey(rId)) throw new CustomException("존재하지 않는 예약번호입니다.");

        Reservation reservation = reservations.get(rId);

        System.out.print("변경하실 날짜를 입력해주세요(예시, 2023/4/29): ");
        String input = sc.next();
        String[] split = input.split("/");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        if(!invalidReservationDate(year,month,day)) throw new CustomException("잘못된 예약 날짜입니다.");

        System.out.print("예약하실 기간을 입력해주세요: ");
        int duration = sc.nextInt();

        int price = hotel.getRoom(reservation.getRoomId()).getPrice() * duration;
        int guestCash = reservation.getTotalPrice() + reservation.getGuest().getCash();

        if (guestCash < price) throw new CustomException("소지금이 부족합니다.");

        reservation.setTotalPrice(price);
        reservation.getGuest().setCash(guestCash - price);

        ArrayList<String> dates = new ArrayList<>();
        for (int i = 0; i < duration; i++) {
            dates.add(ZonedDateTime.of(LocalDateTime.of(year, month, day, 15, 0), ZoneId.of("Asia/Seoul")).plusDays(i).toString().substring(0, 22));
            if (!hotel.checkDate(reservation.getRoomId(), dates.get(i))){
                if (reservation.getPeriodOfStay().contains(dates.get(i))) continue;
                throw new CustomException("예약이 불가능한 날짜입니다.");
            }
        }

        System.out.println("날짜를 변경하시겠습니까?");
        System.out.println("1. 확인\t\t\t2. 취소");
        int choice = sc.nextInt();

        if (choice != 1 && choice != 2) throw new CustomException("잘못된 입력입니다.");
        if (choice == 2) {
            System.out.println("예약 변경을 취소합니다.");
            return;
        }

        for (int i = 0; i < reservation.getPeriodOfStay().size(); i++) {
            hotel.removeReservationDate(reservation.getRoomId(), reservation.getPeriodOfStay().get(i));
        }

        hotel.addImpossibleList(reservation.getRoomId(), dates);
        reservation.setPeriodOfStay(dates);
        hotel.addReservationList(rId, reservation);

        System.out.println("날짜 변경이 완료되었습니다.");
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
        String[] split = input.split("/");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        if(!invalidReservationDate(year,month,day)) throw new CustomException("잘못된 예약 날짜입니다.");


        System.out.print("예약하실 기간을 입력해주세요: ");
        int duration = sc.nextInt();

        ArrayList<String> date = new ArrayList<>();
        for (int i = 0; i < duration + 1; i++) {
            date.add(ZonedDateTime.of(LocalDateTime.of(year, month, day, 15, 0), ZoneId.of("Asia/Seoul")).plusDays(i).toString().substring(0, 22));
        }

        ArrayList<Integer> checkAvailableRoom = hotel.printAvailableRooms(date);

        System.out.print("예약할 방 번호를 입력해주세요: ");
        int roomIndex = sc.nextInt();
        if(!checkAvailableRoom.contains(roomIndex)){
            throw new CustomException("잘못된 방번호 입력입니다.");
        }
        Room room = hotel.getRoom(roomIndex);
        if (room == null) throw new CustomException("존재하지 않는 방 번호입니다.");
        if (room.getPrice() * date.size() > cash) throw new CustomException("소지금이 부족합니다.");


        System.out.print("이용 인원을 입력해주세요: ");
        int personnel = sc.nextInt();
        if (room.getCapacity() < personnel) throw new CustomException("숙박 가능한 인원을 초과했습니다.");

        // guest Price 변경
        guest.setCash(cash - room.getPrice() * duration);

        String rId = UUID.randomUUID().toString();
        Reservation reservation = new Reservation(roomIndex, guest, room.getPrice() * date.size(), date);

        hotel.addReservationList(rId, reservation);
        hotel.addImpossibleList(roomIndex,date);

        if (guest.isNew()) hotel.addGuest(guest);

        System.out.println("예약이 완료되었습니다.");
        System.out.println("고객님의 예약 번호는");
        System.out.println(rId + "입니다.");
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

    private static Reservation getReservation(Map<String, Reservation> reservations, String UUID) throws CustomException{
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
            System.out.println("입실 날짜: " +reservation.getPeriodOfStay().get(0));
            System.out.println("퇴실 날짜: " +reservation.getPeriodOfStay().get(reservation.getPeriodOfStay().size() - 1));
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
        } else {
            int targetRoomId = reservations.get(UUID).getRoomId();
            Room targetRoom;
            for (Room room : hotel.getRoomsList()) {
                if (room.getRoomId() == targetRoomId) {
                    System.out.println("취소하시는 예약정보를 확인해 주세요.");
                    room.printRoomInfo();
                    System.out.print("숙박 기간 : ");
                    hotel.getReservations().get(UUID).printPeriodOfStay();
                    int stayDays = hotel.getReservations().get(UUID).getStayDays();
                    System.out.println("총 비용 : " + (room.getPrice() * stayDays));
                    printConfirmMessage();

                    int confirm = sc.nextInt();
                    if (confirm == 1) {
                        hotel.getImpossibleList().remove(targetRoomId);
                        reservations.remove(UUID);
                    // 취소 혹은 잘못입력시 어떻게 할지 논의 while문으로 돌릴지 else 다 메인으로 추방할지..
                    } else if (confirm == 2){
                        System.out.println("취소합니다.");
                    } else {
                        System.out.println("잘못 입력했습니다.");
                    }
                }
            }

        }

        System.out.println("요청하신 취소가 완료 되었습니다.\n");
    }

    private static void printConfirmMessage() {
        System.out.println("\n1. 확인      2. 취소");
    }

    private static String dateFormator(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmXXX");
        Date date = sdf.parse(dateStr);

        // 포맷 변경
        sdf.applyPattern("yyyy/MM/dd");
        return sdf.format(date);
    }

    private static boolean invalidReservationDate(int year, int month, int day) {
        if (2022 < year && year < 2100) {
            return switch (month) {
                case 1, 3, 5, 7, 8, 10, 12 -> day <= 31 && day >= 1;
                case 2 -> day <= 28 && day >= 1;
                case 4, 6, 9, 11 -> day <= 30 && day >= 1;
                default -> false;
            };
        }
        return false;
    }
    // 예약 리스트 조회
    private static void hotelInquireReservations(){
        Map<String, Reservation> inquireReservations = hotel.getReservations();
        System.out.println("[전체 예약 정보]");
        inquireReservations.forEach((key,value) -> {
            System.out.println("예약번호: "+ key);
            System.out.println("이름: " + value.getGuest().getName());
            System.out.println("전화 번호: " + value.getGuest().getPhone());
            System.out.println("돈: " + value.getGuest().getCash());
            System.out.println("예약 방 번호 : " + value.getRoomId());
            System.out.println("예약 날짜: " + value.getPeriodOfStay().get(0));
            System.out.println("숙박 기간: " + value.getPeriodOfStay());
            System.out.println("------------------------------------------------------------------------");
        });
    }
}