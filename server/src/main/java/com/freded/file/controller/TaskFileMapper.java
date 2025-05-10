package com.freded.file.controller;

import com.freded.dtos.TaskFileDTO;
import com.freded.entities.TaskFileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper()
public interface TaskFileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)
    @Mapping(target = "uploadedBy", ignore = true)
    @Mapping(target = "task", ignore = true)
    TaskFileEntity toEntity(TaskFileDTO taskFileDTO);

    // @Mapping(target = "task", ignore = true)
    TaskFileDTO toDTO(TaskFileEntity taskFileEntity);

    List<TaskFileDTO> toDTOList(List<TaskFileEntity> taskFileEntityList);

    List<TaskFileEntity> toEntityList(List<TaskFileDTO> taskFileDTOList);
}
