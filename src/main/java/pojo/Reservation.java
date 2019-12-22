package pojo;

public class Reservation {
// ===========================================================
// Constants & Variables
// ===========================================================
    private int ageId = -1;
    private int amount_of_trips = 1;
    private String custName;
    private String pay_status;
    private int departureId;
    private int toStopId;

// ===========================================================
// Constructors
// ===========================================================
    public Reservation(int ageId, int amount_of_trips, String custName, String pay_status, int departureId, int toStopId) {
        this.ageId = ageId;
        this.amount_of_trips = amount_of_trips;
        this.custName = custName;
        this.pay_status = pay_status;
        this.departureId = departureId;
        this.toStopId = toStopId;
    }

// ===========================================================
// Getters & Setters
// ===========================================================
    public int getAgeId() { return ageId; }
    public void setAgeId(int ageId) { this.ageId = ageId; }
    public int getAmount_of_trips() { return amount_of_trips; }
    public void setAmount_of_trips(int amount_of_trips) { this.amount_of_trips = amount_of_trips; }
    public String getCustName() { return custName; }
    public void setCustName(String custName) { this.custName = custName; }
    public String getPay_status() { return pay_status; }
    public void setPay_status(String pay_status) { this.pay_status = pay_status; }
    public int getDepartureId() { return departureId; }
    public void setDepartureId(int departureId) { this.departureId = departureId; }
    public int getToStopId() { return toStopId; }
    public void setToStopId(int toStopId) { this.toStopId = toStopId; }
}
