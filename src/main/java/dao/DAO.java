package dao;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

abstract class DAO<T> {

  protected @NotNull
  Connection connection;

  private final @NotNull
  @Getter
  String tableName;

  private @NotNull
  String getSQL;

  private @NotNull
  String getByIdSQL;

  DAO(@NotNull Connection connection, @NotNull String tableName) {
    this.connection = connection;
    this.tableName = tableName;
    this.getSQL = String.format("SELECT * FROM %s", tableName);
    this.getByIdSQL = String.format("%s WHERE ID = ?", getSQL);
  }

  @NotNull
  public T get(@NotNull Integer id) {
    try (var statement = connection.prepareStatement(getByIdSQL)) {
      statement.setInt(1, id);
      final var res = statement.executeQuery();
      final var results = resultList(res);
      if (results.size() != 1) {
        throw new IllegalStateException(String.format(
            "Get by id %d returned %d results, while expecting 1", id, results.size()
        ));
      }
      return results.get(0);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    throw new IllegalStateException(String.format(
        "Record with id %d not found", id
    ));
  }

  @NotNull
  public List<T> all() {
    try {
      final var statement = connection.prepareStatement(getSQL);
      final var res = statement.executeQuery();
      return resultList(res);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    throw new IllegalStateException("Something went wrong, while trying get records");
  }

  @NotNull
  private List<T> resultList(@NotNull ResultSet resultSet) {
    final var list = new ArrayList<T>();
    try {
      while (resultSet.next()) {
        list.add(resultToEntity(resultSet));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  abstract void insert(@NotNull T entity);

  abstract void update(@NotNull T entity);

  abstract void delete(@NotNull T entity);

  @NotNull
  abstract T resultToEntity(@NotNull ResultSet resultSet);

}