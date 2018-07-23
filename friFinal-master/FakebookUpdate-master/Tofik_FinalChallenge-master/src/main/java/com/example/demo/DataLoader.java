package com.example.demo;

import com.example.demo.model.Friend;
import com.example.demo.model.Profile;
import com.example.demo.model.UserPost;
import com.example.demo.model.apis.randomuserapi.RandomUser;
import com.example.demo.model.apis.randomuserapi.RandomUsers;
import com.example.demo.model.auth.AppUser;
import com.example.demo.model.auth.UserRole;
import com.example.demo.model.repositories.FriendRepository;
import com.example.demo.model.repositories.ProfileRepository;
import com.example.demo.model.repositories.UserPostRepository;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashSet;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    UserService users;

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    ProfileRepository profileRepository;


    @Autowired
    UserPostRepository posts;

    @Override
    public void run(String... args) throws Exception {

        //Create Roles
        users.addAppRole("SUSPENDED");
        users.addAppRole("ADMIN");
        users.addAppRole("USER");
        users.addAppRole("MANAGER");

        //Create users with single administrative rights
        users.addUser("auser","apassword","ADMIN");
        users.addUser("anotheruser","nopassword","USER");

        //Add multiple roles to a user (comma-separated)
        String[]rolesToAdd={"ADMIN","MANAGER"};

        users.addUser("multipleroleuser","password",rolesToAdd);


        //        Random USER API - add users here:
        RestTemplate fromAPI = new RestTemplate();

        RandomUsers randomUsers =   fromAPI.getForObject("https://randomuser.me/api?results=10",RandomUsers.class);
        int i = 0; int countadmins = 0;
        for (RandomUser eachuser: randomUsers.getResults()) {
            System.out.println("Image:"+eachuser.getImage().get("large"));
            System.out.println("Username:"+eachuser.getUsername());
            System.out.println("Password:"+eachuser.getUnEncryptedPassword());
            System.out.println("Email:"+eachuser.getEmail());

            Profile profile = new Profile();
            profile.setFullName(eachuser.getName().get("first"));
            profile.setEmail(eachuser.getEmail());
            profile.setProfilepicture(eachuser.getImageURL());
            profile.setBirthDate(eachuser.getDob().get("date"));
            profile.setStreetAddress(eachuser.getLocation().getStreet());
            profile.setCity(eachuser.getLocation().getCity());
            profile.setState(eachuser.getLocation().getState());
            profile.setZipcode(eachuser.getLocation().getPostcode());
            profileRepository.save(profile);
            // 'Convert' random user data into our app user data
            AppUser aUser = new AppUser();
            aUser.addRole(users.findRole("USER"));

            /*
                The first 1 random user is a manager
             */
            while(countadmins++ < 1 ){
                aUser.addRole(users.findRole("MANAGER"));
            }
            aUser.setPassword(eachuser.getUnEncryptedPassword());
            aUser.setUsername(eachuser.getUsername());
            aUser.setProfile(profile);
            aUser.setEmail(eachuser.getEmail());



            UserPost post = new UserPost();
            post.setWhoPosted(profile);
            post.setText("This is post number " + i++);
            post.setTimePosted(LocalDateTime.now());

            profile.setProfileOwner(aUser);

            users.saveMe(aUser);
            profileRepository.save(profile);
            posts.save(post);

        }

    }
}
