package createcourier;

import org.apache.commons.lang3.RandomStringUtils;

public class GeneratorCourier {
    public static CourierCreate getRandom() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new CourierCreate(login,password,firstName);
    }
}
