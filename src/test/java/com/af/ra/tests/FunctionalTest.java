package com.af.ra.tests;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class FunctionalTest {

	@BeforeClass
	public static void setup() {
		String port = System.getProperty("server.port");
		if (port == null) {
			RestAssured.port = Integer.valueOf(8080);
		} else {
			RestAssured.port = Integer.valueOf(port);
		}

		String basePath = System.getProperty("server.base");
		if (basePath == null) {
			basePath = "/rest/v2/";
		}
		RestAssured.basePath = basePath;

		String baseHost = System.getProperty("server.host");
		if (baseHost == null) {
			baseHost = "https://restcountries.eu";
		}
		RestAssured.baseURI = baseHost;

	}

	@Test
	public void searchByRegionTest() {
		Response response = given().when().get("/region/europe");

		// verification for 200 response
		assertEquals(200, response.getStatusCode());

		// verification for size > 0
		List<String> s = response.jsonPath().get("name");
		assertTrue(s.size() > 0);

	}

	@Test
	public void searchByInvalidRegionTest() {
		Response response = given().pathParam("region", "worngvalue").when().get("/region/{region}");

		// verification for 404 response
		assertEquals(404, response.getStatusCode());

		// verification for message not found
		String message = response.jsonPath().getString("message");
		String status = response.jsonPath().getString("status");
		assertTrue(message.equals("Not Found"));
		assertTrue(status.equals("404"));

	}

	@Test
	public void getAll() {
		Response response = given().when().get("all");

		// verification for 200 response
		assertEquals(200, response.getStatusCode());

		// verification for size > 0
		List<String> s = response.jsonPath().get("name");
		assertTrue(s.size() > 200);

	}

	//
	@Test
	public void getFilterResponse() {
		Response response = given().pathParam("name", "united").when().get("name/{name}?fields=name;capital");

		// verification for 200 response
		assertEquals(200, response.getStatusCode());

		// verification for size > 0
		JsonPath jp = response.jsonPath();
		System.out.println(jp.toString());

	}

}