package edu.java.bot.storage;

import com.pengrad.telegrambot.model.User;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InMemoryUserLinksStorageService implements UserLinksStorageService {

    private final Map<User, Set<String>> data = new HashMap<>();

    @Override
    public boolean registerUser(User user) {
        if (!isRegistered(user)) {
            data.put(user, new HashSet<>());
            return true;
        }
        return false;
    }

    @Override
    public boolean isRegistered(User user) {
        return data.containsKey(user);
    }

    @Override
    public boolean hasLink(User user, String url) {
        return data.containsKey(user) && data.get(user).contains(url);
    }

    @Override
    public boolean addLink(User user, String url) {
        if (!hasLink(user, url)) {
            data.get(user).add(url);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeLink(User user, String url) {
        if (hasLink(user, url)) {
            data.get(user).remove(url);
            return true;
        }
        return false;
    }

    @Override
    public Set<String> getLinks(User user) {
        return data.get(user);
    }
}
