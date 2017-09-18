package com.automation.test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.automation.config.Configuration;
import com.automation.ivu.IVUWSProcessor;
import com.automation.model.WSTestCaseTemplate;
import com.automation.processor.TestCaseProcessor;
import com.automation.util.FileUtil;

public class WebServiceTest {

    private String testCaseLocation;
    private String webserviceEndPoint;

    @Test
    public void f() {
        List<String> fileNames = FileUtil.listFiles(testCaseLocation);
        ExecutorService executorService = Executors.newFixedThreadPool(fileNames.size());
        for (String fileName : fileNames) {
            runTest(fileName);
//            executorService.execute(() -> runTest(fileName));
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
    }

    public void runTest(String fileName) {
        TestCaseProcessor processor = new TestCaseProcessor(testCaseLocation + fileName);
        List<WSTestCaseTemplate> testCases = processor.getWSTestCaseTemplateFromExcel();
        IVUWSProcessor.runTest(testCases, webserviceEndPoint);
        processor.writeWSTestResultToExcel(testCases);
    }

    @BeforeClass
    public void beforeClass() {
        testCaseLocation = Configuration.getInstance().getTestCaseLocation() + "ws\\";
        webserviceEndPoint = Configuration.getInstance().getWebServiceEndPoint();
    }

}
