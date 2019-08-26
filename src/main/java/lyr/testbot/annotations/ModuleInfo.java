package lyr.testbot.annotations;

import lyr.testbot.templates.Command;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ModuleInfo {
    String name() default "\0";
    String[] aliases() default {};  // used for module command
    String desc() default "";
    Class<Command>[] commands() default {};
    boolean essential() default false;
}
