package com.ivu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.automation.model.WSTestCaseTemplate;
import com.automation.processor.DBConstant;
import com.automation.util.Constant;
import com.automation.util.DatabaseUtil;
import com.automation.util.HttpUlti;
import com.automation.util.PathValidator;

public class IVUWSProcessor {

    private static final Logger LOGGER = Logger.getLogger(IVUWSProcessor.class);

    public static void runTest(List<WSTestCaseTemplate> testCases, String webserviceEndPoint) {
        DatabaseUtil dbUtil = new DatabaseUtil();
        dbUtil.openDBConnection(DBConstant.DB_DRIVER, DBConstant.DB_CONNECTION, DBConstant.DB_USER, DBConstant.DB_PASSWORD);
        List<BasicNameValuePair> listHeaders = headerAuthsJson(webserviceEndPoint + "/login", "admin", "123456");

        String fullURL = "";
        String response = "";
        for (WSTestCaseTemplate testCase : testCases) {
            fullURL = webserviceEndPoint + testCase.getUrl();
            response = HttpUlti.callWebService(fullURL, testCase.getMethod(), listHeaders, testCase.getRequest());
            testCase.setResponse(response);
            testCase.setResult(PathValidator.jsonPathValidate(response, testCase.getJsonPath()));

            if (!dbUtil.checkSelectSQL(testCase.getSql())) {
                testCase.setResult(Constant.RESULT_FAIL);
            }
        }

        dbUtil.closeDBConnection();
    }

    public static List<BasicNameValuePair> headerAuthsJson(String url, String userName, String password) {
        String entity = "username=" + userName + "&password=" + password + "&submit=";
        List<BasicNameValuePair> headersAuth = new ArrayList<BasicNameValuePair>();
        headersAuth.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
        List<BasicNameValuePair> headers = new ArrayList<BasicNameValuePair>();
        headers.add(getAuthCookie(url, headersAuth, entity));
        headers.add(new BasicNameValuePair("Content-Type", "application/json"));
        return headers;
    }

    public static BasicNameValuePair getAuthCookie(String url, List<BasicNameValuePair> listHeader, String entity) {
        HttpResponse response = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            if (listHeader != null) {
                for (BasicNameValuePair header : listHeader) {
                    post.addHeader(header.getName(), header.getValue());
                }
            }
            post.setEntity(new StringEntity(entity));
            response = client.execute(post);
            Header cookie = response.getFirstHeader("Set-Cookie");
            return new BasicNameValuePair("Cookie", cookie.getValue());
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

}
