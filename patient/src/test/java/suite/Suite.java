package suite;

import org.junit.runner.RunWith;
import scenarios.*;

@RunWith(org.junit.runners.Suite.class)
@org.junit.runners.Suite.SuiteClasses({
        ServiceHealth.class,
        CreatePatient.class,
        FindPatient.class,
        UpdatePatient.class,
        DeletePatient.class
})

public class Suite {
}
