package repository;

import exception.CarNotFoundException;
import model.Car;
import model.Client;

import java.util.List;

public interface CarRepository {
    void addCar(Car car);
    Car findCarByLicensePlate(String licensePlate) throws CarNotFoundException;
    List<Car> getCarsByClient(Client client);
    List<Car> getAllCars();
}