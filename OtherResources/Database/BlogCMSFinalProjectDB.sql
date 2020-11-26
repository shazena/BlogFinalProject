drop database if exists BlogCMSFinalProjectDB;
create database BlogCMSFinalProjectDB;

use BlogCMSFinalProjectDB;

CREATE TABLE `user` (
    `userId` INT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(30) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `enabled` BIT NOT NULL,
    lastLogin DATETIME NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    profilePicture VARCHAR(255)
);

CREATE TABLE `role` (
    `roleId` INT PRIMARY KEY AUTO_INCREMENT,
    `role` VARCHAR(30) NOT NULL
);

CREATE TABLE `userRole` (
    `user_id` INT NOT NULL,
    `role_id` INT NOT NULL,
    PRIMARY KEY (`user_id` , `role_id`),
    FOREIGN KEY (`user_id`)
        REFERENCES `user` (`userId`),
    FOREIGN KEY (`role_id`)
        REFERENCES `role` (`roleId`)
);

CREATE TABLE hashtag (
    hashtagId INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE post (
    postId INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200),
    userId INT NOT NULL,
    createdAt DATETIME NOT NULL,
    postAt DATETIME NOT NULL,
    expireAt DATETIME,
    lastEditedAt DATETIME NOT NULL,
    content TEXT NOT NULL,
    approvalStatus BIT NOT NULL,
    staticPage BIT NOT NULL,
    titlePhoto VARCHAR(255),
    FOREIGN KEY (userId)
        REFERENCES `user` (`userId`)
);

CREATE TABLE postHashtag (
    postId INT NOT NULL,
    hashtagId INT NOT NULL,
    PRIMARY KEY (postId , hashtagId),
    FOREIGN KEY (postId)
        REFERENCES post (postId),
    FOREIGN KEY (hashtagId)
        REFERENCES hashtag (hashtagId)
);

CREATE TABLE comment (
    commentId INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    content VARCHAR(1000) NOT NULL,
    createdAt DATETIME NOT NULL,
    approvalStatus BIT NOT NULL,
    postId INT NOT NULL,
    userId INT NOT NULL,
    FOREIGN KEY (postId)
        REFERENCES post (postId),
    FOREIGN KEY (userId)
        REFERENCES user (userId)
);


