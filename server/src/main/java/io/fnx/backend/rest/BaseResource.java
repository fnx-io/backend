package io.fnx.backend.rest;

import io.fnx.backend.auth.CallContext;
import io.fnx.backend.domain.filter.FilterLimits;
import com.google.inject.Inject;
import io.fnx.backend.service.DontValidate;
import io.fnx.backend.tools.hydration.Hydrator;

import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Base resource containing useful functionality
 */
@Consumes("application/json;charset=utf-8")
@Produces("application/json;charset=utf-8")
public class BaseResource {

    //******* these are set from Query parameters of incoming request and used for pagination when filtering *******
    private int page;
    private boolean allPages = true;

    @Inject
    protected Provider<CallContext> callContextProvider;

	protected Hydrator hydrator;

    @DontValidate
	public CallContext cc() {
        return callContextProvider.get();
    }

	@OPTIONS
    @DontValidate
    public Response rootOptions() {
        return Response.ok(null).build();
    }

    /**
     *
     * @return request had succeeded, but there is no content to send to user
     */
    protected Response ok() {
        return Response.status(Response.Status.NO_CONTENT).build(); // no-content
    }

    /**
     * Return status 200 with payload
     * @param body the response payload to send
     * @return response indicating http request has been fulfilled with payload
     */
    protected Response ok(Object body) {
        return Response.status(Response.Status.OK).entity(body).build();
    }

    /**
     * Return status 404 indicating requested resource has not been found
     * @return response indicating requested resource has not been found
     */
    protected Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Returns status 201 indicated request succeeded and resulted in new entity being created
     *
     * @param body the response body to embed in the response
     * @return response request succeeded
     */
    protected Response created(Object body) {
        return Response.status(Response.Status.CREATED).entity(body).build();
    }

    /**
     *
     * @return status 401 indicating proper authorization is required
     */
    protected Response unauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     *
     * @return status 403 indicating current authorization is insufficient for requested resource
     */
    protected Response forbidden() {
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    /**
     *
     * @return status 400 indicating the request was bad
     */
    protected Response badRequest() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * @param record the record to return
     * @return status 200 when the record is not null. Returns 404 if given record is null.
     */
    protected Response maybeFound(Object record) {
        if (record == null) {
            return notFound();
        } else {
            return ok(record);
        }
    }

    /**
     * Return either 200 with body or 404 when body is null
     * @param body
     * @return response to send to client depending on whether the body exists or is null
     */
    protected Response response(final Object body) {
        if (body == null) {
            return notFound();
        } else {
            return ok(body);
        }
    }

    @DontValidate
    @QueryParam("page")
    public void setPage(final int page) {
        this.page = page;
    }

    @DontValidate
    @QueryParam("allPages")
    public void setAllPages(final boolean allPages) {
        this.allPages = allPages;
    }

    @DontValidate
    public FilterLimits filterLimits() {
        return new FilterLimits(page, allPages);
    }


	@Inject
	protected void setHydrator(Hydrator hydrator) {
		this.hydrator = hydrator;
	}
	
}
