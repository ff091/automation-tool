package com.honegori;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.automation.model.UITestCaseTemplate;

@RestController
@RequestMapping("/honegori")
public class HonegoriController {
    
    @Value("${honegori.login.path}")
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
    public UITestCaseTemplate runTestOneWithoutLogin(@RequestBody UITestCaseTemplate testCase) {
        init();
        try {
            HonegoriUIProcessor processor = new HonegoriUIProcessor(driver);
            return processor.runTest(testCase);
        } catch (Exception e) {
            testCase.setMessage(e.getMessage());
            return testCase;
        } finally {
            close();
        }
    }

}
