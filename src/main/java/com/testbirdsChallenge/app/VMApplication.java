package com.testbirdsChallenge.app;

import com.testbirdsChallenge.rest.VMController;
import com.testbirdsChallenge.rest.VMServiceImpl;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class VMApplication extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    public VMApplication() {
        // Register our hello service
        singletons.add(new VMController(new VMServiceImpl()));
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
