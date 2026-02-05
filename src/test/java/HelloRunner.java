package features;

import com.intuit.karate.Runner;
import com.intuit.karate.junit5.Karate;

public class HelloRunner {

    @Karate.Test
    Karate testOne(){
        return Karate.run("classpath:features/sample.feature");
    }
}
