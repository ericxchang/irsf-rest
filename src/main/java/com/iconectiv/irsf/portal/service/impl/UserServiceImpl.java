package com.iconectiv.irsf.portal.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.iconectiv.irsf.portal.exception.AuthException;
import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.common.UserDefinitionRepository;
import com.iconectiv.irsf.portal.service.UserService;


@Service
public class UserServiceImpl implements UserService {
	private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDefinitionRepository userRepo;
	@Autowired
	private CustomerDefinitionRepository customerRepo;
	
	@Autowired
	BCryptPasswordEncoder encoder;	
	
	@Override
	public UserDefinition findUserById(Integer userId) throws AuthException {
		UserDefinition user = userRepo.findOne(userId);
		
		if (user != null) {
			getUserDetails(user);
		}
		
		return user;
	}
	
	private void getUserDetails(UserDefinition user) {
		if (user.getCustomerId() != null) {
			CustomerDefinition customer = customerRepo.findOne(user.getCustomerId());
			if (customer != null) {
				user.setCustomerDefinition(customer);
				user.setCustomerName(customer.getCustomerName());
				user.setSchemaName(customer.getSchemaName());
			}
		}
	}

	@Override
	public UserDefinition findUserByName(String userName) throws AuthException {
		UserDefinition user = userRepo.findOneByUserName(userName);
		
		if (user != null) {
			getUserDetails(user);
		}
		
		return user;
	}

	@Override
	public Iterable<UserDefinition> findAllUsers() throws AuthException {
		return userRepo.findAll();
	}
	
	@Override
	public void updateUser(UserDefinition user) throws AuthException {
		if (user.getId() == null) {
			user.setPassword(encoder.encode(user.getPassword()));
		} else {
			UserDefinition existingUser = userRepo.findOne(user.getId());
			user.setPassword(existingUser.getPassword());
		}
		
		userRepo.save(user);
		return;		
	}
	
	@Override
	public void deleteUser(Integer id) throws AuthException {
		userRepo.delete(id);
		return;
	}

	@Override
	public void changePassword(Integer userId, String password) throws AuthException {
		UserDefinition user = userRepo.findOne(userId);
		
		if (user == null) {
			throw new AuthException("Invalid userId");
		}
		
		user.setPassword(encoder.encode(password));
		userRepo.save(user);
		return;				
	}

	@Override
	public void changePassword(String userName, String password) throws AuthException {
		UserDefinition user = userRepo.findOneByUserName(userName);
		
		if (user == null) {
			throw new AuthException("Invalid userName");
		}
		
		user.setPassword(encoder.encode(password));
		userRepo.save(user);
		return;				
	}
}
