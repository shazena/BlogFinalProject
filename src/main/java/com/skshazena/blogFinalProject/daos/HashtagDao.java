package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.dtos.Hashtag;
import java.util.List;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 18, 2020
 */
public interface HashtagDao {

    Hashtag getHashtagById(int hashtagId);

    Hashtag getHashtagByTitle(String title);

    List<Hashtag> getAllHashtags();

    List<Hashtag> getAllHashtagsForPost(int postId);

    List<Hashtag> getAllHashtagsNotUsedInPost(int postId);

    void updateHashtag(Hashtag hashtag);

    void deleteHashtag(int hashtagId);

    Hashtag createHashtag(Hashtag hashtag);
}
