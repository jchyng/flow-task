package com.task.flow.dto;

import com.task.flow.validation.ExtensionValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ValidationTest {
    private final ExtensionValidator validator = new ExtensionValidator();

    @DisplayName("확장자의 이름이 20자 보다 크면 예외를 발생시킨다.")
    @Test
    void validate_length_over() {
        assertThatThrownBy(() -> validator.validateLength("4444_4444_4444_4444_1"))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("확장자의 이름이 빈 문자열이라면 예외를 발생시킨다.")
    @Test
    void validate_length_under() {
        assertThatThrownBy(() -> validator.validateLength(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("확장자 이름의 길이가 1~20 사이라면 정상")
    @Test
    void validate_length_correct(){
        validator.validateLength("1234");
    }

    @DisplayName("커스텀 확장자의 요소의 개수가 200개 이상이면 예외를 발생시킨다.")
    @Test
    void validate_count_over() {
        //Given
        Set<String> customExtensions = new HashSet<>();
        for(int i=0; i<201; i++){
            customExtensions.add("a" + i);
        }
        //When & Then
        assertThatThrownBy(() -> validator.validateCount(customExtensions))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("커스텀 확장자의 요소의 개수가 200개 이하라면 정상")
    @Test
    void validate_count_correct() {
        //Given
        Set<String> customExtensions = new HashSet<>();
        for(int i=0; i<200; i++){
            customExtensions.add("a" + i);
        }
        //When & Then
        validator.validateCount(customExtensions);
    }
}
