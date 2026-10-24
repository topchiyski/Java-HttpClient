package main;

import org.testng.TestNG;
import tests.CreateUserPostTest;
import tests.HeaderCheckTest;
import tests.SimpleGetTest;
import tests.StatusCheckTest;

public class Main {

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
