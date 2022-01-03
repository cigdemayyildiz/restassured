package get;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import pojo.PetPojo;

import java.util.ArrayList;
import java.util.Map;

public class PetStore {

    @Test
    public void getIntro(){
        Response response = RestAssured.given().header("Accept","application/json") // bu formattaki header i
                .when().get("https://petstore.swagger.io/v2/pet/1") // bu url icin kontrol et
                .then().statusCode(200) // ve 200 status code aldigindan emin ol
                .extract().response(); // response methodunu calistirdigin zaman bunu response a store etmen gerekir.

        Map<String, Object> deserializedResponse = response.as(new TypeRef<Map<String, Object>>() {});
        deserializedResponse.get("name");
        System.out.println(deserializedResponse.get("name"));
        ArrayList<Map<String, Object>> tagValues = (ArrayList<Map<String, Object>>)deserializedResponse.get("tags");

        deserializedResponse.get("tags");
        System.out.println(deserializedResponse.get("tags"));
    }

    @Test
    public void pojoParseTest(){
        Response response = RestAssured.given().header("Accept","application/json")
                .when().get("https://petstore.swagger.io/v2/pet/10")
                .then().statusCode(200)
                .extract().response();

        PetPojo petPojo = response.as(PetPojo.class);
        System.out.println(petPojo.getId()); // 10
        System.out.println(petPojo.getCategory()); // {id=10, name=John Doe}
        System.out.println(petPojo.getName()); // name
        System.out.println(petPojo.getStatus()); // status
        System.out.println(petPojo.getPhotoUrls()); // [John Doe]
        System.out.println(petPojo.getTags()); // [{id=10, name=John Doe}]
    }
}
