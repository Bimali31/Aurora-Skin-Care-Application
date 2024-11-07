import java.time.LocalDate;
import java.util.UUID;

public class Appointment {
    private final UUID appointmentId;
    private final Patient patient;
    private final Dermatologist dermatologist;
    private final Treatment treatment;
    private LocalDate appointmentDate;
    private String time;
    private final double registrationFee;

    public Appointment(Patient patient, Dermatologist dermatologist, Treatment treatment, LocalDate appointmentDate, String time) {
        this.appointmentId = UUID.randomUUID();
        this.patient = patient;
        this.dermatologist = dermatologist;
        this.treatment = treatment;
        this.appointmentDate = appointmentDate;
        this.time = time;
        this.registrationFee = 500.0;
    }

    public UUID getAppointmentId() { return appointmentId; }
    public Patient getPatient() { return patient; }
    public Dermatologist getDermatologist() { return dermatologist; }
    public Treatment getTreatment() { return treatment; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public String getTime() { return time; }
    public double getRegistrationFee() { return registrationFee; }
    
    public void updateAppointmentDetails(LocalDate newDate, String newTime) {
        this.appointmentDate = newDate;
        this.time = newTime;
    }
}
