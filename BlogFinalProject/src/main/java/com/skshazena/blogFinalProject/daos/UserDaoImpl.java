package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.daos.RoleDaoImpl.RoleMapper;
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
    public User getUserById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User getUserByUsername(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<User> getAllUsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteUser(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User createUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
