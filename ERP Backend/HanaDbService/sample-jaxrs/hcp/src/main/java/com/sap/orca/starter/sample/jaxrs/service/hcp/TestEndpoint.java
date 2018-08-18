package com.sap.orca.starter.sample.jaxrs.service.hcp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Component
@Path("/testhcp")
@Api(value = "/testhcp", description = "Demo call to test hcp specific code")
@Produces(MediaType.TEXT_PLAIN)
public class TestEndpoint {

    @GET
    @ApiOperation(value = "Demo call to test hcp specific code", response = Response.class)
    public Response testGet() {
        return Response.ok("Made it in").build();
    }
}
