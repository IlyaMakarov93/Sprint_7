package createcourier;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest {

    private CourierManager courierManager;
    private int courierId;

    @BeforeClass

    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured());
    }

    @Before
    public void setUp() {
        courierManager = new CourierManager();
    }

    @After
    public void clearData() {
        courierManager.removeCourier(courierId);
    }

    @Test
    public void courierCanBeCreatedWithValidDataTest() {

        CourierCreate courier = GeneratorCourier.getRandom();
        courierManager.create(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));
        courierId = courierManager.login(CourierLogin.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");
    }

    @Test
    public void creatingTwoCouriersTest() {

        CourierCreate courier = GeneratorCourier.getRandom();
        courierManager.create(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));
        courierId = courierManager.login(CourierLogin.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");
        courierManager.create(courier)
                .assertThat()
                .statusCode(SC_CONFLICT)
                .assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }

    @Test
    public void creatingOfCourierWithoutFillingInTHeFieldsTest() {

        CourierCreate courier = new CourierCreate(null, null, null);
        courierManager.create(courier)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    public void creatingOfCourierWithouFillingInTHeFieldsTest() {

        CourierCreate courier = new CourierCreate("", "", "");
        courierManager.create(courier)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    public void creatingOfCourierWithoutFillingInTheLoginFieldTest() {

        CourierCreate courier = new CourierCreate(null, RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
        courierManager.create(courier)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    public void creatingOfCourierWithoutFillingInThePasswordFieldTest() {

        CourierCreate courier = new CourierCreate(RandomStringUtils.randomAlphabetic(10), null,
                RandomStringUtils.randomAlphabetic(10));
        courierManager.create(courier)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    public void creatingOfCourierWithoutFillingInTheFirstNameFieldTest() {

        CourierCreate courier = new CourierCreate(RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                null);
        courierManager.create(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));
        courierId = courierManager.login(CourierLogin.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");
    }

    @Test
    public void createCourierWithExistingLoginTest() {
        CourierCreate courier = GeneratorCourier.getRandom();
        courierManager.create(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));
        courierId = courierManager.login(CourierLogin.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");
        CourierCreate newCourier = new CourierCreate(courier.getLogin(), RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
        courierManager.create(newCourier)
                .assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        courierManager.removeCourier(courierId)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("ok", is(true));
    }


}

