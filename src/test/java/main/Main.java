package main;

import org.testng.TestNG;
import tests.CreateUserPostTest;
import tests.HeaderCheckTest;
import tests.SimpleGetTest;
import tests.StatusCheckTest;

/** Runs the project's TestNG API tests from a standalone entry point. */
public class Main {

    /**
     * Executes all configured test classes and exits with TestNG's result status.
     *
     * @param args command-line arguments; currently unused
     */
    public static void main(String[] args) {
        TestNG testng = new TestNG();
        testng.setTestClasses(new Class[]{
                CreateUserPostTest.class,
                HeaderCheckTest.class,
                SimpleGetTest.class,
                StatusCheckTest.class
        });
        testng.run();
        System.exit(testng.getStatus());
    }
}
