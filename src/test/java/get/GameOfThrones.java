package get;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameOfThrones {

    @Test
    public void snowHouse(){

        // https://api.got.show/api/show/characters
        RestAssured.baseURI = "https://api.got.show"; // baseURI == host
        RestAssured.basePath = "api/show/characters";
        // baseURI ve basePath @Before annotation icine yazilirsa daha mantikli olur. Her yeni @Tes annotationi
        // altindaki methodda kullanmak istedigimizde tekrar tekrar olusturmak zorunda kalmayiz.

        Response response = RestAssured.given().header("Accept", "application/json")
                .when().get() // eger baseURI olarak tanimladiysak burada get icinde url i cagirmaya gerek yok
                .then().statusCode(200).contentType("application/json; charset=utf-8") // .contentType methodu ile
                // header da belirttigimiz Accept application/json i validate ediyoruz. Biz json typeinda olan header i
                // getir dedik ama gercekten o mu geldi diye validate ediyoruz. status code validationindan sonra olmali
                .extract().response();

        List<Map<String,Object>> characterList = response.as(new TypeRef<List<Map<String, Object>>>() {});
        for (int i=0; i<characterList.size(); i++){
            Map<String, Object> characterMap = characterList.get(i);
            String characterName = characterMap.get("name").toString(); // casting yapmanin baska bir yolu
            //String characterName = ""+characterMap.get("name"); // casting yapmanin bir baska yolu
            //String characterName = (String) characterMap.get("name"); // casting yapmanin baska bir yolu daha
            //String characterName = String.valueOf(characterMap.get("name")); // casting yapmanin bir baska yolu daha
            if (characterName.equals("Jon Snow")){
                System.out.println(characterName); // Jon Snow
                System.out.println(characterMap.get("house")); // House Stark --> Jon Snow un house unu yazdir
            }
        }
    }

    @Test
    public void charactersToHouseMappingTest(){
        RestAssured.baseURI = "https://api.got.show";
        RestAssured.basePath = "api/show/characters";

        Response response = RestAssured.given().header("Accept", "application/json")
                .when().get()
                .then().statusCode(200)
                .and() // kodun daha okunabilir olmasi icin araya and() ekleyebiliriz
                .contentType("application/json; charset=utf-8")
                .extract().response();

        List<Map<String, Object>> characterList = response.as(new TypeRef<List<Map<String, Object>>>() {}); // deserialization ya da parsing
        Map<String, List<String>> charactersByHouse = new HashMap<>();
        for (Map<String, Object> characterMap : characterList) {
            String tempName = "" + characterMap.get("name");
            String tempHouse = "" + characterMap.get("house");
            if (tempHouse.equals("null")){
                continue;
            }
            if (charactersByHouse.containsKey(tempHouse)) {
                List<String> characterNames = charactersByHouse.get(tempHouse);
                characterNames.add(tempName);
                charactersByHouse.put(tempHouse, characterNames);
                // charactersByHouse.get(tempHouse).add(tempName); // yukaridaki uc satiri tek bir satirda bu sekilde yapiyoruz
            }else {
                List<String> newCharacterName = new ArrayList<>();
                newCharacterName.add(tempName);
                charactersByHouse.put(tempHouse, newCharacterName);
            }
        }
        System.out.println(charactersByHouse);
        System.out.println("Number of houses: "+charactersByHouse.keySet().size());
    }

    @Test
    public void genderOfCharacters(){
        RestAssured.baseURI = "https://api.got.show";
        RestAssured.basePath = "api/show/characters";

        Response response = RestAssured.given().header("Accept", "application/json")
                .when().get()
                .then().statusCode(200)
                .extract().response();

        List<Map<String, Object>> characterList = response.as(new TypeRef<List<Map<String, Object>>>() {});
        for (int i=0; i<characterList.size(); i++){
            Map<String, Object> gender = characterList.get(i);
            String characterGender = ""+gender.get("gender");
            if (characterGender.equals("female")) {
                System.out.println(gender.get("name"));
            }
        }
    }
}
