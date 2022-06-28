package com.gigathinking.validations.resources;

import com.gigathinking.validations.constraints.Exists;
import com.gigathinking.validations.models.Todo;
import com.gigathinking.validations.models.requests.TodoCreateRequest;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/todos")
public class TodoResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Todo> getTodos() {
        return Todo.listAll();
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTodo(@Valid TodoCreateRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.title);
        todo.setCompleted(request.isCompleted);
        todo.persist();

        return Response.status(Response.Status.CREATED).entity(todo).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Todo getTodo(
        @Exists(table = "todos")    /*  We use the custom validation here to check if the id exists in the table */
        @PathParam("id") Long todoId
    ) {
        return Todo.findById(todoId);
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Todo updateTodo(
        @Exists(table = "todos")    /*  We use the custom validation here to check if the id exists in the table */
        @PathParam("id") Long todoId,
        @Valid TodoCreateRequest request
    ) {
        Todo todo = Todo.findById(todoId);
        todo.setTitle(request.title);
        todo.setCompleted(request.isCompleted);
        todo.persist();

        return todo;
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTodo(
        @Exists(table = "todos")    /*  We use the custom validation here to check if the id exists in the table */
        @PathParam("id") Long todoId
    ) {
        Todo.deleteById(todoId);

        return Response.noContent().build();
    }
}