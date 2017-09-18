package com.automation.config;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import com.automation.model.UITestCaseTemplate;

@Component
public class TestCaseCache {
    // key = fileName_testCaseId
    private static ConcurrentMap<String, List<UITestCaseTemplate>> testCaseCaches = new ConcurrentHashMap<String, List<UITestCaseTemplate>>();
    
    public List<UITestCaseTemplate> getTestCasesByKey(String fileName) {
        return testCaseCaches.get(fileName);
    }
    
    public void addTestCase(String fileName, UITestCaseTemplate testCase) {
        List<UITestCaseTemplate> testCasesInCache = getTestCasesByKey(fileName);
        boolean isAddNew = true;
        for (UITestCaseTemplate testCaseInCache: testCasesInCache) {
            if (testCaseInCache.getId().equalsIgnoreCase(testCase.getId())) {
                testCaseInCache = testCase;
                isAddNew = false;
            }
        }
        if (isAddNew) {
            testCasesInCache.add(testCase);
        }
        
        testCaseCaches.put(fileName, testCasesInCache);
    }
}
