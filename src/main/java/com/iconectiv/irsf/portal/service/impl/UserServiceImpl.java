package com.iconectiv.irsf.portal.service.impl;

import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.exception.AuthException;
import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.common.UserDefinitionRepository;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.portal.service.UserService;
import com.iconectiv.irsf.util.DateTimeHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class UserServiceImpl implements UserService {
	private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDefinitionRepository userRepo;
	@Autowired
	private CustomerDefinitionRepository customerRepo;
	@Autowired
	private AuditTrailService auditService;
	
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
	
	@Transactional
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
		user.setLastUpdated(DateTimeHelper.nowInUTC());
		user.setCreateTimestamp(DateTimeHelper.nowInUTC());
		
		userRepo.save(user);
		
        auditService.saveAuditTrailLog(user.getUserName(), user.getCustomerName(), "create user", "success created user " + user.getUserName(), "system");

		return;		
	}

    @Transactional
	@Override
	public void updateUser(UserDefinition user) throws AuthException {
		if (user.getId() == null) {
			throw new AuthException("user id is not defined");
		} 

		UserDefinition existingUser = userRepo.findOne(user.getId());
		
		if (existingUser == null) {
			throw new AuthException("user id is invalid");
		}
		
		user.setPassword(existingUser.getPassword());
		user.setLastUpdated(DateTimeHelper.nowInUTC());
		user.setCreateTimestamp(existingUser.getCreateTimestamp());
		userRepo.save(user);
		
        auditService.saveAuditTrailLog(user.getUserName(), user.getCustomerName(), "update user", "success updated user " + user.getUserName(), "system");

		return;		
	}

    @Transactional
	@Override
	public void changePassword(UserDefinition user) throws AuthException {
		UserDefinition existingUser = null;
		
		if (user.getId() != null) {
			existingUser = userRepo.findOne(user.getId());
		} else if (user.getUserName() != null) {
			existingUser = userRepo.findOneByUserName(user.getUserName());
		}
		
		if (existingUser == null) {
			throw new AuthException("user id is invalid");
		}
		
		existingUser.setPassword(encoder.encode(user.getPassword()));
		existingUser.setLastUpdated(DateTimeHelper.nowInUTC());
		
		userRepo.save(existingUser);

		auditService.saveAuditTrailLog(user.getUserName(), user.getCustomerName(), "change password", "success change password for user " + user.getUserName(), "system");

		return;		
	}

    @Transactional
	@Override
	public void deleteUser(Integer id) throws AuthException {
		userRepo.delete(id);
		return;
	}

    @Transactional
	@Override
	public void changePassword(Integer userId, String password) throws AuthException {
		UserDefinition user = userRepo.findOne(userId);
		
		if (user == null) {
			throw new AuthException("Invalid userId");
		}
		
		user.setPassword(encoder.encode(password));
		user.setLastUpdated(DateTimeHelper.nowInUTC());

		userRepo.save(user);

		auditService.saveAuditTrailLog(user.getUserName(), user.getCustomerName(), "change password", "success change password for user " + user.getUserName(), "system");
		return;				
	}


}
