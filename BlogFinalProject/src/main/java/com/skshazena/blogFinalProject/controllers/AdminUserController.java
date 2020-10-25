package com.skshazena.blogFinalProject.controllers;

import com.skshazena.blogFinalProject.daos.*;
import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Role;
import com.skshazena.blogFinalProject.dtos.User;
import com.skshazena.blogFinalProject.service.BlogFinalProjectService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 20, 2020
 */
@Controller
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    BlogFinalProjectService service;

    @Autowired
    ImageDao imageDao;

    @Autowired
    CommentDao commentDao;

    @Autowired
    HashtagDao hashtagDao;

    @Autowired
    PostDao postDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    UserDao userDao;

    @Autowired
    PasswordEncoder encoder;

    String USER_PHOTO_UPLOAD_DIRECTORY = "Users";

    Set<ConstraintViolation<User>> violationsUserAdd = new HashSet<>();
    Set<ConstraintViolation<User>> violationsUserEdit = new HashSet<>();
    Set<ConstraintViolation<User>> violationsUserEditPassword = new HashSet<>();

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> allUsers = userDao.getAllUsers();
        model.addAttribute("users", allUsers);

        violationsUserAdd.clear();
        model.addAttribute("errors", violationsUserAdd);

        return "adminDashboardUsers";
    }

    @GetMapping("/userDetails")
    public String getUserDetails(Integer id, Model model) {
        User userById = userDao.getUserById(id);

        List<Comment> allCommentsWrittenByUser = commentDao.getAllCommentsWrittenByUser(id);

        model.addAttribute("user", userById);
        model.addAttribute("comments", allCommentsWrittenByUser);

        return "adminDashboardUserDetails";
    }

    @GetMapping("/userEdit")
    public String getUserToEdit(Integer id, Model model) {
        User userById = userDao.getUserById(id);

        List<Role> allRoles = roleDao.getAllRoles();

        model.addAttribute("roles", allRoles);

        violationsUserEdit.clear();
        model.addAttribute("errors", violationsUserEdit);

        model.addAttribute("user", userById);
        return "adminDashboardUserEdit";
    }

    @GetMapping("/passwordEdit")
    public String getUserToEditPassword(Integer id, Model model) {
        User userById = userDao.getUserById(id);

        violationsUserEditPassword.clear();
        model.addAttribute("errors", violationsUserEditPassword);

        model.addAttribute("user", userById);
        return "adminDashboardUserPasswordEdit";
    }

    @GetMapping("/userCreate")
    public String createNewUser(Model model) {

        List<Role> allRoles = roleDao.getAllRoles();

        model.addAttribute("roles", allRoles);

        violationsUserAdd.clear();
        model.addAttribute("errors", violationsUserAdd);

        return "adminDashboardUserCreate";
    }

    @PostMapping("/userEdit")
    public String editUserInDB(HttpServletRequest request, Model model, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("cancel")) {
            return "redirect:/admin/users";
        }

        //TODO needs some extra validation, don't allow admins to make themselves disabled.
        //if only one admin, don't allow change to just a user
        User user = new User();
        return "redirect:/admin/userDetails" + user.getUserId();
    }

    @PostMapping("/passwordEdit")
    public String editUserPasswordInDB(HttpServletRequest request, Model model, @RequestParam(value = "action", required = true) String action) {
        String userIdAsString = request.getParameter("userId");
        if (action.equals("cancel")) {
            return "redirect:/admin/userDetails?id=" + Integer.parseInt(userIdAsString);
        }

        //TODO needs some extra validation, don't allow admins to make themselves disabled.
        //if only one admin, don't allow change to just a user
        User user = new User();
        return "redirect:/admin/userDetails" + user.getUserId();
    }

    @PostMapping("/userCreate")
    public String createNewUserInDB(HttpServletRequest request, Model model, @RequestParam("file") MultipartFile file, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("cancel")) {
            return "redirect:/admin/users";
        }

        String usernameToAdd = request.getParameter("usernameToAdd");
        String passwordToAdd = request.getParameter("passwordToAdd");
        String confirmPasswordToAdd = request.getParameter("confirmPasswordToAdd");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String[] rolesSelected = request.getParameterValues("roles");
        String enabledAsString = request.getParameter("enabled");

        boolean enabled = false;
        if (enabledAsString == null) {
            enabled = false;
        } else {
            enabled = true;
        }

        Set<Role> roles = new HashSet<>();
        if (rolesSelected != null) {
            for (String roleId : rolesSelected) {
                roles.add(roleDao.getRoleById(Integer.parseInt(roleId)));
            }
        } else {
            rolesSelected = null;
        }

        Set<Error> customErrors = new HashSet<Error>();

        //if we have a user and a creator, don't allow that.
        Role adminRole = roleDao.getRoleByRole("ROLE_ADMIN");
        Role creatorRole = roleDao.getRoleByRole("ROLE_CREATOR");

        if (!passwordToAdd.equals(confirmPasswordToAdd)) {
            customErrors.add(new Error("Your passwords did not match. Please try again."));
        }

        if (roles.contains(adminRole) && roles.contains(creatorRole)) {
            customErrors.add(new Error("You cannot create an account that is an admin and a content creator"));
        }
        if (customErrors.size() > 0) {
            List<Role> allRoles = roleDao.getAllRoles();

            model.addAttribute("roles", allRoles);
            model.addAttribute("errors", customErrors);
            return "adminDashboardUserCreate";
        }

        String fileLocation = imageDao.saveImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), USER_PHOTO_UPLOAD_DIRECTORY);

        User user = new User();
        user.setUsername(usernameToAdd);
        user.setPassword(passwordToAdd);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(enabled);
        user.setRoles(roles);
        user.setLastLogin(LocalDateTime.now());
        user.setProfilePicture(fileLocation);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violationsUserAdd = validate.validate(user);

        if (violationsUserAdd.isEmpty()) {
            user.setPassword(encoder.encode(passwordToAdd));
            user = userDao.createUser(user);

            return "redirect:/admin/userDetails?id=" + user.getUserId();
        } else {
            List<Role> allRoles = roleDao.getAllRoles();

            model.addAttribute("roles", allRoles);
            model.addAttribute("errors", violationsUserAdd);
            return "adminDashboardUserCreate";

        }
    }

    @PostMapping("/userDelete")
    public String deleteUserFromDB(HttpServletRequest request, Model model) {

        String userIdAsString = request.getParameter("userIdToDelete");
        String userDoingDeletion = request.getParameter("userId");

        User userToDelete = userDao.getUserById(Integer.parseInt(userIdAsString));
        User userDoingDeleting = userDao.getUserById(Integer.parseInt(userDoingDeletion));

        Set<Error> customErrors = new HashSet<Error>();
        if (userDoingDeleting.equals(userToDelete)) {
            customErrors.add(new Error("You cannot delete yourself!!!"));
        }

        Role adminRole = roleDao.getRoleByRole("ROLE_ADMIN");
        List<User> allAdmins = new ArrayList<>();
        List<User> allUsers = userDao.getAllUsers();
        for (User user : allUsers) {
            if (user.getRoles().contains(adminRole)) {
                allAdmins.add(user);
            }
        }

        if (customErrors.size() > 0) {
            model.addAttribute("errors", customErrors);
            List<User> allUsersForPage = userDao.getAllUsers();
            model.addAttribute("users", allUsersForPage);
            return "adminDashboardUsers";
        }

        userDao.deleteUser(userToDelete.getUserId());
        return "redirect:/admin/users";
    }

}
