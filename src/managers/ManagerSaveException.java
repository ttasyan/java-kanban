package managers;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {

    }

    public ManagerSaveException(String message, Throwable e) {
        super(message, e);
    }
}
