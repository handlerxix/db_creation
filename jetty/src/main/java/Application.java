import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.jetbrains.annotations.NotNull;

public final class Application {

  public static void main(@NotNull String[] args) throws Exception {
    final var server = new Server();
    final var httpConfig = new HttpConfiguration();
    final var httpConnectionFactory = new HttpConnectionFactory(httpConfig);
    final var connector = new ServerConnector(server, httpConnectionFactory);

    connector.setHost("localhost");
    connector.setPort(3500);
    server.addConnector(connector);
    server.start();
  }
}
