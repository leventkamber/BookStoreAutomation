package api.endpoints;

public class Routes {

    public static  String user_base_url = "https://demoqa.com/Account/v1";

    //User module
    public static String user_post_url = String.format("%s/User", user_base_url);
    public static String user_get_url = String.format("%s/User/{UUID}", user_base_url);
    public static String user_jwt_post_url = String.format("%s/GenerateToken", user_base_url);


    public static  String book_base_url = "https://demoqa.com/BookStore/v1";

    //Book module
    public static String books_get_url = String.format("%s/Books", book_base_url);
    public static String books_post_url = String.format("%s/Books", book_base_url);
    public static String book_get_url = String.format("%s/Book", book_base_url);
    public static String book_update_url = String.format("%s/Books/{ISBN}", book_base_url);
    public static String book_delete_url = String.format("%s/Book", book_base_url);




}
