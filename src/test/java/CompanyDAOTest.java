import dao.CompanyDAO;
import dao.ProductDAO;
import dao.WayBillDAO;
import dao.WayBillProductsDAO;
import entity.Company;
import entity.Product;
import entity.WayBill;
import entity.WayBillProducts;
import main.DBCreator;
import main.JDBCCredentials;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;

public final class CompanyDAOTest {
  @NotNull
  public final JDBCCredentials creds = JDBCCredentials.DEFAULT;

  @NotNull
  public CompanyDAO companyDAO;

  @NotNull
  public ProductDAO productDAO;

  @NotNull
  public WayBillDAO wayBillDAO;

  @NotNull
  public WayBillProductsDAO wayBillProductsDAO;

  @BeforeClass
  public void init() {
    try {
      final var connection = DriverManager.getConnection(creds.getUrl(), creds.getUser(), creds.getPassword());
      companyDAO = new CompanyDAO(connection);
      productDAO = new ProductDAO(connection);
      wayBillDAO = new WayBillDAO(connection);
      wayBillProductsDAO = new WayBillProductsDAO(connection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Before
  public void initTest() {
    DBCreator.createDB(creds);
  }

  @Test
  public void topCompaniesLimitTest() {
    Stream.of(
        new Company(1, "C1", "C1 Tax", "C1 Check"),
        new Company(2, "C2", "C2 Tax", "C2 Check")
    ).forEach(c -> companyDAO.insert(c));
    Stream.of(
        new Product(1, "P1", "P1 Code"),
        new Product(2, "P2", "P2 Code")
    ).forEach(p -> productDAO.insert(p));
    Stream.of(
        new WayBill(1, Date.valueOf("2021-01-01"), 1),
        new WayBill(2, Date.valueOf("2021-01-02"), 2),
        new WayBill(3, Date.valueOf("2021-01-03"), 1)
    ).forEach(w -> wayBillDAO.insert(w));
    Stream.of(
        new WayBillProducts(1, 1, 1, 10, 1),
        new WayBillProducts(2, 2, 2, 10, 2),
        new WayBillProducts(3, 3, 1, 10, 1),
        new WayBillProducts(4, 3, 2, 10, 1)
    ).forEach(wp -> wayBillProductsDAO.insert(wp));

    final var limit1 = 1;
    final var top1 = companyDAO.topCompaniesByProductsCount(limit1);
    assertEquals(top1.size(), limit1);
  }
}
