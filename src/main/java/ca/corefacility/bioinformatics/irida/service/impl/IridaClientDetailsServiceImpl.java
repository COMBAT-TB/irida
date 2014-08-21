package ca.corefacility.bioinformatics.irida.service.impl;

import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

import ca.corefacility.bioinformatics.irida.exceptions.EntityExistsException;
import ca.corefacility.bioinformatics.irida.exceptions.EntityNotFoundException;
import ca.corefacility.bioinformatics.irida.exceptions.InvalidPropertyException;
import ca.corefacility.bioinformatics.irida.model.IridaClientDetails;
import ca.corefacility.bioinformatics.irida.repositories.IridaClientDetailsRepository;
import ca.corefacility.bioinformatics.irida.service.IridaClientDetailsService;

/**
 * Service for storing and retrieving {@link IridaClientDetails} object.
 * Implements {@link ClientDetailsService} for use with OAuth approvals.
 * 
 * @author Thomas Matthews <thomas.matthews@phac-aspc.gc.ca>
 *
 */
@Service("clientDetails")
public class IridaClientDetailsServiceImpl extends CRUDServiceImpl<Long, IridaClientDetails> implements
		IridaClientDetailsService {
	private IridaClientDetailsRepository clientDetailsRepository;

	@Autowired
	public IridaClientDetailsServiceImpl(IridaClientDetailsRepository repository, Validator validator) {
		super(repository, validator, IridaClientDetails.class);
		this.clientDetailsRepository = repository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		IridaClientDetails client = clientDetailsRepository.loadClientDetailsByClientId(clientId);
		if (client == null) {
			throw new NoSuchClientException("Client with this clientId does not exist: " + clientId);
		}
		return client;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IridaClientDetails create(IridaClientDetails object) throws ConstraintViolationException,
			EntityExistsException {
		return super.create(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IridaClientDetails update(Long id, Map<String, Object> updatedFields) throws ConstraintViolationException,
			EntityExistsException, InvalidPropertyException {
		return super.update(id, updatedFields);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(Long id) throws EntityNotFoundException {
		super.delete(id);
	}

}