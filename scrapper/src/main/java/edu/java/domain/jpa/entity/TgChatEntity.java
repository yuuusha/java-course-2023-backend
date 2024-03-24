package edu.java.domain.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "chat")
public class TgChatEntity {

    @Id
    @Column(name = "chat_id")
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "chat_link",
        joinColumns = {@JoinColumn(name = "chat_id")},
        inverseJoinColumns = {@JoinColumn(name = "link_id")}
    )
    private Set<LinkEntity> links;

    public TgChatEntity() {
        this.links = new HashSet<>();
    }

    public TgChatEntity(Long id) {
        this.id = id;
        this.links = new HashSet<>();
    }

    public void addLink(LinkEntity link) {
        links.add(link);
        link.getTgChats().add(this);
    }

    public void removeLink(LinkEntity link) {
        links.removeIf(linkEntity -> link.getId().equals(linkEntity.getId()));
        link.getTgChats().remove(this);
    }
}
