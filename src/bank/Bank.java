package bank;

import java.util.ArrayList;
import java.util.List;

import bank.exceptions.AccountNotFoundException;
import bank.exceptions.DuplicateAccountException;
import bank.exceptions.InsufficientFundsException;
import bank.exceptions.InsufficientReservesException;
import bank.exceptions.InvalidDepositAmountException;
import bank.exceptions.InvalidLoanAmountException;
import bank.exceptions.InvalidWithdrawalAmountException;

/**
 * Represents a bank with functionality to manage accounts, deposits,
 * withdrawals, loans, and reserves.
 * Provides checks and constraints to ensure valid operations within defined
 * limits.
 */
public class Bank {

    private double maxDeposit;
    private double maxWithdrawal;
    private double maxLoan;

    private List<Account> accounts = new ArrayList<>();
    private double reserves = 0;

    /**
     * Constructs a Bank instance with specified operational limits.
     *
     * @param maxDeposit    the maximum allowable deposit amount
     * @param maxWithdrawal the maximum allowable withdrawal amount
     * @param maxLoan       the maximum allowable loan amount
     */
    public Bank(double maxDeposit, double maxWithdrawal, double maxLoan) {
        this.maxDeposit = maxDeposit;
        this.maxWithdrawal = maxWithdrawal;
        this.maxLoan = maxLoan;
    }

    /**
     * @return the maximum deposit limit.
     */
    public double getMaxDeposit() {
        return maxDeposit;
    }

    /**
     * @return the maximum withdrawal limit.
     */
    public double getMaxWithdrawal() {
        return maxWithdrawal;
    }

    /**
     * @return the maximum loan limit.
     */
    public double getMaxLoan() {
        return maxLoan;
    }

    /**
     * @return the list of accounts in the bank.
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * @return the current reserve amount.
     */
    public double getReserves() {
        return reserves;
    }

    /**
     * Sets the maximum deposit limit.
     *
     * @param maxDeposit the new maximum deposit limit
     */
    public void setMaxDeposit(double maxDeposit) {
        this.maxDeposit = maxDeposit;
    }

    /**
     * Sets the maximum withdrawal limit.
     *
     * @param maxWithdrawal the new maximum withdrawal limit
     */
    public void setMaxWithdrawal(double maxWithdrawal) {
        this.maxWithdrawal = maxWithdrawal;
    }

    /**
     * Sets the maximum loan limit.
     *
     * @param maxLoan the new maximum loan limit
     */
    public void setMaxLoan(double maxLoan) {
        this.maxLoan = maxLoan;
    }

    /**
     * Validates the deposit amount against bank constraints.
     *
     * @param amount the deposit amount to check
     * @throws InvalidDepositAmountException if the amount is invalid
     */
    public void checkDepositAmount(double amount)
            throws InvalidDepositAmountException {
        if (amount <= 0)
            throw new InvalidDepositAmountException(amount, "Amount must be greater than zero");
        if (amount > maxDeposit)
            throw new InvalidDepositAmountException(amount, "Amount exceeds the maximum allowed deposit limit");
    }

    /**
     * Validates the withdrawal amount against bank constraints.
     *
     * @param amount the withdrawal amount to check
     * @throws InvalidWithdrawalAmountException if the amount is invalid
     */
    public void checkWithdrawalAmount(double amount)
            throws InvalidWithdrawalAmountException {
        if (amount <= 0)
            throw new InvalidWithdrawalAmountException(amount, "Amount must be greater than zero");
        if (amount > maxWithdrawal)
            throw new InvalidWithdrawalAmountException(amount, "Amount exceeds the maximum allowed withdrawal limit");
    }

    /**
     * Validates the loan amount against bank constraints.
     *
     * @param amount the loan amount to check
     * @throws InvalidLoanAmountException if the amount is invalid
     */
    public void checkLoanAmount(double amount)
            throws InvalidLoanAmountException {
        if (amount <= 0)
            throw new InvalidLoanAmountException(amount, "Amount must be greater than zero");
        if (amount > maxLoan)
            throw new InvalidLoanAmountException(amount, "Amount exceeds the maximum allowed loan limit");
    }

    /**
     * Adds a specified amount to the bank's reserves.
     *
     * @param amount the amount to add
     */
    public void addToReserves(double amount) {
        reserves += amount;
    }

    /**
     * Subtracts a specified amount from the bank's reserves.
     *
     * @param amount the amount to subtract
     * @throws InsufficientReservesException if reserves are insufficient
     */
    public void subtractFromReserves(double amount)
            throws InsufficientReservesException {
        checkAmountInReserves(amount);
        reserves -= amount;
    }

    /**
     * Checks if the bank's reserves are sufficient for the specified amount.
     *
     * @param amount the amount to check
     * @throws InsufficientReservesException if reserves are insufficient
     */
    public void checkAmountInReserves(double amount)
            throws InsufficientReservesException {
        if (reserves < amount)
            throw new InsufficientReservesException(amount, reserves);
    }

    /**
     * Retrieves an account by the account holder's name.
     *
     * @param accountHolder the account holder's name
     * @return the matching account
     * @throws AccountNotFoundException if the account does not exist
     */
    public Account getAccount(String accountHolder)
            throws AccountNotFoundException {
        return accounts.stream()
                .filter(account -> account.getAccountHolder().equals(accountHolder))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(accountHolder));
    }

    /**
     * Adds a new account with an initial deposit.
     *
     * @param accountHolder  the name of the account holder
     * @param initialDeposit the initial deposit amount
     * @throws InvalidDepositAmountException if the initial deposit is invalid
     * @throws DuplicateAccountException     if the account details are already in
     *                                       use
     */
    public void addAccount(String accountHolder, double initialDeposit)
            throws InvalidDepositAmountException, DuplicateAccountException {
        checkDepositAmount(initialDeposit);

        boolean accountExists = accounts.stream()
                .anyMatch(account -> account.getAccountHolder().equals(accountHolder));
        if (accountExists)
            throw new DuplicateAccountException(accountHolder);

        accounts.add(new Account(accountHolder, initialDeposit));
        addToReserves(initialDeposit);
    }

    /**
     * Removes an account after validating its closure eligibility.
     *
     * @param accountHolder the name of the account holder
     * @throws AccountNotFoundException      if the account does not exist
     * @throws InvalidLoanAmountException    if the account has an outstanding loan
     * @throws InsufficientReservesException if reserves are insufficient
     */
    public void removeAccount(String accountHolder)
            throws AccountNotFoundException,
            InvalidLoanAmountException,
            InsufficientReservesException {
        Account account = getAccount(accountHolder);
        double loanBalance = account.getLoanBalance();
        if (loanBalance > 0)
            throw new InvalidLoanAmountException(loanBalance, "Loan balance must be 0 to close account");
        accounts.removeIf(a -> a.getAccountHolder().equals(accountHolder));
        subtractFromReserves(account.getAccountBalance());
    }

    /**
     * Retrieves the balance of an account.
     *
     * @param accountHolder the account holder's name
     * @return the account balance
     * @throws AccountNotFoundException if the account does not exist
     */
    public double getAccountBalance(String accountHolder)
            throws AccountNotFoundException {
        return getAccount(accountHolder).getAccountBalance();
    }

    /**
     * Deposits an amount into an account.
     *
     * @param accountHolder the account holder's name
     * @param amount        the deposit amount
     * @throws InvalidDepositAmountException if the amount is invalid
     * @throws AccountNotFoundException      if the account does not exist
     */
    public void deposit(String accountHolder, double amount)
            throws InvalidDepositAmountException,
            AccountNotFoundException {
        checkDepositAmount(amount);
        getAccount(accountHolder)
                .deposit(amount);
        addToReserves(amount);
    }

    /**
     * Withdraws an amount from an account.
     *
     * @param accountHolder the account holder's name
     * @param amount        the withdrawal amount
     * @throws InsufficientFundsException       if the account has insufficient
     *                                          funds
     * @throws InsufficientReservesException    if reserves are insufficient
     * @throws AccountNotFoundException         if the account does not exist
     * @throws InvalidWithdrawalAmountException if the amount is invalid
     */
    public void withdraw(String accountHolder, double amount)
            throws InsufficientFundsException,
            InsufficientReservesException,
            AccountNotFoundException,
            InvalidWithdrawalAmountException {
        checkWithdrawalAmount(amount);
        checkAmountInReserves(amount);
        getAccount(accountHolder)
                .withdraw(amount);
        subtractFromReserves(amount);
    }

    /**
     * Approves a loan for an account.
     *
     * @param accountHolder the account holder's name
     * @param loanAmount    the loan amount
     * @throws InsufficientReservesException if reserves are insufficient
     * @throws AccountNotFoundException      if the account does not exist
     * @throws InvalidLoanAmountException    if the loan amount is invalid
     */
    public void approveLoan(String accountHolder, double loanAmount)
            throws InsufficientReservesException,
            AccountNotFoundException,
            InvalidLoanAmountException {
        checkLoanAmount(loanAmount);
        checkAmountInReserves(loanAmount);
        getAccount(accountHolder)
                .addToLoanBalance(loanAmount);
        subtractFromReserves(loanAmount);

    }

    /**
     * Retrieves the loan balance of an account.
     *
     * @param accountHolder the account holder's name
     * @return the loan balance
     * @throws AccountNotFoundException if the account does not exist
     */
    public double getLoanBalance(String accountHolder)
            throws AccountNotFoundException {
        return getAccount(accountHolder).getLoanBalance();
    }

    /**
     * Repays a loan for an account.
     *
     * @param accountHolder the account holder's name
     * @param amount        the repayment amount
     * @throws AccountNotFoundException      if the account does not exist
     * @throws InvalidLoanAmountException    if the repayment amount is invalid
     * @throws InvalidDepositAmountException if the repayment amount is invalid
     */
    public void repayLoan(String accountHolder, double amount)
            throws AccountNotFoundException,
            InvalidLoanAmountException,
            InvalidDepositAmountException {
        checkDepositAmount(amount);
        getAccount(accountHolder)
                .subtractFromLoanBalance(amount);
        addToReserves(amount);
    }

}
