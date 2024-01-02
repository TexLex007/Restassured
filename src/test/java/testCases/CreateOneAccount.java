package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateOneAccount extends GenerateBearerToken {
	String baseURI;
	String createAccountFilePath;
	String lastAccountId;

	public CreateOneAccount() {
		baseURI = "https://qa.codefios.com/api";
		createAccountFilePath = "src\\main\\java\\data\\createAccountPayload.json";
	}

	@Test(priority = 1)
	public void createOneAccount() {
		Response response =

				given().baseUri(baseURI).header("Content-Type", "application/json")
						.body(new File(createAccountFilePath)).header("Authorization", "Bearer " + bearerToken).log()
						.all().when().post("/account/create").then().log().all().extract().response();

		int responseStatus = response.getStatusCode();
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time is:" + responseTime);

		System.out.println("Response Code: " + responseStatus);

		Assert.assertEquals(responseStatus, 201);

		String responseHeader = response.getHeader("Content-type");

		System.out.println("Response Header: " + responseHeader);

		Assert.assertEquals(responseHeader, "application/json", "Response header not matching!!");

		String responseBody = response.getBody().asString();

		System.out.println("Response body: " + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		String accountMessage = jp.getString("message");
		Assert.assertEquals(accountMessage, "Account created successfully.", "Account not created!!");

	}

	@Test(priority = 2)
	public void getLastAccountId() {
		Response response =

				given().baseUri(baseURI).header("Content-Type", "application/json")
						.header("Authorization", "Bearer " + bearerToken).log().all().when().get("/account/getall")
						.then().log().all().extract().response();

		
		String responseBody = response.getBody().asString();

		JsonPath jp = new JsonPath(responseBody);
		lastAccountId = jp.getString("records[0].account_id");

		if (lastAccountId != null) {
			System.out.println("last account id is not null");
		} else {
			System.out.println("Last account id is null.");
		}

	}
	@Test(priority = 3)
	public void readLastAccount() {
		Response response =

				given().baseUri(baseURI).header("Content-Type", "application/json").auth().preemptive()
						.basic("demo1@codefios.com", "abc123").queryParam("account_id", lastAccountId).log().all().when()
						.get("/account/getone").then().log().all().extract().response();

		

		String responseBody = response.getBody().asString();

		File requestBody = new File(createAccountFilePath);
		JsonPath jp2 = new JsonPath(requestBody);
		String expectedAccountName = jp2.getString("account_name");
		String expectedDescription = jp2.getString("description");
		String expectedBalance = jp2.getString("balance");
		String expectedAccountNumber = jp2.getString("account_number");
		

		JsonPath jp = new JsonPath(responseBody);
		String actualAccountName = jp.getString("account_name");
		Assert.assertEquals(actualAccountName, expectedAccountName, "Account Name doesn't match!!");
		
		String actualDescription = jp.getString("description");
		Assert.assertEquals(actualDescription, expectedDescription, "Account Description doesn't match!!");
		
		String actualBalance = jp.getString("balance");
		Assert.assertEquals(actualBalance, expectedBalance, "Account Balance doesn't match!!");
		
		String actualAccountNumber = jp.getString("account_number");
		Assert.assertEquals(actualAccountNumber, expectedAccountNumber, "Account Number doesn't match!!");
		
		System.out.println("Congratulations!!");
	}
}
