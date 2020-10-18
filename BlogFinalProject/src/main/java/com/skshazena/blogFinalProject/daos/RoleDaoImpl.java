package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.dtos.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
public class RoleDaoImpl implements RoleDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Role getRoleById(int roleId) {
        try {
            final String SELECT_ROLE_BY_ID = "SELECT * FROM role WHERE roleId = ?";
            return jdbc.queryForObject(SELECT_ROLE_BY_ID, new RoleMapper(), roleId);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public Role getRoleByRole(String role) {
        try {
            final String SELECT_ROLE_BY_ROLE = "SELECT * FROM role WHERE role = ?";
            return jdbc.queryForObject(SELECT_ROLE_BY_ROLE, new RoleMapper(), role);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Role> getAllRoles() {
        final String SELECT_ALL_ROLES = "SELECT * FROM role";
        return jdbc.query(SELECT_ALL_ROLES, new RoleMapper());
    }

    @Override
    public void deleteRole(int roleId) {
        final String DELETE_USER_ROLE = "DELETE FROM userRole WHERE role_id = ?";
        final String DELETE_ROLE = "DELETE FROM role WHERE roleId = ?";
        jdbc.update(DELETE_USER_ROLE, roleId);
        jdbc.update(DELETE_ROLE, roleId);
    }

    @Override
    public void updateRole(Role role) {
        final String UPDATE_ROLE = "UPDATE role SET role = ? WHERE roleId = ?";
        jdbc.update(UPDATE_ROLE, role.getRole(), role.getRoleId());
    }

    @Override
    @Transactional
    public Role createRole(Role role) {
        final String INSERT_ROLE = "INSERT INTO role(role) VALUES(?)";
        jdbc.update(INSERT_ROLE, role.getRole());
        int newId = jdbc.queryForObject("select LAST_INSERT_ID()", Integer.class);
        role.setRoleId(newId);
        return role;
    }

    public static final class RoleMapper implements RowMapper<Role> {

        @Override
        public Role mapRow(ResultSet rs, int i) throws SQLException {
            Role role = new Role();
            role.setRoleId(rs.getInt("roleId"));
            role.setRole(rs.getString("role"));
            return role;
        }
    }

}
