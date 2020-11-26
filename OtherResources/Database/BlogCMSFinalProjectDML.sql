USE BlogCMSFinalProjectDB;

INSERT INTO `user`(`userId`,`username`,`password`,`enabled`, lastLogin, firstName, lastName)
    VALUES(1,"admin", "password", 1, NOW(), "Shazena", "Khan"),
        (2,"writer","password",1, NOW(), "Zari", "Tomaz" ),
        (3,"user","password",1, NOW(), "Thor", "Odinson" );

INSERT INTO `role`(`roleId`,`role`)
    VALUES(1,"ROLE_ADMIN"), 
    (2,"ROLE_CREATOR"), 
    (3,"ROLE_USER");
    
INSERT INTO `userRole`(`user_id`,`role_id`)
    VALUES(1,1),
    (2,2),
    (3,3),
    (2,3);
    
INSERT INTO hashtag (hashtagId, title)
	VALUES (1, "travel"), 
    (2, "food"), 
    (3, "fashion");

INSERT INTO post(postId, title, userId, createdAt, postAt, expireAt, lastEditedAt, content, approvalStatus, staticPage, titlePhoto) 
VALUES (1, "The First Blog Post", 1, NOW(), NOW(), NULL, NOW(), "This is some blog post content. It will need to be changed after I get more updated Data.", 1, 0, NULL),
    (2, "The Second Blog Post", 2, NOW(), NOW(), NULL, NOW(), "This is some blog post content. It will need to be changed after I get more updated Data.", 0, 0, NULL),
    (3, "The Third Blog Post", 1, NOW(), NOW(), NULL, NOW(), "This is some blog post content. It will need to be changed after I get more updated Data.", 1, 1, NULL),
    (4, "The Fourth Blog Post", 2, NOW(), NOW(), NULL, NOW(), "This is some blog post content. It will need to be changed after I get more updated Data.", 1, 1, NULL);

INSERT INTO postHashtag(postId, hashtagId) 
VALUES (1, 1),
(1, 2),
(2, 2),
(2, 3),
(3, 1),
(3, 2),
(3, 3);

INSERT INTO comment(commentId, title, createdAt, approvalStatus, postId, userId, content) 
VALUES (1, "The First Comment", NOW(), 0, 1, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. "),
(2, "The Second Comment", NOW(), 1, 1, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. "),
(3, "The Third Comment", NOW(), 0, 2, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. "),
(4, "The Fourth Comment", NOW(), 0, 2, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. "),
(5, "The Fifth Comment", NOW(), 0, 2, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. "),
(6, "The Sixth Comment", NOW(), 1, 3, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. "),
(7, "The Seventh Comment", NOW(), 1, 3, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. "),
(8, "The Eighth Comment", NOW(), 0, 3, 3, "This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. This is the comment content. ");
    