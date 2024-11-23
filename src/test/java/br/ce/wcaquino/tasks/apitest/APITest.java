package br.ce.wcaquino.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}
	
	@Test
	public void deveRetornarTarefas() {
		RestAssured.given()
		.when()
			.get("/todo")
		.then()
			.log().all()
		;
	}
	
	@Test
	public void deveAdicionarTarefaComSucesso() {
		RestAssured.given()
			.body("{ \"task\": \"Teste via API\",\"dueDate\": \"2025-01-28\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201)
		;
		
	}
	
	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		RestAssured.given()
			.body("{ \"task\": \"Teste via API\",\"dueDate\": \"2023-09-28\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"))
		;
		
	}
	
	@Test
	public void deveRemoverTarefaComSucesso() {
		// inserir
		Integer id = RestAssured.given()
			.body("{ \"task\": \"Tarefa para remoção\",\"dueDate\": \"2025-01-28\" }")
			.contentType(ContentType.JSON)
		.when()
			//.log().all()
			.post("/todo")
		.then()
			.statusCode(201)
			.extract().path("id")
		;
		
		System.out.println(id);
		
		// remover
		RestAssured.given()
		.when()
			.delete("/todo/"+id)
		.then()
			.statusCode(204)
		;
	}
	
}

