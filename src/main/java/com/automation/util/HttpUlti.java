package com.automation.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

public class HttpUlti {

    private static final Logger LOGGER = Logger.getLogger(HttpUlti.class);

    public static String sendGet(String url, List<BasicNameValuePair> listHeader) {
        try {
            LOGGER.info("GET request to: " + url);
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            if (listHeader != null) {
                for (BasicNameValuePair header : listHeader) {
                    request.addHeader(header.getName(), header.getValue());
                }
            }
            HttpResponse response = client.execute(request);

            LOGGER.info("Response Code : " + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();

        } catch (Exception e) {
            LOGGER.error(e);
            return "";
        }
    }

    public static String sendPost(String url, List<BasicNameValuePair> listHeader, String entity) {
        try {
            LOGGER.info("POST request to: " + url);
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            if (listHeader != null) {
                for (BasicNameValuePair header : listHeader) {
                    post.addHeader(header.getName(), header.getValue());
                }
            }
            post.setEntity(new StringEntity(entity));
            HttpResponse response = client.execute(post);
            LOGGER.info("Response Code : " + response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            LOGGER.error(e);
            return "";
        }

    }

    public static String sendPut(String url, List<BasicNameValuePair> listHeader, String entity) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            LOGGER.info("PUT request to: " + url);
            HttpPut put = new HttpPut(url);
            StringEntity params = new StringEntity(entity, "UTF-8");
            put.setEntity(params);
            if (listHeader != null) {
                for (BasicNameValuePair header : listHeader) {
                    put.addHeader(header.getName(), header.getValue());
                }
            }
            HttpResponse response = httpClient.execute(put);
            LOGGER.info("Response Code : " + response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();

        } catch (Exception e) {
            LOGGER.error(e);
            return "";
        }
    }

    // http delete
    public static int sendDelete(String URL, List<BasicNameValuePair> listHeader) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        int responseCode = 0;

        try {
            LOGGER.info("Delete request to: " + URL);
            HttpDelete delete = new HttpDelete(URL);
            if (listHeader != null) {
                for (BasicNameValuePair header : listHeader) {
                    delete.addHeader(header.getName(), header.getValue());
                }
            }
            HttpResponse response = httpClient.execute(delete);
            LOGGER.info("Response Code : " + response.getStatusLine().getStatusCode());
            responseCode = response.getStatusLine().getStatusCode();

        } catch (Exception e) {
            LOGGER.error(e);
        }
        return responseCode;
    }
    
    public static String callWebService(String url, String method, List<BasicNameValuePair> listHeaders, String entity) {
        String response = "";
        if (Constant.GET_METHOD.equalsIgnoreCase(method)) {
            response = HttpUlti.sendGet(url, listHeaders);
        } else if (Constant.POST_METHOD.equalsIgnoreCase(method)) {
            response = HttpUlti.sendPost(url, listHeaders, entity);
        } else if (Constant.PUT_METHOD.equalsIgnoreCase(method)) {
            response = HttpUlti.sendPut(url, listHeaders, entity);
        } else if (Constant.DELETE_METHOD.equalsIgnoreCase(method)) {
            response = String.valueOf(HttpUlti.sendDelete(url, listHeaders));
        }
        return response;
    }

}
