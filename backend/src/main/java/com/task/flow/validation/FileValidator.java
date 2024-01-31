package com.task.flow.validation;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class FileValidator {
    private static final int MAX_FILE_NAME_LENGTH = 260;

    public void validateFileNameLength(String fileName){
        if(fileName.length() > MAX_FILE_NAME_LENGTH){
            throw new IllegalArgumentException("파일 이름은 " + MAX_FILE_NAME_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    public void validateFileNameContainSpecialCharacter(String fileName){
        Pattern allowedPattern = Pattern.compile("^[가-힣a-z0-9_\\s-]+$", Pattern.CASE_INSENSITIVE);

        if (!allowedPattern.matcher(fileName).matches()) {
            throw new IllegalArgumentException("파일 이름은 '-', '_' 이외의 특수 문자를 사용할 수 없습니다.");
        }
    }
}
