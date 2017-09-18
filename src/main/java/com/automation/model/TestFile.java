package com.automation.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestFile {
    private String fileName;
    private String host;
    private int port;
    private List<UITestCaseTemplate> testCases;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<UITestCaseTemplate> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<UITestCaseTemplate> testCases) {
        this.testCases = testCases;
    }

}
