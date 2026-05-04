package repository.impl;

import exception.AppointmentConflictException;
import model.Appointment;
import model.Car;
import repository.AppointmentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryAppointmentRepository implements AppointmentRepository {
    private final List<Appointment> appointments = new ArrayList<>();

    @Override
    public void scheduleAppointment(Appointment appointment) throws AppointmentConflictException {

        boolean conflict = appointments.stream()
                .filter(a -> !a.isCompleted())
                .anyMatch(a -> a.getDateTime().equals(appointment.getDateTime()));

        if (conflict) {
            throw new AppointmentConflictException(appointment.getDateTime());
        }
        appointments.add(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsByCar(Car car) {
        return appointments.stream()
                .filter(a -> a.getCar().equals(car))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> getAppointmentsByDate(String date) {
        return appointments.stream()
                .filter(a -> a.getDateTime().startsWith(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    @Override
    public void markAppointmentCompleted(Appointment appointment) {
        appointment.markCompleted();
    }
}
