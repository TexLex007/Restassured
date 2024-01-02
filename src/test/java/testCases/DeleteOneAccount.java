package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DeleteOneAccount {
	String baseURI;
	String authFilePath;
	String bearerToken;

	public DeleteOneAccount() {
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
						.when().delete("/account/deleteone")
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
		String deleteAccount = jp.getString("message");
		Assert.assertEquals(deleteAccount, "Account deleted successfully.", "Error deleting account!!");
		
		
		
		
	}
}
