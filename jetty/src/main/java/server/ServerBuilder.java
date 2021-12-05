package server;

import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.IOException;

public final class ServerBuilder {
  private static final @NotNull
  Integer port = 3500;

  @NotNull
  private final ProductServlet productServlet;

  @Inject
  public ServerBuilder(@NotNull ProductServlet productServlet) {
    this.productServlet = productServlet;
  }

  public @NotNull
  Server build(@NotNull Integer port) throws IOException {
    final var server = new Server();
    final var httpConnectionFactory = new HttpConnectionFactory();
    final var connector = new ServerConnector(server, httpConnectionFactory);

    connector.setHost("localhost");
    connector.setPort(port);
    server.addConnector(connector);

    final var context = new ServletContextHandler(
        ServletContextHandler.NO_SESSIONS
    );
    context.setContextPath("/");
    server.setHandler(context);

    context.setBaseResource(Resource.newResource(resource("/static/info.html")));
    context.addServlet(new ServletHolder("DefaultServlet", DefaultServlet.class), "/*");
    context.addServlet(new ServletHolder(
            "ProductServlet", productServlet),
        "/product"
    );

    final var jdbcConfig = resource("/security_jdbc.conf");
    final var loginService = new JDBCLoginService("login", jdbcConfig);
    final var securityHandler = SecurityHandlerBuilder.build(loginService);
    server.addBean(loginService);
    securityHandler.setHandler(context);
    server.setHandler(securityHandler);

    return server;
  }

  public @NotNull
  Server build() throws IOException {
    return build(port);
  }

  @SuppressWarnings("ConstantConditions")
  public @NotNull
  String resource(@NotNull String path) {
    return this.getClass().getResource(path).toExternalForm();
  }
}
