package io.fnx.backend.rest;

import com.googlecode.objectify.Key;
import io.fnx.backend.auth.CallContext;
import io.fnx.backend.auth.guards.AllAllowed;
import io.fnx.backend.domain.CmsArticleEntity;
import io.fnx.backend.domain.Role;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.service.CmsArticleService;
import io.fnx.backend.service.ListResult;
import io.fnx.backend.service.filter.CmsArticleFilter;
import io.fnx.backend.tools.hydration.HydrationRecipe;
import io.fnx.backend.tools.hydration.HydrationRecipeStep;
import io.fnx.backend.tools.hydration.SimpleHydrationRecipe;
import io.fnx.backend.util.conf.ClientConfiguration;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

