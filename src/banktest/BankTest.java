package banktest;

import static org.junit.jupiter.api.Assertions.*;

import bank.Account;
import bank.Bank;
import bank.exceptions.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for the {@link Bank} class.
 */
public class BankTest {

        private static final double MAX_DEPOSIT = 20_000.0;
        private static final double MAX_WITHDRAWAL = 10_000.0;
        private static final double MAX_LOAN = 15_000.0;

        private static final double INITIAL_RESERVE = 100_000.0;
        private static final double INITIAL_DEPOSIT = 5_000.0;

        private static final String ACCOUNT_HOLDER_1 = "Alice";
        private static final String ACCOUNT_HOLDER_2 = "Bob";
        private static final List<String> INITIAL_ACCOUNT_HOLDERS = List.of(ACCOUNT_HOLDER_1, ACCOUNT_HOLDER_2);

        private static Bank bank = new Bank(MAX_DEPOSIT, MAX_WITHDRAWAL, MAX_LOAN);

        /**
         * Initializes the bank's reserves before all tests are executed.
         */
        @BeforeAll
        public static void setUpBeforeClass() {
                bank.addToReserves(INITIAL_RESERVE);
        }

        /**
         * Cleans up the bank's reserves after all tests are executed.
         */
        @AfterAll
        public static void tearDownAfterClass()
                        throws InsufficientReservesException {
                bank.subtractFromReserves(INITIAL_RESERVE);
        }

        /**
         * Sets up the initial accounts before each test is executed.
         */
        @BeforeEach
        public void setUp()
                        throws InvalidDepositAmountException,
                        DuplicateAccountException {
                for (String accountHolder : INITIAL_ACCOUNT_HOLDERS)
                        bank.addAccount(accountHolder, INITIAL_DEPOSIT);
        }

        /**
         * Removes all accounts after each test is executed.
         */
        @AfterEach
        public void tearDown()
                        throws AccountNotFoundException,
                        InvalidLoanAmountException,
                        InsufficientReservesException {
                for (String accountHolder : INITIAL_ACCOUNT_HOLDERS)
                        bank.removeAccount(accountHolder);
        }

        /**
         * Verifies that accounts are successfully added during setup.
         */
        @Test
        public void testAddAccountsSetUpSuccess()
                        throws AccountNotFoundException {
                assertEquals(INITIAL_ACCOUNT_HOLDERS.size(),
                                bank.getAccounts().size());

                for (String accountHolder : INITIAL_ACCOUNT_HOLDERS)
                        assertEquals(INITIAL_DEPOSIT,
                                        bank.getAccountBalance(accountHolder));

                assertEquals(INITIAL_RESERVE + INITIAL_ACCOUNT_HOLDERS.size() * INITIAL_DEPOSIT,
                                bank.getReserves());
        }

        /**
         * Verifies that an invalid deposit amount results in an exception.
         */
        @Test
        public void testAddAccountInvalidDeposit() {
                assertThrows(InvalidDepositAmountException.class,
                                () -> bank.addAccount(INITIAL_ACCOUNT_HOLDERS.getFirst(), -1000));
        }

        /**
         * Verifies that depositing an amount exceeding the maximum limit throws an
         * exception.
         */
        @Test
        public void testDepositExceedsMaxLimit()
                        throws InvalidDepositAmountException {
                assertThrows(InvalidDepositAmountException.class,
                                () -> bank.deposit(INITIAL_ACCOUNT_HOLDERS.getFirst(), 25000));
        }

        /**
         * Verifies that a deposit is processed successfully and updates reserves and
         * account balance correctly, within a time limit of 1000ms.
         */
        @Test
        @Timeout(1000)
        public void testDepositSuccess()
                        throws AccountNotFoundException,
                        InvalidDepositAmountException {
                Account account = bank.getAccount(INITIAL_ACCOUNT_HOLDERS.getFirst());
                double deposit = 2000;

                double reservesBefore = bank.getReserves();
                double balanceBefore = account.getAccountBalance();

                bank.deposit(account.getAccountHolder(), deposit);

                double reservesAfter = bank.getReserves();
                double balanceAfter = account.getAccountBalance();

                assertEquals(reservesAfter, reservesBefore + deposit);
                assertEquals(balanceAfter, balanceBefore + deposit);
        }

        /**
         * Verifies that withdrawing an amount exceeding bank reserves throws an
         * exception.
         */
        @Test
        public void testWithdrawExceedsBankReserves() {
                assertThrows(InsufficientReservesException.class,
                                () -> bank.subtractFromReserves(INITIAL_RESERVE
                                                + (INITIAL_ACCOUNT_HOLDERS.size() + 1) * INITIAL_DEPOSIT));
        }

        /**
         * Verifies that withdrawing an amount exceeding the account's balance throws an
         * exception.
         */
        @Test
        public void testWithdrawInsufficientFunds() {
                assertThrows(InsufficientFundsException.class,
                                () -> bank.withdraw(INITIAL_ACCOUNT_HOLDERS.getFirst(), MAX_WITHDRAWAL));
        }

        /**
         * Verifies that approving a loan exceeding bank reserves throws an exception.
         */
        @Test
        public void testApproveLoanExceedsBankReserves()
                        throws AccountNotFoundException,
                        InvalidLoanAmountException,
                        InvalidDepositAmountException,
                        InsufficientReservesException,
                        InsufficientFundsException,
                        InvalidWithdrawalAmountException,
                        DuplicateAccountException {
                // create an account and set new bank limits
                bank.addAccount("John Doe", 1);
                bank.setMaxLoan(INITIAL_RESERVE);
                bank.setMaxDeposit(INITIAL_RESERVE);

                assertThrows(InsufficientReservesException.class, () -> {
                        bank.approveLoan("John Doe", INITIAL_RESERVE);
                        // second one should fail
                        bank.approveLoan("John Doe", INITIAL_RESERVE);
                });

                // clean up
                bank.repayLoan("John Doe", INITIAL_RESERVE);
                bank.withdraw("John Doe", 1);
                bank.removeAccount("John Doe");
                bank.setMaxDeposit(MAX_DEPOSIT);
                bank.setMaxLoan(MAX_LOAN);

        }

        /**
         * Verifies that a loan repayment is processed successfully.
         */
        @Test
        public void testRepayLoanSuccess()
                        throws InvalidDepositAmountException,
                        InsufficientReservesException,
                        AccountNotFoundException,
                        InvalidLoanAmountException {
                bank.approveLoan(INITIAL_ACCOUNT_HOLDERS.getFirst(), 5_000);
                bank.repayLoan(INITIAL_ACCOUNT_HOLDERS.getFirst(), 3_000);

                assertEquals(2_000,
                                bank.getLoanBalance(INITIAL_ACCOUNT_HOLDERS.getFirst()));

                assertEquals(INITIAL_RESERVE + INITIAL_DEPOSIT * INITIAL_ACCOUNT_HOLDERS.size() - 5_000 + 3_000,
                                bank.getReserves());

                // clean up
                bank.repayLoan(INITIAL_ACCOUNT_HOLDERS.getFirst(), 2_000);
        }

        /**
         * Verifies that attempting to find a non-existent account throws an exception.
         */
        @Test
        public void testFindAccountNotFound() {
                assertThrows(AccountNotFoundException.class, () -> bank.getAccount("Non Existent"));
        }

        /**
         * Verifies that bank reserves are updated correctly after various operations.
         */
        @Test
        public void testReservesUpdateAfterOperations()
                        throws InvalidDepositAmountException,
                        AccountNotFoundException,
                        InsufficientFundsException,
                        InsufficientReservesException,
                        InvalidWithdrawalAmountException,
                        InvalidLoanAmountException,
                        DuplicateAccountException {
                bank.addAccount("John Doe", 10_000);
                bank.deposit("John Doe", 5_000);
                bank.withdraw("John Doe", 3_000);

                assertEquals(INITIAL_RESERVE + INITIAL_DEPOSIT * INITIAL_ACCOUNT_HOLDERS.size()
                                + 10_000 + 5_000 - 3_000,
                                bank.getReserves());

                // clean up
                bank.removeAccount("John Doe");
        }

        /**
         * Verifies that adding a duplicate account throws an exception.
         */
        @Test
        public void testAddDuplicateAccount()
                        throws AccountNotFoundException,
                        InvalidLoanAmountException,
                        InsufficientReservesException {
                assertThrows(DuplicateAccountException.class, () -> {
                        bank.addAccount("Jane Doe", 1_000);
                        bank.addAccount("Jane Doe", 1_000);
                });

                // clean up
                bank.removeAccount("Jane Doe");
        }

        /**
         * Verifies that invalid deposit amounts are correctly handled.
         *
         * @param invalidAmount   the invalid deposit amount
         * @param expectedMessage the expected exception message
         */
        @ParameterizedTest
        @MethodSource("provideInvalidAmountsForDeposit")
        public void testInvalidDepositAmount(double invalidAmount, String expectedMessage) {

                InvalidDepositAmountException exception = assertThrows(
                                InvalidDepositAmountException.class,
                                () -> bank.deposit(INITIAL_ACCOUNT_HOLDERS.getFirst(), invalidAmount));

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        /**
         * Provides test data for invalid deposit amounts.
         *
         * @return a stream of invalid deposit amounts and expected messages
         */
        private static Stream<Arguments> provideInvalidAmountsForDeposit() {
                return Stream.of(Arguments.of(-1.0, "Amount must be greater than zero"),
                                Arguments.of(MAX_DEPOSIT + 1_000.0,
                                                "Amount exceeds the maximum allowed deposit limit"));
        }

        /**
         * Verifies that invalid loan amounts are correctly handled.
         *
         * @param invalidLoanAmount the invalid loan amount
         * @param expectedMessage   the expected exception message
         */
        @ParameterizedTest
        @MethodSource("provideInvalidAmountsForLoan")
        public void testInvalidLoanAmount(double invalidLoanAmount, String expectedMessage) {

                InvalidLoanAmountException exception = assertThrows(
                                InvalidLoanAmountException.class,
                                () -> bank.approveLoan(INITIAL_ACCOUNT_HOLDERS.getFirst(), invalidLoanAmount));

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        /**
         * Provides test data for invalid loan amounts.
         *
         * @return a stream of invalid loan amounts and expected messages
         */
        private static Stream<Arguments> provideInvalidAmountsForLoan() {
                return Stream.of(Arguments.of(-5_000.0, "Amount must be greater than zero"),
                                Arguments.of(MAX_LOAN + 0.1, "Amount exceeds the maximum allowed loan limit"));
        }
}
