package taskmanager.taskmanager.exceptions;


public class NotExistentUser extends Exception {

    public NotExistentUser() {
        super("User does not exist.");
    }
}