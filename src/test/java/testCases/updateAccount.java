package testCases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class updateAccount extends GenerateBearerToken {
	String baseURI;
	HashMap<String,String> updateAccountMap;
	String lastAccountId;

	public updateAccount() {
		baseURI = "https://qa.codefios.com/api";
		updateAccountMap = new HashMap<String,String>();
	}
	
	public Map<String,String> updateMap(){
		updateAccountMap.put("account_id", "231");
		updateAccountMap.put("account_name", "This Techfios account");
		updateAccountMap.put("account_number", "333333330");
		updateAccountMap.put("description", "description");
		updateAccountMap.put("balance", "112123.00");
		
		
		return updateAccountMap;
	}

	@Test(priority = 1)
	public void updateOneAccount() {
		Response response =

				given().baseUri(baseURI).header("Content-Type", "application/json")
						.body(updateMap()).header("Authorization", "Bearer " + bearerToken).log()
						.all().when().put("/account/update").then().log().all().extract().response();

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
		String accountMessage = jp.getString("message");
		Assert.assertEquals(accountMessage, "Account updated successfully.", "Account not updated!!");

	}

	@Test(priority = 2)
	public void readLastAccount() {
		Response response =

				given().baseUri(baseURI).header("Content-Type", "application/json").auth().preemptive()
						.basic("demo1@codefios.com", "abc123").queryParam("account_id", updateMap().get("account_id")).log().all().when()
						.get("/account/getone").then().log().all().extract().response();

		

		String responseBody = response.getBody().asString();

		
		String expectedAccountName = updateMap().get("account_name");
		String expectedDescription = updateMap().get("description");
		String expectedBalance = updateMap().get("balance");
		String expectedAccountNumber = updateMap().get("account_number");
		

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
