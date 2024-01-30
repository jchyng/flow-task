package com.task.flow.dto;

import java.util.List;

public record ExtensionDto(
        List<FixedExtensionDto> fixedExtensionDtos,
        CustomExtensionDtos customExtensionDtos
) {


}
