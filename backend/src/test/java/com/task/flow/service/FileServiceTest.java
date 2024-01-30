package com.task.flow.service;

import com.task.flow.repository.CustomExtensionRepository;
import com.task.flow.repository.FixedExtensionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @InjectMocks private FileService fileService;
    @Mock private FixedExtensionRepository fixedExtensionRepository;
    @Mock private CustomExtensionRepository customExtensionRepository;



    @DisplayName("업로드한 파일의 확장자를 검사한다. - 제한된 확장자 사용")
    @Test
    void upload_restrictedExtensionsFile_Exception() {
        //Given
        String content = "제한된 확장자를 가진 파일 업로드 시 예외를 반환한다.";
        MockMultipartFile file = new MockMultipartFile("file", "test.exe", "multipart/form-data", content.getBytes());
        given(fixedExtensionRepository.existsByNameAndIsChecked(anyString())).willReturn(1);
        //When & Then
        assertThatThrownBy(() -> fileService.fileUpload(file))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("업로드한 파일의 확장자를 검사한다. - 허가된 확장자 사용")
    @Test
    void upload_restrictedExtensionsFile_valid() throws IOException {
        //Given
        String content = "제한된 확장자를 가진 파일 업로드 시 예외를 반환한다.";
        MockMultipartFile file = new MockMultipartFile("file", "test.exe", "multipart/form-data", content.getBytes());
        given(fixedExtensionRepository.existsByNameAndIsChecked(anyString())).willReturn(0);
        //When & Then
        fileService.fileUpload(file);
    }

}