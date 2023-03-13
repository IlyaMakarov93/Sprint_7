package listoforders;

import databaseuri.BaseURI;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ListOfOrdersManager extends BaseURI {

    @Step(" Get list of orders")
    public ValidatableResponse getListOfOrders() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(COURIER_URI_ORDER)
                .then();
    }
}