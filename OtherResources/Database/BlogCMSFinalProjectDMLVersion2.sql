USE BlogCMSFinalProjectDB;

INSERT INTO `user`(`userId`,`username`,`password`,`enabled`, lastLogin, firstName, lastName) VALUES
    (1,"admin", "password", 1, SYSDATE(), "Shazena", "Khan");
SELECT SLEEP(5);
INSERT INTO `user`(`userId`,`username`,`password`,`enabled`, lastLogin, firstName, lastName) VALUES
	(2,"writer","password",1, SYSDATE(), "Zari", "Tomaz" );
SELECT SLEEP(5);
INSERT INTO `user`(`userId`,`username`,`password`,`enabled`, lastLogin, firstName, lastName) VALUES 
	(3,"user","password",1, SYSDATE(), "Thor", "Odinson" );
SELECT SLEEP(5);

INSERT INTO `role`(`roleId`,`role`) VALUES (1,"ROLE_ADMIN");
INSERT INTO `role`(`roleId`,`role`) VALUES (2,"ROLE_CREATOR");
INSERT INTO `role`(`roleId`,`role`) VALUES (3,"ROLE_USER");
    
INSERT INTO `userRole`(`user_id`,`role_id`) VALUES (1,1);
INSERT INTO `userRole`(`user_id`,`role_id`) VALUES (2,2);
INSERT INTO `userRole`(`user_id`,`role_id`) VALUES (3,3);
INSERT INTO `userRole`(`user_id`,`role_id`) VALUES (2,3);
    
INSERT INTO hashtag (hashtagId, title) VALUES (1, "travel");
INSERT INTO hashtag (hashtagId, title) VALUES (2, "food");
INSERT INTO hashtag (hashtagId, title) VALUES (3, "fashion");

INSERT INTO post(postId, title, userId, createdAt, postAt, expireAt, lastEditedAt, content, approvalStatus, staticPage, titlePhoto)  VALUES (1, "The First Blog Post", 1, SYSDATE(), SYSDATE(), NULL, SYSDATE(), "This is some blog post content. It will need to be changed after I get more updated Data.", 1, 0, NULL);
SELECT SLEEP(5);
INSERT INTO post(postId, title, userId, createdAt, postAt, expireAt, lastEditedAt, content, approvalStatus, staticPage, titlePhoto)  VALUES  (2, "The Second Blog Post", 2, SYSDATE(), SYSDATE(), NULL, SYSDATE(), "This is some blog post content. It will need to be changed after I get more updated Data.", 0, 0, NULL);
SELECT SLEEP(5);
INSERT INTO post(postId, title, userId, createdAt, postAt, expireAt, lastEditedAt, content, approvalStatus, staticPage, titlePhoto)  VALUES  (3, "The Third Blog Post", 1, SYSDATE(), SYSDATE(), NULL, SYSDATE(), "This is some blog post content. It will need to be changed after I get more updated Data.", 1, 1, NULL);
SELECT SLEEP(5);
INSERT INTO post(postId, title, userId, createdAt, postAt, expireAt, lastEditedAt, content, approvalStatus, staticPage, titlePhoto)  VALUES (4, "The Fourth Blog Post", 2, SYSDATE(), SYSDATE(), NULL, SYSDATE(), "This is some blog post content. It will need to be changed after I get more updated Data.", 1, 1, NULL);
SELECT SLEEP(5);

INSERT INTO postHashtag(postId, hashtagId) 
VALUES 
(1, 1),
(1, 2),
(2, 2),
(2, 3),
(3, 1),
(3, 2),
(3, 3);

INSERT INTO comment(commentId, title, createdAt, approvalStatus, postId, userId, content) VALUES (1, "The First Comment", SYSDATE(), 0, 1, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. ");
SELECT SLEEP(5);
INSERT INTO comment(commentId, title, createdAt, approvalStatus, postId, userId, content) VALUES (2, "The Second Comment", SYSDATE(), 1, 1, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. ");
SELECT SLEEP(5);
INSERT INTO comment(commentId, title, createdAt, approvalStatus, postId, userId, content) VALUES (3, "The Third Comment", SYSDATE(), 0, 2, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. ");
SELECT SLEEP(5);
INSERT INTO comment(commentId, title, createdAt, approvalStatus, postId, userId, content) VALUES (4, "The Fourth Comment", SYSDATE(), 0, 2, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. ");
SELECT SLEEP(5);
INSERT INTO comment(commentId, title, createdAt, approvalStatus, postId, userId, content) VALUES (5, "The Fifth Comment", SYSDATE(), 0, 2, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. ");
SELECT SLEEP(5);
INSERT INTO comment(commentId, title, createdAt, approvalStatus, postId, userId, content) VALUES (6, "The Sixth Comment", SYSDATE(), 1, 3, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. ");
SELECT SLEEP(5);
INSERT INTO comment(commentId, title, createdAt, approvalStatus, postId, userId, content) VALUES (7, "The Seventh Comment", SYSDATE(), 1, 3, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. ");
SELECT SLEEP(5);
INSERT INTO comment(commentId, title, createdAt, approvalStatus, postId, userId, content) VALUES (8, "The Eighth Comment", SYSDATE(), 0, 3, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. ");
    