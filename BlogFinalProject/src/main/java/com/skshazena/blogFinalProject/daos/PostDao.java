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

    List<Post> getAllPosts();

    List<Post> getAllPostsThatAreStatic();

    List<Post> getAllPostsForHashtag(int hashtagId);

    List<Post> getAllPostsNeedingApprovalWrittenByUser(int userId);

    List<Post> getAllPostsWrittenByUser(int userId);

    List<Post> getAllPostsNeedingApprovalForAdmin();

    void updatePost(Post post);

    void deletePost(int postId);

    Post createPost(Post post);
}
