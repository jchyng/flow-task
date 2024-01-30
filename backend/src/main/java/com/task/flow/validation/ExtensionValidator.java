package com.task.flow.validation;

import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
public class ExtensionValidator {
    private static final int MAX_LENGTH = 20;
    private static final int MIN_LENGTH = 1;
    private static final int MAX_COUNT = 200;


    public void validateLength(String name) {
        if (name.length() > MAX_LENGTH || name.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("확장자 이름은 " + MIN_LENGTH + "~" + MAX_LENGTH + "자까지만 허용합니다.");
        }
    }

    public <T> void validateCount(Collection<T> customExtensions) {
        if (customExtensions.size() > MAX_COUNT) {
            throw new IllegalArgumentException("커스텀 확장자는 최대 " + MAX_COUNT + "자까지만 등록 할 수 있습니다.");
        }
    }
}
