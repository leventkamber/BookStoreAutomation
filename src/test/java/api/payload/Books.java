package api.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Books {
    String userId;
    List<ISBN> collectionOfIsbns;
    String isbn;

    public Books(String userId, List<ISBN> collectionOfIsbns, String isbn) {
        this.userId = userId;
        this.collectionOfIsbns = collectionOfIsbns;
        this.isbn = isbn;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getisbn() {
        return isbn;
    }

    public void setisbn(String isbn) {
        this.isbn = isbn;
    }
    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("collectionOfIsbns")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<ISBN> getCollectionOfIsbns() {
        return collectionOfIsbns;
    }

    public void setCollectionOfIsbns(List<ISBN> collectionOfIsbns) {
        this.collectionOfIsbns = collectionOfIsbns;
    }

}
