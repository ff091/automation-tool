package com.automation.test;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.automation.config.Configuration;
import com.automation.ivu.IVUUIProcessor;
import com.automation.model.UITestCaseTemplate;
import com.automation.processor.TestCaseProcessor;
import com.automation.util.FileUtil;

public class UserInterfaceTest {
    
    private final Logger LOGGER = LoggerFactory.getLogger(UserInterfaceTest.class);

    private String testCaseLocation;
    private String endPoint;
    private WebDriver driver;
    private String loginPath;

    @Test
    public void f() {
        List<String> fileNames = FileUtil.listFiles(testCaseLocation);
//        ExecutorService executorService = Executors.newFixedThreadPool(fileNames.size());
        for (String fileName : fileNames) {
//            executorService.execute(() -> runTest(fileName));
            runTest(fileName);
        }

//        executorService.shutdown();
//        while (!executorService.isTerminated()) {
//        }
    }

    public void runTest(String fileName) {
        TestCaseProcessor processor = new TestCaseProcessor(testCaseLocation + fileName);
        List<UITestCaseTemplate> testCases = processor.getUITestCaseTemplateFromExcel();
        String fullURL = endPoint + testCases.get(0).getUrl();
        IVUUIProcessor ivuProcessor = new IVUUIProcessor(driver);
        try {
            ivuProcessor.login(fullURL, loginPath);
        } catch (Exception e) {
            LOGGER.error("Login fail " + e.getMessage());
            return;
        }
        List<UITestCaseTemplate> results = new ArrayList<UITestCaseTemplate>();
        for (UITestCaseTemplate testCase : testCases) {
            testCase.setUrl(fullURL);
            UITestCaseTemplate temp = ivuProcessor.runTest(testCase);
            results.add(temp);
        }
        
        processor.writeUITestResultToExcel(results);
    }

    @BeforeClass
    public void beforeClass() {
        testCaseLocation = Configuration.getInstance().getTestCaseLocation() + "ui\\";
        endPoint = Configuration.getInstance().getUiEndPoint();
        loginPath = Configuration.getInstance().getLoginPath();
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterClass
    public void afterClass() {
        driver.close();
    }

}
