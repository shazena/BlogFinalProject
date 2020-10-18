package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.dtos.User;
import java.util.List;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 18, 2020
 */
public interface UserDao {

    User getUserById(int userId);

    User getUserByUsername(String username);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUser(int userId);

    User createUser(User user);
}
