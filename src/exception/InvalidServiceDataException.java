package exception;

public class InvalidServiceDataException extends AutoServiceException {
    public InvalidServiceDataException(String field) {
        super("Некорректные данные: " + field);
    }
}