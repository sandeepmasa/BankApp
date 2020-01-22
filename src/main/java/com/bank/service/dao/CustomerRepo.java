package com.bank.service.dao;

import com.bank.service.exception.InsufficientBalanceException;
import com.bank.service.model.CustomerPayLoad;

import java.util.List;

/**
 * Segregates methods of customer CRUD operations
 *
 */

public interface CustomerRepo {
	
	List<CustomerPayLoad> getAllUsers() throws InsufficientBalanceException;

	CustomerPayLoad getUserById(long userId) throws InsufficientBalanceException;

	CustomerPayLoad getUserByName(String userName) throws InsufficientBalanceException;

	/**
	 * @param customerPayLoad:
	 * customerPayLoad to be created
	 * @return userId generated from insertion. return -1 on error
	 */
	long insertUser(CustomerPayLoad customerPayLoad) throws InsufficientBalanceException;

	int updateUser(Long userId, CustomerPayLoad customerPayLoad) throws InsufficientBalanceException;

	int deleteUser(long userId) throws InsufficientBalanceException;

}
