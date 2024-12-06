package bank.exceptions;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(double withdrawalAmount, double availableBalance) {
        super("Insufficient funds for withdrawal. Requested: " + withdrawalAmount
                + ", Available: " + availableBalance);
    }
}
