package com.automation.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.automation.model.UIResult;
import com.automation.model.UITestCaseTemplate;
import com.automation.model.UserAction;
import com.automation.model.WSTestCaseTemplate;
import com.automation.util.ExcelUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestCaseProcessor {

    private String fileName;
    private List<List<String>> testCases;

    public TestCaseProcessor(String fileName) {
        super();
        this.fileName = fileName;
    }

    public List<WSTestCaseTemplate> getWSTestCaseTemplateFromExcel() {
        testCases = ExcelUtil.readExcel(fileName);
        List<WSTestCaseTemplate> results = new ArrayList<WSTestCaseTemplate>();
        WSTestCaseTemplate testCaseTemplate = null;
        for (List<String> testCase : testCases) {
            testCaseTemplate = new WSTestCaseTemplate();
            testCaseTemplate.setId(testCase.get(0));
            testCaseTemplate.setName(testCase.get(1));
            testCaseTemplate.setUrl(testCase.get(2));
            testCaseTemplate.setMethod(testCase.get(3));
            testCaseTemplate.setRequest(testCase.get(4));
            testCaseTemplate.setJsonPath(testCase.get(6));
            testCaseTemplate.setSql(testCase.get(7));
            results.add(testCaseTemplate);
        }
        return results;
    }

    public void writeWSTestResultToExcel(List<WSTestCaseTemplate> testCaseResults) {
        int totalTestCases = testCaseResults.size();
        WSTestCaseTemplate testCase = null;
        for (int i = 0; i < totalTestCases; i++) {
            testCase = testCaseResults.get(i);
            testCases.get(i).set(5, testCase.getResponse());
            testCases.get(i).set(8, testCase.getResult());
        }

        ExcelUtil.updateExcel(testCases, fileName);

    }

    public List<UITestCaseTemplate> getUITestCaseTemplateFromExcel() {
        testCases = ExcelUtil.readExcel(fileName);
        List<UITestCaseTemplate> results = new ArrayList<UITestCaseTemplate>();
        UITestCaseTemplate testCaseTemplate = null;
        for (List<String> testCase : testCases) {
            testCaseTemplate = new UITestCaseTemplate();
            testCaseTemplate.setId(testCase.get(0));
            testCaseTemplate.setDescription(testCase.get(1));
            testCaseTemplate.setUrl(testCase.get(2));

            List<UserAction> userActions = new ArrayList<UserAction>();
            List<UIResult> uiResults = new ArrayList<UIResult>();
            try {
                ObjectMapper mapper = new ObjectMapper();
                userActions = mapper.readValue(testCase.get(3), new TypeReference<List<UserAction>>() {
                });
                // sort user action by step
                Collections.sort(userActions, (userAction1, userAction2) -> userAction1.getStep().compareTo(userAction2.getStep()));
                uiResults = mapper.readValue(testCase.get(4), new TypeReference<List<UIResult>>() {
                });
            } catch (Exception e) {
                userActions = null; // detect wrong template
                uiResults = null;
            }
            
            testCaseTemplate.setSqls(testCase.get(5));

            testCaseTemplate.setUserActions(userActions);
            testCaseTemplate.setUiResults(uiResults);

            results.add(testCaseTemplate);
        }
        return results;
    }

    public void writeUITestResultToExcel(List<UITestCaseTemplate> testCaseResults) {
        int totalTestCases = testCaseResults.size();
        UITestCaseTemplate testCase = null;
        for (int i = 0; i < totalTestCases; i++) {
            testCase = testCaseResults.get(i);
            testCases.get(i).set(6, testCase.getResult());
        }

        ExcelUtil.updateExcel(testCases, fileName);

    }

}
