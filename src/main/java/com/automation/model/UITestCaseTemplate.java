package com.automation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.automation.util.Constant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UITestCaseTemplate {
    private String id;
    private String description;
    private String url;
    private List<UserAction> userActions;
    private List<UIResult> uiResults;
    private String sqls;
    private String result;
    private String message;

    public UITestCaseTemplate() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<UserAction> getUserActions() {
        if (CollectionUtils.isEmpty(userActions)) {
            return new ArrayList<UserAction>();
        }
        // sort user action by step
        Collections.sort(userActions, (userAction1, userAction2) -> userAction1.getStep().compareTo(userAction2.getStep()));
        return userActions;
    }

    public void setUserActions(List<UserAction> userActions) {
        this.userActions = userActions;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<UIResult> getUiResults() {
        return uiResults;
    }

    public void setUiResults(List<UIResult> uiResults) {
        this.uiResults = uiResults;
    }

    public String getSqls() {
        return sqls;
    }

    public void setSqls(String sqls) {
        this.sqls = sqls;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void updateTestResult() {
        int count = 0;
        for (UIResult uiResult : uiResults) {
            if (Constant.RESULT_PASS.equalsIgnoreCase(uiResult.getResult())) {
                count++;
            }
        }
        if (count == uiResults.size()) {
            result = Constant.RESULT_PASS;
        } else {
            result = Constant.RESULT_FAIL;
        }
    }

}
