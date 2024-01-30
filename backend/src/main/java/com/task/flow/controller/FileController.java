package com.task.flow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.flow.dto.RequestExtensionDto;
import com.task.flow.service.FileService;
import com.task.flow.dto.ResponseExtensionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin(origins = "http://3.37.22.176:8080")
@RequiredArgsConstructor
@RestController
public class FileController {
    private final FileService fileService;

    @GetMapping
    public ResponseEntity<ResponseExtensionDto> getExtensions() {
        return ResponseEntity.status(HttpStatus.OK).body(fileService.getExtensions());
    }

    @PostMapping
    public ResponseEntity<String> postFile(@RequestPart MultipartFile file,
                                         @RequestPart String extensionDto) throws JsonProcessingException {
        RequestExtensionDto dto = new ObjectMapper().readValue(extensionDto, RequestExtensionDto.class);

        try {
            fileService.updateFixedExtensions(dto.fixedExtensionDtos());
            fileService.removeCustomExtensions(dto.removedCustomExtensionDtos());
            fileService.addCustomExtensions(dto.addedCustomExtensionDtos());

            fileService.fileUpload(file);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("파일이 정상적으로 업로드 되었습니다.");
    }
}
