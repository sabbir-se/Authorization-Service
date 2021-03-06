package com.dsi.authorization.util;

import com.dsi.authorization.filter.ResponseCORSFilter;
import com.dsi.authorization.resource.UserResource;
import com.dsi.checkauthorization.filter.CheckAuthorizationFilter;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by sabbir on 6/16/16.
 */
public class AuthorizationService extends ResourceConfig {

    public AuthorizationService(){
        packages("com.dsi.authorization");
        register(ResponseCORSFilter.class);
	register(CheckAuthorizationFilter.class);

        SessionUtil.getSession();
    }
}
