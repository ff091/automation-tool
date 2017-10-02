package com.automation.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import com.automation.model.UITestCaseTemplate;
import com.automation.processor.TestCaseProcessor;
import com.automation.util.ExcelUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class SeleniumService {
    private final Logger LOGGER = LoggerFactory.getLogger(SeleniumService.class);

    @Value("${template.location}")
    private String templateLocation;
    
    @Autowired
    private TestCaseCache testCaseCache;


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
