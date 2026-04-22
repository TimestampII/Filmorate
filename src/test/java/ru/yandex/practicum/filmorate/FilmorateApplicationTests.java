package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, FilmDbStorage.class})
class FilmorateApplicationTests {

    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testAddAndFindUserById() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testlogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User saved = userStorage.add(user);

        Optional<User> found = userStorage.findById(saved.getId());
        assertThat(found)
                .isPresent()
                .hasValueSatisfying(u -> {
                    assertThat(u.getEmail()).isEqualTo("test@mail.ru");
                    assertThat(u.getLogin()).isEqualTo("testlogin");
                });
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setEmail("update@mail.ru");
        user.setLogin("updatelogin");
        user.setName("Before");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User saved = userStorage.add(user);
        saved.setName("After");
        userStorage.update(saved);

        Optional<User> found = userStorage.findById(saved.getId());
        assertThat(found).isPresent()
                .hasValueSatisfying(u -> assertThat(u.getName()).isEqualTo("After"));
    }

    @Test
    public void testFindAllUsers() {
        User u1 = new User();
        u1.setEmail("a@mail.ru");
        u1.setLogin("aaa");
        u1.setName("A");
        u1.setBirthday(LocalDate.of(1990, 1, 1));
        User u2 = new User();
        u2.setEmail("b@mail.ru");
        u2.setLogin("bbb");
        u2.setName("B");
        u2.setBirthday(LocalDate.of(1991, 2, 2));
        userStorage.add(u1);
        userStorage.add(u2);

        Collection<User> all = userStorage.findAll();
        assertThat(all.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testAddFriendAndGetFriends() {
        User u1 = new User();
        u1.setEmail("f1@mail.ru");
        u1.setLogin("fr1");
        u1.setName("Fr1");
        u1.setBirthday(LocalDate.of(1990, 1, 1));
        User u2 = new User();
        u2.setEmail("f2@mail.ru");
        u2.setLogin("fr2");
        u2.setName("Fr2");
        u2.setBirthday(LocalDate.of(1991, 2, 2));
        User saved1 = userStorage.add(u1);
        User saved2 = userStorage.add(u2);

        userStorage.addFriend(saved1.getId(), saved2.getId());
        Collection<User> friends = userStorage.getFriends(saved1.getId());
        assertThat(friends.size()).isEqualTo(1);
    }

    @Test
    public void testRemoveFriend() {
        User u1 = new User();
        u1.setEmail("rf1@mail.ru");
        u1.setLogin("rfl1");
        u1.setName("Rf1");
        u1.setBirthday(LocalDate.of(1990, 1, 1));
        User u2 = new User();
        u2.setEmail("rf2@mail.ru");
        u2.setLogin("rfl2");
        u2.setName("Rf2");
        u2.setBirthday(LocalDate.of(1991, 2, 2));
        User saved1 = userStorage.add(u1);
        User saved2 = userStorage.add(u2);

        userStorage.addFriend(saved1.getId(), saved2.getId());
        userStorage.removeFriend(saved1.getId(), saved2.getId());
        Collection<User> friends = userStorage.getFriends(saved1.getId());
        assertThat(friends.size()).isEqualTo(0);
    }

    @Test
    public void testGetCommonFriends() {
        User u1 = new User();
        u1.setEmail("c1@mail.ru");
        u1.setLogin("cl1");
        u1.setName("C1");
        u1.setBirthday(LocalDate.of(1990, 1, 1));
        User u2 = new User();
        u2.setEmail("c2@mail.ru");
        u2.setLogin("cl2");
        u2.setName("C2");
        u2.setBirthday(LocalDate.of(1991, 2, 2));
        User u3 = new User();
        u3.setEmail("c3@mail.ru");
        u3.setLogin("cl3");
        u3.setName("C3");
        u3.setBirthday(LocalDate.of(1992, 3, 3));
        User s1 = userStorage.add(u1);
        User s2 = userStorage.add(u2);
        User s3 = userStorage.add(u3);

        userStorage.addFriend(s1.getId(), s3.getId());
        userStorage.addFriend(s2.getId(), s3.getId());

        Collection<User> common = userStorage.getCommonFriends(s1.getId(), s2.getId());
        assertThat(common.size()).isEqualTo(1);
    }

    @Test
    public void testAddAndFindFilmById() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G"));
        Film saved = filmStorage.add(film);

        Optional<Film> found = filmStorage.findById(saved.getId());
        assertThat(found)
                .isPresent()
                .hasValueSatisfying(f -> assertThat(f.getName()).isEqualTo("Test Film"));
    }

    @Test
    public void testGetPopularFilms() {
        Film film = new Film();
        film.setName("Popular Film");
        film.setDescription("Popular");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G"));
        filmStorage.add(film);

        Collection<Film> popular = filmStorage.getPopular(10);
        assertThat(popular.size()).isGreaterThanOrEqualTo(1);
    }
}