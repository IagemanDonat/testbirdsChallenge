package com.testbirdsChallenge.rest;

import javax.ws.rs.core.Response;

public interface VMService {

    public Response start(String macAdress);

    public Response stop(String macAdress);

    public Response status(String macAdress);

    public Response update(String macAdress);
}
