package me.d0by.singledungeon.database;

import me.d0by.singledungeon.profile.Profile;
import org.jetbrains.annotations.NotNull;

public interface DB {

    void connect();

    void disconnect();

    void loadProfile(@NotNull Profile profile);

    void saveProfile(@NotNull Profile profile);

}
