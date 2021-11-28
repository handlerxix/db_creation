package dao;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.sql.Connection;

abstract class Manager<T extends Record> {

  private final @NotNull
  @Getter
  DSLContext context;

  protected Manager(@NotNull Connection connection) {
    this.context = DSL.using(connection);
  }

  @Nullable
  abstract T get(@NotNull Integer id);

  @NotNull
  abstract Result<T> all();

  abstract void insert(@NotNull T rec);

  abstract void update(@NotNull T rec);

  abstract void delete(@NotNull T rec);
}