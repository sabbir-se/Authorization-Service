package com.dsi.authorization.service.impl;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by sabbir on 7/12/16.
 */
public class APIProvider {

    private static final Logger logger = Logger.getLogger(APIProvider.class);

    public static final String API_URLS_FILE = "Api.properties";
    private static final Properties apiProp= new Properties();

    static{
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream propIS = loader.getResourceAsStream(API_URLS_FILE);
            apiProp.load(propIS);
        } catch (IOException e) {
            logger.error("An error occurred while loading urls.", e);
        }
    }

    public static final String BASE_URL = apiProp.getProperty("base.url");
    public static final String API_CREATE_SESSION = BASE_URL + apiProp.getProperty("authentication.create");
}
