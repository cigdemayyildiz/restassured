package get;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Constants;

import java.util.List;
import java.util.Map;

public class Football {

    @Test
    public void testFootball(){
        RestAssured.baseURI = "https://api.football-data.org";
        RestAssured.basePath = "v2/competitions/";

        // get list of all competitions
        Response response = RestAssured.given().accept(Constants.APPLICATION_JSON)
                .header("X-Auth-Token","c55b7a64e8424d46a52051bce36d1c0a")
                .when().get()
                .then().statusCode(200)
                .contentType("application/json;charset=UTF-8")
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> competitionList = jsonPath.getList("competitions");

        for (int i=0; i<competitionList.size(); i++){
            Map<String, Object> competitionMap = competitionList.get(i);
            if (competitionMap.get("name").equals("Premier Liga")){
                int id = (int) competitionMap.get("id");
                Assert.assertEquals(id, 2035);
            }
        }
    }

    @Test
    public void testFootball2(){
        RestAssured.baseURI = "https://api.football-data.org";
        RestAssured.basePath = "v2/competitions/";

        // get list of all competitions
        Response response = RestAssured.given().accept(Constants.APPLICATION_JSON)
                .header("X-Auth-Token","c55b7a64e8424d46a52051bce36d1c0a")
                .when().get()
                .then().statusCode(200)
                .body("competitions.find { it.name == 'Premier Liga' }.id", Matchers.equalTo(2035))
                .contentType("application/json;charset=UTF-8")
                .extract().response();

        // path method works with Groovy programming language
        // find search yapmaya yarayan method, kirisik parantez icine ne aramak istedigimizi belirtiyoruz
        int competitionId = response.path("competitions.find { it.name == 'Premier Liga' }.id");
        System.out.println(competitionId);
        Assert.assertEquals(competitionId, 2035);
        // yukaridaki uc satiri sadece ustteki body kodu ile de yapabiliriz.. iki yontemi de bilmek icin buradan
        // silmiyorum ama bu assertion i body methodu ile de yapabiliyoruz..

    }
}
