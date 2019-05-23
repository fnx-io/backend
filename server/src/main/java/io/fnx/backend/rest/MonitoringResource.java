package io.fnx.backend.rest;

import com.google.common.collect.Maps;
import io.fnx.backend.tools.authorization.AllAllowed;
import org.joda.time.DateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;

@Path("/v1/secure/monitoring")
public class MonitoringResource extends BaseResource {

    @GET
    @Path("/status")
    @AllAllowed
    public Map<String, Object> ping() {
        final HashMap<String, Object> resp = Maps.newHashMap();
        resp.put("status", "ok");
        resp.put("timestamp", DateTime.now());
        return resp;
    }
}
