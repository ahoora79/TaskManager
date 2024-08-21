package taskmanager.taskmanager.exceptions;


public class UsernameAlreadyTaken extends Exception {
    public UsernameAlreadyTaken() {
        super("The username is already taken.");
    }
}