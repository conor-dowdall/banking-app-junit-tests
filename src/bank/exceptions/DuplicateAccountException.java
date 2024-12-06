package bank.exceptions;

public class DuplicateAccountException extends Exception {
    public DuplicateAccountException(String accountHolder) {
        super("An account with these details already exits: " + accountHolder);
    }

}
