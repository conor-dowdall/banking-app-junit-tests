package banktest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Test suite for the Bank application.
 * <p>
 * This suite aggregates all unit tests for the {@link Bank} and {@link Account}
 * classes, ensuring comprehensive validation of core banking functionality.
 * </p>
 * <p>
 * Included test classes:
 * <ul>
 * <li>{@link AccountTest} - Tests for the {@link Account} class, covering
 * account operations.</li>
 * <li>{@link BankTest} - Tests for the {@link Bank} class, covering banking
 * operations.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * To execute, run this suite as a JUnit test using your IDE or build tool.
 * </p>
 */
@Suite
@SelectClasses({ AccountTest.class, BankTest.class })
public class BankTestSuite {

}
