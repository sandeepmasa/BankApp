package com.bank.service.dao;

public abstract class DAOFactory {

	public static final int H2 = 1;

	public abstract CustomerRepo getUserDAO();

	public abstract AccountRepo getAccountDAO();

	public abstract void LoadTestData();

	public static DAOFactory getDAOFactory(int factoryCode) {

		switch (factoryCode) {
		case H2:
			return new H2DAOFactory();
		default:
			// by default using H2 in memory database
			return new H2DAOFactory();
		}
	}
}
