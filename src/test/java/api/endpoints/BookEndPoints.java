package api.endpoints;


import api.payload.Books;
import api.payload.ISBN;
import api.payload.Book;
import api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BookEndPoints {

    public static Response createBooks(String userId, String token, List<ISBN> isbns)
    {
        Books body = new Books(userId, isbns, null);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(body)
                .when()
                .post(Routes.books_post_url);

        return response;
    }
    public static Response getBooks()
    {
        Response response = given()
                .when()
                .get(Routes.books_get_url);

        return response;
    }
    public static Response getBook(String isbn, String token)
    {
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("ISBN",isbn)
                .when()
                .get(Routes.book_get_url);

        return response;
    }
    public static Response deleteBook(String userId, String token, String isbn)
    {
        Books body = new Books(userId, null, isbn);
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParam("userId", userId)
                .body(body)
                .when()
                .delete(Routes.book_delete_url);

        return response;
    }
    public static Response updateBook(String userId, String token, String oldIsbn, String newIsbn)
    {
        Books body = new Books(userId, null, newIsbn);
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("ISBN", oldIsbn)
                .body(body)
                .when()
                .put(Routes.book_update_url);

        return response;
    }
}
