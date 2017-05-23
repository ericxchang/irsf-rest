package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.exception.AuthException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;

public interface UserService {
	UserDefinition findUserById(Integer userId) throws AuthException;

	UserDefinition findUserByName(String userName) throws AuthException;

	Iterable<UserDefinition> findAllUsers() throws AuthException;

	void createUser(UserDefinition user) throws AuthException;

	void updateUser(UserDefinition user) throws AuthException;

	void deleteUser(Integer id) throws AuthException;

	void changePassword(Integer userId, String password) throws AuthException;
	
	void changePassword(UserDefinition user) throws AuthException;

}
