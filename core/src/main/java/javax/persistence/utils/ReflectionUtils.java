package javax.persistence.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionUtils {

	private ReflectionUtils() {
	}

	public static final Class<?> getParameterType(Object instance, int paramIndex) {
		Type type = instance.getClass().getGenericSuperclass();
		ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
		return Class.class.cast(parameterizedType.getActualTypeArguments()[paramIndex]);
	}

}