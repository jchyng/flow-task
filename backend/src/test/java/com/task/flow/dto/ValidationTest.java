package com.task.flow.dto;

import com.task.flow.validation.ExtensionValidator;
import com.task.flow.validation.FileValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ValidationTest {
    private final ExtensionValidator extensionValidator = new ExtensionValidator();
    private final FileValidator fileValidator = new FileValidator();

    @DisplayName("확장자의 이름이 20자 보다 크면 예외를 발생시킨다.")
    @Test
    void validateExtension_length_over() {
        assertThatThrownBy(() -> extensionValidator.validateLength("4444_4444_4444_4444_1"))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("확장자의 이름이 빈 문자열이라면 예외를 발생시킨다.")
    @Test
    void validateExtension_length_under() {
        assertThatThrownBy(() -> extensionValidator.validateLength(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("확장자 이름의 길이가 1~20 사이라면 정상")
    @Test
    void validateExtension_length_correct(){
        extensionValidator.validateLength("1234");
    }

    @DisplayName("커스텀 확장자의 요소의 개수가 200개 이상이면 예외를 발생시킨다.")
    @Test
    void validateExtension_count_over() {
        //Given
        Set<String> customExtensions = new HashSet<>();
        for(int i=0; i<201; i++){
            customExtensions.add("a" + i);
        }
        //When & Then
        assertThatThrownBy(() -> extensionValidator.validateCount(customExtensions))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("커스텀 확장자의 요소의 개수가 200개 이하라면 정상")
    @Test
    void validateExtension_count_correct() {
        //Given
        Set<String> customExtensions = new HashSet<>();
        for(int i=0; i<200; i++){
            customExtensions.add("a" + i);
        }
        //When & Then
        extensionValidator.validateCount(customExtensions);
    }

    @DisplayName("파일 이름의 길이가 260 이상이면 예외를 발생시킨다.")
    @Test
    void validateFile_length_over() {
        //Given
        StringBuilder fileName = new StringBuilder();
        for(int i=0; i<261; i++){
            fileName.append("a");
        }
        //When & Then
        assertThatThrownBy(() -> fileValidator.validateFileNameLength(fileName.toString()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("파일 이름의 길이가 260이하면 성공")
    @Test
    void validateFile_length_correct() {
        //Given
        StringBuilder fileName = new StringBuilder();
        for(int i=0; i<260; i++){
            fileName.append("a");
        }
        //When & Then
        fileValidator.validateFileNameLength(fileName.toString());
    }

    @DisplayName("파일 이름에 특수문자를 포함하면 예외를 발생시킨다.")
    @Test
    void validateFile_contain_specialCharacter() {
        //Given
        String fileName = "!@#";
        //When & Then
        assertThatThrownBy(() -> fileValidator.validateFileNameContainSpecialCharacter(fileName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("파일 이름에 특수문자를 포함하면 예외를 발생시킨다.")
    @Test
    void validateFile_NoContain_specialCharacter() {
        //Given
        String fileName = "가";
        //When & Then
        fileValidator.validateFileNameContainSpecialCharacter(fileName);
    }
}
