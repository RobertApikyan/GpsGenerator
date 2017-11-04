package test;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Created by Robert on 30.09.2017.
 */
public class PrintTestNameRule implements TestRule {
    @Override
    public Statement apply(final Statement statement, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.out.println(description.getMethodName());
                System.out.println(System.currentTimeMillis());
                statement.evaluate();
                System.out.println(System.currentTimeMillis() + "\n");
            }
        };
    }
}
