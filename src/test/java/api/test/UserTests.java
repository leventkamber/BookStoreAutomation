package api.test;

import api.endpoints.UserEndPoints;
import api.payload.User;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserTests {
    Logger logger = LogManager.getLogger(this.getClass());
    Faker faker;
    User user;

    @BeforeClass
    public void setupData()
    {
        faker = new Faker();
        user = new User();
        String username = faker.name().username();
        user.setUsername(username);
        user.setPassword("S@1sdada.");
    }

    @Test(priority = 1)
    public void testPostUser()
    {
        logger.info("******** Creating user ************");
        Response response = UserEndPoints.createUser(user);

        Assert.assertEquals(response.getStatusCode(), 201);
        logger.info("******** User is created ************");
    }
    @Test(priority = 2)
    public void testPostUserWithDuplicatedCredentials()
    {
        logger.info("******** Creating user with duplicated username and password ************");
        Response response = UserEndPoints.createUser(user);

        Assert.assertEquals(response.getStatusCode(), 406);
        logger.info("******** User is not being created ************");
    }

    @Test(priority = 3)
    public void testPostUserWithEmptyCredentials()
    {
        logger.info("******** Creating user with empty username and password ************");
        user.setUsername("");
        user.setPassword("");
        Response response = UserEndPoints.createUser(user);

        Assert.assertEquals(response.getStatusCode(), 400);
        logger.info("******** User is not being created ************");
    }

    @Test(priority = 5)
    public void testPostUserWithShortPassword()
    {
        logger.info("******** Creating user with short password ************");
        user.setPassword("1As/.!");
        Response response = UserEndPoints.createUser(user);

        Assert.assertEquals(response.getStatusCode(), 400);
        logger.info("******** User is not being created ************");
    }

    @Test(priority = 6)
    public void testPostUserNotContainingDigit()
    {
        logger.info("******** Creating user without at least 1 digit in password ************");
        user.setPassword("sd@#aAs/.!");
        Response response = UserEndPoints.createUser(user);

        Assert.assertEquals(response.getStatusCode(), 400);
        logger.info("******** User is not being created ************");
    }

    @Test(priority = 7)
    public void testPostUserNotContainingAlphaChar()
    {
        logger.info("******** Creating user without containing one non alphanumeric character in password ************");
        user.setPassword("sd12aAsdsadas");
        Response response = UserEndPoints.createUser(user);
        Assert.assertEquals(response.getStatusCode(), 400);
        logger.info("******** User is not being created ************");
    }

    @Test(priority = 8)
    public void testPostUserNotContainingUpperCase()
    {
        logger.info("******** Creating user without containing one uppercase ('A'-'Z') in password ************");
        user.setPassword("sd&%>12asadas");
        Response response = UserEndPoints.createUser(user);

        Assert.assertEquals(response.getStatusCode(), 400);
        logger.info("******** User is not being created ************");
    }

    @Test(priority = 9)
    public void testPostUserNotContainingLowerCase()
    {
        logger.info("******** Creating user without containing one lowercase ('a'-'z') in password ************");
        user.setPassword("A@?>312ASDASSDS");
        Response response = UserEndPoints.createUser(user);

        Assert.assertEquals(response.getStatusCode(), 400);
        logger.info("******** User is not being created ************");
    }



    //Stress test
    @Test(priority = 10, enabled = false)
    public void testPostUserWithTooLongPassword()
    {
        user.setUsername("myUser");
        user.setPassword("!1Ss?." + faker.internet().password(250,251));
        Response response = UserEndPoints.createUser(user);
        Assert.assertEquals(response.getStatusCode(), 504, "Gateway Time-out!");
    }



}
