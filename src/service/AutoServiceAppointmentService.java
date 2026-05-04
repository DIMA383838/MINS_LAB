package service;

import exception.AppointmentConflictException;
import exception.CarNotFoundException;
import model.Appointment;
import model.Car;
import model.Service;
import model.WorkHistory;
import repository.AppointmentRepository;
import repository.CarRepository;
import repository.HistoryRepository;
import repository.ServiceRepository;

import java.util.List;

public class AutoServiceAppointmentService {
    private final CarRepository carRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;
    private final HistoryRepository historyRepository;


    public AutoServiceAppointmentService(
            CarRepository carRepository,
            ServiceRepository serviceRepository,
            AppointmentRepository appointmentRepository,
            HistoryRepository historyRepository) {
        this.carRepository = carRepository;
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
        this.historyRepository = historyRepository;
    }

    public void createAppointment(String licensePlate, String serviceName, String dateTime)
            throws CarNotFoundException, AppointmentConflictException {

        Car car = carRepository.findCarByLicensePlate(licensePlate);

        Service service = serviceRepository.findServiceByName(serviceName);
        if (service == null) {
            throw new IllegalArgumentException("Услуга не найдена: " + serviceName);
        }

        Appointment appointment = new Appointment(car, service, dateTime);

        appointmentRepository.scheduleAppointment(appointment);
    }

    public void completeAppointment(Appointment appointment) {

        appointmentRepository.markAppointmentCompleted(appointment);


        WorkHistory history = new WorkHistory(
                appointment,
                "Выполнены работы по " + appointment.getService().getName(),
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date())
        );
        historyRepository.addToHistory(history);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.getAllAppointments();
    }
}

