package com.bank.service.dao;

import com.bank.service.dao.impl.AccountRepository;
import com.bank.service.dao.impl.CustomerRepository;
import com.bank.service.utils.Utils;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Factory Class implements DAOFactory
 *
 */
public class H2DAOFactory extends DAOFactory {
	private static final String h2_driver = Utils.getStringProperty("h2_driver");
	private static final String h2_connection_url = Utils.getStringProperty("h2_connection_url");
	private static final String h2_user = Utils.getStringProperty("h2_user");
	private static final String h2_password = Utils.getStringProperty("h2_password");
	private static Logger log = Logger.getLogger(H2DAOFactory.class);

	private final CustomerRepository userDAO = new CustomerRepository();
	private final AccountRepository accountDAO = new AccountRepository();

	H2DAOFactory() {
		DbUtils.loadDriver(h2_driver);
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(h2_connection_url, h2_user, h2_password);

	}

	public CustomerRepo getUserDAO() {
		return userDAO;
	}

	public AccountRepo getAccountDAO() {
		return accountDAO;
	}

	@Override
	public void LoadTestData() {
		log.info("Loading test CustomerPayLoad Table and data ..... ");
		Connection conn = null;
		try {
			conn = H2DAOFactory.getConnection();
			RunScript.execute(conn, new FileReader("src/test/resources/test.sql"));
		} catch (SQLException e) {
			log.error("LoadTestData(): Error populating user data: ", e);
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("populateTestData(): Error finding test script file ", e);
			throw new RuntimeException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

}
