package get;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.PokemonPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Pokemon {

    @Test
    public void getPokemonTest(){
        Response response = RestAssured.given().header("Accept","application/json")
                .when().get("https://pokeapi.co/api/v2/pokemon")
                .then().statusCode(200)
                .log().all() // log() console a cikti almak icin
                .extract().response();

        Map<String, Object> pokemonMap = response.as(new TypeRef<Map<String, Object>>() {});
        int actualCount = (int) pokemonMap.get("count"); // 1118 - pokemonMap icin key String value Object olmali, cunku
        // pokemon json objecti icinde valuesu int olan da var String olan da var, bu nedenle tek bir return type
        // vermiyoruz direkt object return type veriyoruz ki json object value olarak ne iceriyor olursa olsun biz onu
        // kullanabilelim. Burada count key ine karsilik int bir value oldugunu biliyoruz ve onu bir int icine store
        // etmek istiyoruz. pokemonMap map inin value su Object oldugu icin casting yapmamiz gerekiyor ki Object icine
        // int bir deger store edebilelim.
        Assert.assertEquals(actualCount, 1118);

        List<Map<String, String>> resultsMap = (List<Map<String, String>>) pokemonMap.get("results");
        for (int i=0; i<resultsMap.size(); i++){
            Map<String, String> singlePokemon = resultsMap.get(i);
            String pokemonName = singlePokemon.get("name");
            System.out.println(pokemonName);
        }
    }

    @Test
    public void getPikachu(){
        // https://pokeapi.co/api/v2/pokemon?limit=1118
        Response response = RestAssured.given().header("Accept","application/json")
                .param("limit","1118") // ana url i .when().get() ile yazip o url e ait query parameteri
                // girmek icin param() methodunu kullanabiliriz. komple url i parametresi ile de yazabiliriz bu yontemle
                // url i yazip query parameteri ayri da belirtebiliriz.
                .when().get("https://pokeapi.co/api/v2/pokemon")
                .then().statusCode(200)
                .extract().response();

        Map<String, Object> pokemonMap = response.as(new TypeRef<Map<String, Object>>() {});
        List<Map<String, String>> pokemonList = (List<Map<String, String>>) pokemonMap.get("results");
        for (Map<String, String> singlePokemon : pokemonList) {
            String pokemonName = singlePokemon.get("name");
            String pokemonURL = singlePokemon.get("url");
            if (pokemonName.equals("pikachu")) {
                System.out.println(pokemonURL);
                // System.out.println(singlePokemon.get("url")); // bu sekilde de yapilabilir
            }
        }
    }

    @Test
    public void countPikachu(){
        Response response = RestAssured.given().header("Accept","application/json")
                .param("limit","1118")
                .when().get("https://pokeapi.co/api/v2/pokemon")
                .then().statusCode(200)
                .extract().response();

        Map<String, Object> pokemonMap = response.as(new TypeRef<Map<String, Object>>() {});
        List<Map<String, String>> pokemonList = (List<Map<String, String>>) pokemonMap.get("results");
        List<String> pikachu = new ArrayList<>();
        for (Map<String, String> singlePokemon : pokemonList) {
            String pokemonName = singlePokemon.get("name");
            if (pokemonName.contains("pikachu")) {
                pikachu.add(pokemonName);
            }
        }
        System.out.println(pikachu.size()); // 15
    }

    @Test
    public void test3(){
        Response response = RestAssured.given().accept("application/json")
                .when().get("https://pokeapi.co/api/v2/pokemon")
                .then().statusCode(200)
                .and()
                .contentType("application/json; charset=utf-8")
                .extract().response();
        PokemonPojo pokemonPojo = response.as(PokemonPojo.class);
        System.out.println(pokemonPojo.getCount());
        System.out.println(pokemonPojo.getNext());
        System.out.println(pokemonPojo.getPrevious());
        System.out.println(pokemonPojo.getResults());

        Assert.assertEquals(pokemonPojo.getCount(), 1118);
    }

    @Test
    public void getPokemon(){
        RestAssured.baseURI = "https://pokeapi.co";
        RestAssured.basePath = "api/v2/pokemon";

        Response response = RestAssured.given().accept("application/json")
                .param("limit",10)
                .when().get()
                .then().statusCode(200)
                .log().all()
                .extract().response();

        // POJO class ile GET HTTP methodu ile cagirilan URL in bodysindeki verileri POJO class icine store edip
        // Test methodu altina object ile cagirip o verileri kullaniyoruz. Bu yontem okunurluk acisindan daha
        // kullanisli.
        PokemonPojo pokemonPojo = response.as(PokemonPojo.class);
        System.out.println(pokemonPojo.getCount());
        Assert.assertEquals(pokemonPojo.getCount(),1118);
        System.out.println(pokemonPojo.getNext());
        System.out.println(pokemonPojo.getPrevious());
        System.out.println(pokemonPojo.getResults());
        for (Map<String,String> pokemonMap : pokemonPojo.getResults()){
            if (pokemonMap.get("url").equals("https://pokeapi.co/api/v2/pokemon/7/")){
                System.out.println(pokemonMap.get("name"));
            }
        }

        // GET merhodu ile cagirdigimiz URL in body sindeki yapiyi burada deserialization yaparak json i
        // java diline cevirip uzerinde oynamalar yapiyor. POJO structure ina gore daha karmasik ve okunulabilirligi az.

        Map<String,Object> pokemonMap = response.as(new TypeRef<Map<String, Object>>() {});
        System.out.println(pokemonMap);

        System.out.println(pokemonMap.get("count"));
        System.out.println(pokemonMap.get("next"));
        System.out.println(pokemonMap.get("previous"));
        System.out.println(pokemonMap.get("results"));

        List<Map<String,String>> results = (List<Map<String,String>>) pokemonMap.get("results");
        System.out.println(results);
        System.out.println(results.get(2));
        for (Map<String, String> resultMap : results) {
            if (resultMap.get("name").equals("squirtle")) {
                System.out.println(resultMap.get("url"));
            }
            System.out.println(resultMap.get("name"));
            System.out.println(resultMap.get("url"));
        }
        Assert.assertEquals(results.size(),10);
    }
}
