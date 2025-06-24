package org.yearup.data;


import org.yearup.models.Profile;

import java.util.List;

public interface ProfileDao {
    Profile create(Profile profile);
    List<Profile> getAll();
    Profile getByUserId(int userId);
    Profile updateProfile(Profile profile);

}
