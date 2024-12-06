package bank.exceptions;

public class InvalidDepositAmountException extends Exception {
    public InvalidDepositAmountException(double amount, String reason) {
        super("Invalid deposit amount: " + amount + ". Reason: " + reason);
    }
}
