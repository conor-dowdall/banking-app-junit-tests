package bank;

import bank.exceptions.InsufficientFundsException;
import bank.exceptions.InvalidLoanAmountException;

public class Account {

    private String accountHolder;
    private double accountBalance;
    private double loanBalance = 0.0;

    public Account(String accountHolder, double balance) {
        this.accountHolder = accountHolder;
        this.accountBalance = balance;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void checkAmountInAccount(double amount)
            throws InsufficientFundsException {
        if (accountBalance < amount)
            throw new InsufficientFundsException(amount, accountBalance);
    }

    public void deposit(double amount) {
        accountBalance += amount;
    }

    public void withdraw(double amount)
            throws InsufficientFundsException {
        checkAmountInAccount(amount);
        accountBalance -= amount;
    }

    public double getLoanBalance() {
        return loanBalance;
    }

    public void checkAmountInLoanBalance(double amount)
            throws InvalidLoanAmountException {
        if (amount > loanBalance)
            throw new InvalidLoanAmountException(amount, "Repayment amount exceeds loan balance");
    }

    public void addToLoanBalance(double amount) {
        loanBalance += amount;
    }

    public void subtractFromLoanBalance(double amount)
            throws InvalidLoanAmountException {
        checkAmountInLoanBalance(amount);
        loanBalance -= amount;
    }

}
