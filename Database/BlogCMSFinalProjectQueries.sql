USE BlogCMSFinalProjectDB;
select * from `user` u JOIN userRole ON u.userId = userRole.user_id  JOIN `Role` r On userRole.role_id = r.roleId;
-- Select Queries Main DB
SELECT * from blogcmsfinalprojectdb.user;
SELECT * from blogcmsfinalprojectdb.userRole;
SELECT * from blogcmsfinalprojectdb.role;
SELECT * from blogcmsfinalprojectdb.hashtag;
SELECT * from blogcmsfinalprojectdb.post;
SELECT * from blogcmsfinalprojectdb.posthashtag;
SELECT * from blogcmsfinalprojectdb.comment;

-- Select Queries Test DB
SELECT * from blogcmsfinalprojectdbtest.user;
SELECT * from blogcmsfinalprojectdbtest.useRole;
SELECT * from blogcmsfinalprojectdbtest.role;
SELECT * from blogcmsfinalprojectdbtest.hashtag;
SELECT * from blogcmsfinalprojectdbtest.post;
SELECT * from blogcmsfinalprojectdbtest.posthashtag;
SELECT * from blogcmsfinalprojectdbtest.comment;


SELECT 
    *
FROM
    post
WHERE
    userid = 1
ORDER BY postAt DESC;
SELECT * from Comment WHERE approvalStatus = 0;

SELECT * from Post WHERE (approvalStatus = 0) AND (userId = 2);

SELECT h.* FROM hashtag h
JOIN    posthashtag ph ON h.hashtagId = ph.hashtagId
WHERE    ph.postId = 1;

SELECT h.* FROM hashtag h WHERE h.hashtagId NOT IN (SELECT h.hashtagId FROM hashtag h JOIN posthashtag ph ON h.hashtagId = ph.hashtagId WHERE ph.postId = 1);  
SELECT 
    *
FROM
    Post
WHERE postAt <= NOW() AND (expireAt >= NOW() OR expireAt is null) AND approvalStatus = 1;
SELECT * FROM Post where postAt<= NOW() AND expireAt >= NOW();  

delete from postHashtag where hashtagId = 5;
delete from hashtag where hashtagId = 5;
select p.postId, p.title, ph.hashtagId from post p JOIN postHashtag ph ON p.postId = ph.postId;
delete from postHashtag where postId > 4;
delete from blogcmsfinalprojectdb.post where postId >4;


update post SET
content = "<p>wpofjpweojfpowekjf</p><p>These are my own personal thoughts, beliefs, and perspectives of the world. This is not representative of any employers, planning committees, clients, or any other associations tied with me or my name. Every person has an opinion on something, and these are mine. We may end up having to agree to disagree, and I can guarantee you that not everyone will feel the same way that I do.&nbsp;</p>
<p>I may post happy things or sad things, run-of-the-mill things or controversial things. Whatever I post is most likely posted straight out of my mind, as I write stream-of-consciousness style.&nbsp;</p>
<p>If you have any questions for me personally, please feel free to email me at my first name at this domain.&nbsp;</p>
<p>~Sarah \"sadukie\" Dutkiewicz</p>"
where postId = 9;


ALTER TABLE blogcmsfinalprojectdb.post MODIFY content TEXT NOT NULL;
ALTER TABLE blogcmsfinalprojectdb.post MODIFY content TEXT NOT NULL;