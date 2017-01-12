package org.icefaces.ace.component;

import java.lang.annotation.*;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface PassthroughAttributes {
    String[] value() default {};
}
