package com.testbirdsChallenge.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/api/vm")
public class VMController {

    private VMService vmService;

    @Inject
    public VMController(VMService vmService){
        this.vmService = vmService;
    }

    @GET
    @Path("/start/{macAddress}")
    public Response start(@PathParam("macAddress") String macAdress) {
        return vmService.start(macAdress);
    }

    @GET
    @Path("/stop/{macAddress}")
    public Response stop(@PathParam("macAddress") String macAdress) {
        return vmService.stop(macAdress);
    }

    @GET
    @Path("/status/{macAddress}")
    public Response status(@PathParam("macAddress") String macAdress) {
        return vmService.status(macAdress);
    }

    @GET
    @Path("/update/{macAdress}")
    public Response update (@PathParam("macAdress") String macAdress){
        return vmService.update(macAdress);
    }


}