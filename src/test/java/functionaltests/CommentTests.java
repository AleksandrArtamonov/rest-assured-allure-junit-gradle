package functionaltests;

import core.Dictionaries;
import core.Matchers;
import dto.Pagination;
import dto.Comment;
import dto.CommentPage;
import helpers.CommentHelper;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class CommentTests extends CommentHelper {
    @Nested
    public class NeedOneCommentForTest {

        @BeforeEach
        public void testSetup() {
            createCommentAndSetId(testComment);
        }

        @Nested
        public class NeedToDeleteCommentAfterTest {

            @AfterEach
            public void tearDown() {
                deleteCommentWithPostAndUser(testComment, testPost);
            }

            @Test
            public void createCommentTest() {
                checkComment(testComment);
            }

            @Test
            public void updateCommentTest() {
                Comment updatedComment = testComment.toBuilder().name("updated comment name").email("updatedcomment@email.com").body("updated comment body").build();
                Response response = updateComment(updatedComment);
                response.then().statusCode(HttpStatus.SC_OK);
                checkComment(updatedComment);
            }

            @Test
            public void getComment() {
                Response response = getOneComment(testComment);
                response.then().statusCode(HttpStatus.SC_OK);
                Comment actual = response.jsonPath().getObject("data", Comment.class);
                assertThat(testComment, samePropertyValuesAs(actual, "createdAt", "updatedAt"));
            }

            @Nested
            public class GetCommentsForPost {
                Comment secondTestComment;

                @AfterEach
                public void tearDown() {
                    deleteComment(secondTestComment);
                }

                @Test
                public void getCommentsForPostTest() {
                    secondTestComment = testComment.toBuilder().name("second comment name").email("secondcomment@email.com").body("second comment body").build();
                    secondTestComment.setId(createComment(secondTestComment).jsonPath().getInt("data.id"));
                    Response response = getCommentsForPost(testPost);
                    response.then().statusCode(HttpStatus.SC_OK);
                    CommentPage commentPage = response.as(CommentPage.class);
                    assertThat(new Pagination(2, 1, 1, 20), samePropertyValuesAs(commentPage.meta.getPagination()));
                    Matchers.checkLists(Arrays.asList(testComment, secondTestComment), Arrays.asList(commentPage.data));
                }
            }

        }

        @Nested
        public class DontNeedToDeleteCommentAfterTest {

            @AfterEach
            public void tearDown() {
                deletePostAndUser(testPost);
            }

            @Test
            public void deleteCommentTest() {
                Response response = deleteComment(testComment);
                response.then().statusCode(HttpStatus.SC_OK);
                Assertions.assertEquals(HttpStatus.SC_NO_CONTENT, response.jsonPath().getInt("code"));
                response = getOneComment(testComment);
                Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.jsonPath().getInt("code"));
                Assertions.assertEquals(Dictionaries.resourceNotFound, response.jsonPath().getString("data.message"));
            }
        }
    }

    @Test
    public void getAllCommentsTest() {
        Response response = getAllComments();
        CommentPage commentPage = response.as(CommentPage.class);
        Assertions.assertEquals(commentPage.getMeta().getPagination().getPage(), 1);
        Assertions.assertEquals(commentPage.getData().length, commentPage.getMeta().getPagination().getLimit());
        Assertions.assertEquals(commentPage.getMeta().getPagination().getTotal()/commentPage.getMeta().getPagination().getLimit() + 1, commentPage.getMeta().getPagination().getPages());
    }

    @Test
    public void commentPaginationTest() {
        Response response = getFilteredComments(new HashMap<String, Integer>() {{
            put("page", 2);
        }});
        response.then().statusCode(HttpStatus.SC_OK);
        CommentPage commentPage = response.as(CommentPage.class);
        Assertions.assertEquals(commentPage.getMeta().getPagination().getPage(), 2);
    }
}
