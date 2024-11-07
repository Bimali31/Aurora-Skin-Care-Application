public class Dermatologist {
    private final String name;
    private final String[] availableTimes;

    public Dermatologist(String name, String[] availableTimes) {
        this.name = name;
        this.availableTimes = availableTimes;
    }

    public String getName() { return name; }
    public String[] getAvailableTimes() { return availableTimes; }
}
