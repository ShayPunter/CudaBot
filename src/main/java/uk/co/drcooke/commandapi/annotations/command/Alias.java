package uk.co.drcooke.commandapi.annotations.command;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Alias {
    String[] value();
}
