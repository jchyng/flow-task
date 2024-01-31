package com.task.flow.service;

import com.task.flow.domain.CustomExtension;
import com.task.flow.domain.FixedExtension;
import com.task.flow.dto.ResponseExtensionDto;
import com.task.flow.validation.ExtensionValidator;
import com.task.flow.dto.FixedExtensionDto;
import com.task.flow.repository.CustomExtensionRepository;
import com.task.flow.repository.FixedExtensionRepository;
import com.task.flow.validation.FileValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Transactional
@RequiredArgsConstructor
@Service
public class FileService {
    private final FixedExtensionRepository fixedExtensionRepository;
    private final CustomExtensionRepository customExtensionRepository;
    private final ExtensionValidator extensionValidator;
    private final FileValidator fileValidator;


    public ResponseExtensionDto getExtensions() {
        List<FixedExtension> fixedExtensions = fixedExtensionRepository.findAll();
        List<CustomExtension> customExtensions = customExtensionRepository.findAll();

        return ResponseExtensionDto.of(fixedExtensions, customExtensions);
    }

    public void fileUpload(MultipartFile file) {
        String[] fileInfo = file.getOriginalFilename().split("\\.");
        String fileName = fileInfo[0];
        String extension = fileInfo[1];

        fileValidator.validateFileNameLength(fileName);
        fileValidator.validateFileNameContainSpecialCharacter(fileName);
        validateRestrictedExtension(extension);

        /* File 저장 로직 */
    }

    public void updateFixedExtensions(List<FixedExtensionDto> dtos) {
        for (FixedExtensionDto dto : dtos) {
            extensionValidator.validateLength(dto.name());
            fixedExtensionRepository.updateIsCheckedById(dto.isChecked(), dto.id());
        }
    }

    public void addCustomExtensions(Set<String> dtos) {
        extensionValidator.validateCount(dtos);

        List<CustomExtension> customExtensions = dtos.stream()
                .map(c -> {
                    extensionValidator.validateLength(c);
                    return new CustomExtension(c.toLowerCase());
                })
                .toList();
        customExtensionRepository.saveAll(customExtensions);
    }

    public void removeCustomExtensions(Set<String> dtos) {
        if(dtos.size() > 0){
            customExtensionRepository.deleteByNameIn(dtos);
        }
    }

    private void validateRestrictedExtension(String extension){
        if (fixedExtensionRepository.existsByNameAndIsChecked(extension) > 0
                || customExtensionRepository.existsByName(extension)) {
            throw new IllegalArgumentException(extension + "는 제한된 확장자 입니다.");
        }
    }
}
