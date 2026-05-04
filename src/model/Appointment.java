package model;

public class Appointment {
    private Car car;
    private Service service;
    private String dateTime;
    private boolean completed;

    public Appointment(Car car, Service service, String dateTime) {
        this.car = car;
        this.service = service;
        this.dateTime = dateTime;
        this.completed = false;
    }

    public Car getCar() { return car; }
    public Service getService() { return service; }
    public String getDateTime() { return dateTime; }
    public boolean isCompleted() { return completed; }

    public void markCompleted() {
        this.completed = true;
    }

    public double calculateCost() {
        return service.getPrice();
    }

    @Override
    public String toString() {
        String status = completed ? "ВЫПОЛНЕНО" : "ЗАПЛАНИРОВАНО";
        return String.format("[%s] %s: %s -> %s (%s)",
                dateTime, car, service.getName(), status, calculateCost() + " руб.");
    }
}