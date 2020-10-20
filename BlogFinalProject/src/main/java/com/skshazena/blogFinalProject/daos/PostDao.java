package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.dtos.Post;
import java.util.List;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 18, 2020
 */
public interface PostDao {

    Post getPostById(int postId);

    List<Post> getAllPostsForAdminNewestFirst();

    List<Post> getAllPostsForBlogNonStaticNewestFirst();

    List<Post> getAllPostsForBlogThatAreStaticNewestFirst();

    List<Post> getAllPostsForHashtagForAdminNewestFirst(int hashtagId);

    List<Post> getAllPostsForHashtagForUserNewestFirst(int hashtagId);

    List<Post> getAllPostsNeedingApprovalWrittenByCreatorOldestFirst(int userId);

    List<Post> getAllPostsWrittenByCreatorNewestFirst(int userId);

    List<Post> getAllPostsNeedingApprovalForAdminOldestFirst();

    void updatePost(Post post);

    void deletePost(int postId);

    Post createPost(Post post);
}
