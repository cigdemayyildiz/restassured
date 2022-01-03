package delete;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import utils.PayloadUtils;

import static utils.Constants.APPLICATION_JSON;

public class DeletePet {

    @Test
    public void deletePetTest() throws InterruptedException {
        RestAssured.baseURI = "https://petstore.swagger.io";
        RestAssured.basePath = "v2/pet";

        //delete etmek icin once olusturuyoruz
        RestAssured.given().accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(PayloadUtils.getPetPayload("77755","Hatiko","waiting"))
                .when().post()
                .then().statusCode(200)
                .contentType(APPLICATION_JSON)
                .log().all();

        Thread.sleep(2000);

        // deleting a pet

        // https://petstore.swagger.io/v2/pet/{petId}

        RestAssured.given().accept(APPLICATION_JSON)
                .when().delete("77755") // silmek istedigimiz pet in id sini buraya veriyoruz
                .then().statusCode(200)
                .contentType(APPLICATION_JSON)
                .log().body();

        // recall the pet to validate it isn't there
        RestAssured.given().accept(APPLICATION_JSON)
                .when().get("77755")
                .then().statusCode(404)
                .contentType(APPLICATION_JSON)
                .body("message", Matchers.is("Pet not found"))
                .log().all();
    }
}
