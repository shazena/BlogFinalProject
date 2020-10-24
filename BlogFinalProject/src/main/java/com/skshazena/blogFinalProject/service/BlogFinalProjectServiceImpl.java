package com.skshazena.blogFinalProject.service;

import com.skshazena.blogFinalProject.daos.CommentDao;
import com.skshazena.blogFinalProject.daos.HashtagDao;
import com.skshazena.blogFinalProject.daos.ImageDao;
import com.skshazena.blogFinalProject.daos.PostDao;
import com.skshazena.blogFinalProject.daos.RoleDao;
import com.skshazena.blogFinalProject.daos.UserDao;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<Post> processExcerpts(List<Post> listOfPosts) {
        for (Post post : listOfPosts) {

            Document document = Jsoup.parse(post.getContent());

            Elements elementsByTag = document.getElementsByTag("p");

            String postText = elementsByTag.text();

            String excerpt = postText.substring(0, Integer.min(postText.length(), 200));

            post.setContent(excerpt);

        }
        return listOfPosts;
    }

    @Override
    public List<Hashtag> parseStringIntoHashtags(String hashtagsForPostAsString) {

        List<Hashtag> hashtagsForPost = new ArrayList<>();

        if (hashtagsForPostAsString == null || hashtagsForPostAsString.isBlank()) {
            return hashtagsForPost; //don't process if string is blank
        }

        Set<String> hashtagsForPostAsSet = new HashSet<String>();

        String[] hashtags = hashtagsForPostAsString.split(","); //split the string at each comma

        for (String hashtag : hashtags) { //put each String in a set. This will help prevent duplicates
            hashtag = hashtag.strip();
            if (!hashtag.isBlank()) {
                hashtagsForPostAsSet.add(hashtag);
            }
        }

        for (String title : hashtagsForPostAsSet) { //for each hashtag in the set

            Hashtag hashtagByTitle = hashtagDao.getHashtagByTitle(title); //check to see if it's in the dB already

            if (hashtagByTitle == null) { //if it's not, make a new Hashtag object and  put the title inside
                Hashtag hashtag = new Hashtag();
                hashtag.setTitle(title);
                hashtagsForPost.add(hashtag); //add the new one to the set
            } else { //if it was not null
                hashtagsForPost.add(hashtagByTitle); //add that non-null object to the list
            }

        }

        return hashtagsForPost;

    }

    @Override
    public List<Post> getOlderAndNewerPost(int postId) {
        Post currentPost = postDao.getPostById(postId);

        List<Post> olderAndNewerPosts = new ArrayList<>();
        Post olderPost = null;
        Post newerPost = null;

        List<Post> allPostsForBlogNonStaticNewestFirst = postDao.getAllPostsForBlogNonStaticNewestFirst();
        int indexOfCurrentPost = allPostsForBlogNonStaticNewestFirst.indexOf(currentPost);

        if (indexOfCurrentPost < 0) {
            return olderAndNewerPosts;
        }

        int indexOfOlderPost = indexOfCurrentPost + 1;
        int indexOfNewerPost = indexOfCurrentPost - 1;

        if (allPostsForBlogNonStaticNewestFirst != null) {
            if (allPostsForBlogNonStaticNewestFirst.size() > 2) { //there is a previous and a next post

                //if the current post is the last in the set, then there is no older post
                //if the current post is the first in the set, then there is no newer post
                if (indexOfCurrentPost == 0) {// if this is the first post, there is no newer, just older

                    olderPost = allPostsForBlogNonStaticNewestFirst.get(indexOfOlderPost);//older post will be the post in there

                    newerPost = null; //then newer post will be null

                    olderAndNewerPosts.add(olderPost);
                    olderAndNewerPosts.add(newerPost);
                } else if (indexOfCurrentPost == allPostsForBlogNonStaticNewestFirst.size() - 1) { //if index of Current post is 1, then there is no older, just newer

                    olderPost = null; //then older post will be null

                    newerPost = allPostsForBlogNonStaticNewestFirst.get(indexOfNewerPost);//newer post will be the post in there

                    olderAndNewerPosts.add(olderPost);
                    olderAndNewerPosts.add(newerPost);
                } else {

                    olderPost = allPostsForBlogNonStaticNewestFirst.get(indexOfOlderPost);

                    newerPost = allPostsForBlogNonStaticNewestFirst.get(indexOfNewerPost);

                    olderAndNewerPosts.add(olderPost);
                    olderAndNewerPosts.add(newerPost);
                }
            } else if (allPostsForBlogNonStaticNewestFirst.size() == 2) {//if this has this post and another post
                if (indexOfCurrentPost == 0) {// if this is the first post, there is no newer, just older

                    olderPost = allPostsForBlogNonStaticNewestFirst.get(indexOfOlderPost);//older post will be the post in there

                    newerPost = null; //then newer post will be null

                    olderAndNewerPosts.add(olderPost);
                    olderAndNewerPosts.add(newerPost);
                } else { //if index of Current post is 1, then there is no older, just newer

                    olderPost = null; //then older post will be null

                    newerPost = allPostsForBlogNonStaticNewestFirst.get(indexOfNewerPost);//newer post will be the post in there

                    olderAndNewerPosts.add(olderPost);
                    olderAndNewerPosts.add(newerPost);

                }
            } else {//this post is the only post, there are no previous or next posts.
                olderPost = null;
                newerPost = null;

                olderAndNewerPosts.add(olderPost);
                olderAndNewerPosts.add(newerPost);
            }
        }

        return olderAndNewerPosts;
    }

}
