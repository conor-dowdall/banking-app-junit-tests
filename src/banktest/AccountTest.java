package banktest;

import bank.Account;
import bank.exceptions.InsufficientFundsException;
import bank.exceptions.InvalidLoanAmountException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Account} class.
 */
public class AccountTest {

    private static final String ACCOUNT_HOLDER = "Test User";
    private static final double INITIAL_BALANCE = 10_000.0;

    private Account account;

    /**
     * Sets up an account instance before each test.
     */
    @BeforeEach
    public void setUp() {
        account = new Account(ACCOUNT_HOLDER, INITIAL_BALANCE);
    }

    /**
     * Tests that the account holder's name is correctly retrieved.
     */
    @Test
    public void testGetAccountHolder() {
        assertEquals(ACCOUNT_HOLDER, account.getAccountHolder(), "Account holder's name should match.");
    }

    /**
     * Tests that the initial account balance is correctly set.
     */
    @Test
    public void testGetAccountBalance() {
        assertEquals(INITIAL_BALANCE, account.getAccountBalance(), "Initial account balance should match.");
    }

    /**
     * Tests successful deposit updates the account balance.
     */
    @Test
    public void testDepositSuccess() {
        double depositAmount = 5_000.0;
        account.deposit(depositAmount);
        assertEquals(INITIAL_BALANCE + depositAmount, account.getAccountBalance(),
                "Balance should increase after deposit.");
    }

    /**
     * Tests successful withdrawal updates the account balance.
     * 
     * @throws InsufficientFundsException
     */
    @Test
    public void testWithdrawSuccess()
            throws InsufficientFundsException {
        double withdrawalAmount = 2_000.0;
        account.withdraw(withdrawalAmount);
        assertEquals(INITIAL_BALANCE - withdrawalAmount, account.getAccountBalance(),
                "Balance should decrease after withdrawal.");
    }

    /**
     * Tests that withdrawing more than the available balance throws an exception.
     */
    @Test
    public void testWithdrawInsufficientFunds() {
        double excessiveAmount = INITIAL_BALANCE + 1_000.0;
        assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(excessiveAmount),
                "Expected to throw InsufficientFundsException when withdrawing more than balance.");
    }

    /**
     * Tests that the loan balance starts at zero.
     */
    @Test
    public void testInitialLoanBalance() {
        assertEquals(0.0, account.getLoanBalance(), "Initial loan balance should be zero.");
    }

    /**
     * Tests adding to the loan balance.
     */
    @Test
    public void testAddToLoanBalance() {
        double loanAmount = 3_000.0;
        account.addToLoanBalance(loanAmount);
        assertEquals(loanAmount, account.getLoanBalance(), "Loan balance should increase after adding to it.");
    }

    /**
     * Tests subtracting from the loan balance successfully.
     */
    @Test
    public void testSubtractFromLoanBalanceSuccess() throws InvalidLoanAmountException {
        double loanAmount = 4_000.0;
        double repaymentAmount = 2_000.0;

        account.addToLoanBalance(loanAmount);
        account.subtractFromLoanBalance(repaymentAmount);

        assertEquals(loanAmount - repaymentAmount, account.getLoanBalance(),
                "Loan balance should decrease after repayment.");
    }

    /**
     * Tests that subtracting more than the loan balance throws an exception.
     */
    @Test
    public void testSubtractFromLoanBalanceExceeds() {
        double loanAmount = 5_000.0;
        double excessiveRepayment = 6_000.0;

        account.addToLoanBalance(loanAmount);

        InvalidLoanAmountException exception = assertThrows(
                InvalidLoanAmountException.class,
                () -> account.subtractFromLoanBalance(excessiveRepayment),
                "Expected to throw InvalidLoanAmountException when repayment exceeds loan balance.");

        assertTrue(exception.getMessage().contains("Repayment amount exceeds loan balance"));
    }

    /**
     * Tests that the checkAmountInAccount method throws an exception for
     * insufficient funds.
     */
    @Test
    public void testCheckAmountInAccountInsufficientFunds() {
        double excessiveAmount = INITIAL_BALANCE + 1_000.0;
        assertThrows(
                InsufficientFundsException.class,
                () -> account.checkAmountInAccount(excessiveAmount),
                "Expected to throw InsufficientFundsException for insufficient funds.");
    }

    /**
     * Tests that the checkAmountInAccount method does not throw an exception for
     * sufficient funds.
     */
    @Test
    public void testCheckAmountInAccountSufficientFunds() {
        assertDoesNotThrow(() -> account.checkAmountInAccount(INITIAL_BALANCE / 2),
                "No exception should be thrown for sufficient funds.");
    }

    /**
     * Tests that the checkAmountInLoanBalance method throws an exception for
     * excessive repayment.
     */
    @Test
    public void testCheckAmountInLoanBalanceInvalid() {
        double loanAmount = 4_000.0;
        double excessiveRepayment = 5_000.0;

        account.addToLoanBalance(loanAmount);

        assertThrows(
                InvalidLoanAmountException.class,
                () -> account.checkAmountInLoanBalance(excessiveRepayment),
                "Expected to throw InvalidLoanAmountException for excessive repayment.");
    }

    /**
     * Tests that the checkAmountInLoanBalance method does not throw an exception
     * for valid repayment.
     */
    @Test
    public void testCheckAmountInLoanBalanceValid() {
        double loanAmount = 4_000.0;
        double validRepayment = 2_000.0;

        account.addToLoanBalance(loanAmount);

        assertDoesNotThrow(() -> account.checkAmountInLoanBalance(validRepayment),
                "No exception should be thrown for valid repayment amount.");
    }
}
