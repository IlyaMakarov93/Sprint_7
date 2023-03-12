package databaseuri;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseURI {

    protected static final String URI_SCOOTER_API = "https://qa-scooter.praktikum-services.ru/api/v1/";
    protected static final String COURIER_URI_ORDER = URI_SCOOTER_API + "orders/";
    protected static final String COURIER_URI = URI_SCOOTER_API + "courier/";
    protected static final String COURIER_URI_lOGIN= COURIER_URI + "login/";

    protected RequestSpecification getBaseReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(URI_SCOOTER_API)
                .build();

    }

}

