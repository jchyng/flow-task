package com.task.flow.dto;


import java.util.List;
import java.util.Set;


public record RequestExtensionDto(
        List<FixedExtensionDto> fixedExtensionDtos,
        Set<String> addedCustomExtensionDtos,
        Set<String> removedCustomExtensionDtos
) {

}