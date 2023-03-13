package listoforders;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;


public class ListOfOrdersManagerTest {
    private ListOfOrdersManager manager;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured());
    }

    @Before
    public void setUp() {
        manager = new ListOfOrdersManager();
    }

    @Test
    public void getList() {
        manager.getListOfOrders()
                .assertThat()
                .statusCode(SC_OK)
                .and().assertThat().body("orders", notNullValue());
    }


}