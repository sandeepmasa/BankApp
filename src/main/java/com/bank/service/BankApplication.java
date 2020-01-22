package com.bank.service;

import com.bank.service.controllers.AccountController;
import com.bank.service.dao.DAOFactory;
import com.bank.service.controllers.BankController;
import com.bank.service.service.ServiceExceptionMapper;
import com.bank.service.service.UserService;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * BankApplication start point, Initializes H2 DB and Starts embedded Jetty server
 *
 * Loads initial DB scripts and test applications.
 *
 */
public class BankApplication {

	private static Logger log = Logger.getLogger(BankApplication.class);

	public static void main(String[] args) throws Exception {
		// Initialize H2 database with demo data
		log.info("Loading Test Data .....");
		DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

		// Load Test Data
		h2DaoFactory.LoadTestData();
		log.info("Completed Loading Test Data");

		// Start Jetty service
		startService();
	}

	/**
	 * Start inbuilt Server to make it run independently.
	 * @throws Exception
	 */
	private static void startService() throws Exception {
		Server server = new Server(8080);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
		servletHolder.setInitParameter("jersey.config.server.provider.classnames",
				UserService.class.getCanonicalName() + ","
						+ AccountController.class.getCanonicalName() + ","
						+ BankController.class.getCanonicalName() + ","
						+ ServiceExceptionMapper.class.getCanonicalName());
		try {
			server.start();
			server.join();
		} finally {
			server.destroy();
		}
	}

}
