package com.gigathinking.validations;

import com.gigathinking.validations.models.Todo;
import com.gigathinking.validations.models.requests.TodoCreateRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.ws.rs.core.Response;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoResourceTest {

    public static final String TODO_TITLE = "This is a task";
    public static final Boolean TODO_COMPLETION_STATUS = false;

    public static final String TODO_UPDATED_TITLE = "This is an updated task";
    public static final Boolean TODO_UPDATED_COMPLETION_STATUS = true;

    private static Long createdTodoId;

    @Test
    @Order(1)
    public void createsTodo() {
        TodoCreateRequest request = new TodoCreateRequest();
        request.title = TODO_TITLE;
        request.isCompleted = TODO_COMPLETION_STATUS;

        Todo todo = given()
                .when()
                .contentType(JSON)
                .accept(JSON)
                .body(request)
                .post("/api/v1/todos")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract().body().as(Todo.class);

        assertNotNull(todo);
        assertEquals(TODO_TITLE, todo.getTitle());
        assertEquals(TODO_COMPLETION_STATUS, todo.getCompleted());

        createdTodoId = todo.getId();
        assertNotNull(createdTodoId);
    }

    @Test
    @Order(2)
    public void getsTodos() {
        List<Todo> todoList = given()
                .when()
                .accept(JSON)
                .contentType(JSON)
                .get("/api/v1/todos")
                .then()
                .statusCode(OK.getStatusCode())
                .extract()
                .body()
                .as(new TypeRef<List<Todo>>() {});

        assertNotNull(todoList);
        assertEquals(1, todoList.size());

        Todo todo = todoList.get(0);
        assertNotNull(todo);
        assertEquals(TODO_TITLE, todo.getTitle());
        assertEquals(TODO_COMPLETION_STATUS, todo.getCompleted());
    }

    @Test
    @Order(3)
    public void getsParticularTodo() {
        Todo todo = given()
                .when()
                .accept(JSON)
                .get("/api/v1/todos/" + createdTodoId.toString())
                .then()
                .statusCode(OK.getStatusCode())
                .extract().body().as(Todo.class);

        assertNotNull(todo);
        assertEquals(TODO_TITLE, todo.getTitle());
        assertEquals(TODO_COMPLETION_STATUS, todo.getCompleted());
    }

    @Test
    @Order(4)
    public void failsRequestingInvalidTodo() {
        long invalidTodoId = new Random().nextLong();
        given()
                .when()
                .accept(JSON)
                .get("/api/v1/todos/" + invalidTodoId)
                .then()
                .statusCode(BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(5)
    public void updatesTodo() {
        TodoCreateRequest request = new TodoCreateRequest();
        request.title = TODO_UPDATED_TITLE;
        request.isCompleted = TODO_UPDATED_COMPLETION_STATUS;

        Todo todo = given()
                .when()
                .contentType(JSON)
                .accept(JSON)
                .body(request)
                .patch("/api/v1/todos/" + createdTodoId)
                .then()
                .statusCode(OK.getStatusCode())
                .extract().body().as(Todo.class);

        assertNotNull(todo);
        assertEquals(TODO_UPDATED_TITLE, todo.getTitle());
        assertEquals(TODO_UPDATED_COMPLETION_STATUS, todo.getCompleted());

        assertEquals(createdTodoId, todo.getId());
    }

    @Test
    @Order(6)
    public void failsUpdatingInvalidTodo() {
        TodoCreateRequest request = new TodoCreateRequest();
        request.title = TODO_UPDATED_TITLE;
        request.isCompleted = TODO_UPDATED_COMPLETION_STATUS;

        long invalidTodoId = new Random().nextLong();

        given()
                .when()
                .contentType(JSON)
                .accept(JSON)
                .body(request)
                .patch("/api/v1/todos/" + invalidTodoId)
                .then()
                .statusCode(BAD_REQUEST.getStatusCode());
    }

}