package com.automation.config;

import java.io.IOException;
import java.util.Properties;

public class Configuration {

    private static Configuration instance = null;
    private String webServiceEndPoint;
    private String uiEndPoint;
    private String testCaseLocation;
    private String loginPath;

    private Configuration() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
            webServiceEndPoint = prop.getProperty("service.endpoint");
            uiEndPoint = prop.getProperty("ui.endpoint");
            testCaseLocation = prop.getProperty("testcase.location");
            loginPath = prop.getProperty("login.path");
        } catch (IOException e) {

        }
    }

    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public String getWebServiceEndPoint() {
        return webServiceEndPoint;
    }

    public String getTestCaseLocation() {
        return testCaseLocation;
    }

    public String getUiEndPoint() {
        return uiEndPoint;
    }

    public String getLoginPath() {
        return loginPath;
    }

}
