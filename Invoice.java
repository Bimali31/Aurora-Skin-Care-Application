public class Invoice {
    private final Appointment appointment;
    private final double taxRate = 0.025;

    public Invoice(Appointment appointment) {
        this.appointment = appointment;
    }

    public double calculateTotal() {
        double treatmentCost = appointment.getTreatment().getPrice();
        double tax = treatmentCost * taxRate;
        return Math.round((treatmentCost + tax + appointment.getRegistrationFee()) * 100.0) / 100.0;
    }

    public void generateInvoice() {
        System.out.println("Invoice for Appointment ID: " + appointment.getAppointmentId());
        System.out.println("Patient: " + appointment.getPatient().getName());
        System.out.println("Dermatologist: " + appointment.getDermatologist().getName());
        System.out.println("Treatment: " + appointment.getTreatment().getName());
        System.out.println("Registration Fee: LKR " + appointment.getRegistrationFee());
        System.out.println("Total with tax: LKR " + calculateTotal());
    }
}
