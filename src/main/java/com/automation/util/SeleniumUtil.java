package com.automation.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.automation.exception.SeleniumException;
import com.automation.model.UIResult;
import com.automation.model.UserAction;

public class SeleniumUtil {
    
    private static final Logger LOGGER = Logger.getLogger(SeleniumUtil.class);

    public static final String SEND_KEY = "send_key";
    public static final String CLICK = "click";
    public static final String DOUBLE_CLICK = "double_click";
    public static final String DRAG_AND_DROP = "drag_and_drop";
    
    
    public static final String EQUAL = "equal";
    public static final String CONTAIN = "contain";
    public static final String GRID = "grid";
    public static final String ENABLED = "enabled";
    public static final String DISABLED = "disabled";
    public static final String SELECTED = "selected";

    public static void doUserAction(UserAction userAction, WebDriver driver) throws SeleniumException {
        try {
            switch (userAction.getInputType()) {
            case CLICK:
                driver.findElement(By.xpath(userAction.getXpath())).click();
                break;
            case SEND_KEY:
                driver.findElement(By.xpath(userAction.getXpath())).sendKeys(userAction.getValue());
                break;
            case DOUBLE_CLICK:
                Actions action = new Actions(driver);
                action.moveToElement(driver.findElement(By.xpath(userAction.getXpath()))).doubleClick().perform();
                break;
            case DRAG_AND_DROP:
                break;
            default:
                break;
            }
        } catch (Exception e) {
            throw new SeleniumException("Fail on step " + userAction.getStep() + " with message " + e.getMessage());
        }
    }
    
    public static void wait2Second() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }
    
    public static void updateUIResult(UIResult uiResult, WebDriver driver) throws SeleniumException {
        try {
            String expectedResult = uiResult.getExpected();
            if (StringUtils.isEmpty(expectedResult)) {
                return;
            }
            
            WebElement element = driver.findElement(By.xpath(uiResult.getXpath()));            
            switch (uiResult.getOperator()) {
            case EQUAL:
                String elementValue = element.getText();
                uiResult.setActual(elementValue);
                setResult(uiResult, elementValue != null && elementValue.trim().equalsIgnoreCase(uiResult.getExpected().trim()));
                break;

            case GRID:
                String[] arrayResult = expectedResult.split(Constant.VERTICAL_BAR_KEY);
                List<WebElement> resultGrids = driver.findElements(By.xpath(uiResult.getXpath()));
                List<String[]> rowResults = new ArrayList<String[]>();

                for (WebElement webElement: resultGrids) {
                    rowResults.add(webElement.getText().split(Constant.BREAK_LINE_KEY));
                }
                
                uiResult.setActual(buildGridActualResult(rowResults));
                uiResult.setResult(gridRowValidate(arrayResult, rowResults));
                break;
                
            case CONTAIN:
                break;
            case ENABLED:
                setResult(uiResult, element.isEnabled());
            case DISABLED:
                setResult(uiResult, !element.isEnabled());
            case SELECTED:
                setResult(uiResult, element.isSelected());
                break;
            default:
                break;
            }
        } catch (Exception e) {
            throw new SeleniumException("Fail on step " + uiResult.getName() + " with message " + e.getMessage());
        }
    }
    
    public static String gridRowValidate(String[] inputString, List<String[]> gridRows) {
        try {
            for (String[] row : gridRows) {
                if (stringArrayComparator(row, inputString)) {
                    return Constant.RESULT_PASS;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return Constant.RESULT_FAIL;
    }
    
    private static String buildGridActualResult(List<String[]> gridResult) {
        List<String> results = new ArrayList<String>();
        for (String[] actuals: gridResult) {
            StringBuilder builder = new StringBuilder();
            for (String actual: actuals) {
                builder.append(actual.trim());
                builder.append(Constant.VERTICAL_BAR_KEY);
            }
            String result = builder.toString();
            results.add(result.substring(0, result.length()-1));
        }
 
        return results.toString();
    }

    private static boolean stringArrayComparator(String[] actual, String[] expected) {
        if (ArrayUtils.isEmpty(actual) || ArrayUtils.isEmpty(expected)) {
            return false;
        }

        int length = expected.length;
        int count = 0;
        for (int i = 0; i < length; i++) {
            if (expected[i] != null && expected[i].trim().equalsIgnoreCase(actual[i].trim())) {
                count++;
            }
        }

        if (count == length) {
            return true;
        } else {
            return false;
        }
    }
    
    public static String createURL(String host, int port) {
        StringBuilder result = new StringBuilder();
        result.append("http://");
        result.append(host);
        result.append(":");
        result.append(port);
        return result.toString();
    }
    
    private static void setResult(UIResult uiResult, boolean expression) {
        if (expression) {
            uiResult.setResult(Constant.RESULT_PASS);
        } else {
            uiResult.setResult(Constant.RESULT_FAIL);
        }
    }

}
