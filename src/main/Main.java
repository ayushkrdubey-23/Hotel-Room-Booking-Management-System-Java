package main;

import model.Booking;
import model.Guest;
import model.Room;
import model.RoomType;
import repository.DataRepository;
import service.HotelService;
import utility.InputValidator;
import utility.FileManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DataRepository repository = new DataRepository();
        HotelService hotelService = new HotelService(repository);
        InputValidator validator = new InputValidator(scanner);

        System.out.println("=================================================");
        System.out.println("🏨 Grand Horizon Hotel Management Console Engine 🏨");
        System.out.println("=================================================");

        while (true) {
            System.out.println("\n--- MAIN NAVIGATION MENU ---");
            System.out.println("1] View All Rooms Inventory");
            System.out.println("2] Search Available Rooms By Date");
            System.out.println("3] Create New Reservation");
            System.out.println("4] View Booking Details by Reference ID");
            System.out.println("5] Cancel Existing Reservation");
            System.out.println("6] Guest Check-In");
            System.out.println("7] Guest Check-Out & Generate Invoice");
            System.out.println("8] Exit Application safely");

            int menuChoice = validator.readInt("Select operation index [1-8]", 1, 8);

            switch (menuChoice) {
                case 1 -> displayInventory(hotelService);
                case 2 -> runSearchWorkflow(hotelService, validator);
                case 3 -> runBookingWorkflow(hotelService, validator);
                case 4 -> runViewBookingWorkflow(hotelService, validator);
                case 5 -> runCancellationWorkflow(hotelService, validator);
                case 6 -> runCheckInWorkflow(hotelService, validator);
                case 7 -> runCheckOutWorkflow(hotelService, validator);
                case 8 -> {
                    System.out.println("\nShutting down engine core operations. Data backup complete. Goodbye!");
                    scanner.close();
                    System.exit(0);
                }
            }
        }
    }

    private static void displayInventory(HotelService service) {
        System.out.println("\n--- COMPLETE ROOMS INVENTORY STATUS ---");
        System.out.printf("%-12s %-15s %-15s %-12s%n", "Room Number", "Category Type", "Current Status", "Nightly Price");
        System.out.println("-----------------------------------------------------------------");
        for (Room r : service.displayAllRoomsInventory()) {
            System.out.printf("%-12d %-15s %-15s ₹%-12s%n",
                    r.getNumber(), r.getType().getLabel(), r.getStatus(), r.getType().getNightlyRate().toPlainString());
        }
    }

    private static void runSearchWorkflow(HotelService service, InputValidator val) {
        RoomType type = val.pickRoomType();
        LocalDate in = val.readDate("Enter Proposed Check-In Date");
        LocalDate out = val.readDate("Enter Proposed Check-Out Date");
        if (!val.validateDateRange(in, out)) return;

        List<Room> freeRooms = service.findAvailableRooms(type, in, out);
        if (freeRooms.isEmpty()) {
            System.out.println("❌ No vacant rooms found matching your dates and room tier choices.");
        } else {
            System.out.println("\n✅ Match Found! Available Rooms:");
            for (Room r : freeRooms) {
                System.out.printf(" • Room #%d [%s] - Rate: ₹%s/night%n", r.getNumber(), r.getType().getLabel(), r.getType().getNightlyRate());
            }
        }
    }

    private static void runBookingWorkflow(HotelService service, InputValidator val) {
        RoomType type = val.pickRoomType();
        LocalDate in = val.readDate("Enter Check-In Date");
        LocalDate out = val.readDate("Enter Check-Out Date");
        if (!val.validateDateRange(in, out)) return;

        List<Room> freeRooms = service.findAvailableRooms(type, in, out);
        if (freeRooms.isEmpty()) {
            System.out.println("❌ Operational Block: No inventory matching requirements for these dates.");
            return;
        }

        System.out.println("\nSelect a specific room number below:");
        List<Integer> allowedNumbers = freeRooms.stream().map(Room::getNumber).toList();
        for (Room r : freeRooms) {
            System.out.printf(" -> Room Number: %d (₹%s/night)%n", r.getNumber(), r.getType().getNightlyRate());
        }

        int selectedRoom = val.readRoomChoice("Confirm Allocation Choice Room Number", allowedNumbers);
        
        System.out.println("\n--- Guest Information Records ---");
        String name = val.readNonEmptyString("Enter Guest Full Legal Name");
        String phone = val.readPhoneNumber("Enter 10-Digit Phone Number");
        String email = val.readEmail("Enter Primary Contact Email");

        Guest guest = new Guest(name, phone, email);
        int bId = service.bookRoom(selectedRoom, guest, in, out);

        System.out.println("\n🎉 Reservation Registered Successfully!");
        Booking b = service.getBookingDetails(bId);
        printBookingSummary(b);
        FileManager.appendBookingRecord(b); // Log initial book action
    }

    private static void runViewBookingWorkflow(HotelService service, InputValidator val) {
        int id = val.readInt("Enter Transaction Booking Reference ID", 1000, 99999);
        Booking b = service.getBookingDetails(id);
        if (b == null) {
            System.out.println("❌ Reference entry does not exist.");
            return;
        }
        printBookingSummary(b);
    }

    private static void runCancellationWorkflow(HotelService service, InputValidator val) {
        int id = val.readInt("Enter Reservation ID to Cancel", 1000, 99999);
        Booking b = service.getBookingDetails(id);
        if (b == null) return;
        
        if (service.cancelReservation(id)) {
            System.out.println("✅ Reservation has been successfully cancelled.");
            FileManager.appendBookingRecord(b); // Log state change
        }
    }

    private static void runCheckInWorkflow(HotelService service, InputValidator val) {
        int id = val.readInt("Enter Check-In Target Reservation ID", 1000, 99999);
        Booking b = service.getBookingDetails(id);
        if (b == null) return;

        if (service.executeCheckIn(id)) {
            System.out.println("✅ System updated. Guest is now safely checked into Room #" + b.getRoomNumber());
            FileManager.appendBookingRecord(b); // Log check-in change
        }
    }

    private static void runCheckOutWorkflow(HotelService service, InputValidator val) {
        int id = val.readInt("Enter Check-Out Target Reservation ID", 1000, 99999);
        Booking b = service.getBookingDetails(id);
        if (b == null) return;

        if (service.executeCheckOut(id)) {
            System.out.println("✅ Room cleared out successfully.");
            printFinalInvoice(b);
            FileManager.appendBookingRecord(b); // Log finalized invoice checkout state
        }
    }

    private static void printBookingSummary(Booking b) {
        System.out.println("=================================================");
        System.out.println("📄 RESERVATION MANIFEST SUMMARY");
        System.out.println("=================================================");
        System.out.printf("Booking Reference ID : %d%n", b.getId());
        System.out.printf("Assigned Room Number : %d%n", b.getRoomNumber());
        System.out.printf("Primary Guest Name   : %s%n", b.getGuest().getFullName());
        System.out.printf("Current Status       : %s%n", b.getStatus());
        System.out.printf("Stay Schedule Window : %s to %s (%d Nights)%n", 
                b.getCheckIn().format(DTF), b.getCheckOut().format(DTF), b.getNights());
        System.out.printf("Total Calculated Bill: ₹%s%n", b.getTotal().toPlainString());
        System.out.println("=================================================");
    }

    private static void printFinalInvoice(Booking b) {
        System.out.println("\n=================================================");
        System.out.println("🧾 FINAL ITEMISED STATEMENT & INVOICE");
        System.out.println("=================================================");
        System.out.printf("Invoice Reference   : INV-%d%n", b.getId());
        System.out.printf("Guest Client Name   : %s%n", b.getGuest().getFullName());
        System.out.printf("Room Occupied       : Room %d%n", b.getRoomNumber());
        System.out.printf("Timeline Frame      : %s to %s%n", b.getCheckIn().format(DTF), b.getCheckOut().format(DTF));
        System.out.printf("Total Duration      : %d Nights%n", b.getNights());
        System.out.println("-------------------------------------------------");
        System.out.printf("Daily Room Cost Rate: ₹%15s%n", b.getNightlyRate().toPlainString());
        System.out.printf("Subtotal Room Rent  : ₹%15s%n", b.getRoomCharge().toPlainString());
        System.out.printf("Tax Surcharge (10%%) : ₹%15s%n", b.getTax().toPlainString());
        System.out.println("-------------------------------------------------");
        System.out.printf("GRAND TOTAL CHARGED : ₹%15s%n", b.getTotal().toPlainString());
        System.out.println("=================================================");
        System.out.println("       Thank you for choosing Grand Horizon!       ");
        System.out.println("=================================================\n");
    }
}
