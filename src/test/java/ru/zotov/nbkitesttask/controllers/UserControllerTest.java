package ru.zotov.nbkitesttask.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.zotov.nbkitesttask.dto.UserRequest;
import ru.zotov.nbkitesttask.dto.UserResponse;
import ru.zotov.nbkitesttask.entity.User;
import ru.zotov.nbkitesttask.repository.UserRepository;
import ru.zotov.nbkitesttask.utils.UserUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTest {

    @Autowired
    UserRepository userRepository;


    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8888;
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void getUser() {
        User testUser = addTestUser();
        UserResponse response = RestAssured
                .given()
                    .log().all()
                .when()
                    .get("/user/%s".formatted(testUser.getId()))
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .extract()
                    .as(UserResponse.class);

        assertNotNull(response.getId());
        assertEquals(testUser.getId(), response.getId());
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(testUser.getFirstName(), response.getFirstName());
        assertEquals(testUser.getSurname(), response.getSurname());
        assertEquals(testUser.getAge(), response.getAge());
    }

    @Test
    void createUser() {
        UserRequest userRequest = UserUtils.produceUserRequest();

        UserResponse userResponse = RestAssured
                .given()
                    .log().all()
                .when()
                    .body(userRequest)
                    .contentType(ContentType.JSON)
                    .post("/user")
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(UserResponse.class);

        assertNotNull(userResponse.getId());
        assertEquals(userRequest.getFirstName(), userResponse.getFirstName());
        assertEquals(userRequest.getSurname(), userResponse.getSurname());
        assertEquals(userRequest.getEmail(), userResponse.getEmail());
        assertEquals(userRequest.getAge(), userResponse.getAge());
    }

    @Test
    void updateUser() {
        User user = addTestUser();
        UserRequest userRequest = new UserRequest(
                "updatedName",
                "updatedSurname",
                "updatedEmail@mail.com",
                30
        );

        UserResponse response = RestAssured
                .given()
                    .log().all()
                .when()
                    .body(userRequest)
                    .contentType(ContentType.JSON)
                    .put("/user/%s".formatted(user.getId()))
                .then()
                    .extract()
                    .as(UserResponse.class);

        assertNotNull(response);
        assertEquals(user.getId(), response.getId());
        assertEquals(userRequest.getFirstName(), response.getFirstName());
        assertEquals(userRequest.getSurname(), response.getSurname());
        assertEquals(userRequest.getEmail(), response.getEmail());
        assertEquals(userRequest.getAge(), response.getAge());
    }

    @Test
    void deleteUser() {
        User user = addTestUser();

        RestAssured
                .given()
                .log().all()
                .when()
                .delete("/user/%s".formatted(user.getId()))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    private User addTestUser() {
        User user = UserUtils.produceUser();
        user.setId(null);
        return userRepository.save(user);
    }
}