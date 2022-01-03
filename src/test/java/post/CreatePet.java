package post;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.PetPojo;
import utils.PayloadUtils;

import java.io.File;

public class CreatePet {

    @Test
    public void createTest1(){

        RestAssured.baseURI = "https://petstore.swagger.io";
        RestAssured.basePath = "v2/pet";

        Response response = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(PayloadUtils.getPetPayload("268282","Bro","don't touch"))
                .when().post()
                .then().statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .log().all()
                .extract().response();

        PetPojo petPojo = response.as(PetPojo.class);
        System.out.println(petPojo.getId());
        System.out.println(petPojo.getName());
        System.out.println(petPojo.getStatus());

        Assert.assertEquals(petPojo.getId(), 268282);
        Assert.assertEquals(petPojo.getName(), "Bro");
        Assert.assertEquals(petPojo.getStatus(), "don't touch");
    }

    @Test
    public void createTest2() {

        RestAssured.baseURI = "https://petstore.swagger.io";
        RestAssured.basePath = "v2/pet/268282";

        Response response = RestAssured.given().accept("application/json")
                .when().get()
                .then().statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .extract().response();

        PetPojo petPojo = response.as(PetPojo.class);
        Assert.assertEquals(petPojo.getId(), 268282);
        Assert.assertEquals(petPojo.getName(), "Bro");
        Assert.assertEquals(petPojo.getStatus(), "don't touch");
    }

    @Test
    public void createTest3(){
        RestAssured.baseURI = "https://petstore.swagger.io";
        RestAssured.basePath = "v2/pet";

        File petJson = new File("src/test/resources/payloads/pet.json");
        Response response = RestAssured.given().accept("application/json")
                .contentType("application/json")
                .body(petJson)
                .when().post()
                .then().statusCode(200)
                .contentType(ContentType.JSON)
                .log().all()
                .extract().response();

        PetPojo parsedPostResponse = response.as(PetPojo.class);
        Assert.assertEquals(parsedPostResponse.getId(), 268283);
        Assert.assertEquals(parsedPostResponse.getName(), "Jerry");
        Assert.assertEquals(parsedPostResponse.getStatus(), "don't touch");
    }

    @Test
    public void createTest4(){
        RestAssured.baseURI = "https://petstore.swagger.io";
        RestAssured.basePath = "v2/pet";

        PetPojo petPojo = new PetPojo();
        petPojo.setId(268284);
        petPojo.setName("Gigi");
        petPojo.setStatus("touch me");

        Response response = RestAssured.given().accept("application/json")
                .contentType("application/json")
                .body(petPojo)
                .when().post()
                .then().statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .log().all()
                .extract().response();

        PetPojo petPojo1 = response.as(PetPojo.class);
        Assert.assertEquals(petPojo1.getId(), 268284);
        Assert.assertEquals(petPojo1.getName(), "Gigi");
        Assert.assertEquals(petPojo1.getStatus(), "touch me");
    }
}
