package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.dtos.Role;
import java.util.List;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 18, 2020
 */
public interface RoleDao {

    Role getRoleById(int id);

    Role getRoleByRole(String role);

    List<Role> getAllRoles();

    void deleteRole(int id);

    void updateRole(Role role);

    Role createRole(Role role);
}
