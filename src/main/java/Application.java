import org.jetbrains.annotations.NotNull;

public final class Application {

  public static void main(@NotNull String[] args) {
    final var creds = JDBCCredentials.DEFAULT;
    DBCreator.createDB(creds);
  }
}
