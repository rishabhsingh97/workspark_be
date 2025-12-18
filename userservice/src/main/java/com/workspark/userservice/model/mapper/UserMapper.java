package com.workspark.userservice.model.mapper;

import com.workspark.userservice.model.dto.UserDto;
import com.workspark.userservice.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);
    User toEntity(UserDto userDto);

}