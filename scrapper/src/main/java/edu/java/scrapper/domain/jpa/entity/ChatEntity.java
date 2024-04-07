package edu.java.scrapper.domain.jpa.entity;

import edu.java.scrapper.model.Chat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatEntity {

    @Id
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(
        name = "chats_links",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "link_id")
    )
    private Set<LinkEntity> links = new HashSet<>();

    public ChatEntity(Long id) {
        this.id = id;
    }

    public static ChatEntity fromModelChat(Chat chat) {
        return new ChatEntity(
            chat.id()
        );
    }

    public void addLink(LinkEntity linkEntity) {
        links.add(linkEntity);
        linkEntity.getChats().add(this);
    }

    public void removeLink(LinkEntity linkEntity) {
        links.remove(linkEntity);
        linkEntity.getChats().remove(this);
    }
}
