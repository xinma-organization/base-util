package com.xinma.base.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * Jackson JSON 数据处理工具类
 * 
 * @author yongyi.zhang
 *
 */
public class JacksonUtils {

	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static ObjectMapper mapper = new ObjectMapper();

	static {
		// to enable standard indentation ("pretty-printing"):
		mapper.disable(SerializationFeature.INDENT_OUTPUT);
		// to allow serialization of "empty" POJOs (no properties to serialize)
		// (without this setting, an exception is thrown in those cases)
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		// to write java.util.Date, Calendar as number (timestamp):
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

		// DeserializationFeature for changing how JSON is read as POJOs:
		// to prevent exception when encountering unknown property:
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// to allow coercion of JSON empty String ("") to null Object value:
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	}

	/**
	 * 将Object对象转换为JSON格式的字符串
	 * 
	 * @param obj
	 *            要转换成JSON格式字符串的对象
	 * @return 序列化成功，返回json格式的字符串；否则返回null
	 */
	public static String write2JsonString(Object obj) {
		String result = null;

		if (obj != null) {
			try {
				result = mapper.writeValueAsString(obj);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 将Object对象转换为制定JsonView的JSON格式的字符串
	 * 
	 * @param value
	 *            要转换成JSON格式字符串的对象
	 * @param serializationView
	 *            JsonView定义类
	 * @return 序列化成功，返回json格式的字符串；否则返回null
	 */
	public static String write2JsonStringWithJsonView(Object value, Class<?> serializationView) {
		String result = null;

		if (value != null) {
			try {
				result = mapper.writerWithView(serializationView).writeValueAsString(value);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 将含Date类型属性值的Object对象转换为JSON格式的字符串
	 * 
	 * @param obj
	 *            要转换成JSON格式字符串的对象
	 * @param format
	 *            日期格式，默认格式为"yyyy-MM-dd HH:mm:ss"
	 * @return 序列化成功，返回json格式的字符串；否则返回null
	 */
	public static String write2JsonStringWithDateFromat(Object obj, String format) {
		String result = null;

		if (obj != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					StringUtils.isEmpty(format) ? DEFAULT_DATE_FORMAT : format);
			try {
				result = mapper.writer(dateFormat).writeValueAsString(obj);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 将JSON格式的字符串转换为期望类型的对象
	 * 
	 * @param jsonString
	 *            JSON格式的字符串
	 * @param clazz
	 *            要转换的目标对象类型；当不存在具体的实体类时，可传入JsonNode.class，
	 *            将JSON字符串转换为一个JsonNode对象
	 * @return 反序列化成功，返回T类型的对象；否则返回null
	 */
	public static <T> T readJson2Entity(String jsonString, Class<T> clazz) {

		T result = null;
		try {
			result = mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 根据TypeReference将JSON格式的字符串转换为期望类型的对象
	 * 
	 * @param jsonString
	 *            JSON格式的字符串
	 * @param valueTypeRef
	 *            要转换的目标对象类型，例如：
	 * 
	 *            <pre>
	 *            TypeReference ref = new TypeReference&lt;List&lt;Integer&gt;&gt;() {
	 *            };
	 *            </pre>
	 * 
	 * @return 反序列化成功，返回T类型的对象；否则返回null
	 */
	public static <T> T readJson2EntityByTypeReference(String jsonString, TypeReference<T> valueTypeRef) {

		T result = null;
		try {
			result = mapper.readValue(jsonString, valueTypeRef);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 将JSON格式的字符串转换为期望类型的对象
	 * 
	 * @param jsonString
	 *            JSON格式的字符串
	 * @param clazz
	 *            要转换的目标对象类型
	 * @return 反序列化成功，返回T类型的对象列表；否则返回null
	 */
	public static <T> List<T> readJson2EntityList(String jsonString, Class<T> clazz) {
		List<T> result = null;

		JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);

		try {
			result = mapper.readValue(jsonString, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 将JSON格式的字符串转换为期望类型的对象集合
	 * 
	 * @param jsonString
	 *            JSON格式的字符串
	 * @param clazz
	 *            要转换的目标对象类型
	 * @return 反序列化成功，返回T类型的对象Set；否则返回null
	 */
	public static <T> Set<T> readJson2EntitySet(String jsonString, Class<T> clazz) {
		Set<T> result = null;

		JavaType javaType = mapper.getTypeFactory().constructCollectionType(Set.class, clazz);

		try {
			result = mapper.readValue(jsonString, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取Jackson的JsonNodeFactory对象,用于创建各种JsonNode对象
	 * 
	 * @return JsonNodeFactory
	 */
	public static JsonNodeFactory getJsonNodeFactory() {
		return mapper.getNodeFactory();
	}

}
