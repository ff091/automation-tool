package com.automation.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.automation.config.TestCaseCache;
import com.automation.ivu.IVUUIProcessor;
import com.automation.model.TestFile;
import com.automation.model.UITestCaseTemplate;
import com.automation.processor.TestCaseProcessor;
import com.automation.util.ExcelUtil;
import com.automation.util.SeleniumUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class SeleniumService {
    private final Logger LOGGER = LoggerFactory.getLogger(SeleniumService.class);

    @Value("${template.location}")
    private String templateLocation;
    
    @Value("${login.path}")
    private String loginPath;

    @Autowired
    private TestCaseCache testCaseCache;

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

    @RequestMapping(method = RequestMethod.POST, value = "/add-testCase")
    @ResponseBody
    public void addTestCase(@RequestBody UITestCaseTemplate testCase, @RequestParam(value = "fileName") String fileName) {
        testCaseCache.addTestCase(fileName, testCase);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/export")
    @ResponseBody
    public void exportTestCase(@RequestParam(value = "fileName") String fileName) throws Exception {
        List<UITestCaseTemplate> testCases = testCaseCache.getTestCasesByKey(fileName);
        List<List<String>> datas = new ArrayList<List<String>>();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        for (UITestCaseTemplate testCase : testCases) {
            List<String> data = new ArrayList<String>();
            data.add(testCase.getId());
            data.add(testCase.getDescription());
            data.add(testCase.getUrl());
            data.add(ow.writeValueAsString(testCase.getUserActions()));
            data.add(ow.writeValueAsString(testCase.getUiResults()));
            data.add(testCase.getResult());
            datas.add(data);
        }

        ExcelUtil.createExcel(datas, templateLocation + "ui.xlsx", templateLocation + fileName + ".xlsx");

    }

    @RequestMapping(method = RequestMethod.POST, value = "/import")
    @ResponseBody
    public List<UITestCaseTemplate> importTestCases(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            LOGGER.error("File was empty");
            return Collections.emptyList();
        }

        Path filePath = Paths.get(templateLocation + file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        TestCaseProcessor processor = new TestCaseProcessor(filePath.toAbsolutePath().toString());
        return processor.getUITestCaseTemplateFromExcel();
    }
}
