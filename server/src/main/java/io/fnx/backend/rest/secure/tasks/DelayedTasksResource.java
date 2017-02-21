package io.fnx.backend.rest.secure.tasks;

import io.fnx.backend.domain.DelayedTaskEntity;
import io.fnx.backend.rest.BaseResource;
import io.fnx.backend.service.DelayedTaskService;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/v1/secure/tasks/delayed")
public class DelayedTasksResource extends BaseResource {

    private DelayedTaskService delayedTaskService;

    @GET
    public Response createDelayedTask(@QueryParam("taskUrl") String taskUrl,
                                      @QueryParam("delayUntil") Long timestamp,
                                      @QueryParam("taskId") String taskId,
                                      @QueryParam("archive") Boolean archive) {
        DateTime delayed = null;
        if (taskId != null) {
            delayed = new DateTime(timestamp);
        }
        if (archive == null) archive = DelayedTaskEntity.DEFAULT_ARCHIVE;
        return created(delayedTaskService.createDelayedTask(taskId, delayed, taskUrl, archive));
    }

    @GET
    @Path("/processing/start")
    public Response scheduleDelayedTasksProcessing() {
        return ok(delayedTaskService.scheduledDelayedTasksProcessing(DateTime.now()));
    }

    @GET
    @Path("/{taskId}/process")
    public Response processTask(@PathParam("taskId") String taskId) {
        return ok(delayedTaskService.process(taskId));
    }

    @Inject
    public void setDelayedTaskService(DelayedTaskService delayedTaskService) {
        this.delayedTaskService = delayedTaskService;
    }
}
