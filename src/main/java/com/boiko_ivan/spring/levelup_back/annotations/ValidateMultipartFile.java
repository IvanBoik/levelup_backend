package com.boiko_ivan.spring.levelup_back.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateMultipartFile {
    ValidFileTypes value();

    enum ValidFileTypes {
        IMAGE("image"),
        VIDEO("video");

        public final String value;

        ValidFileTypes(String value) {
            this.value = value;
        }
    }
}
