package com.task.flow.dto;

import com.task.flow.domain.CustomExtension;
import com.task.flow.domain.FixedExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ResponseExtensionDto(
        List<FixedExtensionDto> fixedExtensionDtos,
        Set<String> customExtensionDtos
) {

    public static ResponseExtensionDto of(List<FixedExtension> fixedExtensions, List<CustomExtension> customExtensions) {
        List<FixedExtensionDto> fixedExtensionDtos = fixedExtensions.stream()
                .map(f -> FixedExtensionDto.fromEntity(f))
                .toList();
        Set<String> customExtensionDtos = customExtensions.stream()
                .map(c -> c.getName())
                .collect(Collectors.toSet());

        return new ResponseExtensionDto(fixedExtensionDtos, customExtensionDtos);
    }
}
