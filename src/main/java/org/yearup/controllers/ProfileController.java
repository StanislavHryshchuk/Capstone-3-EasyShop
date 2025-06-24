package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
public class ProfileController {

    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping("profile")
    public Profile getProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails){
            userName = ((UserDetails) principal).getUsername();
        }else {
            userName = principal.toString();
        }
        int userId = userDao.getIdByUsername(userName);

        return profileDao.getProfile(userId);

    }

    @PutMapping("profile")
    public Profile updateProfile(@RequestBody Profile profile){
        return profileDao.updateProfile(profile);
    }
}
