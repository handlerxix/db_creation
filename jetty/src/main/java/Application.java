import com.google.inject.Guice;
import org.eclipse.jetty.server.Server;
import org.jetbrains.annotations.NotNull;
import server.ServerBuilder;

public final class Application {

  public static void main(@NotNull String[] args) {

    final var injector = Guice.createInjector(
        new ApplicationModule()
    );

    final Server server;
    try {
      server = injector.getInstance(ServerBuilder.class).build();
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
