package createorder;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;

public class GeneratorOrder {

    public static CreateOrder getRandom() {

        String firstName = RandomStringUtils.randomAlphabetic(10);
        String lastName = RandomStringUtils.randomAlphabetic(10);
        String address = RandomStringUtils.randomAlphabetic(10);
        String metroStation = String.valueOf((int) (Math.random() * 30));
        String phone = RandomStringUtils.randomNumeric(11);
        String rentTime = String.valueOf((int) (Math.random() * 40));
        String deliveryDate = String.valueOf(LocalDate.now());
        String comment = RandomStringUtils.randomAlphabetic(20);
        String[] color = new String[0];
        return new CreateOrder(firstName, lastName, address,
                metroStation, phone, rentTime, deliveryDate, comment, color);
    }
}
