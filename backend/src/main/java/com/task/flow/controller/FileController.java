package com.task.flow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.flow.exception.InvalidFileExtensionException;
import com.task.flow.service.FileService;
import com.task.flow.dto.ExtensionDto;
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
    public ResponseEntity<ExtensionDto> getExtensions() {
        return ResponseEntity.status(HttpStatus.OK).body(fileService.getExtensionDto());
    }

    @PostMapping
    public ResponseEntity<Void> postFile(@RequestPart MultipartFile file,
                                         @RequestPart String extensionDto) throws JsonProcessingException {
        ExtensionDto dto = new ObjectMapper().readValue(extensionDto, ExtensionDto.class);

        try {
            fileService.updateExtensions(dto);
            fileService.validateFileExtension(file);
        } catch (InvalidFileExtensionException | IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
