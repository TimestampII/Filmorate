package ru.yandex.practicum.filmorateApp.storage.user;

import ru.yandex.practicum.filmorateApp.model.User;
import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User add(User user);
    User update(User user);
    void delete(long id);
    Optional<User> findById(long id);
    Collection<User> findAll();
}