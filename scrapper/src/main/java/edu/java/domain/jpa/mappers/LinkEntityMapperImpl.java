package edu.java.domain.jpa.mappers;

import edu.java.domain.jpa.entity.LinkEntity;
import edu.java.domain.jpa.entity.LinkEntityMapper;
import edu.java.dto.Link;
import edu.java.util.URLCreator;
import org.springframework.stereotype.Component;

@Component
public class LinkEntityMapperImpl implements LinkEntityMapper {

    @Override
    public Link linkEntityToLinkDTO(LinkEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Link(entity.getId(),
            URLCreator.createURL(entity.getUrl()),
            entity.getLastUpdate(),
            entity.getLastCheck(),
            entity.getMetaInfo()
        );
    }
}
