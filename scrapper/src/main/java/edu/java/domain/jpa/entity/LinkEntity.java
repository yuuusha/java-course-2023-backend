package edu.java.domain.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "link")
public class LinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    public Long id;

    private String url;

    @Column(name = "last_update")
    private OffsetDateTime lastUpdate;

    @Column(name = "last_check")
    private OffsetDateTime lastCheck;

    @Column(name = "meta_info")
    private String metaInfo;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "links")
    private Set<TgChatEntity> tgChats;

    public LinkEntity() {
        tgChats = new HashSet<>();
    }

    public LinkEntity(
        String url,
        OffsetDateTime lastUpdate,
        OffsetDateTime lastCheck,
        String metaInfo
    ) {
        this.url = url;
        this.lastUpdate = lastUpdate;
        this.lastCheck = lastCheck;
        this.metaInfo = metaInfo;
        this.tgChats = new HashSet<>();
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkEntity that = (LinkEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }

    @Override public String toString() {
        return "LinkEntity{"
            + "id=" + id
            + ", url='" + url + '\''
            + ", lastUpdate=" + lastUpdate
            + ", lastCheck=" + lastCheck
            + ", metaInfo='" + metaInfo + '\''
            + '}';
    }
}
