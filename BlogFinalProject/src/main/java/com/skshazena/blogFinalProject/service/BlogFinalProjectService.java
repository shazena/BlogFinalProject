/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skshazena.blogFinalProject.service;

import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.dtos.Role;
import com.skshazena.blogFinalProject.dtos.User;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 21, 2020
 */
public interface BlogFinalProjectService {

    public List<Hashtag> parseStringIntoHashtags(String hashtagsForPostAsString);

    public int getNumberOfEnabledUsers();

    public List<Post> getOlderAndNewerPost(int postId);

    public String saveImage(MultipartFile file, String fileName, String directory);

    public String updateImage(MultipartFile file, String fileName, String directory);

    public boolean deleteImage(String fileName);

    public Comment getCommentById(int commentId);

    public List<Comment> getAllComments();

    public List<Comment> getAllCommentsForPost(int postId);

    public List<Comment> getAllCommentsWrittenByUser(int userId);

    public List<Comment> getAllCommentsNeedingApproval();

    public void updateComment(Comment comment);

    public void deleteComment(int commentId);

    public Comment createComment(Comment comment);

    public Hashtag getHashtagById(int hashtagId);

    public Hashtag getHashtagByTitle(String title);

    public List<Hashtag> getAllHashtags();

    public List<Hashtag> getAllHashtagsForPost(int postId);

    public List<Hashtag> getAllHashtagsNotUsedInPost(int postId);

    public void updateHashtag(Hashtag hashtag);

    public void deleteHashtag(int hashtagId);

    public Hashtag createHashtag(Hashtag hashtag);

    public Post getPostById(int postId);

    public List<Post> getAllPostsForAdminNewestFirst();

    public List<Post> getAllPostsForBlogNonStaticNewestFirst();

    public List<Post> getAllPostsForBlogThatAreStaticNewestFirst();

    public List<Post> getAllPostsForHashtagForAdminNewestFirst(int hashtagId);

    public List<Post> getAllPostsForBlogForHashtagNewestFirst(int hashtagId);

    public List<Post> getAllPostsNeedingApprovalWrittenByCreatorOldestFirst(int userId);

    public List<Post> getAllPostsWrittenByCreatorNewestFirst(int userId);

    public List<Post> getAllPostsNeedingApprovalForAdminOldestFirst();

    public void updatePost(Post post);

    public void deletePost(int postId);

    public Post createPost(Post post);

    public Role getRoleById(int roleId);

    public Role getRoleByRole(String role);

    public List<Role> getAllRoles();

    public void deleteRole(int roleId);

    public void updateRole(Role role);

    public Role createRole(Role role);

    public User getUserById(int userId);

    public User getUserByUsername(String username);

    public List<User> getAllUsers();

    public void updateUser(User user);

    public void deleteUser(int userId);

    public User createUser(User user);

}
