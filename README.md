# Custom Hibernate Validations

This project demonstrates how to use custom hibernate validations

## Supported validations

Below are the custom hibernate validations available. This project will be updated to have many more custom validations

* Exists

### Exists Validation

This validation can be used to check if an item exists in a table before operating on it. For example before updating
a 'task' item you can check if the item exists in the 'tasks' table or not.

#### Usage Example
```
import com.gigathinking.validations.constraints.Exists;

@Path("/api/v1/tasks/{id}")
@GET
public Task getTask(
    @Exists(table = "tasks") // We can check if the item exists in tasks table before fetching it
    @PathParam("id") Long taskId
) {
  ....
}
```