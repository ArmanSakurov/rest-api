import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;

public class RestApiTests {
    Faker faker = new Faker();
    String name = faker.name().firstName();
    String job = faker.job().position();
    String email = faker.internet().emailAddress();
    String password = faker.number().randomNumber() + name;

    @Test
    void getSingleUserTest() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("data.email", is("janet.weaver@reqres.in"));
    }

    @Test
    public void postCreateUserTest(){
        given().log().all().
                body("{\"name\": \"" + name + "\",\"job\": \"" + job + "\"}")
                .contentType("application/json")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .assertThat().body("name", is(name))
                .assertThat().body("job", is(job));
    }

    @Test
    public void postUpdateUserTest(){
        String name = faker.name().firstName();
        String job = faker.job().position();
        given().log().all()
                .body("{\"name\": \"" + name + "\",\"job\": \"" + job + "\"}")
                .contentType("application/json")
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .assertThat().body("name", is(name))
                .assertThat().body("job", is(job));
    }

    @Test
    public void deleteUserTest(){
        given().log().all()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    public void postRegisterSuccessfulUserTest(){
        given().log().all()
                .body("{\"email\": \"" + email + "\",\"password\": \"" + password + "\"}")
                .contentType("application/json")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201);
    }

    @Test
    public void getUserListTest(){
                when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body("data.email", hasItems("michael.lawson@reqres.in", "lindsay.ferguson@reqres.in", "george.edwards@reqres.in"),
                        "data[0].email", is("michael.lawson@reqres.in"),
                        "data.first_name", hasItems("Lindsay"),
                        "total_pages", is(2));
    }
}
