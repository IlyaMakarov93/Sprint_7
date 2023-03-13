package createcourier;

import databaseuri.BaseURI;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;


public class CourierManager extends BaseURI {

    @Step("Create courier")
    public ValidatableResponse create(CourierCreate courier) {

        return given()
                .spec(getBaseReqSpec())
                .body(courier)
                .when()
                .post(COURIER_URI)
                .then();
    }

    @Step("Login courier")
    public ValidatableResponse login(CourierLogin courierLogin) {
        return given()
                .spec(getBaseReqSpec())
                .body(courierLogin)
                .when()
                .post(COURIER_URI_lOGIN)
                .then();
    }

    public ValidatableResponse removeCourier(int courierID) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .delete(COURIER_URI + courierID)
                .then();
    }
    public ValidatableResponse removeCourierWithoutId() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .delete(COURIER_URI )
                .then();
    }
}