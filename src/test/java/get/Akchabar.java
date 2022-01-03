package get;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.AkchabarPojo;

import static io.restassured.RestAssured.given;

public class Akchabar {

    @Test
    public void ratesTest(){
        Response response = given().accept("application/json")
                .when().get("http://rates.akchabar.kg/get.json")
                .then().statusCode(200)
                .and()
                .contentType(ContentType.JSON) // application/json i cagiran bir command
                .extract().response();

        AkchabarPojo deserializedAkchabarResponse = response.as(AkchabarPojo.class);

        System.out.println(deserializedAkchabarResponse.getDate());
        System.out.println(deserializedAkchabarResponse.getRates());
        System.out.println(deserializedAkchabarResponse.getUpdated_at());

        Assert.assertEquals(deserializedAkchabarResponse.getDate(), "19.01.2021");
        Assert.assertEquals(deserializedAkchabarResponse.getRates().get("btc"), "3080263.948713");
        Assert.assertEquals(deserializedAkchabarResponse.getUpdated_at(), 1610985364);
    }
}
