package com.skshazena.blogFinalProject.dtos;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 18, 2020
 */
public class Comment {

    private int commentId;

    @NotNull(message = "Title must be entered")
    @NotBlank(message = "Title must not be blank")
    @Size(max = 100, message = "Title must be less than 100 characters.")
    private String title;

    @NotNull(message = "Content must be entered")
    @NotBlank(message = "Content must not be blank")
    @Size(max = 1000, message = "Content must be less than 1000 characters.")
    private String content;
    
    @PastOrPresent
    private LocalDateTime createdAt;
    
    private boolean approvalStatus;
    
    @NotNull(message = "Comment must be associated with a Post")
    @Valid
    private Post post;
    
    @NotNull(message = "Comment must be associated with a User")
    @Valid
    private User user;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt.withNano(0);
    }

    public boolean isApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + this.commentId;
        hash = 83 * hash + Objects.hashCode(this.title);
        hash = 83 * hash + Objects.hashCode(this.content);
        hash = 83 * hash + Objects.hashCode(this.createdAt);
        hash = 83 * hash + (this.approvalStatus ? 1 : 0);
        hash = 83 * hash + Objects.hashCode(this.post);
        hash = 83 * hash + Objects.hashCode(this.user);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Comment other = (Comment) obj;
        if (this.commentId != other.commentId) {
            return false;
        }
        if (this.approvalStatus != other.approvalStatus) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        if (!Objects.equals(this.createdAt, other.createdAt)) {
            return false;
        }
        if (!Objects.equals(this.post, other.post)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Comment{" + "commentId=" + commentId + ", title=" + title + ", content=" + content + ", createdAt=" + createdAt + ", approvalStatus=" + approvalStatus + ", post=" + post + ", user=" + user + '}';
    }

}
