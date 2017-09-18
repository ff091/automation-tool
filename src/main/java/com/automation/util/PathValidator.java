package com.automation.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.jayway.jsonpath.JsonPath;

public class PathValidator {

    private static final Logger LOGGER = Logger.getLogger(PathValidator.class);
    private static final Pattern COUNT_PATTERN = Pattern.compile("(count)(\\()(.*)(\\))(\\s?==\\s?)(\\d+)");

    public static String jsonPathValidate(String inputJson, String jsonPath) {
        if (StringUtils.isEmpty(jsonPath)) {
            // no need to verify json path
            return Constant.RESULT_PASS;
        }

        String result = Constant.RESULT_FAIL;
        Matcher matcher = COUNT_PATTERN.matcher(jsonPath);
        List<String> jsonValidators = new ArrayList<String>();
        try {
            if (matcher.find()) {
                String splitInputJsonPath = matcher.group(3);
                String splitCount = matcher.group(6);
                jsonValidators = JsonPath.read(inputJson, splitInputJsonPath);
                if (CollectionUtils.isNotEmpty(jsonValidators) && jsonValidators.size() == Integer.parseInt(splitCount)) {
                    result = Constant.RESULT_PASS;
                }
            } else {
                jsonValidators = JsonPath.read(inputJson, jsonPath);
                if (CollectionUtils.isNotEmpty(jsonValidators)) {
                    result = Constant.RESULT_PASS;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }

        return result;
    }

    public static String xPathValidate(String inputXml, String xpath) {
        String result = Constant.RESULT_FAIL;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(inputXml.getBytes("UTF-8")));
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile(xpath).evaluate(document, XPathConstants.NODESET);
            if (nodeList != null && nodeList.getLength() > 0) {
                result = Constant.RESULT_PASS;
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return result;
    }

}
