package com.freded.file.server.controller;

import com.freded.file.client.dto.TaskFileDTO;
import com.freded.file.server.entity.TaskFileEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper()
public interface TaskFileMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "uploadedBy", ignore = true)
  @Mapping(target = "taskId", ignore = true)
  TaskFileEntity toEntity(TaskFileDTO taskFileDTO);

  TaskFileDTO toDTO(TaskFileEntity taskFileEntity);

  List<TaskFileDTO> toDTOList(List<TaskFileEntity> taskFileEntityList);

  List<TaskFileEntity> toEntityList(List<TaskFileDTO> taskFileDTOList);
}
