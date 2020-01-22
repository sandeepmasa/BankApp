package com.bank.service.service;

import com.bank.service.dao.DAOFactory;
import com.bank.service.exception.InsufficientBalanceException;
import com.bank.service.model.CustomerPayLoad;

import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {
 
	private final DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);
    
	private static Logger log = Logger.getLogger(UserService.class);

	/**
	 * Find by userName
	 * @param userName
	 * @return
	 * @throws InsufficientBalanceException
	 */
    @GET
    @Path("/{userName}")
    public CustomerPayLoad getUserByName(@PathParam("userName") String userName) throws InsufficientBalanceException {
        if (log.isDebugEnabled())
            log.debug("Request Received for get CustomerPayLoad by Name " + userName);
        final CustomerPayLoad customerPayLoad = daoFactory.getUserDAO().getUserByName(userName);
        if (customerPayLoad == null) {
            throw new WebApplicationException("CustomerPayLoad Not Found", Response.Status.NOT_FOUND);
        }
        return customerPayLoad;
    }
    
    /**
	 * Find by all
	 * @return
	 * @throws InsufficientBalanceException
	 */
    @GET
    @Path("/all")
    public List<CustomerPayLoad> getAllUsers() throws InsufficientBalanceException {
        return daoFactory.getUserDAO().getAllUsers();
    }
    
    /**
     * Create CustomerPayLoad
     * @param customerPayLoad
     * @return
     * @throws InsufficientBalanceException
     */
    @POST
    @Path("/create")
    public CustomerPayLoad createUser(CustomerPayLoad customerPayLoad) throws InsufficientBalanceException {
        if (daoFactory.getUserDAO().getUserByName(customerPayLoad.getName()) != null) {
            throw new WebApplicationException("CustomerPayLoad name already exist", Response.Status.BAD_REQUEST);
        }
        final long uId = daoFactory.getUserDAO().insertUser(customerPayLoad);
        return daoFactory.getUserDAO().getUserById(uId);
    }
    
    /**
     * Find by CustomerPayLoad Id
     * @param userId
     * @param customerPayLoad
     * @return
     * @throws InsufficientBalanceException
     */
    @PUT
    @Path("/{userId}")
    public Response updateUser(@PathParam("userId") long userId, CustomerPayLoad customerPayLoad) throws InsufficientBalanceException {
        final int updateCount = daoFactory.getUserDAO().updateUser(userId, customerPayLoad);
        if (updateCount == 1) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    /**
     * Delete by CustomerPayLoad Id
     * @param userId
     * @return
     * @throws InsufficientBalanceException
     */
    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") long userId) throws InsufficientBalanceException {
        int deleteCount = daoFactory.getUserDAO().deleteUser(userId);
        if (deleteCount == 1) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}
