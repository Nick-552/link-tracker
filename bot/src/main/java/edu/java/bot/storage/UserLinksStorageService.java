package edu.java.bot.storage;

import com.pengrad.telegrambot.model.User;
import java.util.Set;

public interface UserLinksStorageService {

    boolean registerUser(User user);

    boolean isRegistered(User user);

    boolean hasLink(User user, String url);

    boolean addLink(User user, String url);

    boolean removeLink(User user, String url);

    Set<String> getLinks(User user);
}
