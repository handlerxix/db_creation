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
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public final class DAOTests {
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

  @Before
  public void initTest() {
    DBCreator.createDB(creds);
    try {
      final var connection = DriverManager.getConnection(creds.getUrl(), creds.getUser(), creds.getPassword());
      companyDAO = new CompanyDAO(connection);
      productDAO = new ProductDAO(connection);
      wayBillDAO = new WayBillDAO(connection);
      wayBillProductsDAO = new WayBillProductsDAO(connection);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void topCompaniesTest() {
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

    var limit = 1;
    var top = companyDAO.topCompaniesByProductsCount(limit);
    assertEquals(limit, top.size());

    limit = 10;
    top = companyDAO.topCompaniesByProductsCount(limit);
    final var expectedLimit = 2;
    assertEquals(expectedLimit, top.size());

    assertEquals(Integer.valueOf(3), top.get(0).getRight());
    assertEquals(Integer.valueOf(2), top.get(1).getRight());

    assertTrue(top.get(0).getRight() > top.get(1).getRight());
  }

  @Test
  public void companiesProductsInPeriodTest() {
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
        new WayBill(2, Date.valueOf("2021-01-03"), 2),
        new WayBill(3, Date.valueOf("2021-01-04"), 1),
        new WayBill(4, Date.valueOf("2021-01-05"), 2)
    ).forEach(w -> wayBillDAO.insert(w));
    Stream.of(
        new WayBillProducts(1, 1, 1, 10, 1),
        new WayBillProducts(2, 2, 2, 10, 2),
        new WayBillProducts(3, 3, 1, 10, 10),
        new WayBillProducts(4, 3, 2, 10, 3),
        new WayBillProducts(5, 4, 1, 10, 5)
    ).forEach(wp -> wayBillProductsDAO.insert(wp));

    final var result = companyDAO.companiesProductsForRange("2021-01-02", "2021-01-04");
    result.sort((a, b) -> a.getLeft() > b.getLeft() ? 1 : a.getLeft() < b.getLeft() ? -1 :
        a.getMiddle() > b.getMiddle() ? 1 : -1);

    var row = result.get(0);
    assertEquals(Integer.valueOf(1),row.getLeft());
    assertEquals(Integer.valueOf(1),row.getMiddle());
    assertEquals(Integer.valueOf(10), row.getRight());

    row = result.get(1);
    assertEquals(Integer.valueOf(1), row.getLeft());
    assertEquals(Integer.valueOf(2), row.getMiddle());
    assertEquals(Integer.valueOf(3), row.getRight());

    row = result.get(2);
    assertEquals(Integer.valueOf(2), row.getLeft());
    assertEquals(Integer.valueOf(2), row.getMiddle());
    assertEquals(Integer.valueOf(2), row.getRight());
  }

  @Test
  public void companyWithoutProductsInPeriodTest() {
    Stream.of(
        new Company(1, "C1", "C1 Tax", "C1 Check")
    ).forEach(c -> companyDAO.insert(c));
    productDAO.insert(new Product(1, "P1", "P1 Code"));
    Stream.of(
        new WayBill(1, Date.valueOf("2021-01-01"), 1),
        new WayBill(2, Date.valueOf("2021-01-05"), 1)
    ).forEach(w -> wayBillDAO.insert(w));
    Stream.of(
        new WayBillProducts(1, 1, 1, 10, 1),
        new WayBillProducts(2, 2, 1, 10, 2)
    ).forEach(wp -> wayBillProductsDAO.insert(wp));

    var result = companyDAO.companiesProductsForRange("2021-01-02", "2021-01-04");

    var first = result.get(0);
    assertEquals(Integer.valueOf(1), first.getLeft());
    assertEquals(Integer.valueOf(0), first.getMiddle());
    assertEquals(Integer.valueOf(0), first.getRight());

    wayBillDAO.insert(new WayBill(3, Date.valueOf("2021-01-03"), 1));
    result = companyDAO.companiesProductsForRange("2021-01-02", "2021-01-04");

    first = result.get(0);
    assertEquals(Integer.valueOf(1), first.getLeft());
    assertEquals(Integer.valueOf(0), first.getMiddle());
    assertEquals(Integer.valueOf(0), first.getRight());
  }

  @Test
  public void companiesWithConditionTest() {
    Stream.of(
        new Company(1, "C1", "C1 Tax", "C1 Check"),
        new Company(2, "C2", "C2 Tax", "C2 Check")
    ).forEach(c -> companyDAO.insert(c));
    Stream.of(
        new Product(1, "P1", "P1 Code"),
        new Product(2, "P1", "P1 Code")
    ).forEach(p -> productDAO.insert(p));
    Stream.of(
        new WayBill(1, Date.valueOf("2021-01-01"), 1),
        new WayBill(2, Date.valueOf("2021-01-05"), 1),
        new WayBill(3, Date.valueOf("2021-01-05"), 2),
        new WayBill(4, Date.valueOf("2021-01-05"), 1)
    ).forEach(w -> wayBillDAO.insert(w));
    Stream.of(
        new WayBillProducts(1, 1, 1, 10, 10),
        new WayBillProducts(2, 3, 1, 10, 9),
        new WayBillProducts(3, 3, 2, 10, 4)
    ).forEach(wp -> wayBillProductsDAO.insert(wp));

    var conditionMap = Map.of(1, 10, 2, 5);

    var result = companyDAO.companiesWithProductsCountCondition(conditionMap);
    assertEquals(0, result.size());

    wayBillProductsDAO.insert(new WayBillProducts(4, 2, 2, 3, 3));
    wayBillProductsDAO.insert(new WayBillProducts(5, 4, 2, 3, 2));

    result = companyDAO.companiesWithProductsCountCondition(conditionMap);
    assertEquals(1, result.size());
    assertEquals(Integer.valueOf(1), result.get(0).getLeft());

    result = companyDAO.companiesWithProductsCountCondition(new HashMap<>());
    assertEquals(0, result.size());
  }

  @Test
  public void productByDaysTest() {
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
        new WayBill(2, Date.valueOf("2021-01-03"), 1),
        new WayBill(3, Date.valueOf("2021-01-03"), 2),
        new WayBill(4, Date.valueOf("2021-01-04"), 2),
        new WayBill(5, Date.valueOf("2021-01-05"), 2)
    ).forEach(w -> wayBillDAO.insert(w));
    Stream.of(
        new WayBillProducts(1, 2, 1, 10, 10),
        new WayBillProducts(2, 3, 1, 10, 3),
        new WayBillProducts(3, 4, 1, 10, 4),
        new WayBillProducts(4, 4, 2, 10, 71)
    ).forEach(wp -> wayBillProductsDAO.insert(wp));

    var result = productDAO.productByDate("2021-01-02", "2021-01-04");
    result.sort((a, b) -> a.getLeft() > b.getLeft() ? 1 : a.getLeft() < b.getLeft() ? -1 :
        StringUtils.compare(a.getMiddle().toString(), b.getMiddle().toString())
    );
    assertEquals(3, result.size());

    var row = result.get(0);
    assertEquals(Integer.valueOf(1), row.getLeft());
    assertEquals(Date.valueOf("2021-01-03"), row.getMiddle());
    assertEquals(Integer.valueOf(13), row.getRight());

    row = result.get(1);
    assertEquals(Integer.valueOf(1), row.getLeft());
    assertEquals(Date.valueOf("2021-01-04"), row.getMiddle());
    assertEquals(Integer.valueOf(4), row.getRight());

    row = result.get(2);
    assertEquals(Integer.valueOf(2), row.getLeft());
    assertEquals(Date.valueOf("2021-01-04"), row.getMiddle());
    assertEquals(Integer.valueOf(71), row.getRight());
  }

  @Test
  public void avgPriceForPeriodTest() {
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
        new WayBill(2, Date.valueOf("2021-01-03"), 1),
        new WayBill(3, Date.valueOf("2021-01-03"), 2),
        new WayBill(4, Date.valueOf("2021-01-04"), 2),
        new WayBill(5, Date.valueOf("2021-01-05"), 2)
    ).forEach(w -> wayBillDAO.insert(w));
    Stream.of(
        new WayBillProducts(1, 1, 1, 10, 10),
        new WayBillProducts(2, 5, 1, 10, 10),
        new WayBillProducts(3, 2, 2, 3, 2),
        new WayBillProducts(4, 3, 2, 2, 1),
        new WayBillProducts(5, 5, 2, 10000, 1)
    ).forEach(wp -> wayBillProductsDAO.insert(wp));

    var result = productDAO.avgProductPeriodPrice("2021-01-02", "2021-01-04");
    result.sort((a, b) -> a.getLeft() > b.getLeft() ? 1 : -1);
    assertEquals(1, result.size());

    assertEquals(Integer.valueOf(2), result.get(0).getLeft());
    assertEquals(1.66f, result.get(0).getRight(), 0.02f);
  }
}
