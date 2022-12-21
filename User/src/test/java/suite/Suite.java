package suite;

import org.junit.runner.RunWith;
import scenarios.*;

@RunWith(org.junit.runners.Suite.class)
@org.junit.runners.Suite.SuiteClasses({
        ServiceHealth.class,
        CreateUser.class,
        UpdateUser.class,
        FindUsers.class,
        Login.class,
        DeleteUser.class
})
public class Suite {
}
