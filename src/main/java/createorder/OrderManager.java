package createorder;

import databaseuri.BaseURI;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderManager extends BaseURI {

       @Step("Create order")
    public ValidatableResponse create(CreateOrder order) {
        return given()
                .spec(getBaseReqSpec())
                .body(order)
                .when()
                .post(COURIER_URI_ORDER)
                .then();
    }
}
