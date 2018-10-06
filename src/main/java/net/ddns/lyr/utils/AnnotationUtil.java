package net.ddns.lyr.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnotationUtil {

    public static List<Method> getAnnotatedMethods(final Class<?> clazz, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<>();
        for (final Method method : clazz.getDeclaredMethods())
            if (method.isAnnotationPresent(annotation))
                methods.add(method);
        return methods;
    }

}
