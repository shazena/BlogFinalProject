package com.skshazena.blogFinalProject.service;

import com.skshazena.blogFinalProject.daos.CommentDao;
import com.skshazena.blogFinalProject.daos.HashtagDao;
import com.skshazena.blogFinalProject.daos.ImageDao;
import com.skshazena.blogFinalProject.daos.PostDao;
import com.skshazena.blogFinalProject.daos.RoleDao;
import com.skshazena.blogFinalProject.daos.UserDao;
import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.dtos.Role;
import com.skshazena.blogFinalProject.dtos.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 21, 2020
 */
@Service
public class BlogFinalProjectServiceImpl implements BlogFinalProjectService {

    @Autowired
    ImageDao imageDao;

    @Autowired
    CommentDao commentDao;

    @Autowired
    HashtagDao hashtagDao;

    @Autowired
    PostDao postDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    UserDao userDao;

//    BASIC CRRUD METHODS FOR IMAGE DAO ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String saveImage(MultipartFile file, String fileName, String directory) {
        return imageDao.saveImage(file, fileName, directory);
    }

    @Override
    public String updateImage(MultipartFile file, String fileName, String directory) {
        return imageDao.updateImage(file, fileName, directory);
    }

    @Override
    public boolean deleteImage(String fileName) {
        return imageDao.deleteImage(fileName);
    }

//    BASIC CRRUD METHODS FOR COMMENT DAO ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Comment getCommentById(int commentId) {
        return commentDao.getCommentById(commentId);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentDao.getAllComments();
    }

    @Override
    public List<Comment> getAllCommentsForPost(int postId) {
        return commentDao.getAllCommentsForPost(postId);
    }

    @Override
    public List<Comment> getAllCommentsWrittenByUser(int userId) {
        return commentDao.getAllCommentsWrittenByUser(userId);
    }

    @Override
    public List<Comment> getAllCommentsNeedingApproval() {
        return commentDao.getAllCommentsNeedingApproval();
    }

    @Override
    public void updateComment(Comment comment) {
        commentDao.updateComment(comment);
    }

    @Override
    public void deleteComment(int commentId) {
        commentDao.deleteComment(commentId);
    }

    @Override
    public Comment createComment(Comment comment) {
        return commentDao.createComment(comment);
    }

//    BASIC CRRUD METHODS FOR HASHTAG DAO ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Hashtag getHashtagById(int hashtagId) {
        return hashtagDao.getHashtagById(hashtagId);
    }

    @Override
    public Hashtag getHashtagByTitle(String title) {
        return hashtagDao.getHashtagByTitle(title);
    }

    @Override
    public List<Hashtag> getAllHashtags() {
        return hashtagDao.getAllHashtags();
    }

    @Override
    public List<Hashtag> getAllHashtagsForPost(int postId) {
        return hashtagDao.getAllHashtagsForPost(postId);
    }

    @Override
    public List<Hashtag> getAllHashtagsNotUsedInPost(int postId) {
        return hashtagDao.getAllHashtagsNotUsedInPost(postId);
    }

    @Override
    public void updateHashtag(Hashtag hashtag) {
        hashtagDao.updateHashtag(hashtag);
    }

    @Override
    public void deleteHashtag(int hashtagId) {
        hashtagDao.deleteHashtag(hashtagId);
    }

    @Override
    public Hashtag createHashtag(Hashtag hashtag) {
        return hashtagDao.createHashtag(hashtag);
    }

//    BASIC CRRUD METHODS FOR POST DAO ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Post getPostById(int postId) {
        return postDao.getPostById(postId);
    }

    @Override
    public List<Post> getAllPostsForAdminNewestFirst() {
        return postDao.getAllPostsForAdminNewestFirst();
    }

    @Override
    public List<Post> getAllPostsForBlogNonStaticNewestFirst() {
        return postDao.getAllPostsForBlogNonStaticNewestFirst();
    }

    @Override
    public List<Post> getAllPostsForBlogThatAreStaticNewestFirst() {
        return postDao.getAllPostsForBlogThatAreStaticNewestFirst();
    }

    @Override
    public List<Post> getAllPostsForHashtagForAdminNewestFirst(int hashtagId) {
        return postDao.getAllPostsForHashtagForAdminNewestFirst(hashtagId);
    }

    @Override
    public List<Post> getAllPostsForHashtagForUserNewestFirst(int hashtagId) {
        return postDao.getAllPostsForHashtagForUserNewestFirst(hashtagId);
    }

    @Override
    public List<Post> getAllPostsNeedingApprovalWrittenByCreatorOldestFirst(int userId) {
        return postDao.getAllPostsNeedingApprovalWrittenByCreatorOldestFirst(userId);
    }

    @Override
    public List<Post> getAllPostsWrittenByCreatorNewestFirst(int userId) {
        return postDao.getAllPostsWrittenByCreatorNewestFirst(userId);
    }

    @Override
    public List<Post> getAllPostsNeedingApprovalForAdminOldestFirst() {
        return postDao.getAllPostsNeedingApprovalForAdminOldestFirst();
    }

    @Override
    public void updatePost(Post post) {
        postDao.updatePost(post);
    }

    @Override
    public void deletePost(int postId) {
        postDao.deletePost(postId);
    }

    @Override
    public Post createPost(Post post) {
        return postDao.createPost(post);
    }

//    BASIC CRRUD METHODS FOR ROLE DAO ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Role getRoleById(int roleId) {
        return roleDao.getRoleById(roleId);
    }

    @Override
    public Role getRoleByRole(String role) {
        return roleDao.getRoleByRole(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Override
    public void deleteRole(int roleId) {
        roleDao.deleteRole(roleId);
    }

    @Override
    public void updateRole(Role role) {
        roleDao.updateRole(role);
    }

    @Override
    public Role createRole(Role role) {
        return roleDao.createRole(role);
    }

//    BASIC CRRUD METHODS FOR USER DAO ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void deleteUser(int userId) {
        userDao.deleteUser(userId);
    }

    @Override
    public User createUser(User user) {
        return userDao.createUser(user);
    }

}
