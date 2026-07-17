package utility;

import model.RoomType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class InputValidator {
    private final Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("❌ Invalid input. Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid choice. Please enter a valid whole number.");
            }
        }
    }

    public int readRoomChoice(String prompt, List<Integer> allowedRoomNumbers) {
        Set<Integer> validSet = allowedRoomNumbers.stream().collect(Collectors.toSet());
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (!validSet.contains(value)) {
                    System.out.println("❌ Choice must be one of the available options: " + allowedRoomNumbers);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a valid room number.");
            }
        }
    }

    public String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("❌ Value cannot be empty.");
                continue;
            }
            return input;
        }
    }

    public String readPhoneNumber(String prompt) {
        // Basic pattern matching for standard 10-digit mobile numbers
        while (true) {
            String input = readNonEmptyString(prompt);
            if (input.matches("\\d{10}")) {
                return input;
            }
            System.out.println("❌ Invalid phone number. Must be exactly a 10-digit number.");
        }
    }

    public String readEmail(String prompt) {
        // Standard concise regex format for email structural checks
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        while (true) {
            String input = readNonEmptyString(prompt);
            if (input.matches(emailRegex)) {
                return input;
            }
            System.out.println("❌ Invalid email format. Use pattern user@domain.com.");
        }
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMATTER);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("❌ Date cannot be in the past. Today is " + LocalDate.now());
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid format. Please match the exact pattern YYYY-MM-DD (e.g., 2026-05-24).");
            }
        }
    }

    public boolean validateDateRange(LocalDate checkIn, LocalDate checkOut) {
        if (!checkOut.isAfter(checkIn)) {
            System.out.println("❌ Logical error: Checkout date must fall at least one day after the check-in date.");
            return false;
        }
        return true;
    }

    public RoomType pickRoomType() {
        System.out.println("\n--- Available Categories ---");
        RoomType[] types = RoomType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("%d] %s (Base Rate: ₹%s/night)%n", i + 1, types[i].getLabel(), types[i].getNightlyRate().toPlainString());
        }
        int option = readInt("Select Category Index", 1, types.length);
        return types[option - 1];
    }
}

