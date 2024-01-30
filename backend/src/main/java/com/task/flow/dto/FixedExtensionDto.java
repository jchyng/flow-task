package com.task.flow.dto;


import com.task.flow.domain.FixedExtension;
import lombok.Getter;

@Getter
public class FixedExtensionDto extends ExtensionValidator{
    private Long id;
    private String name;
    private boolean isChecked;


    public FixedExtensionDto(Long id, String name, boolean isChecked) {
        validateLength(name);

        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
    }

    public static FixedExtensionDto fromEntity(FixedExtension fixedExtension){
        return new FixedExtensionDto(
                fixedExtension.getId(),
                fixedExtension.getName(),
                fixedExtension.isChecked()
        );
    }
}