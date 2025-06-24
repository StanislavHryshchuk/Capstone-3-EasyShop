package org.yearup.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;
import java.security.Principal;

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
    public Profile getProfile(Principal principal){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName;
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof UserDetails){
//            userName = ((UserDetails) principal).getUsername();
//        }else {
//            userName = principal.toString();
//        }
//        int userId = userDao.getIdByUsername(userName);
        try{
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return profileDao.getProfile(userId);
        } catch (Exception e) {
            if (principal == null){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("profile")
    public Profile updateProfile(Principal principal, @RequestBody Profile profile){
        try{
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return profileDao.updateProfile(userId,profile);
        } catch (Exception e) {
            if (principal == null){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
