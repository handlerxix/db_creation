package server;

import dao.ProductManager;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class ProductServlet extends HttpServlet {
  private final @NotNull
  ProductManager manager;

  @Inject
  public ProductServlet(@NotNull ProductManager manager) {
    this.manager = manager;
  }

  @Override
  public void doGet(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp) {
    final var allProducts = manager.allProducts();
    try {
      resp.getOutputStream().println(allProducts.toString());
      resp.setContentType("text/plain");
      resp.setStatus(HttpServletResponse.SC_OK);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void doPost(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp) {
    final var newProduct = req.getParameter("name");
    if (newProduct == null) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
    manager.add(newProduct);
    resp.setStatus(HttpServletResponse.SC_OK);
  }
}
