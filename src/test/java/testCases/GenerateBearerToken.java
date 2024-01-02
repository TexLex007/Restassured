package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class GenerateBearerToken {
	String baseURI;
	String authFilePath;
	public static String bearerToken;

	public GenerateBearerToken() {
		baseURI = "https://qa.codefios.com/api";
		authFilePath = "src\\main\\java\\data\\authPayload.json";
		tokengenerator();
	}
	
	public void tokengenerator() {

		/*
		 * given: all input details(baseURI, Headers, Authorizations, Payload/Body,
		 * QueryParameters ) when: submit api requests(HTTP methods, Endpoints/Resource)
		 * then: validate responses(status code, Headers, response time, payload/Body)
		 */

		Response response =

				given().baseUri(baseURI).header("Content-Type", "application/json").body(new File(authFilePath)).log()
						.all().when().post("/user/login").then().log().all().extract().response();
		
		String responseBody = response.getBody().asString();
		JsonPath jp = new JsonPath(responseBody);
		bearerToken = jp.getString("access_token");
		
		
}
}