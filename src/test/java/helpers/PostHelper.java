package helpers;

import core.endpoints.PostRequests;
import dto.Post;
import dto.User;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class PostHelper extends UserHelper implements PostRequests {
    public Post testPost = Post.builder().title("post title").body("post body").build();

    public void createPostAndSetId(Post post) {
        createUserAndSetId(testUser);
        testPost.setUserId(testUser.getId());
        Response response = createPost(post);
        response.then().statusCode(HttpStatus.SC_OK);
        post.setId(response.jsonPath().getInt("data.id"));
    }

    public void deletePostAndUser(Post post) {
        deletePost(post);
        deleteUser(User.builder().id(post.getUserId()).build());
    }

    public void checkPost(Post expected) {
        Response response = getOnePost(expected);
        Post actual = response.jsonPath().getObject("data", Post.class);
        assertThat(expected, samePropertyValuesAs(actual, "createdAt", "updatedAt"));
    }
}
