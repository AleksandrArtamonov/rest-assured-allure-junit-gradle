package core.endpoints;

import core.Endpoints;
import core.Requests;
import dto.Post;
import dto.User;
import io.restassured.response.Response;

import java.util.HashMap;

public interface PostRequests extends Requests {
    default Response createPost(Post post) {
        return postRequestWithToken(Endpoints.postPath, post);
    }

    default Response updatePost(Post post) {
        return putRequestWithToken(Endpoints.postPath + "/" + post.getId(), post);
    }

    default Response getOnePost(Post post) {
        return getRequest(Endpoints.postPath + "/" + post.getId());
    }

    default Response getAllPosts() {
        return getRequest(Endpoints.postPath);
    }

    default Response getPostsForUser(User user) {
        return getRequest(Endpoints.userPath + "/" + user.getId() + Endpoints.postPath );
    }

    default Response createPostForUser(User user) {
        return getRequest(Endpoints.userPath + "/" + user.getId() + Endpoints.postPath );
    }

    default Response getFilteredPosts(HashMap params) {
        return getRequestWithReqParams(Endpoints.postPath, params);
    }

    default Response deletePost(Post post) {
        return deleteRequestWithToken(Endpoints.postPath + "/"+ post.getId());
    }
}
