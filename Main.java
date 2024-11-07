import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Appointment> appointments = new ArrayList<>();

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            // Define Dermatologists
            Dermatologist[] dermatologists = {
                new Dermatologist("Dr. Silva", new String[]{"Monday", "Wednesday", "Friday", "Saturday"}),
                new Dermatologist("Dr. Fernando", new String[]{"Monday", "Wednesday", "Friday", "Saturday"})
            };
            
            // Define Treatments
            Treatment[] treatments = {
                new Treatment("Acne Treatment", 2750.00),
                new Treatment("Skin Whitening", 7650.00),
                new Treatment("Mole Removal", 3850.00),
                new Treatment("Laser Treatment", 12500.00)
            };
            
            // Loop for user interaction
            boolean exit = false;
            while (!exit) {
                System.out.println("\n----- Aurora Skin Care -----");
                System.out.println("1. Make Appointment");
                System.out.println("2. Update Appointment");
                System.out.println("3. View Appointments by Date");
                System.out.println("4. Search Appointment");
                System.out.println("5. Generate Invoice");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");
                int option = sc.nextInt();
                
                switch (option) {
                    case 1 -> makeAppointment(sc, dermatologists, treatments);
                    case 2 -> updateAppointment(sc);
                    case 3 -> viewAppointmentsByDate(sc);
                    case 4 -> searchAppointment(sc);
                    case 5 -> generateInvoice(sc);
                    case 6 -> {
                        exit = true;
                        System.out.println("Exiting...");
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }

    // Method to make an appointment
    public static void makeAppointment(Scanner sc, Dermatologist[] dermatologists, Treatment[] treatments) {
        sc.nextLine(); // consume newline
    
        // Ask for registration fee payment confirmation
        System.out.println("To book an appointment, a registration fee of Rs.500 is required.");
        System.out.print("Enter the amount you are paying: ");
        double registrationFee = sc.nextDouble();
        
        if (registrationFee != 500.00) {
            System.out.println("Invalid amount. Appointment cannot be booked without the correct registration fee.");
            return;  // Cancel the appointment booking process
        }
        
        sc.nextLine(); // consume newline after entering fee
    
        // Collect patient details
        System.out.print("Enter NIC: ");
        String nic = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();
    
        Patient patient = new Patient(nic, name, email, phone);
    
        // Select Dermatologist
        System.out.println("Select Dermatologist:");
        for (int i = 0; i < dermatologists.length; i++) {
            System.out.println((i + 1) + ". " + dermatologists[i].getName());
        }
        int dermoChoice = sc.nextInt();
        Dermatologist dermatologist = dermatologists[dermoChoice - 1];
    
        // Select Appointment Date
        LocalDate chosenDate = selectAppointmentDate(sc);
        if (chosenDate == null) {
            return; // Exit if date is invalid
        }
    
        // Provide available time slots based on the selected date
        LocalTime[] availableSlots = getAvailableSlotsForDay(chosenDate.getDayOfWeek().toString());
        System.out.println("Available Time Slots:");
        for (int i = 0; i < availableSlots.length; i++) {
            System.out.println((i + 1) + ". " + availableSlots[i]);
        }
        int slotChoice = sc.nextInt();
        LocalTime chosenTime = availableSlots[slotChoice - 1];
    
        // Select Treatment
        System.out.println("Select Treatment:");
        for (int i = 0; i < treatments.length; i++) {
            System.out.println((i + 1) + ". " + treatments[i].getName() + " - LKR " + treatments[i].getPrice());
        }
        int treatmentChoice = sc.nextInt();
        Treatment treatment = treatments[treatmentChoice - 1];
    
        // Check if the same treatment is already booked at the same time and date
        if (isSlotBooked(chosenDate, chosenTime, treatment)) {
            System.out.println("This slot has already been reserved for the same treatment on the same day and time.");
            return;  // Exit if slot is booked
        }
    
        // Create the appointment
        Appointment appointment = new Appointment(patient, dermatologist, treatment, chosenDate, chosenTime.toString());
        appointments.add(appointment);
    
        System.out.println("\nAppointment Created. ID: " + appointment.getAppointmentId());
    }
    
    // Method to allow only specific days for appointment selection
    public static LocalDate selectAppointmentDate(Scanner sc) {
        System.out.println("Select Appointment Date (Monday, Wednesday, Friday, Saturday only): ");
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String dateInput = sc.next();
        LocalDate chosenDate;
        try {
            chosenDate = LocalDate.parse(dateInput);
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return null;
        }
    
        // Check if the date falls on Monday, Wednesday, Friday, or Saturday
        String dayOfWeek = chosenDate.getDayOfWeek().toString();
        if (!dayOfWeek.equals("MONDAY") && !dayOfWeek.equals("WEDNESDAY") && !dayOfWeek.equals("FRIDAY") && !dayOfWeek.equals("SATURDAY")) {
            System.out.println("Appointments can only be scheduled on Monday, Wednesday, Friday, or Saturday.");
            return null;
        }
    
        return chosenDate;
    }
    
    // Method to check if a slot is already booked for the same treatment, date, and time
    public static boolean isSlotBooked(LocalDate date, LocalTime time, Treatment treatment) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentDate().equals(date) &&
                appointment.getTime().equals(time.toString()) &&
                appointment.getTreatment().getName().equals(treatment.getName())) {
                return true;  // Slot is already booked
            }
        }
        return false;  // Slot is available
    }
    // Method to view appointments filtered by date
    public static void viewAppointmentsByDate(Scanner sc) {
        System.out.print("Enter Date (YYYY-MM-DD) to view appointments: ");
        String dateInput = sc.next();
        LocalDate chosenDate;
        try {
            chosenDate = LocalDate.parse(dateInput);
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return;
        }

        // Ensure that the selected date is Monday, Wednesday, Friday, or Saturday
        String dayOfWeek = chosenDate.getDayOfWeek().toString();
        if (!dayOfWeek.equals("MONDAY") && !dayOfWeek.equals("WEDNESDAY") && !dayOfWeek.equals("FRIDAY") && !dayOfWeek.equals("SATURDAY")) {
            System.out.println("Appointments can only be viewed for Monday, Wednesday, Friday, or Saturday.");
            return;
        }

        boolean appointmentFound = false;
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentDate().equals(chosenDate)) {
                // Display appointment details
                System.out.println("Appointment ID: " + appointment.getAppointmentId());
                System.out.println("Patient Name: " + appointment.getPatient().getName());
                System.out.println("Dermatologist: " + appointment.getDermatologist().getName());
                System.out.println("Treatment: " + appointment.getTreatment().getName());
                System.out.println("Time: " + appointment.getTime());
                System.out.println("-------------------------------");
                appointmentFound = true;
            }
        }

        if (!appointmentFound) {
            System.out.println("No appointments found on " + chosenDate + ".");
        }
    }

    // Method to get available slots based on the day
    public static LocalTime[] getAvailableSlotsForDay(String day) {
        return switch (day) {
            case "MONDAY" -> generateTimeSlots(LocalTime.of(10, 0), LocalTime.of(13, 0));
            case "WEDNESDAY" -> generateTimeSlots(LocalTime.of(14, 0), LocalTime.of(17, 0));
            case "FRIDAY" -> generateTimeSlots(LocalTime.of(16, 0), LocalTime.of(20, 0));
            case "SATURDAY" -> generateTimeSlots(LocalTime.of(9, 0), LocalTime.of(13, 0));
            default -> new LocalTime[0];
        };
    }

    // Method to generate 15-minute interval slots between a start and end time
    public static LocalTime[] generateTimeSlots(LocalTime start, LocalTime end) {
        ArrayList<LocalTime> slots = new ArrayList<>();
        while (start.isBefore(end)) {
            slots.add(start);
            start = start.plusMinutes(15);
        }
        return slots.toArray(LocalTime[]::new); // Use method reference to avoid creating a new array just for conversion
    }

    // Method to update an appointment
    public static void updateAppointment(Scanner sc) {
        System.out.print("Enter Appointment ID: ");
        String id = sc.next();
    
        // Search for the appointment by ID
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().toString().equals(id)) {
                System.out.print("Enter New Date (YYYY-MM-DD): ");
                String newDate = sc.next();
                LocalDate updatedDate;
                try {
                    updatedDate = LocalDate.parse(newDate);
                } catch (Exception e) {
                    System.out.println("Invalid date format.");
                    return;
                }
    
                // Check if the date is on valid clinic days (Monday, Wednesday, Friday, or Saturday)
                String dayOfWeek = updatedDate.getDayOfWeek().toString();
                System.out.print("Enter New Time (HH:mm): ");
                String newTime = sc.next();
    
                // Validate the new time based on the clinic's working hours for the day
                if (!isValidTimeForDay(dayOfWeek, newTime)) {
                    System.out.println("No clinic available at this time.");
                    return;
                }
    
                // Check if the time slot is already booked
                if (isTimeSlotBooked(updatedDate, newTime, appointment.getDermatologist())) {
                    System.out.println("This time slot is already booked for this treatment.");
                    return;
                }
    
                // Update the appointment details
                appointment.updateAppointmentDetails(updatedDate, newTime);
                System.out.println("Appointment updated successfully.");
                return;
            }
        }
        System.out.println("Appointment not found.");
    }
    
    // Helper method to check if the time is valid for the given day of the week
    public static boolean isValidTimeForDay(String dayOfWeek, String newTime) {
        LocalTime time;
        try {
            time = LocalTime.parse(newTime);
        } catch (Exception e) {
            return false; // Invalid time format
        }
    
        return switch (dayOfWeek) {
            case "MONDAY" -> isValidTimeForClinic(time, LocalTime.of(10, 0), LocalTime.of(13, 0), 12);
            case "WEDNESDAY" -> isValidTimeForClinic(time, LocalTime.of(14, 0), LocalTime.of(17, 0), 12);
            case "FRIDAY" -> isValidTimeForClinic(time, LocalTime.of(16, 0), LocalTime.of(20, 0), 16);
            case "SATURDAY" -> isValidTimeForClinic(time, LocalTime.of(9, 0), LocalTime.of(13, 0), 16);
            default -> false;
        }; // Invalid clinic day
    }
    
    // Helper method to validate the time slots based on the clinic's schedule
    public static boolean isValidTimeForClinic(LocalTime time, LocalTime start, LocalTime end, int slots) {
        LocalTime currentTime = start;
    
        for (int i = 0; i < slots; i++) {
            if (time.equals(currentTime)) {
                return true;
            }
            currentTime = currentTime.plusMinutes(15); // 15-minute increments
        }
        return false;
    }
    
    // Helper method to check if the time slot is already booked
    public static boolean isTimeSlotBooked(LocalDate date, String time, Dermatologist dermatologist) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentDate().equals(date)
                && appointment.getTime().equals(time)
                && appointment.getDermatologist().equals(dermatologist)) {
                return true; // Time slot is already booked
            }
        }
        return false;
    }
    

    // Method to search for an appointment by Patient Name or Appointment ID
    public static void searchAppointment(Scanner sc) {
        System.out.print("Enter Patient Name or Appointment ID: ");
        sc.nextLine(); // Consume the leftover newline from previous input
        String query = sc.nextLine().trim(); // Get full input, including spaces
    
        for (Appointment appointment : appointments) {
            // Check if input matches either the Patient Name or the Appointment ID
            if (appointment.getPatient().getName().equalsIgnoreCase(query) ||
                appointment.getAppointmentId().toString().equals(query)) {
                
                // Display appointment details if a match is found
                System.out.println("Appointment ID: " + appointment.getAppointmentId());
                System.out.println("Patient Name: " + appointment.getPatient().getName());
                System.out.println("Dermatologist: " + appointment.getDermatologist().getName());
                System.out.println("Treatment: " + appointment.getTreatment().getName());
                System.out.println("Date: " + appointment.getAppointmentDate());
                System.out.println("Time: " + appointment.getTime());
                return;
            }
        }
        System.out.println("No appointment found for the given query.");
    }

    // Method to generate an invoice (placeholder)
    public static void generateInvoice(Scanner sc) {
        System.out.print("Enter Appointment ID to Generate Invoice: ");
        String id = sc.next();

        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().toString().equals(id)) {
                Invoice invoice = new Invoice(appointment);
                invoice.generateInvoice();
                return;
            }
        }
        System.out.println("Appointment not found.");
    }
}
