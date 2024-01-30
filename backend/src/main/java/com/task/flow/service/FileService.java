package com.task.flow.service;

import com.task.flow.domain.CustomExtension;
import com.task.flow.domain.FixedExtension;
import com.task.flow.dto.CustomExtensionDtos;
import com.task.flow.dto.ExtensionDto;
import com.task.flow.dto.FixedExtensionDto;
import com.task.flow.exception.InvalidFileExtensionException;
import com.task.flow.repository.CustomExtensionRepository;
import com.task.flow.repository.FixedExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class FileService {
    private final FixedExtensionRepository fixedExtensionRepository;
    private final CustomExtensionRepository customExtensionRepository;


    public ExtensionDto getExtensionDto() {
        List<FixedExtension> fixedExtensions = fixedExtensionRepository.findAll();
        List<CustomExtension> customExtensions = customExtensionRepository.findAll();

        List<FixedExtensionDto> fixedExtensionDtos = fixedExtensions.stream()
                .map(f -> FixedExtensionDto.fromEntity(f))
                .toList();
        CustomExtensionDtos customExtensionDtos = new CustomExtensionDtos(
                customExtensions.stream()
                        .map(c -> c.getName())
                        .collect(Collectors.toSet())
        );
        return new ExtensionDto(fixedExtensionDtos, customExtensionDtos);
    }

    public void validateFileExtension(MultipartFile file) {
        String extension = file.getOriginalFilename().split("\\.")[1];

        if (fixedExtensionRepository.existsByNameAndIsChecked(extension) > 0 || customExtensionRepository.existsByName(extension)) {
            throw new InvalidFileExtensionException(extension + "는 제한된 확장자 입니다.");
        }
    }

    public void updateExtensions(ExtensionDto dto) {
        updateFixedExtensions(dto.fixedExtensionDtos());
        updateCustomExtensions(dto.customExtensionDtos());
    }

    private void updateFixedExtensions(List<FixedExtensionDto> dtos) {
        List<FixedExtension> fixedExtensions = fixedExtensionRepository.findAll();

        for (int i = 0; i < dtos.size(); i++) {
            FixedExtensionDto fixedExtensionDto = dtos.get(i);
            FixedExtension fixedExtension = fixedExtensions.get(i);

            fixedExtension.setChecked(fixedExtensionDto.isChecked());
        }
    }

    private void updateCustomExtensions(CustomExtensionDtos dtos) {
        List<CustomExtension> customExtensions = CustomExtensionDtos.toEntity(dtos);

        customExtensionRepository.deleteAll();
        customExtensionRepository.saveAll(customExtensions);
    }
}
