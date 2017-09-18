package com.automation.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUlti {
	public static ObjectMapper mapper = new ObjectMapper();
	public static void output (String filePath,Object obj)
	{
		try
		{
			Files.deleteIfExists(Paths.get(filePath));			
			FileOutputStream out = new FileOutputStream(filePath);		
			out.write(mapper.writeValueAsBytes(obj));			
			out.close();			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static String outputString (Object obj)
	{
		
		try
		{
			return mapper.writeValueAsString(obj);			
						
		}catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
	
	public static <targetClass> Object jsonStringToListObject(String jsonString, Class targetClass) {
			List<Object> objectList = null;
			try {
				objectList = mapper.readValue(jsonString, new TypeReference<List<targetClass>>(){});
			} catch (Exception e) {
				e.printStackTrace();
			}
		return objectList;
	}
	
	public static <targetClass> Object jsonStringToObject(String jsonString, Class targetClass) {
		Object targetObject = null;
		try {
			targetObject = mapper.readValue(jsonString,targetClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	return targetObject;
}
	
	public static <targetClass> Object ComplicatedJsonToListObject(String jsonString, Class targetClass) {
	    try {
	        TypeFactory typeFactory = mapper.getTypeFactory();
	        CollectionType collectionType = typeFactory.constructCollectionType(
	                                            List.class, targetClass);
	        return mapper.readValue(jsonString, collectionType);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
}
