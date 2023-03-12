package createorder;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderParametrizedTest {
    @BeforeClass

    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured());
    }

    @Before
    public void setUp() {
        orderManager = new OrderManager();
    }

    private OrderManager orderManager;
    private int track;
    private String color;

    public CreateOrderParametrizedTest(String color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getParameter() {
        return new Object[][]{
                {"BLACK"},
                {"GREY"},
                {"BLACK,GREY"},
                {""}
        };
    }

    @Test
    public void courierCanBeCreatedWithValidDataTest() {
        CreateOrder order = GeneratorOrder.getRandom();
        String[] colorArray = color.split(",");
        order.setColor(colorArray);
        track = orderManager.create(order)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("track", notNullValue())
                .extract().path("track");

    }
}