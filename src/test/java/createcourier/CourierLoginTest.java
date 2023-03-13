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

public class CourierLoginTest {
    private CourierManager courierManager;
    private int courierId;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        courierManager = new CourierManager();
    }

    @After
    public void removeCourier() {

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
        courierManager.login(CourierLogin.from(courier))
                .assertThat()
                .body("id", equalTo(courierId))
                .extract().path("id");

    }

    @Test
    public void courierAuthorizationNotValidTest() {
        CourierLogin login = new CourierLogin("", "");
        courierManager.login(login)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void courierAuthorizationNullTest() {
        CourierLogin login = new CourierLogin(null, null);
        courierManager.login(login)
                .assertThat()
                .statusCode(SC_GATEWAY_TIMEOUT);
    }

    @Test
    public void courierAuthorizationWithNullLoginTest() {
        CourierLogin login = new CourierLogin(null, RandomStringUtils.randomAlphabetic(10));
        courierManager.login(login)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    public void courierAuthorizationWithoutLoginTest() {
        CourierLogin login = new CourierLogin("", RandomStringUtils.randomAlphabetic(10));
        courierManager.login(login)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    public void courierAuthorizationWithNullPasswordTest() {
        CourierLogin login = new CourierLogin(RandomStringUtils.randomAlphabetic(10), null);
        courierManager.login(login)
                .assertThat()
                .statusCode(SC_GATEWAY_TIMEOUT);

    }

    @Test
    public void courierAuthorizationWithoutPasswordPasswordTest() {
               CourierLogin login = new CourierLogin(RandomStringUtils.randomAlphabetic(10), "");
        courierManager.login(login)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    public void courierAuthorizationNotValidPasswordAndLoginTest() {
        CourierCreate courier = GeneratorCourier.getRandom();
        courierManager.create(courier);
        courierId = courierManager.login(CourierLogin.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");
        CourierLogin login = new CourierLogin(courier.getLogin(),
                RandomStringUtils.randomAlphabetic(10));
                courierManager.login(login)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    public void courierAuthorizationNotValidLoginTest() {
        CourierCreate courier = GeneratorCourier.getRandom();
        courierManager.create(courier);
        courierId = courierManager.login(CourierLogin.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");
        CourierLogin login = new CourierLogin(RandomStringUtils.randomAlphabetic(10),
                courier.getPassword());
        courierManager.login(login)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    public void courierAuthorizationNotValidLoginAndPasswordTest() {
        CourierLogin login = new CourierLogin(RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
        courierManager.login(login)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }
}