package functionaltests;

import core.Dictionaries;
import core.Matchers;
import dto.*;
import helpers.PostHelper;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class PostTests extends PostHelper {

    @Nested
    public class NeedOnePostForTest {

        @BeforeEach
        public void testSetup() {
            createPostAndSetId(testPost);
        }

        @Nested
        public class NeedToDeletePostAfterTest {

            @AfterEach
            public void tearDown() {
                deletePostAndUser(testPost);
            }

            @Test
            public void createPostTest() {
                checkPost(testPost);
            }

            @Test
            public void updatePostTest() {
                Post updatedPost = testPost.toBuilder().title("post title").body("post body").build();
                Response response = updatePost(updatedPost);
                response.then().statusCode(HttpStatus.SC_OK);
                checkPost(updatedPost);
            }

            @Test
            public void getPost() {
                Response response = getOnePost(testPost);
                response.then().statusCode(HttpStatus.SC_OK);
                Post actual = response.jsonPath().getObject("data", Post.class);
                assertThat(testPost, samePropertyValuesAs(actual, "createdAt", "updatedAt"));
            }

            @Nested
            public class GetPostsForUser {
                Post secondTestPost;

                @AfterEach
                public void tearDown() {
                    deletePost(secondTestPost);
                }

                @Test
                public void getPostsForUserTest() {
                    secondTestPost = testPost.toBuilder().body("second body").title("second title").build();
                    secondTestPost.setId(createPost(secondTestPost).jsonPath().getInt("data.id"));
                    Response response = getPostsForUser(testUser);
                    response.then().statusCode(HttpStatus.SC_OK);
                    PostPage postPage = response.as(PostPage.class);
                    assertThat(new Pagination(2, 1, 1, 20), samePropertyValuesAs(postPage.meta.getPagination()));
                    Matchers.checkLists(Arrays.asList(testPost, secondTestPost), Arrays.asList(postPage.data));
                }
            }

        }

        @Nested
        public class DontNeedToDeletePostAfterTest {

            @AfterEach
            public void tearDown() {
                deleteUser(testUser);
            }

            @Test
            public void deletePostTest() {
                Response response = deletePost(testPost);
                response.then().statusCode(HttpStatus.SC_OK);
                Assertions.assertEquals(HttpStatus.SC_NO_CONTENT, response.jsonPath().getInt("code"));
                response = getOnePost(testPost);
                Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.jsonPath().getInt("code"));
                Assertions.assertEquals(Dictionaries.resourceNotFound, response.jsonPath().getString("data.message"));
            }
        }
    }

    @Test
    public void getAllPostsTest() {
        Response response = getAllPosts();
        PostPage postPage = response.as(PostPage.class);
        Assertions.assertEquals(postPage.getMeta().getPagination().getPage(), 1);
        Assertions.assertEquals(postPage.getData().length, postPage.getMeta().getPagination().getLimit());
        Assertions.assertEquals(postPage.getMeta().getPagination().getTotal()/postPage.getMeta().getPagination().getLimit() + 1, postPage.getMeta().getPagination().getPages());
    }

    @Test
    public void postPaginationTest() {
        Response response = getFilteredPosts(new HashMap<>() {{
            put("page", 2);
        }});
        response.then().statusCode(HttpStatus.SC_OK);
        PostPage postPage = response.as(PostPage.class);
        Assertions.assertEquals(postPage.getMeta().getPagination().getPage(), 2);
    }
}
