package bank;

import bank.exceptions.InsufficientFundsException;
import bank.exceptions.InvalidLoanAmountException;

/**
 * Represents a bank account for a customer, with functionalities to deposit,
 * withdraw, and manage loan balances.
 * <p>
 * This class handles the core operations associated with an individual bank
 * account, including checking the account balance, depositing and withdrawing
 * funds, as well as managing the loan balance. It throws exceptions when
 * operations are not permissible due to insufficient funds or loan balance.
 * </p>
 */
public class Account {

    private String accountHolder;
    private double accountBalance;
    private double loanBalance = 0.0;

    /**
     * Constructs a new {@link Account} for the specified account holder with an
     * initial balance.
     * 
     * @param accountHolder the name of the account holder
     * @param balance       the initial balance of the account
     */
    public Account(String accountHolder, double balance) {
        this.accountHolder = accountHolder;
        this.accountBalance = balance;
    }

    /**
     * Retrieves the account holder's name.
     *
     * @return the name of the account holder
     */
    public String getAccountHolder() {
        return accountHolder;
    }

    /**
     * Retrieves the current balance of the account.
     *
     * @return the current account balance
     */
    public double getAccountBalance() {
        return accountBalance;
    }

    /**
     * Checks whether the specified amount is available in the account balance.
     * 
     * @param amount the amount to check
     * @throws InsufficientFundsException if the amount exceeds the current account
     *                                    balance
     */
    public void checkAmountInAccount(double amount)
            throws InsufficientFundsException {
        if (accountBalance < amount)
            throw new InsufficientFundsException(amount, accountBalance);
    }

    /**
     * Deposits the specified amount into the account, increasing the account
     * balance.
     *
     * @param amount the amount to deposit
     */
    public void deposit(double amount) {
        accountBalance += amount;
    }

    /**
     * Withdraws the specified amount from the account, reducing the account
     * balance.
     * 
     * @param amount the amount to withdraw
     * @throws InsufficientFundsException if the withdrawal amount exceeds the
     *                                    current balance
     */
    public void withdraw(double amount)
            throws InsufficientFundsException {
        checkAmountInAccount(amount);
        accountBalance -= amount;
    }

    /**
     * Retrieves the current loan balance for the account holder.
     *
     * @return the current loan balance
     */
    public double getLoanBalance() {
        return loanBalance;
    }

    /**
     * Checks whether the specified repayment amount exceeds the current loan
     * balance.
     *
     * @param amount the amount to check
     * @throws InvalidLoanAmountException if the repayment amount exceeds the loan
     *                                    balance
     */
    public void checkAmountInLoanBalance(double amount)
            throws InvalidLoanAmountException {
        if (amount > loanBalance)
            throw new InvalidLoanAmountException(amount, "Repayment amount exceeds loan balance");
    }

    /**
     * Adds the specified amount to the loan balance.
     *
     * @param amount the amount to add to the loan balance
     */
    public void addToLoanBalance(double amount) {
        loanBalance += amount;
    }

    /**
     * Subtracts the specified amount from the loan balance.
     *
     * @param amount the amount to subtract from the loan balance
     * @throws InvalidLoanAmountException if the repayment amount exceeds the loan
     *                                    balance
     */
    public void subtractFromLoanBalance(double amount)
            throws InvalidLoanAmountException {
        checkAmountInLoanBalance(amount);
        loanBalance -= amount;
    }

}
