package com.ivu;

import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.automation.model.UIResult;
import com.automation.model.UITestCaseTemplate;
import com.automation.model.UserAction;
import com.automation.processor.DBConstant;
import com.automation.util.Constant;
import com.automation.util.DatabaseUtil;
import com.automation.util.SeleniumUtil;

public class IVUUIProcessor {
    private WebDriver driver;
    
    public IVUUIProcessor(WebDriver driver) {
        this.driver = driver;
    }
    
    public void login(String url, String loginPath) throws Exception {
        URL urlTemp = new URL(url);
        driver.get(SeleniumUtil.createURL(urlTemp.getHost(), urlTemp.getPort()) + loginPath);
        driver.findElement(By.xpath(".//*[@id='login_page']/form/input[1]")).sendKeys("admin");
        driver.findElement(By.xpath(".//*[@id='login_page']/form/input[2]")).sendKeys("123456");
        driver.findElement(By.xpath("//.//*[@id='login_page']/form/div/div/button")).click();
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

        try {
            if (StringUtils.isNotEmpty(testCase.getSqls())) {
                DatabaseUtil dbUtil = new DatabaseUtil();
                dbUtil.openDBConnection(DBConstant.DB_DRIVER, DBConstant.DB_CONNECTION, DBConstant.DB_USER, DBConstant.DB_PASSWORD);
                String[] sqls = testCase.getSqls().split(Constant.SEMI_COLON_KEY);
                for (String sql : sqls) {
                    dbUtil.deleteSQL(sql);
                }
                dbUtil.closeDBConnection();
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
