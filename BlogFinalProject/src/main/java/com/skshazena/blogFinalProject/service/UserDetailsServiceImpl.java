package com.skshazena.blogFinalProject.service;

import com.skshazena.blogFinalProject.daos.UserDao;
import com.skshazena.blogFinalProject.dtos.EnhancedSpringUser;
import com.skshazena.blogFinalProject.dtos.Role;
import com.skshazena.blogFinalProject.dtos.User;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 21, 2020
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDao users;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.getUserByUsername(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

//        org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(user.getFirstName(), user.getPassword(), grantedAuthorities);
//        return user1;
        org.springframework.security.core.userdetails.User enhancedUser = buildUserForAuthentication(user, grantedAuthorities);
        return enhancedUser;
    }

    private org.springframework.security.core.userdetails.User buildUserForAuthentication(User user, Set<GrantedAuthority> grantedAuthorities) {
        String username = user.getUsername();
        String password = user.getPassword();
        boolean enabled = user.isEnabled();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        EnhancedSpringUser enhancedSpringUser = new EnhancedSpringUser(username, password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, grantedAuthorities);
        enhancedSpringUser.setFirstName(user.getFirstName());
        enhancedSpringUser.setLastName(user.getLastName());
        enhancedSpringUser.setLastLogin(user.getLastLogin());
        enhancedSpringUser.setProfilePicture(user.getProfilePicture());
        enhancedSpringUser.setUserId(user.getUserId());

        return enhancedSpringUser;
    }

}
