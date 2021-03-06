package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.daos.PostDaoImpl.PostMapper;
import com.skshazena.blogFinalProject.daos.RoleDaoImpl.RoleMapper;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.dtos.Role;
import com.skshazena.blogFinalProject.dtos.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 18, 2020
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public User getUserById(int userId) {
        try {
            final String SELECT_USER_BY_ID = "SELECT * FROM user WHERE userId = ?";
            User user = jdbc.queryForObject(SELECT_USER_BY_ID, new UserMapper(), userId);
            user.setRoles(getRolesForUser(user.getUserId()));
            return user;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            final String SELECT_USER_BY_USERNAME = "SELECT * FROM user WHERE username = ?";
            User user = jdbc.queryForObject(SELECT_USER_BY_USERNAME, new UserMapper(), username);
            user.setRoles(getRolesForUser(user.getUserId()));
            return user;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        final String SELECT_ALL_USERS = "SELECT * FROM user";
        List<User> users = jdbc.query(SELECT_ALL_USERS, new UserMapper());
        for (User user : users) {
            user.setRoles(getRolesForUser(user.getUserId()));
        }
        return users;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        final String UPDATE_USER = "UPDATE user SET "
                + "username = ?, "
                + "password = ?, "
                + "enabled = ?, "
                + "lastLogin = ?, "
                + "firstName = ?, "
                + "lastName = ?, "
                + "profilePicture = ? "
                + "WHERE userId = ?";
        jdbc.update(UPDATE_USER,
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.getLastLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfilePicture(),
                user.getUserId());

        final String DELETE_USER_ROLE = "DELETE FROM userRole WHERE user_id = ?";
        jdbc.update(DELETE_USER_ROLE, user.getUserId());
        for (Role role : user.getRoles()) {
            final String INSERT_USER_ROLE = "INSERT INTO userRole(user_id, role_id) VALUES(?,?)";
            jdbc.update(INSERT_USER_ROLE, user.getUserId(), role.getRoleId());
        }
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {

        //also delete comments by user and then posts by user
        final String SELECT_POSTS_WRITTEN_BY_USER = "SELECT * FROM post "
                + "WHERE userId = ?";
        List<Post> postsByUser = jdbc.query(SELECT_POSTS_WRITTEN_BY_USER, new PostMapper(), userId);

        for (Post post : postsByUser) {
            final String DELETE_FROM_POSTHASHTAG = "DELETE FROM postHashtag "
                    + "WHERE postId = ?";
            jdbc.update(DELETE_FROM_POSTHASHTAG, post.getPostId());

        }

        final String DELETE_COMMENTS_BY_USER = "DELETE FROM comment WHERE userId = ?";
        final String DELETE_POSTS_BY_USER = "DELETE FROM post WHERE userId = ?";
        final String DELETE_USER_ROLE = "DELETE FROM userRole WHERE user_id = ?";
        final String DELETE_USER = "DELETE FROM user WHERE userId = ?";
        
        jdbc.update(DELETE_COMMENTS_BY_USER, userId);
        jdbc.update(DELETE_POSTS_BY_USER, userId);
        jdbc.update(DELETE_USER_ROLE, userId);
        jdbc.update(DELETE_USER, userId);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        final String INSERT_USER = "INSERT INTO user(username, password, enabled, lastLogin, firstName, lastName, profilePicture) "
                + "VALUES(?,?,?,?,?,?,?)";
        jdbc.update(INSERT_USER,
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.getLastLogin().withNano(0),
                user.getFirstName(),
                user.getLastName(),
                user.getProfilePicture());
        int newId = jdbc.queryForObject("select LAST_INSERT_ID()", Integer.class);
        user.setUserId(newId);

        for (Role role : user.getRoles()) {
            final String INSERT_USER_ROLE = "INSERT INTO userRole(user_id, role_id) VALUES(?,?)";
            jdbc.update(INSERT_USER_ROLE, user.getUserId(), role.getRoleId());
        }
        return user;
    }

    //Helper Methods
    private Set<Role> getRolesForUser(int userId) throws DataAccessException {
        final String SELECT_ROLES_FOR_USER = "SELECT r.* FROM userRole ur "
                + "JOIN role r ON ur.role_id = r.roleId "
                + "WHERE ur.user_id = ?";
        Set<Role> roles = new HashSet(jdbc.query(SELECT_ROLES_FOR_USER, new RoleMapper(), userId));
        return roles;
    }

    public static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User();
            user.setUserId(rs.getInt("userId"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setLastLogin(rs.getTimestamp("lastLogin").toLocalDateTime());
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setProfilePicture(rs.getString("profilePicture"));
            return user;
        }
    }
}
