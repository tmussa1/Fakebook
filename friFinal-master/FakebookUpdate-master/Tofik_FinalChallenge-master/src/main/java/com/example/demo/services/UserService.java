package com.example.demo.services;


import com.example.demo.model.Profile;
import com.example.demo.model.auth.AppUser;
import com.example.demo.model.auth.AppUserRepository;
import com.example.demo.model.auth.UserRole;
import com.example.demo.model.auth.UserRoleRepository;
import com.example.demo.model.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    AppUserRepository users;

    @Autowired
    UserRoleRepository roles;

    @Autowired
    ProfileRepository profiles;

    @Autowired
    private JavaMailSender javaMailSender;

    public void addUser(String ausername, String apassword, String role) {
        AppUser user = new AppUser();
        user.setUsername(ausername);
        user.setPassword(apassword);
        user.addRole(roles.findByRole(role));

    }

    public void addUser(String ausername, String apassword, String[] roles) {
        AppUser user = new AppUser();
        user.setUsername(ausername);
        user.setPassword(apassword);
        for(String role : roles)
            user.addRole(this.roles.findByRole(role));

    }

    public void addAppRole(String role) {
        UserRole newRole = new UserRole(role);
        roles.save(newRole);
    }

    public AppUser findUser(Authentication authentication){
        return users.findByUsername(authentication.getName());
    }

    public UserRole findRole(String role) {
        return roles.findByRole(role);
    }

    public void saveMe(AppUser aUser) {
        users.save(aUser);
    }

    //profiling stuff below this line

    public void saveProfile(Profile profile, Authentication authentication){
        AppUser user = users.findByUsername(authentication.getName());
        profile.setProfileOwner(user);
        profiles.save(profile);
    }

    public AppUser findByEmail(String email){
        return users.findByEmail(email);
    }

    public AppUser findByConfirmationco(String confirmationcode){
        return users.findByConfirmationcode(confirmationcode);
    }

    public void sendEmail(SimpleMailMessage email){
        javaMailSender.send(email);
    }
    public Profile findProfile(Authentication authentication){
        return profiles.findByProfileOwner_Username(authentication.getName());
    }
    public Profile findProfile(long id){
        return profiles.findById(id).get();
    }
}