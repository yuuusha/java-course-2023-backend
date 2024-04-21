package edu.java.domain.jpa.entity;

import edu.java.dto.Link;
import edu.java.util.URLCreator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(imports = URLCreator.class)
public interface LinkEntityMapper {

    @Mapping(target = "linkId", source = "entity.id")
    @Mapping(target = "url", expression = "java(URLCreator.createURL(entity.getUrl()))")
    @Mapping(target = "lastUpdate", source = "entity.lastUpdate")
    @Mapping(target = "lastCheck", source = "entity.lastCheck")
    @Mapping(target = "metaInfo", source = "entity.metaInfo")
    Link linkEntityToLinkDTO(LinkEntity entity);
}
