package com.sap.orca.starter.sample.jaxrs.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.sap.orca.starter.core.context.SessionContext;
import com.sap.orca.starter.core.context.SessionContextHolder;
import com.sap.orca.starter.core.security.IUser;
import com.sap.orca.starter.sample.jaxrs.model.Model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * JAX-RS Based API
 */
@Component
@Path("/v1/model")
@Api(value = "/v1/model", description = "REST Service for model")
@Produces(MediaType.APPLICATION_JSON)
public class ModelService {

    private Map<String, Map<String, Model>> stores;
    private AtomicInteger seq = new AtomicInteger(2);

    public ModelService() {
        stores = new ConcurrentHashMap<>();
    }

    @GET
    @ApiOperation(value = "List model", response = List.class)
    public Collection<Model> list() {
        return getStore().values();
    }

    @GET
    @Path("{id}")
    @ApiOperation(value = "Get model", response = Model.class)
    public Model get(@PathParam("id") String id) {
        return getStore().get(id);
    }

    @Path("async")
    @GET
    @ApiOperation(value = "Get model Async", response = List.class)
    public void asyncGet(@Suspended final AsyncResponse asyncResponse) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Collection<Model> models = getStore().values();
                asyncResponse.resume(models);
            }
        }).start();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create model", response = Model.class)
    public Model create(Model model) {
        int id = seq.incrementAndGet();

        SessionContext sessionContext = SessionContextHolder.get();
        // Demo how to get user and tenant from session context
        IUser user = sessionContext.getUser();
        String tenant = sessionContext.getTenantId();

        Model newModel = new Model();
        newModel.setTitle(model.getTitle());
        newModel.setDescription(model.getDescription());
        newModel.setOwner(tenant + ":" + user.getName());
        newModel.setId(String.valueOf(id));
        getStore().put(newModel.getId(), newModel);

        return newModel;
    }

    private Map<String, Model> getStore() {
        SessionContext sessionContext = SessionContextHolder.get();
        String tenantId = sessionContext.getTenantId();
        Map<String, Model> store = null;

        if (tenantId == null) {
            tenantId = "";
        }

        synchronized (tenantId.intern()) {
            store = stores.get(tenantId);
            if (store == null) {
                store = new ConcurrentHashMap<>();
                Model newModel = new Model();
                newModel.setId("default");
                newModel.setOwner(sessionContext.getUser().getName());
                newModel.setDescription("Default for " + tenantId);
                store.put(newModel.getId(), newModel);
                stores.put(tenantId, store);
            }
        }
        return store;
    }
}
