package tests;

import com.github.javafaker.Faker;
import io.qameta.allure.restassured.AllureRestAssured;
import models.lombok.CreateUserLombokModel;
import models.lombok.CreateUserResponseLombokModel;
import models.pojo.CreateUserModel;
import models.pojo.CreateUserResponseModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static specs.CreateUserSpecs.createUserRequestSpec;
import static specs.CreateUserSpecs.createUserResponseSpec;

public class RestApiExtendedTests {
//    Faker faker = new Faker();
//    String name = faker.name().firstName();
//    String job = faker.job().position();
//    String email = faker.internet().emailAddress();
//    String password = faker.number().randomNumber() + name;
//
//
//    @Test
//    public void postCreateUserTest(){
//        given().log().all().
//                body("{\"name\": \"" + name + "\",\"job\": \"" + job + "\"}")
//                .contentType(JSON)
//                .when()
//                .post("https://reqres.in/api/users")
//                .then()
//                .statusCode(201)
//                .assertThat().body("name", is(name))
//                .assertThat().body("job", is(job));
//    }

    @Test
    public void createUserWithPojoModelTest(){
        CreateUserModel body = new CreateUserModel();
        body.setName("testName");
        body.setJob("testJob");

        CreateUserResponseModel response =
                given()
                        .filter(new AllureRestAssured())
                        .log().all()
                        .contentType(JSON)
                        .body(body)
                        .when()
                        .post("https://reqres.in/api/users")
                        .then()
                        .statusCode(201)
                        .extract().as(CreateUserResponseModel.class);

        Assertions.assertThat(response.getName()).isEqualTo("testName");
        Assertions.assertThat(response.getJob()).isEqualTo("testJob");
    }

    @Test
    void createUserWithLombokTest() {
        CreateUserLombokModel body = new CreateUserLombokModel();
        body.setName("testName");
        body.setJob("testJob");

        CreateUserResponseLombokModel response = given()
                .filter(withCustomTemplates()) //with Custom Allure Listener
                .log().all()
                .contentType(JSON)
                .body(body)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().as(CreateUserResponseLombokModel.class);

        Assertions.assertThat(response.getName()).isEqualTo("testName");
        Assertions.assertThat(response.getJob()).isEqualTo("testJob");
    }

    @Test
    void createUserWithSpecsTest() {
        CreateUserLombokModel body = new CreateUserLombokModel();
        body.setName("testName");
        body.setJob("testJob");

        CreateUserLombokModel response = given() //given(createUserRequestSpec)
                .spec(createUserRequestSpec)
                .body(body)
                .when()
                .post()
                .then()
                .spec(createUserResponseSpec)
                .extract().as(CreateUserLombokModel.class);

        Assertions.assertThat(response.getName()).isEqualTo("testName");
        Assertions.assertThat(response.getJob()).isEqualTo("testJob");
    }
}
