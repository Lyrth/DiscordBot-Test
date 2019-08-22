package lyr.testbot.annotations;

import lyr.testbot.enums.CommandType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CommandInfo {
    String name();
    String[] aliases() default {};
    CommandType type() default CommandType.OWNER;
    String desc() default "";
    String usage() default "";
    int minArgs() default 0;
}
