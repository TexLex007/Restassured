package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadOneAccount {
	String baseURI;
	String authFilePath;
	String bearerToken;

	public ReadOneAccount() {
		baseURI = "https://qa.codefios.com/api";
		authFilePath = "src\\main\\java\\data\\authPayload.json";
	}

	@Test(priority = 1)
	public void tokengenerator() {

		/*
		 * given: all input details(baseURI, Headers, Authorizations, Payload/Body,
		 * QueryParameters ) when: submit api requests(HTTP methods, Endpoints/Resource)
		 * then: validate responses(status code, Headers, response time, payload/Body)
		 */

		Response response =

				given().baseUri(baseURI).header("Content-Type", "application/json").body(new File(authFilePath)).log()
						.all().when().post("/user/login").then().log().all().extract().response();

		int responseStatus = response.getStatusCode();

		System.out.println("Response Code: " + responseStatus);

		Assert.assertEquals(responseStatus, 201);

		String responseHeader = response.getHeader("Content-type");

		System.out.println("Response Header: " + responseHeader);

		Assert.assertEquals(responseHeader, "application/json", "Response header not matching!!");

		String responseBody = response.getBody().asString();

		System.out.println("Response body: " + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		bearerToken = jp.getString("access_token");
		System.out.println("Bearer Token: " + bearerToken);

	}

	@Test(priority = 2)
	public void readOneAccount() {
		Response response =

				given().baseUri(baseURI).header("Content-Type", "application/json")
						.auth().preemptive().basic("demo1@codefios.com", "abc123")
						.queryParam("account_id", "221")
						.log().all()
						.when().get("/account/getone")
						.then().log().all().extract().response();

		int responseStatus = response.getStatusCode();
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time is:" + responseTime);

		System.out.println("Response Code: " + responseStatus);

		Assert.assertEquals(responseStatus, 200);

		String responseHeader = response.getHeader("Content-type");

		System.out.println("Response Header: " + responseHeader);

		Assert.assertEquals(responseHeader, "application/json", "Response header not matching!!");

		String responseBody = response.getBody().asString();

		System.out.println("Response body: " + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		String accountName = jp.getString("account_name");
		Assert.assertEquals(accountName, "qa student account 111", "Account Name doesn't match!!");
		
		if(responseTime < 2000) {
			System.out.println("Response time is within the range.");
		}
		else {
			System.out.println("Reponse time is longer than expected.");
		}
		
		
	}
}
