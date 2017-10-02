package com.honegori;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.automation.model.UIResult;
import com.automation.model.UITestCaseTemplate;
import com.automation.model.UserAction;
import com.automation.util.Constant;
import com.automation.util.SeleniumUtil;

public class HonegoriUIProcessor {
    private WebDriver driver;
    
    public HonegoriUIProcessor(WebDriver driver) {
        this.driver = driver;
    }
    
    public UITestCaseTemplate runTest(UITestCaseTemplate testCase) {
        List<UserAction> userActions = testCase.getUserActions();
        List<UIResult> uiResults = testCase.getUiResults();
        UITestCaseTemplate result = testCase;

        driver.get(testCase.getUrl());
        SeleniumUtil.wait2Second();

        try {
            // Run step by step
            for (UserAction userAction : userActions) {
                SeleniumUtil.doUserAction(userAction, driver);
                SeleniumUtil.wait2Second();
            }

            // compare result
            for (UIResult uiResult : uiResults) {
                SeleniumUtil.updateUIResult(uiResult, driver);
            }

        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setResult(Constant.RESULT_FAIL);
            return result;
        }

        result.updateTestResult();
        return result;
    }
}
