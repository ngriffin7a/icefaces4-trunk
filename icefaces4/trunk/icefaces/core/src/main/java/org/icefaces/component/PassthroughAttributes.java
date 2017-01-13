package org.icefaces.component;

import java.lang.annotation.*;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface PassthroughAttributes {
    /**
     * passthrough attribute names
     * @return an array with the attribute names
     */
    String[] value() default {};
    /**
     * javadocs for the component class. Goes into the component class.
     * @return javadocs for the component class.
     */
    String javadoc() default "Renders the provided value as an HTML attribute with the same name on the root element of the component.";

    /**
     * tld docs for the component class. Goes into the Tld documents.
     * @return component doc for tld.
     */
    String tlddoc() default "Renders the provided value as an HTML attribute with the same name on the root element of the component.";
}
