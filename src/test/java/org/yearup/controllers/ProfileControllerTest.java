package org.yearup.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProfileControllerTest {
    private ProfileDao profileDao;
    private UserDao userDao;
    private ProfileController controller;
    private Principal principal;

    @BeforeEach
    public void setup() {
        profileDao = mock(ProfileDao.class);
        userDao = mock(UserDao.class);
        principal = mock(Principal.class);
        controller = new ProfileController(profileDao, userDao);
    }

    @Test
    public void testGetProfile() {
        // Arrange
        when(principal.getName()).thenReturn("testUser");

        User user = new User();
        user.setId(1);
        user.setUsername("testUser");

        when(userDao.getByUserName("testUser")).thenReturn(user);

        Profile profile = new Profile(
                1, "Stan", "Hry", "123-456", "sth@example.com",
                "123 Main St", "Seattle", "WA", "98101"
        );
        when(profileDao.getProfile(1)).thenReturn(profile);

        // Act
        Profile result = controller.getProfile(principal);

        // Assert
        assertNotNull(result);
        assertEquals("Stan", result.getFirstName());
        assertEquals("Hry", result.getLastName());
    }

    @Test
    public void testUpdateProfile() {
        // Arrange
        when(principal.getName()).thenReturn("testUser");

        User user = new User();
        user.setId(1);
        user.setUsername("testUser");
        when(userDao.getByUserName("testUser")).thenReturn(user);

        Profile profile = new Profile(
                1, "Stan", "Doe", "123-456", "sth@example.com",
                "123 Main St", "Seattle", "WA", "98101"
        );
        when(profileDao.updateProfile(1, profile)).thenReturn(profile);

        // Act
        Profile result = controller.updateProfile(principal, profile);

        // Assert
        assertNotNull(result);
        assertEquals("Stan", result.getFirstName());
        assertEquals("Seattle", result.getCity());
    }
}
