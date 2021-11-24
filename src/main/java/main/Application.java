package main;

import dao.ProductDAO;
import org.jetbrains.annotations.NotNull;

import java.sql.DriverManager;
import java.sql.SQLException;

public final class Application {

  public static void main(@NotNull String[] args) {
    final var creds = JDBCCredentials.DEFAULT;
    DBCreator.createDB(creds);

    try {
      final var connection = DriverManager.getConnection(creds.getUrl(), creds.getUser(), creds.getPassword());
      final var dao = new ProductDAO(connection);
      final var prods = dao.avgProductPeriodPrice("2021-01-01", "2022-01-21");
      prods.forEach(System.out::println);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
