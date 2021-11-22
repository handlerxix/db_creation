import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

public final class DBCreator {
  public static void createDB(@NotNull JDBCCredentials creds){
    final var flyway = Flyway
        .configure()
        .dataSource(creds.getUrl(), creds.getUser(), creds.getPassword())
        .locations("migrations")
        .load();
    flyway.clean();
    flyway.migrate();
  }
}
