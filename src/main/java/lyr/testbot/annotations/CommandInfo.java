package lyr.testbot.annotations;

import lyr.testbot.enums.CommandType;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CommandInfo {
    String name() default "\0";
    String[] aliases() default {};
    CommandType type() default CommandType.OWNER;
    String desc() default "";
    String usage() default "\0";
    int minArgs() default 0;
}
