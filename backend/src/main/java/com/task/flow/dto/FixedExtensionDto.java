package com.task.flow.dto;


import com.task.flow.domain.FixedExtension;

public record FixedExtensionDto (
        Long id,
        String name,
        boolean isChecked) {

    public static FixedExtensionDto fromEntity(FixedExtension fixedExtension){
        return new FixedExtensionDto(
                fixedExtension.getId(),
                fixedExtension.getName(),
                fixedExtension.isChecked()
        );
    }

    public static FixedExtension toEntity(FixedExtensionDto dto){
        return new FixedExtension(dto.id, dto.name, dto.isChecked());
    }
}