# Banking App with JUnit Tests

This is a simple Bank Application built in Java, implementing core banking functionalities such as account management, deposits, withdrawals, and loan handling. The application has been refactored for maintainability and tested with JUnit 5 to ensure the correctness of the codebase.

Javadoc for this project can be accessed at 
[https://conor-dowdall.github.io/banking-app-junit-tests/](https://conor-dowdall.github.io/banking-app-junit-tests/).

## Features

- **Account Management**: 
  - Create and remove accounts.
  - Deposit and withdraw funds.
  - Track account balance and loan balance.
  
- **Loan Management**:
  - Approve and repay loans.
  - Loan balance tracking and validation.
  
- **Bank Reserves**:
  - The bank has an initial reserve.
  - The reserve is updated with deposits, withdrawals, and loan repayments.

## Refactoring Overview

The Bank App has undergone a series of refactorings to improve code readability, reduce duplication, and ensure maintainability. Key refactorings include:

1. **Separation of Concerns**: The `Account` and `Bank` classes were refactored to clearly separate their responsibilities. 
   - `Account` now only manages account-specific operations like deposit/withdrawal and loan management.
   - `Bank` focuses on managing accounts, reserves, and loan approvals.

2. **Error Handling**: Introduced custom exceptions (e.g., `InsufficientFundsException`, `InvalidLoanAmountException`) to handle edge cases such as insufficient funds or invalid loan repayments.

3. **Test Coverage**: A comprehensive suite of tests has been added to ensure the application functions as expected. JUnit 5 is used to test all core functionalities of the Bank App.

## Testing

### Test Strategy

The Bank App is extensively tested using **JUnit 5**. The primary focus of testing includes:

1. **Unit Testing**: Tests were created to verify individual methods and ensure they behave as expected. For example, deposit and withdrawal functionality, as well as loan repayment, are thoroughly tested.

2. **Exception Handling**: Tests ensure that exceptions like `InsufficientFundsException` and `InvalidLoanAmountException` are thrown when invalid operations are attempted.

3. **Boundary Conditions**: Tests check for boundary cases such as maximum deposit limits, withdrawals exceeding account balances, and invalid loan repayment amounts.

### Key Test Classes

- **`AccountTest.java`**: Contains tests for the `Account` class, focusing on deposit, withdrawal, and loan management methods.
  
- **`BankTest.java`**: Contains tests for the `Bank` class, ensuring proper account management, loan approval, and reserve handling.
  
- **`BankTestSuite.java`**: A test suite that runs multiple tests across the `BankTest` and `AccountTest` classes, ensuring that the application is fully covered.

### Refactoring with Tests

The refactoring was done with the following goals in mind:

1. **Test-Driven Development (TDD)**: 
   - Refactoring was approached by first considering what tests need to be run to ensure the banking functionality works without error, in a predictable and robust way.

2. **Code Coverage**:
   - JUnit 5 tests cover all critical paths of the application, ensuring nearly 100% code coverage. Tests include scenarios for both successful operations (like deposits) and expected failure conditions (like insufficient funds).

3. **Improved Readability**:
   - Methods were refactored for clarity, ensuring that tests can easily understand what functionality is being tested.

## Folder Structure

```bash
src/
├── bank/
│   ├── Account.java
│   ├── Bank.java
│   └── exceptions/
│       ├── AccountNotFoundException.java
│       ├── DuplicateAccountException.java
│       ├── InsufficientFundsException.java
│       ├── InsufficientReservesException.java
│       ├── InvalidDepositAmountException.java
│       ├── InvalidLoanAmountException.java
│       └── InvalidWithdrawalAmountException.java
└── banktest/
    ├── AccountTest.java
    ├── BankTest.java
    └── BankTestSuite.java
