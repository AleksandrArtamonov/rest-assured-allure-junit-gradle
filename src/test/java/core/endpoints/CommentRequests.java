package core.endpoints;

import core.Endpoints;
import core.Requests;
import dto.Comment;
import dto.Post;
import io.restassured.response.Response;

import java.util.HashMap;

public interface CommentRequests extends Requests {
    default Response createComment(Comment comment) {
        return postRequestWithToken(Endpoints.commentPath, comment);
    }

    default Response updateComment(Comment comment) {
        return putRequestWithToken(Endpoints.commentPath + "/" + comment.getId(), comment);
    }

    default Response getOneComment(Comment comment) {
        return getRequest(Endpoints.commentPath + "/" + comment.getId());
    }

    default Response getAllComments() {
        return getRequest(Endpoints.commentPath);
    }

    default Response getCommentsForPost(Post post) {
        return getRequest(Endpoints.postPath + "/" + post.getId() + Endpoints.commentPath);
    }

    default Response getFilteredComments(HashMap params) {
        return getRequestWithReqParams(Endpoints.commentPath, params);
    }

    default Response deleteComment(Comment comment) {
        return deleteRequestWithToken(Endpoints.commentPath + "/"+ comment.getId());
    }
}
