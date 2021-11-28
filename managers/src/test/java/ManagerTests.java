import commons.DBCreator;
import commons.JDBCCredentials;
import dao.CompanyManager;
import dao.ProductManager;
import dao.WayBillManager;
import dao.WayBillProductsManager;
import generated.Tables;
import generated.tables.Company;
import generated.tables.Waybill;
import generated.tables.WaybillProducts;
import generated.tables.records.CompanyRecord;
import generated.tables.records.ProductRecord;
import generated.tables.records.WaybillProductsRecord;
import generated.tables.records.WaybillRecord;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

@SuppressWarnings("ConstantConditions")
public final class ManagerTests {
  @NotNull
  public CompanyManager companyDAO;
  @NotNull
  public ProductManager productDAO;
  @NotNull
  public WayBillManager wayBillDAO;

  @NotNull
  public final JDBCCredentials creds = JDBCCredentials.DEFAULT;
  @NotNull
  public WayBillProductsManager wayBillProductsDAO;
  private final @NotNull
  Company COMPANY = Tables.COMPANY;
  private final @NotNull
  Waybill WAYBILL = Tables.WAYBILL;
  private final @NotNull
  WaybillProducts WAYBILL_PRODUCTS = Tables.WAYBILL_PRODUCTS;

  @Before
  public void initTest() {
    DBCreator.createDB(creds);
    try {
      final var connection = DriverManager.getConnection(creds.getUrl(), creds.getUser(), creds.getPassword());
      companyDAO = new CompanyManager(connection);
      productDAO = new ProductManager(connection);
      wayBillDAO = new WayBillManager(connection);
      wayBillProductsDAO = new WayBillProductsManager(connection);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void topCompaniesTest() {
    Stream.of(
        new CompanyRecord(1, "C1", "C1 Tax", "C1 Check"),
        new CompanyRecord(2, "C2", "C2 Tax", "C2 Check")
    ).forEach(c -> companyDAO.insert(c));
    Stream.of(
        new ProductRecord(1, "P1", "P1 Code"),
        new ProductRecord(2, "P2", "P2 Code")
    ).forEach(p -> productDAO.insert(p));
    Stream.of(
        new WaybillRecord(1, OffsetDateTime.parse("2021-01-01T00:00:00+03:00"), 1),
        new WaybillRecord(2, OffsetDateTime.parse("2021-01-02T00:00:00+03:00"), 2),
        new WaybillRecord(3, OffsetDateTime.parse("2021-01-03T00:00:00+03:00"), 1)
    ).forEach(w -> wayBillDAO.insert(w));
    Stream.of(
        new WaybillProductsRecord(1, 1, 1, 10, 1),
        new WaybillProductsRecord(2, 2, 2, 10, 2),
        new WaybillProductsRecord(3, 3, 1, 10, 1),
        new WaybillProductsRecord(4, 3, 2, 10, 1)
    ).forEach(wp -> wayBillProductsDAO.insert(wp));

    var limit = 1;
    var top = companyDAO.topCompaniesByProductsCount(limit);
    assertEquals(limit, top.size());

    limit = 10;
    top = companyDAO.topCompaniesByProductsCount(limit);
    final var expectedLimit = 2;
    assertEquals(expectedLimit, top.size());

    assertEquals(BigDecimal.valueOf(3), top.get(0).get("PRODUCTS_COUNTS"));
    assertEquals(BigDecimal.valueOf(2), top.get(1).get("PRODUCTS_COUNTS"));

    final var firstCount = top.get(0).get("PRODUCTS_COUNTS", BigDecimal.class);
    final var secondCount = top.get(1).get("PRODUCTS_COUNTS", BigDecimal.class);
    assertEquals(1, firstCount.compareTo(secondCount));
  }

  @SuppressWarnings("ComparatorMethodParameterNotUsed")
  @Test
  public void companiesProductsInPeriodTest() {
    Stream.of(
        new CompanyRecord(1, "C1", "C1 Tax", "C1 Check"),
        new CompanyRecord(2, "C2", "C2 Tax", "C2 Check")
    ).forEach(c -> companyDAO.insert(c));
    Stream.of(
        new ProductRecord(1, "P1", "P1 Code"),
        new ProductRecord(2, "P2", "P2 Code")
    ).forEach(p -> productDAO.insert(p));
    Stream.of(
        new WaybillRecord(1, OffsetDateTime.parse("2021-01-01T00:00:00+03:00"), 1),
        new WaybillRecord(2, OffsetDateTime.parse("2021-01-03T00:00:00+03:00"), 2),
        new WaybillRecord(3, OffsetDateTime.parse("2021-01-04T00:00:00+03:00"), 1),
        new WaybillRecord(4, OffsetDateTime.parse("2021-01-05T00:00:00+03:00"), 2),
        new WaybillRecord(5, OffsetDateTime.parse("2021-01-03T00:00:00+03:00"), 2)
    ).forEach(w -> wayBillDAO.insert(w));
    Stream.of(
        new WaybillProductsRecord(1, 1, 1, 10, 1),
        new WaybillProductsRecord(2, 2, 2, 10, 2),
        new WaybillProductsRecord(3, 3, 1, 10, 10),
        new WaybillProductsRecord(4, 3, 2, 10, 3),
        new WaybillProductsRecord(5, 4, 1, 10, 5),
        new WaybillProductsRecord(6, 5, 2, 10, 7)
    ).forEach(wp -> wayBillProductsDAO.insert(wp));

    final var result = companyDAO.companiesProductsForRange(
        "2021-01-02T00:00:00+03:00",
        "2021-01-04T00:00:00+03:00"
    );
    result.sort((a, b) -> {
          final var aCompany = a.get("COMPANY_ID", Integer.class);
          final var bCompany = b.get("COMPANY_ID", Integer.class);
          final var aProduct = a.get("PRODUCT_ID", Integer.class);
          final var bProduct = b.get("PRODUCT_ID", Integer.class);
          return aCompany > bCompany ? 1 : aCompany < bCompany ? -1 :
              aProduct > bProduct ? 1 : -1;
        }
    );

    var row = result.get(0);
    assertEquals(1, row.get("COMPANY_ID"));
    assertEquals(1, row.get("PRODUCT_ID"));
    assertEquals(BigDecimal.valueOf(10), row.get("SUM"));

    row = result.get(1);
    assertEquals(1, row.get("COMPANY_ID"));
    assertEquals(2, row.get("PRODUCT_ID"));
    assertEquals(BigDecimal.valueOf(3), row.get("SUM"));

    row = result.get(2);
    assertEquals(2, row.get("COMPANY_ID"));
    assertEquals(2, row.get("PRODUCT_ID"));
    assertEquals(BigDecimal.valueOf(9), row.get("SUM"));
  }

  @Test
  public void companyWithoutProductsInPeriodTest() {
    Stream.of(
        new CompanyRecord(1, "C1", "C1 Tax", "C1 Check")
    ).forEach(c -> companyDAO.insert(c));
    productDAO.insert(new ProductRecord(1, "P1", "P1 Code"));
    Stream.of(
        new WaybillRecord(1, OffsetDateTime.parse("2021-01-01T00:00:00+03:00"), 1),
        new WaybillRecord(2, OffsetDateTime.parse("2021-01-05T00:00:00+03:00"), 1)
    ).forEach(w -> wayBillDAO.insert(w));
    Stream.of(
        new WaybillProductsRecord(1, 1, 1, 10, 1),
        new WaybillProductsRecord(2, 2, 1, 10, 2)
    ).forEach(wp -> wayBillProductsDAO.insert(wp));

    var result = companyDAO.companiesProductsForRange(
        "2021-01-02T00:00:00+03:00",
        "2021-01-04T00:00:00+03:00"
    );

    var first = result.get(0);
    assertEquals(1, first.get("COMPANY_ID"));
    assertNull(first.get("PRODUCT_ID"));
    assertNull(first.get("SUM"));

    wayBillDAO.insert(new WaybillRecord(3, OffsetDateTime.parse("2021-01-03T00:00:00+03:00"), 1));
    result = companyDAO.companiesProductsForRange("2021-01-02T00:00:00+03:00", "2021-01-04T00:00:00+03:00");

    first = result.get(0);
    assertEquals(1, first.get("COMPANY_ID"));
    assertNull(first.get("PRODUCT_ID"));
    assertNull(first.get("SUM"));
  }

  @Test
  public void companiesWithConditionTest() {
    Stream.of(
        new CompanyRecord(1, "C1", "C1 Tax", "C1 Check"),
        new CompanyRecord(2, "C2", "C2 Tax", "C2 Check")
    ).forEach(c -> companyDAO.insert(c));
    Stream.of(
        new ProductRecord(1, "P1", "P1 Code"),
        new ProductRecord(2, "P1", "P1 Code")
    ).forEach(p -> productDAO.insert(p));
    Stream.of(
        new WaybillRecord(1, OffsetDateTime.parse("2021-01-01T00:00:00+03:00"), 1),
        new WaybillRecord(2, OffsetDateTime.parse("2021-01-05T00:00:00+03:00"), 1),
        new WaybillRecord(3, OffsetDateTime.parse("2021-01-05T00:00:00+03:00"), 2),
        new WaybillRecord(4, OffsetDateTime.parse("2021-01-05T00:00:00+03:00"), 1)
    ).forEach(w -> wayBillDAO.insert(w));
    Stream.of(
        new WaybillProductsRecord(1, 1, 1, 10, 10),
        new WaybillProductsRecord(2, 3, 1, 10, 9),
        new WaybillProductsRecord(3, 3, 2, 10, 4)
    ).forEach(wp -> wayBillProductsDAO.insert(wp));

    var conditionMap = Map.of(1, 10, 2, 5);

    var result = companyDAO.companiesWithProductsCountCondition(conditionMap);
    assertEquals(0, result.size());

    wayBillProductsDAO.insert(new WaybillProductsRecord(4, 2, 2, 3, 3));
    wayBillProductsDAO.insert(new WaybillProductsRecord(5, 4, 2, 3, 2));

    result = companyDAO.companiesWithProductsCountCondition(conditionMap);
    assertEquals(1, result.size());
    assertEquals(Integer.valueOf(1), result.get(0).get("COMPANY_ID", Integer.class));

    result = companyDAO.companiesWithProductsCountCondition(new HashMap<>());
    assertEquals(0, result.size());
  }

  @Test
  public void productByDaysTest() {
    Stream.of(
        new CompanyRecord(1, "C1", "C1 Tax", "C1 Check"),
        new CompanyRecord(2, "C2", "C2 Tax", "C2 Check")
    ).forEach(c -> companyDAO.insert(c));
    Stream.of(
        new ProductRecord(1, "P1", "P1 Code"),
        new ProductRecord(2, "P2", "P2 Code")
    ).forEach(p -> productDAO.insert(p));
    Stream.of(
        new WaybillRecord(1, OffsetDateTime.parse("2021-01-01T00:00:00+03:00"), 1),
        new WaybillRecord(2, OffsetDateTime.parse("2021-01-03T00:00:00+03:00"), 1),
        new WaybillRecord(3, OffsetDateTime.parse("2021-01-03T00:00:00+03:00"), 2),
        new WaybillRecord(4, OffsetDateTime.parse("2021-01-04T00:00:00+03:00"), 2),
        new WaybillRecord(5, OffsetDateTime.parse("2021-01-05T00:00:00+03:00"), 2)
    ).forEach(w -> wayBillDAO.insert(w));
    Stream.of(
        new WaybillProductsRecord(1, 2, 1, 10, 10),
        new WaybillProductsRecord(2, 3, 1, 10, 3),
        new WaybillProductsRecord(3, 4, 1, 10, 4),
        new WaybillProductsRecord(4, 4, 2, 10, 71)
    ).forEach(wp -> wayBillProductsDAO.insert(wp));

    var result = productDAO.productByDate(
        "2021-01-02T00:00:00+03:00",
        "2021-01-04T00:00:00+03:00");
    result.sort((a, b) -> {
          final var aProduct = a.get("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType());
          final var bProduct = b.get("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType());
          return aProduct > bProduct ? 1 : aProduct < bProduct ? -1 :
              a.get("DATE", WAYBILL.DATE.getType()).compareTo(
                  b.get("DATE", WAYBILL.DATE.getType()));
        }
    );
    assertEquals(3, result.size());

    var row = result.get(0);
    assertEquals(Integer.valueOf(1), row.get("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType()));
    assertEquals(OffsetDateTime.parse("2021-01-03T00:00:00+03:00"), row.get("DATE", WAYBILL.DATE.getType()));
    assertEquals(BigDecimal.valueOf(13), row.get("PRODUCT_COUNT"));

    row = result.get(1);
    assertEquals(Integer.valueOf(1), row.get("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType()));
    assertEquals(OffsetDateTime.parse("2021-01-04T00:00:00+03:00"), row.get("DATE", WAYBILL.DATE.getType()));
    assertEquals(BigDecimal.valueOf(4), row.get("PRODUCT_COUNT"));

    row = result.get(2);
    assertEquals(Integer.valueOf(2), row.get("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType()));
    assertEquals(OffsetDateTime.parse("2021-01-04T00:00:00+03:00"), row.get("DATE", WAYBILL.DATE.getType()));
    assertEquals(BigDecimal.valueOf(71), row.get("PRODUCT_COUNT"));
  }

  @SuppressWarnings("ComparatorMethodParameterNotUsed")
  @Test
  public void avgPriceForPeriodTest() {
    Stream.of(
        new CompanyRecord(1, "C1", "C1 Tax", "C1 Check"),
        new CompanyRecord(2, "C2", "C2 Tax", "C2 Check")
    ).forEach(c -> companyDAO.insert(c));
    Stream.of(
        new ProductRecord(1, "P1", "P1 Code"),
        new ProductRecord(2, "P2", "P2 Code")
    ).forEach(p -> productDAO.insert(p));
    Stream.of(
        new WaybillRecord(1, OffsetDateTime.parse("2021-01-01T00:00:00+03:00"), 1),
        new WaybillRecord(2, OffsetDateTime.parse("2021-01-03T00:00:00+03:00"), 1),
        new WaybillRecord(3, OffsetDateTime.parse("2021-01-03T00:00:00+03:00"), 2),
        new WaybillRecord(4, OffsetDateTime.parse("2021-01-04T00:00:00+03:00"), 2),
        new WaybillRecord(5, OffsetDateTime.parse("2021-01-05T00:00:00+03:00"), 2)
    ).forEach(w -> wayBillDAO.insert(w));
    Stream.of(
        new WaybillProductsRecord(1, 1, 1, 10, 10),
        new WaybillProductsRecord(2, 5, 1, 10, 10),
        new WaybillProductsRecord(3, 2, 2, 3, 2),
        new WaybillProductsRecord(4, 3, 2, 2, 1),
        new WaybillProductsRecord(5, 5, 2, 10000, 1)
    ).forEach(wp -> wayBillProductsDAO.insert(wp));

    var result = productDAO.avgProductPeriodPrice(
        "2021-01-02T00:00:00+03:00",
        "2021-01-04T00:00:00+03:00"
    );
    result.sort((a, b) ->
        a.get("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType())
            > b.get("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType())
            ? 1 : -1);
    assertEquals(1, result.size());

    assertEquals(Integer.valueOf(2), result.get(0).get("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType()));
    final var avgPrice = result.get(0).get("AVG_PRICE", Float.class);
    assertEquals(1.66f, avgPrice, 0.02f);
  }
}
