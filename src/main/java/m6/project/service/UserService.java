package m6.project.service;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import m6.project.model.User;
import m6.project.repository.UserRepository;

public class UserService {
    private final UserRepository userRepo;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(EntityManager em) {
        this.userRepo = new UserRepository(em);
    }

    public User loginUser(String name) {
        try {
            User user = userRepo.findByName(name);
            if (user == null) {
                user = new User();
                user.setUserName(name);
                userRepo.save(user);
                logger.info("New user created: {}", name);
            } else {
                logger.info("Welcome back, {}", name);
            }
            return user;
        } catch (Exception e) {
            logger.error("Error during user login for '{}'", name, e);
            return null;
        }
    }
}
