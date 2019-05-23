package io.fnx.backend.rest;

import io.fnx.backend.tools.authorization.AllAllowed;
import io.fnx.backend.util.conf.ClientConfiguration;

import javax.inject.Inject;
import javax.ws.rs.*;

/**
 * REST point for receiving meta information "system" calls, for example GET configuration, start some global upkeep task, etc.
 */
@Path("/v1/config")
public class ConfigResource extends BaseResource {

    private ClientConfiguration clientConfiguration;

	@GET
	@AllAllowed
	public ClientConfiguration getConfiguration() {
		return clientConfiguration;
	}

	@Inject
	@AllAllowed
	public void setClientConfiguration(ClientConfiguration clientConfiguration) {
		this.clientConfiguration = clientConfiguration;
	}
}

