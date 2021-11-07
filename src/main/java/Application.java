import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

public final class Application {

  private static @NotNull
  final String CONNECTION = "jdbc:postgresql://127.0.0.1:5432/";
  private static @NotNull
  final String DB_NAME = "local";
  private static @NotNull
  final String USERNAME = "postgres";
  private static @NotNull
  final String PASSWORD = "postgres";

  public static void main(@NotNull String[] args) {
    final var flyway = Flyway
        .configure()
        .dataSource(CONNECTION+DB_NAME, USERNAME, PASSWORD)
        .locations("migrations")
        .load();
    flyway.clean();
    flyway.migrate();
    System.out.println("Migrations completed");
  }
}
