package controller;

import java.util.List;

import model.User;
import service.UserService;

public class UserController {
	
	UserService userService;
	
	public UserController() {
		this.userService = new UserService();
	}
	public void createUser(User user) {
		userService.createUser(user);
    }
    
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
