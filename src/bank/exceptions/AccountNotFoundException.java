package bank.exceptions;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String accountHolder) {
        super("No account found for account holder: " + accountHolder);
    }
}
