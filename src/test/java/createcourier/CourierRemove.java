package createcourier;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class CourierRemove {
    private int courierId;
    private CourierManager courierManager;

    @BeforeClass
    public static void globalSetUp() {

        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
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
    public void verificationOfCourierRemoveTest() {
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
        courierManager.removeCourier(courierId)
                .assertThat()
                .statusCode(SC_OK)
                .body("ok", is(true));
    }

    @Test
    public void verificationOfCourierDeletionTest() {
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
        courierManager.removeCourier(courierId)
                .assertThat()
                .statusCode(SC_OK)
                .body("ok", is(true));
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
    public void removeNotValidIdTest() {
        courierManager.removeCourier((int) (Math.random() * 100000))
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", equalTo("Курьера с таким id нет."));
    }

    @Test
    public void removeWithoutIdTest() {
        courierManager.removeCourierWithoutId()
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", equalTo("Not Found."));
    }
}
