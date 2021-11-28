
import commons.DBCreator;
import commons.JDBCCredentials;
import generated.Tables;
import org.jetbrains.annotations.NotNull;
import org.jooq.impl.DSL;

import java.sql.DriverManager;
import java.sql.SQLException;

public final class Application {

  public static void main(@NotNull String[] args) {
    final var creds = JDBCCredentials.DEFAULT;
    DBCreator.createDB(creds);

    try {
      final var connection = DriverManager.getConnection(creds.getUrl(), creds.getUser(), creds.getPassword());
      final var context = DSL.using(connection);

      context
          .select()
          .from(Tables.COMPANY)
          .where(Tables.COMPANY.ID.le(2))
          .fetch()
          .forEach(System.out::println);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
