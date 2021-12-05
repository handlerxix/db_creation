package server;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public final class SecurityHandlerBuilder {
  private static final @NotNull
  String ROLE_ADMIN = "admin";
  private static final @NotNull
  String ROLE_USER = "user";

  public static @NotNull
  ConstraintSecurityHandler build(@NotNull LoginService loginService) {
    final var handler = new ConstraintSecurityHandler();
    handler.setLoginService(loginService);

    final var constraintMappings = new ArrayList<ConstraintMapping>();
    handler.setAuthenticator(new BasicAuthenticator());
    handler.setDenyUncoveredHttpMethods(true);

    constraintMappings.addAll(constraintMapping(
        buildConstraint(ROLE_USER, ROLE_ADMIN),
        Collections.singletonList("/product"),
        "GET"
    ));

    constraintMappings.addAll(constraintMapping(
        buildConstraint(ROLE_ADMIN),
        Collections.singletonList("/product"),
        "POST"
    ));

    handler.setConstraintMappings(constraintMappings);

    return handler;
  }

  private static @NotNull
  Collection<ConstraintMapping> constraintMapping(
      @NotNull Constraint constraint,
      @NotNull Collection<String> paths,
      @NotNull String method
  ) {
    return paths.stream().map(
        p -> {
          final var constraintMap = new ConstraintMapping();
          constraintMap.setConstraint(constraint);
          constraintMap.setMethod(method);
          constraintMap.setPathSpec(p);
          return constraintMap;
        }
    ).collect(Collectors.toList());
  }

  private static @NotNull
  Constraint buildConstraint(@NotNull String... roles) {
    final var constraint = new Constraint();
    constraint.setName(Constraint.__BASIC_AUTH);
    constraint.setRoles(roles);
    constraint.setAuthenticate(true);
    return constraint;
  }
}
