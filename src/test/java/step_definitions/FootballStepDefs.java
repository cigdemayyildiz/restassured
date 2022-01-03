package step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import utils.Constants;

import java.util.List;
import java.util.Map;

public class FootballStepDefs {

    Response response;

    @Given("I have football competition endpoint")
    public void i_have_football_competition_endpoint() {
        RestAssured.baseURI = "https://api.football-data.org";
        RestAssured.basePath = "v2/competitions/";
    }
    @When("I send GET request to retrieve list of competitions")
    public void i_send_get_request_to_retrieve_list_of_competitions() {
        response = RestAssured.given().accept(Constants.APPLICATION_JSON)
                .when().get();
    }
    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {
        // response.then().statusCode(expectedStatusCode); // bu sekilde tek kalemde de yapilabilir assertion da icinde
        int actualStatusCode = response.statusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode);
    }
    @Then("competitions list must be more than {int}")
    public void competitions_list_must_be_more_than(int expectedSizeOfCompetitionList) {
        List<Map<String, Object>> competitions = response.jsonPath().getList("competitions");
        Assert.assertTrue(competitions.size()>expectedSizeOfCompetitionList);
    }
}
