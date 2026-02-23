package m6.project.service;

import jakarta.persistence.EntityManager;
import m6.project.model.User;
import m6.project.repository.UserRepository;

public class UserService {  
    private UserRepository userRepo;

    public UserService(EntityManager em) {
        this.userRepo = new UserRepository(em);
    }

    public User loginUser(String userName) {
        User user = userRepo.findByName(userName);
        if (user == null) {
            user = new User();
            user.setUserName(userName);  
            userRepo.save(user);
            System.out.println("New user created: " + userName);
        } else {
            System.out.println("Login user: " + userName);
        }
        return user;
    }
}
