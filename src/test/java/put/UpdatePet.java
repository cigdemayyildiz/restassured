package put;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import utils.PayloadUtils;

import static utils.Constants.APPLICATION_JSON;

public class UpdatePet {

    @Test
    public void updatePetTest(){
        RestAssured.baseURI = "https://petstore.swagger.io";
        RestAssured.basePath = "v2/pet";

        Response response = RestAssured.given().accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(PayloadUtils.getPetPayload("140430","Zeus","updated"))
                .when().put()
                .then().statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("id", Matchers.equalTo(140430)) // assertion i Hamcrest library sinin Matchers classi ile yapiyoruz
                .body("name", Matchers.equalTo("Zeus"))
                .log().all()
                .extract().response();
    }
}
