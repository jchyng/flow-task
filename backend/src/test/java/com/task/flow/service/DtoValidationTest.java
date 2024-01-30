package com.task.flow.service;

import com.task.flow.dto.CustomExtensionDtos;
import com.task.flow.dto.FixedExtensionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DtoValidationTest {

    @DisplayName("확장자의 이름이 20자 보다 크면 예외를 발생시킨다.")
    @Test
    void validate_length_over() {
        assertThatThrownBy(() -> new FixedExtensionDto(1L, "4444_4444_4444_4444_1", false))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("확장자의 이름이 빈 문자열이라면 예외를 발생시킨다.")
    @Test
    void validate_length_under() {
        assertThatThrownBy(() -> new FixedExtensionDto(1L, "", false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("확장자 이름의 길이가 1~20 사이라면 정상")
    @Test
    void validate_length_correct(){
        FixedExtensionDto fixedExtensionDto = new FixedExtensionDto(1L, "123", false);
        assertThat(fixedExtensionDto.getName().length()).isBetween(1, 20);
    }

    @DisplayName("커스텀 확장자의 요소의 개수가 200개 이상이면 예외를 발생시킨다.")
    @Test
    void validate_count_over() {
        //Given
        Set<String> set = new HashSet<>();
        for(int i=0; i<201; i++){
            set.add("a" + i);
        }
        //When & Then
        assertThatThrownBy(() -> new CustomExtensionDtos(set))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("커스텀 확장자의 요소의 개수가 200개 이하라면 정상")
    @Test
    void validate_count_correct() {
        //Given
        Set<String> set = new HashSet<>();
        for(int i=0; i<200; i++){
            set.add("a" + i);
        }
        //When
        CustomExtensionDtos customExtensionDtos = new CustomExtensionDtos(set);
        //Then
        assertThat(customExtensionDtos.getCustomExtensions().size()).isLessThanOrEqualTo(200);
    }
}
