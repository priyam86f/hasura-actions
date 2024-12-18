package com.example.demo.webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Entity.User; // Import the User model class

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    // This will be invoked by Hasura for the custom action
    @PostMapping("/createUserWithUppercase")
    public ResponseEntity<?> handleCreateUser(@RequestBody CreateUserRequest request) {
        // Convert firstName and lastName to uppercase
        String firstNameUpper = request.getFirstName().toUpperCase();
        String lastNameUpper = request.getLastName().toUpperCase();

        // Log for debugging
        System.out.println("Transformed Names: " + firstNameUpper + " " + lastNameUpper);

        // Create a new User entity and set its properties
        User user = new User();
        user.setFirstName(firstNameUpper);
        user.setLastName(lastNameUpper);
        user.setAge(request.getAge());

        // Save the user to the database
        userRepository.save(user);

        // Create the GraphQL-compatible response format
        CreateUserResponse response = new CreateUserResponse("SUCCESS", "User created successfully", user);

        // Return the response wrapped in 'data' for GraphQL format
        return ResponseEntity.ok(new GraphQLResponse(new Data(response)));
    }

    @GetMapping("/home")
    public String getMethodName() {
        return "new String();";
    }
}

// Request class to match Hasura action's input
class CreateUserRequest {
    private String firstName;
    private String lastName;
    private int age;

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

// Response class to send back to Hasura in GraphQL format
class CreateUserResponse {
    private String status;
    private String message;
    private User user;

    // Constructor
    public CreateUserResponse(String status, String message, User user) {
        this.status = status;
        this.message = message;
        this.user = user;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

// Wrapper class for GraphQL response
class GraphQLResponse {
    private Data data;

    public GraphQLResponse(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}

// Data class that contains the actual response of the action
class Data {
    private CreateUserResponse createUserWithUppercase;

    public Data(CreateUserResponse createUserWithUppercase) {
        this.createUserWithUppercase = createUserWithUppercase;
    }

    public CreateUserResponse getCreateUserWithUppercase() {
        return createUserWithUppercase;
    }

    public void setCreateUserWithUppercase(CreateUserResponse createUserWithUppercase) {
        this.createUserWithUppercase = createUserWithUppercase;
    }
}
