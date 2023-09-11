package api.test;

import api.endpoints.BookEndPoints;
import api.endpoints.UserEndPoints;
import api.payload.ISBN;
import api.payload.Book;
import api.payload.User;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;



public class BookTests {

    User user;
    String userID;
    List<Book> collectionOfBooks;
    String jwt;
    String isbn;
    Logger logger = LogManager.getLogger(this.getClass());

    @BeforeClass
    public void setupData()
    {
        //Setup credentials
        Faker faker = new Faker();
        user = new User();
        String username = faker.name().username();
        user.setUsername(username);
        user.setPassword("S@1sdada.");

        //Retrieve userId
        Response userIdResponse = UserEndPoints.createUser(this.user);
        this.userID = userIdResponse.jsonPath().getString("userID");
//        System.out.println(this.userID);

        //Retrieve Authentication Token
        Response jwtResponse = UserEndPoints.getToken(user);
        this.jwt = jwtResponse.jsonPath().getString("token");
//        System.out.println(this.jwt);
    }
    @Test(priority = 1)
    public void testGetBooks()
    {
        logger.info("******** Getting all books ************");
        Response response = BookEndPoints.getBooks();
        this.collectionOfBooks = response.jsonPath().getList("books", Book.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("******** Got all the books ************");
    }

    @Test(priority = 2)
    public void testPostListOfBooks()
    {
        logger.info("******** Adding a book ************");
        this.isbn = this.collectionOfBooks.get(0).getIsbn();
        Response response = BookEndPoints.createBooks(this.userID, this.jwt, new ArrayList<ISBN>(){{add(new ISBN(isbn));}});

        Assert.assertEquals(response.getStatusCode(), 201);
        logger.info("******** Book is added ************");

        //Checking if book is created already
        Response responseAfterPost = UserEndPoints.getUser(this.userID, this.jwt);
        responseAfterPost.then().log().body();

    }

    @Test(priority = 3)
    public void tesPostNonExistingBook()
    {
        logger.info("******** Trying to add a non existing book ************");
        String invalidISBN = "1111111";
        Response response = BookEndPoints.createBooks(this.userID, this.jwt, new ArrayList<ISBN>(){{add(new ISBN(invalidISBN));}});
        Assert.assertEquals(response.getStatusCode(), 400, "ISBN supplied is not available in Books Collection!");
        logger.info("******** Book is not being added ************");
    }

    @Test(priority = 4)
    public void testUpdateFirstPreferredBook()
    {
        logger.info("******** Updating first book from preferred collection ************");
        //Getting first book
        Response responseBeforePut = UserEndPoints.getUser(this.userID, this.jwt);
        String userFirstBookIsbn = responseBeforePut.jsonPath().get("books[0].isbn");

        this.isbn = this.collectionOfBooks.get(2).getIsbn();
        Response response = BookEndPoints.updateBook(this.userID, this.jwt, userFirstBookIsbn, this.isbn);
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("******** Book is being updated ************");

        //Checking if book is updated properly
        Response responseAfterPut = UserEndPoints.getUser(this.userID, this.jwt);
        responseAfterPut.then().log().body();
    }

    @Test(priority = 5)
    public void testGetBookByStaticIsbn()
    {
        logger.info("******** Getting book with static isbn and checking if it has 278 pages ************");
        String staticIsbn = "9781491904244";
        Response response = BookEndPoints.getBook(staticIsbn, this.jwt);
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("******** Got the book ************");
        int bookPages = response.jsonPath().get("pages");
        Assert.assertEquals(bookPages, 278);
        logger.info("******** Checked that the book has 278 pages ************");
    }

    @Test(priority = 6)
    public void testDeleteExistingBook()
    {
        logger.info("******** Deleting existing book ************");
        Response response = BookEndPoints.deleteBook(this.userID, this.jwt, this.isbn);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 204);
        logger.info("******** Book is deleted ************");

        //Checking if book is deleted properly
        Response responseAfterDelete = UserEndPoints.getUser(this.userID, this.jwt);
        responseAfterDelete.then().log().body();
    }

    @Test(priority = 6)
    public void testDeleteNonExistingBook()
    {
        logger.info("******** Deleting non existing book ************");
        String isbn = "11111";
        Response response = BookEndPoints.deleteBook(this.userID, this.jwt, isbn);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 400);
        logger.info("******** Book is not being deleted ************");
    }

}
