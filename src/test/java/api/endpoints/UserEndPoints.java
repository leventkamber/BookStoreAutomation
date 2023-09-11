package api.endpoints;

import api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserEndPoints {

    public static Response createUser(User user)
    {
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(user)
                .when()
                .post(Routes.user_post_url);

        return response;
    }
    public static Response getToken(User user)
    {
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(user)
                .when()
                .post(Routes.user_jwt_post_url);

        return response;
    }
    public static Response getUser(String UserId, String token)
    {
        Response response = given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParam("UUID", UserId)
                .when()
                .get(Routes.user_get_url);

        return response;
    }

}
