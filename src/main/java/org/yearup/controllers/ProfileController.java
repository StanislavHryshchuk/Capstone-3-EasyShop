package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

@RestController
@CrossOrigin
public class ProfileController {

    private ProfileDao profileDao;
    @Autowired
    public ProfileController(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    @GetMapping("profiles/{id}")
    public Profile getProfileById(@PathVariable int id){
        return profileDao.getByUserId(id);
    }

    @PutMapping("/profiles")
    public Profile updateProfile(@RequestBody Profile profile){
        return profileDao.updateProfile(profile);
    }
}
