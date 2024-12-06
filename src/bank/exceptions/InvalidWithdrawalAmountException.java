package bank.exceptions;

public class InvalidWithdrawalAmountException extends Exception {
    public InvalidWithdrawalAmountException(double amount, String reason) {
        super("Invalid withdrawal amount: " + amount + ". Reason: " + reason);
    }
}