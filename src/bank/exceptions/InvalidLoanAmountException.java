package bank.exceptions;

public class InvalidLoanAmountException extends Exception {
    public InvalidLoanAmountException(double amount, String reason) {
        super("Invalid loan amount: " + amount + ". Reason: " + reason);
    }
}
