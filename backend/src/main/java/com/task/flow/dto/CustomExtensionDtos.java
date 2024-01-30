package com.task.flow.dto;

import com.task.flow.domain.CustomExtension;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class CustomExtensionDtos extends ExtensionValidator{
    private Set<String> customExtensions = new HashSet<>();

    public CustomExtensionDtos(Set<String> customExtensions) {
        validateCount(customExtensions);
        for(String customExtension: customExtensions){
            validateLength(customExtension);
        }
    }

    public static List<CustomExtension> toEntity(CustomExtensionDtos dto){
        return dto.customExtensions.stream()
                .map(c -> new CustomExtension(c))
                .toList();
    }
}
