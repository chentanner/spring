package com.sss.app.core.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation, in conjunction with @{@link com.fasterxml.jackson.databind.annotation.JsonSerialize },
 * on a Code property to cause the code's corresponding CodeItem object to be
 * injected into the JSON package into which the code is being serialized.
 * <p>
 * For example:
 * <code>
 *
 * @InjectCodeItem(codeFamily = "${familyName}", injectedCodeItemPropertyName = "${jsonPropertyName")
 * @JsonSerialize(using = CodeItemSerializer.class)
 * public String getTypeCode() {
 * return typeCode;
 * }
 * </code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InjectCodeItem {
    public String codeFamily();

    public String injectedCodeItemPropertyName();
}