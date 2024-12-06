package bank.exceptions;

public class InsufficientReservesException extends Exception {
    public InsufficientReservesException(double requestedAmount, double availableReserves) {
        super("Insufficient reserves. Requested: " + requestedAmount + ", Available: " + availableReserves);
    }
}
