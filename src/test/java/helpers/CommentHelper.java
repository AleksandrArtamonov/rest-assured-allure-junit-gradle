package helpers;

import core.endpoints.CommentRequests;
import dto.Comment;
import dto.Post;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class CommentHelper extends PostHelper implements CommentRequests {
    public Comment testComment = Comment.builder().name("comment name").email("comment@email.com").body("comment body").build();

    public void createCommentAndSetId(Comment comment) {
        createPostAndSetId(testPost);
        comment.setPostId(testPost.getId());
        Response response = createComment(comment);
        response.then().statusCode(HttpStatus.SC_OK);
        comment.setId(response.jsonPath().getInt("data.id"));
    }
    public void deleteCommentWithPostAndUser(Comment comment, Post post) {
        deleteComment(comment);
        deletePostAndUser(post);
    }

    public void checkComment(Comment expected) {
        Response response = getOneComment(expected);
        Comment actual = response.jsonPath().getObject("data", Comment.class);
        assertThat(expected, samePropertyValuesAs(actual, "createdAt", "updatedAt"));
    }
}
