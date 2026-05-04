import exception.AutoServiceException;
import exception.ClientNotFoundException;
import model.Appointment;
import model.Car;
import model.Client;
import model.Service;
import repository.*;
import repository.impl.*;
import service.AutoServiceAppointmentService;
import service.ClientManagementService;
import service.ReportingService;

import java.util.List;
import java.util.Scanner;

//[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
// [Console]::InputEncoding = [System.Text.Encoding]::UTF8
//java -cp out/production/laba_1_mins AutoServiceConsoleApp
public class AutoServiceConsoleApp {
    private final ClientManagementService clientService;
    private final AutoServiceAppointmentService appointmentService;
    private final ReportingService reportingService;
    private final Scanner scanner;

    public AutoServiceConsoleApp() {

        ClientRepository clientRepo = new InMemoryClientRepository();
        CarRepository carRepo = new InMemoryCarRepository();
        ServiceRepository serviceRepo = new InMemoryServiceRepository();
        AppointmentRepository appointmentRepo = new InMemoryAppointmentRepository();
        HistoryRepository historyRepo = new InMemoryHistoryRepository();


        initializeData(clientRepo, carRepo, serviceRepo, appointmentRepo);


        this.clientService = new ClientManagementService(clientRepo, carRepo);
        this.appointmentService = new AutoServiceAppointmentService(carRepo, serviceRepo, appointmentRepo, historyRepo);
        this.reportingService = new ReportingService(appointmentRepo, historyRepo);
        this.scanner = new Scanner(System.in);
    }

    private void initializeData(ClientRepository clientRepo, CarRepository carRepo,
                                ServiceRepository serviceRepo, AppointmentRepository appointmentRepo) {

        serviceRepo.addService(new Service("Замена масла", 1500.0, 30));
        serviceRepo.addService(new Service("Шиномонтаж", 2000.0, 60));
        serviceRepo.addService(new Service("Диагностика двигателя", 2500.0, 45));
        serviceRepo.addService(new Service("Замена тормозных колодок", 3000.0, 90));
        serviceRepo.addService(new Service("Регулировка развала-схождения", 1800.0, 50));
        serviceRepo.addService(new Service("Замена ремня ГРМ", 5000.0, 120));
        serviceRepo.addService(new Service("Заправка кондиционера", 2200.0, 40));
        serviceRepo.addService(new Service("Чистка инжектора", 2800.0, 70));


        Client client1 = new Client("Иван Петров", "+7(999)123-45-67", "ivan@mail.ru");
        Client client2 = new Client("Мария Сидорова", "+7(999)234-56-78", "maria@mail.ru");
        Client client3 = new Client("Алексей Иванов", "+7(999)345-67-89", "alexey@mail.ru");

        clientRepo.addClient(client1);
        clientRepo.addClient(client2);
        clientRepo.addClient(client3);


        Car car1 = new Car("А123ВС77", "Toyota", "Camry", 2018, client1);
        Car car2 = new Car("В456ЕК99", "Hyundai", "Solaris", 2020, client1);
        Car car3 = new Car("С789МР50", "Kia", "Rio", 2019, client2);
        Car car4 = new Car("Х012НТ77", "Lada", "Vesta", 2021, client3);

        carRepo.addCar(car1);
        carRepo.addCar(car2);
        carRepo.addCar(car3);
        carRepo.addCar(car4);


        try {
            Service oilChange = serviceRepo.findServiceByName("Замена масла");
            Service diagnostics = serviceRepo.findServiceByName("Диагностика двигателя");
            Service tires = serviceRepo.findServiceByName("Шиномонтаж");
            Service brakes = serviceRepo.findServiceByName("Замена тормозных колодок");


            String today = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            Appointment app1 = new Appointment(car1, oilChange, today + " 10:00");
            Appointment app2 = new Appointment(car3, tires, today + " 11:30");
            Appointment app3 = new Appointment(car4, diagnostics, today + " 14:00");


            Appointment app4 = new Appointment(car2, brakes, "2026-04-25 15:00");
            Appointment app5 = new Appointment(car1, diagnostics, "2026-04-26 09:00");

            appointmentRepo.scheduleAppointment(app1);
            appointmentRepo.scheduleAppointment(app2);
            appointmentRepo.scheduleAppointment(app3);
            appointmentRepo.scheduleAppointment(app4);
            appointmentRepo.scheduleAppointment(app5);

        } catch (Exception e) {
            System.out.println("Ошибка при инициализации записей: " + e.getMessage());
        }
    }

    public void run() {
        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        createAppointment();
                        break;
                    case "2":
                        completeAppointment();
                        break;
                    case "3":
                        reportingService.printTodaySchedule(getCurrentDate());
                        break;
                    case "4":
                        reportingService.printWorkHistory();
                        break;
                    case "5":
                        reportingService.calculateTotalEarnings();
                        break;
                    case "6":
                        registerNewClient();
                        break;
                    case "7":
                        registerNewCar();
                        break;
                    case "8":
                        listAllClients();
                        break;
                    case "9":
                        listAllAppointments();
                        break;
                    case "0":
                        System.out.println("До свидания!");
                        return;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (AutoServiceException e) {
                System.out.println("ОШИБКА: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("НЕПРЕДВИДЕННАЯ ОШИБКА: " + e.getMessage());
            }

            System.out.println("\nНажмите Enter для продолжения...");
            scanner.nextLine();
        }
    }

    private void printMenu() {
        System.out.println("\n========== АВТОСЕРВИС ==========");
        System.out.println("1. Записать автомобиль на ремонт");
        System.out.println("2. Отметить выполнение работы");
        System.out.println("3. Показать расписание на сегодня");
        System.out.println("4. Показать историю работ");
        System.out.println("5. Показать общую выручку");
        System.out.println("6. Зарегистрировать нового клиента");
        System.out.println("7. Зарегистрировать новый автомобиль");
        System.out.println("8. Список всех клиентов");
        System.out.println("9. Список всех записей");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void createAppointment() throws AutoServiceException {
        System.out.print("Введите госномер автомобиля: ");
        String plate = scanner.nextLine();
        System.out.print("Введите название услуги: ");
        String service = scanner.nextLine();
        System.out.print("Введите дату и время (ГГГГ-ММ-ДД ЧЧ:ММ): ");
        String dateTime = scanner.nextLine();

        appointmentService.createAppointment(plate, service, dateTime);
        System.out.println("Запись успешно создана!");
    }

    private void completeAppointment() {
        List<Appointment> appointments = appointmentService.getAllAppointments()
                .stream()
                .filter(a -> !a.isCompleted())
                .collect(java.util.stream.Collectors.toList());

        if (appointments.isEmpty()) {
            System.out.println("Нет незавершенных записей");
            return;
        }

        System.out.println("Незавершенные записи:");
        for (int i = 0; i < appointments.size(); i++) {
            System.out.println((i + 1) + ". " + appointments.get(i));
        }

        System.out.print("Выберите номер записи для завершения: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;

        if (choice >= 0 && choice < appointments.size()) {
            appointmentService.completeAppointment(appointments.get(choice));
            System.out.println("Работа отмечена как выполненная!");
        }
    }

    private void registerNewClient() {
        System.out.print("Введите имя клиента: ");
        String name = scanner.nextLine();
        System.out.print("Введите телефон: ");
        String phone = scanner.nextLine();
        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        clientService.registerClient(name, phone, email);
        System.out.println("Клиент зарегистрирован!");
    }

    private void registerNewCar() throws ClientNotFoundException {
        System.out.print("Введите госномер: ");
        String plate = scanner.nextLine();
        System.out.print("Введите марку: ");
        String brand = scanner.nextLine();
        System.out.print("Введите модель: ");
        String model = scanner.nextLine();
        System.out.print("Введите год выпуска: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Введите телефон владельца: ");
        String phone = scanner.nextLine();

        clientService.registerCar(plate, brand, model, year, phone);
        System.out.println("Автомобиль зарегистрирован!");
    }

    private void listAllClients() {
        System.out.println("\n=== КЛИЕНТЫ ===");
        clientService.getAllClients().forEach(System.out::println);
    }

    private void listAllAppointments() {
        System.out.println("\n=== ВСЕ ЗАПИСИ ===");
        appointmentService.getAllAppointments().forEach(System.out::println);
    }

    private String getCurrentDate() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    }

    public static void main(String[] args) {
        new AutoServiceConsoleApp().run();
    }
}