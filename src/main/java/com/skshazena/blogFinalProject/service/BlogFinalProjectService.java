package com.skshazena.blogFinalProject.service;

import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import java.util.List;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 21, 2020
 */
public interface BlogFinalProjectService {

    public List<Post> processExcerpts(List<Post> listOfPosts);

    public List<Hashtag> parseStringIntoHashtags(String hashtagsForPostAsString);

    public List<Post> getOlderAndNewerPost(int postId);

}
