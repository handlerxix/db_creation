import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import commons.DBCreator;
import commons.JDBCCredentials;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ApplicationModule extends AbstractModule {

  @NotNull
  final JDBCCredentials dbCreds = JDBCCredentials.DEFAULT;

  @Provides
  public @NotNull
  Connection connectionDB() throws SQLException {
    return DriverManager.getConnection(dbCreds.getUrl(), dbCreds.getUser(), dbCreds.getPassword());
  }

  @Override
  protected void configure() {
    DBCreator.createDB(dbCreds);
    bind(JDBCCredentials.class).toInstance(dbCreds);
  }
}
