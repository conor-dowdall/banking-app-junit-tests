import java.util.ArrayList;
import java.util.List;

/**
 * This program simulates a simple banking application. It allows:
 * - Adding new accounts with an initial deposit.
 * - Depositing and withdrawing money from accounts.
 * - Approving and repaying loans for account holders.
 * - Tracking the total deposits available in the bank.
 * 
 * The program uses a list of Account objects to manage account data.
 */
public class BankingApp {

	public static void main(String[] args) {
	    // Create a new banking application instance
	    BankingApp bank = new BankingApp();

	    // Add accounts
	    bank.addAccount("Alice", 1000);
	    bank.addAccount("Bob", 500);

	    // Test deposits
	    System.out.println("Depositing 200 to Alice: " + bank.deposit("Alice", 200)); // Should return true
	    System.out.println("Alice's balance: " + bank.getBalance("Alice")); // Should be 1200

	    // Test withdrawals
	    System.out.println("Withdrawing 300 from Bob: " + bank.withdraw("Bob", 300)); // Should return true
	    System.out.println("Bob's balance: " + bank.getBalance("Bob")); // Should be 200

	    // Test loan approval
	    System.out.println("Approving a loan of 400 for Alice: " + bank.approveLoan("Alice", 400)); // Should return true
	    System.out.println("Alice's loan: " + bank.getLoan("Alice")); // Should be 400

	    // Test loan repayment
	    System.out.println("Repaying 200 of Alice's loan: " + bank.repayLoan("Alice", 200)); // Should return true
	    System.out.println("Alice's remaining loan: " + bank.getLoan("Alice")); // Should be 200

	    // Check total deposits in the bank
	    System.out.println("Total deposits in the bank: " + bank.getTotalDeposits());
	}
	
	
    // Represents a single bank account with account holder name, balance, and loan amount
    private class Account {
        private String accountHolder; // Name of the account holder
        private double balance;       // Current account balance
        private double loan;          // Outstanding loan amount

        // Constructor to create a new account
        public Account(String accountHolder, double balance) {
            this.accountHolder = accountHolder;
            this.balance = balance;
            this.loan = 0;
        }

        // Getter for the account holder's name
        public String getAccountHolder() {
            return accountHolder;
        }

        // Getter for the account balance
        public double getBalance() {
            return balance;
        }

        // Getter for the loan amount
        public double getLoan() {
            return loan;
        }

        // Method to deposit money into the account
        public void deposit(double amount) {
            balance += amount;
        }

        // Method to withdraw money from the account (only if balance is sufficient)
        public boolean withdraw(double amount) {
            if (amount > balance) return false; // Insufficient funds
            balance -= amount;
            return true;
        }

        // Method to approve a loan for the account
        public void approveLoan(double amount) {
            loan += amount;
        }

        // Method to repay a part of the loan (only if amount <= loan)
        public boolean repayLoan(double amount) {
            if (amount > loan) return false; // Repayment exceeds loan
            loan -= amount;
            return true;
        }
    }

    // List to store all accounts in the banking application
    private List<Account> accounts;
    private double totalDeposits; // Tracks total deposits in the bank

    // Constructor to initialize the banking application
    public BankingApp() {
        accounts = new ArrayList<>();
        totalDeposits = 0;
    }

    /**
     * Helper method to find an account by account holder's name.
     * @param accountHolder The name of the account holder.
     * @return The Account object if found, otherwise null.
     */
    private Account findAccount(String accountHolder) {
        for (Account account : accounts) {
            if (account.getAccountHolder().equals(accountHolder)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Adds a new account with an initial deposit.
     * @param accountHolder The name of the new account holder.
     * @param initialDeposit The initial deposit amount.
     */
    public void addAccount(String accountHolder, double initialDeposit) {
        accounts.add(new Account(accountHolder, initialDeposit));
        totalDeposits += initialDeposit;
    }

    /**
     * Deposits money into an account.
     * @param accountHolder The name of the account holder.
     * @param amount The deposit amount.
     * @return True if the deposit is successful, otherwise false.
     */
    public boolean deposit(String accountHolder, double amount) {
        Account account = findAccount(accountHolder);
        if (account == null || amount <= 0) return false;
        account.deposit(amount);
        totalDeposits += amount;
        return true;
    }

    /**
     * Withdraws money from an account.
     * @param accountHolder The name of the account holder.
     * @param amount The withdrawal amount.
     * @return True if the withdrawal is successful, otherwise false.
     */
    public boolean withdraw(String accountHolder, double amount) {
        Account account = findAccount(accountHolder);
        if (account == null || amount <= 0) return false;
        if (account.withdraw(amount)) {
            totalDeposits -= amount;
            return true;
        }
        return false;
    }

    /**
     * Approves a loan for an account holder.
     * @param accountHolder The name of the account holder.
     * @param loanAmount The loan amount.
     * @return True if the loan is approved, otherwise false.
     */
    public boolean approveLoan(String accountHolder, double loanAmount) {
        Account account = findAccount(accountHolder);
        if (account == null || loanAmount > totalDeposits) return false;
        account.approveLoan(loanAmount);
        totalDeposits -= loanAmount;
        return true;
    }

    /**
     * Repays a part of the loan for an account holder.
     * @param accountHolder The name of the account holder.
     * @param amount The repayment amount.
     * @return True if the repayment is successful, otherwise false.
     */
    public boolean repayLoan(String accountHolder, double amount) {
        Account account = findAccount(accountHolder);
        if (account == null || amount <= 0) return false;
        if (account.repayLoan(amount)) {
            totalDeposits += amount;
            return true;
        }
        return false;
    }

    /**
     * Gets the total deposits available in the bank.
     * @return The total deposits.
     */
    public double getTotalDeposits() {
        return totalDeposits;
    }

    /**
     * Gets the balance of a specific account holder.
     * @param accountHolder The name of the account holder.
     * @return The balance if the account exists, otherwise null.
     */
    public Double getBalance(String accountHolder) {
        Account account = findAccount(accountHolder);
        return account != null ? account.getBalance() : null;
    }

    /**
     * Gets the loan amount of a specific account holder.
     * @param accountHolder The name of the account holder.
     * @return The loan amount if the account exists, otherwise null.
     */
    public Double getLoan(String accountHolder) {
        Account account = findAccount(accountHolder);
        return account != null ? account.getLoan() : null;
    }
}

