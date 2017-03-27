package com.iconectiv.irsf.portal.service.impl;

import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.exception.AuthException;
import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.common.UserDefinitionRepository;
import com.iconectiv.irsf.portal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


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
			user.setPassword(null);
			getUserDetails(user);
		}
		
		return user;
	}

	@Override
	public Iterable<UserDefinition> findAllUsers() throws AuthException {
		return userRepo.findAll();
	}
	
	@Override
	public void createUser(UserDefinition user) throws AuthException {
		user.setId(null);
		
		if (user.getRole() == null) {
			throw new AuthException("permission role is not defined");
		}
		
		
		if (!user.getRole().equals(PermissionRole.Admin.value())) {
			if (user.getCustomerId() == null) {
				throw new AuthException("customer Id is not defined");
			}
			
			CustomerDefinition customer = customerRepo.findOne(user.getCustomerId());
			
			if (customer == null) {
				throw new AuthException("Invalid customer Id");
			}
		} else {
			user.setCustomerId(null);
		}
		
		user.setPassword(encoder.encode(user.getPassword()));
		user.setDisabled(false);
		user.setLocked(false);
		user.setLastUpdated(new Date());
		user.setCreateTimestamp(new Date());
		
		userRepo.save(user);
		return;		
	}
	
	@Override
	public void updateUser(UserDefinition user) throws AuthException {
		if (user.getId() == null) {
			throw new AuthException("user id is not defined");
		} 

		UserDefinition existingUser = userRepo.findOne(user.getId());
		user.setPassword(existingUser.getPassword());

		
		userRepo.save(user);
		return;		
	}
	
	@Override
	public void changePassword(UserDefinition user) throws AuthException {
		UserDefinition existingUser = userRepo.findOne(user.getId());
		
		if (existingUser == null) {
			throw new AuthException("user id is invalid");
		}
		
		existingUser.setPassword(encoder.encode(user.getPassword()));

		
		userRepo.save(existingUser);
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
