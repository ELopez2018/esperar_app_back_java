package com.example.esperar_app.mapper;

import com.example.esperar_app.persistence.dto.inputs.notice.CreateNoticeDto;
import com.example.esperar_app.persistence.entity.notice.Notice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NoticeMapper {

    @Mappings({
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "content", target = "content"),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    Notice toNotice(CreateNoticeDto createNoticeDto);

}
