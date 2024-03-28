package edu.java.scrapper.domain.jpa.entity;

import edu.java.scrapper.model.Link;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "links")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @Column(name = "last_updated_at", nullable = false)
    private OffsetDateTime lastUpdatedAt;

    @Column(name = "last_check_at", nullable = false)
    private OffsetDateTime lastCheckedAt;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "links")
    private Set<ChatEntity> chats = new HashSet<>();

    public LinkEntity(
        String url,
        OffsetDateTime lastUpdatedAt,
        OffsetDateTime lastCheckedAt
    ) {
        this.url = url;
        this.lastUpdatedAt = lastUpdatedAt;
        this.lastCheckedAt = lastCheckedAt;
    }

    public Link toModelLink() {
        return new Link(
            id,
            URI.create(url),
            lastUpdatedAt,
            lastCheckedAt
        );
    }

    public static LinkEntity fromModelLink(Link link) {
        return new LinkEntity(
            link.url().toString(),
            link.lastUpdatedAt(),
            link.lastCheckAt()
        );
    }
}
