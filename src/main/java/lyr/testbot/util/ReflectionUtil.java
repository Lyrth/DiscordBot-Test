package lyr.testbot.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

    public static List<Method> getAnnotatedMethods(final Class<?> clazz, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<>();
        for (final Method method : clazz.getDeclaredMethods())
            if (method.isAnnotationPresent(annotation))
                methods.add(method);
        return methods;
    }

    public static List<Method> getDeclaredMethodsByName(final Class<?> clazz, final String name) {
        final List<Method> methods = new ArrayList<>();
        for (final Method method : clazz.getDeclaredMethods())
            if (method.getName().equals(name))
                methods.add(method);
        return methods;
    }

}
