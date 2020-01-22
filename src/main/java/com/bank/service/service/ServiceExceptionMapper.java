package com.bank.service.service;

import com.bank.service.exception.InsufficientBalanceException;
import org.apache.log4j.Logger;
import com.bank.service.exception.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServiceExceptionMapper implements ExceptionMapper<InsufficientBalanceException> {
	private static Logger log = Logger.getLogger(ServiceExceptionMapper.class);

	public ServiceExceptionMapper() {
	}

	public Response toResponse(InsufficientBalanceException daoException) {
		if (log.isDebugEnabled()) {
			log.debug("Mapping exception to Response....");
		}
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(daoException.getMessage());

		// return internal server error for DAO exceptions
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();
	}

}
