package com.ivu;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.automation.model.TestFile;
import com.automation.model.UITestCaseTemplate;
import com.automation.util.SeleniumUtil;

@RestController
public class IVUController {
    
    @Value("${ivu.login.path}")
    private String loginPath;
    
    private WebDriver driver;
    
    public void init() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    public void close() {
        driver.close();
    }

    public void openURL(String url) {
        driver.get(url);
    }
    
    
    @RequestMapping(method = RequestMethod.POST, value = "/test")
    @ResponseBody
    public UITestCaseTemplate runTestOne(@RequestBody UITestCaseTemplate testCase) {
        init();
        try {
            IVUUIProcessor processor = new IVUUIProcessor(driver);
            processor.login(testCase.getUrl(), loginPath);
            return processor.runTest(testCase);
        } catch (Exception e) {
            testCase.setMessage("Login fail " + e.getMessage());
            return testCase;
        } finally {
            close();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test-all")
    @ResponseBody
    public List<UITestCaseTemplate> runTestAll(@RequestBody TestFile testFile) {
        List<UITestCaseTemplate> result = new ArrayList<UITestCaseTemplate>();
        List<UITestCaseTemplate> testCases = testFile.getTestCases();
        String originalURL = testCases.get(0).getUrl();
        String fullURL = SeleniumUtil.createURL(testFile.getHost(), testFile.getPort()) + originalURL;

        init();
        IVUUIProcessor processor = new IVUUIProcessor(driver);
        try {
            processor.login(fullURL, loginPath);
        } catch (Exception e) {
            close();
            return null;
        }

        try {
            for (UITestCaseTemplate testCase : testCases) {
                testCase.setUrl(fullURL);
                UITestCaseTemplate temp = processor.runTest(testCase);
                temp.setUrl(fullURL);
                result.add(temp);
            }
        } catch (Exception e) {
            return null;
        } finally {
            close();
        }

        return result;
    }
}
