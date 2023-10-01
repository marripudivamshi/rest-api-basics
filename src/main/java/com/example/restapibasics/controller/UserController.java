package com.example.restapibasics.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.restapibasics.dao.UserDaoService;
import com.example.restapibasics.exception.UserNotFoundException;
import com.example.restapibasics.user.User;

import jakarta.validation.Valid;

@RestController
public class UserController {

	@Autowired
	private UserDaoService daoService;
	
	@GetMapping(path="/Users")
	public List<User> findAllUsers() {
		return daoService.findAll();
	}
	
	@GetMapping(path="/Users/{id}")
	public User findUserById(@PathVariable int id) {
		User savedUser = daoService.findOne(id);
		
		if (savedUser == null) {
			throw new UserNotFoundException("User not found for given id : " +id);
		}
		
		return savedUser;
	}
	
	@PostMapping(path="/Users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = daoService.saveUser(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
							.path("/{id}")
							.buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping(path="/Users/{id}")
	public void deleteUserById(@PathVariable int id) {
		daoService.deleteOne(id);
	}
	
}
