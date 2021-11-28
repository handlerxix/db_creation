package commons;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class JDBCCredentials {
  @NotNull
  public static JDBCCredentials DEFAULT = new JDBCCredentials(
      "127.0.0.1", "5432", "local_db", "postgres", "postgres"
  );
  private @NotNull
  final String address;
  private @NotNull
  final String port;
  private @NotNull
  final String dbName;
  private @NotNull
  @Getter
  final String user;
  private @NotNull
  @Getter
  final String password;

  public JDBCCredentials(@NotNull String address, @NotNull String port, @NotNull String dbName, @NotNull String user, @NotNull String password) {
    this.address = address;
    this.port = port;
    this.dbName = dbName;
    this.user = user;
    this.password = password;
  }

  @NotNull
  public String getUrl() {
    return String.format("jdbc:postgresql://%s:%s/%s", address, port, dbName);
  }
}
