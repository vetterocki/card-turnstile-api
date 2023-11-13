package org.example.web.mapper;

import org.example.model.user.User;
import org.example.web.dto.user.UserModifyDto;
import org.example.web.dto.user.UserViewDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
  User toEntity(UserModifyDto userModifyDto);

  UserViewDto toDto(User user);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

  User partialUpdate(UserModifyDto userModifyDto, @MappingTarget User user);
}