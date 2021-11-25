package dao;

import entity.Company;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public final class CompanyDAO extends DAO<Company> {

  private final @NotNull
  String companiesProductsSource =
      "FROM COMPANY LEFT JOIN WAYBILL ON COMPANY.ID = COMPANY_ID "
          + "LEFT JOIN WAYBILL_PRODUCTS ON WAYBILL.ID = WAYBILL_ID ";
  private final @NotNull
  String companiesProductsSQL =
      "SELECT COMPANY_ID, COMPANY.NAME, CASE WHEN SUM(COUNT) IS NULL THEN 0 ELSE SUM(COUNT) END AS PRODUCTS_COUNTS "
          + companiesProductsSource
          + "GROUP BY COMPANY_ID, COMPANY.NAME ORDER BY PRODUCTS_COUNTS DESC ";
  private final @NotNull
  String companiesWithProductsConditionSQL =
      "SELECT COMPANY_ID, COUNT(PRODUCT_ID) AS CONDITION_SUCCEEDED_COUNT FROM (" +
          "SELECT COMPANY_ID, PRODUCT_ID "
          + companiesProductsSource
          + "GROUP BY COMPANY_ID, PRODUCT_ID HAVING %s) x "
          + "GROUP BY COMPANY_ID HAVING COUNT(PRODUCT_ID) >= ?";
  private final @NotNull
  String companiesProductsByDateSQL =
      "SELECT COMPANY.ID AS COMPANY, PRODUCT_ID AS PRODUCT, SUM(COUNT) "
          + "FROM COMPANY LEFT JOIN "
          + "(SELECT * FROM WAYBILL WHERE DATE >= ? AND DATE <= ?) X ON COMPANY.ID = COMPANY_ID "
          + "LEFT JOIN WAYBILL_PRODUCTS ON X.ID = WAYBILL_ID "
          + "GROUP BY COMPANY.ID, PRODUCT_ID";

  public CompanyDAO(@NotNull Connection connection) {
    super(connection, "COMPANY");
  }

  @Override
  public void insert(@NotNull Company entity) {
    try {
      String insertSQL = "INSERT INTO " + getTableName() + " (ID, NAME, INDIVIDUAL_TAX_NUMBER, COMPANY_CHECK) VALUES (?, ?, ?, ?)";
      final var statement = connection.prepareStatement(insertSQL);
      statement.setInt(1, entity.getId());
      statement.setString(2, entity.getName());
      statement.setString(3, entity.getTaxNumber());
      statement.setString(4, entity.getCompanyCheck());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(@NotNull Company entity) {
    try {
      String updateSQL = "UPDATE " + getTableName() + " SET NAME = ?, INDIVIDUAL_TAX_NUMBER = ?, COMPANY_CHECK = ? WHERE ID = ?";
      final var statement = connection.prepareStatement(updateSQL);
      statement.setString(1, entity.getName());
      statement.setString(2, entity.getTaxNumber());
      statement.setString(3, entity.getCompanyCheck());
      statement.setInt(4, entity.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void delete(@NotNull Company entity) {
    try {
      String deleteSQL = "DELETE FROM " + getTableName() + " WHERE ID = ?";
      final var statement = connection.prepareStatement(deleteSQL);
      statement.setInt(1, entity.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @NotNull
  @Override
  Company resultToEntity(@NotNull ResultSet resultSet) {
    try {
      return new Company(resultSet.getInt("ID"),
          resultSet.getString("NAME"),
          resultSet.getString("INDIVIDUAL_TAX_NUMBER"),
          resultSet.getString("COMPANY_CHECK"));
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    throw new IllegalStateException("Unable to convert ResultSet to Company");
  }

  @NotNull
  public List<Pair<String, Integer>> topCompaniesByProductsCount(@NotNull Integer topNumber) {
    try {
      final var statement = connection.prepareStatement(companiesProductsSQL + "LIMIT ?");
      statement.setInt(1, topNumber);
      final var resSet = statement.executeQuery();
      final var ret = new ArrayList<Pair<String, Integer>>();
      while (resSet.next()) {
        ret.add(Pair.of(resSet.getString("NAME"), resSet.getInt("PRODUCTS_COUNTS")));
      }
      return ret;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    throw new IllegalStateException("Something went wrong, while trying get top companies");
  }

  @NotNull
  public List<Triple<Integer, Integer, Integer>> companiesProductsForRange(@NotNull String after, @NotNull String before) {
    try {
      final var statement = connection.prepareStatement(companiesProductsByDateSQL);
      statement.setDate(1, Date.valueOf(after));
      statement.setDate(2, Date.valueOf(before));
      System.out.println(companiesProductsByDateSQL);
      final var resSet = statement.executeQuery();
      final var ret = new ArrayList<Triple<Integer, Integer, Integer>>();
      while (resSet.next()) {
        ret.add(Triple.of(
            resSet.getInt("COMPANY"),
            resSet.getInt("PRODUCT"),
            resSet.getInt("SUM")
        ));
      }
      return ret;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    throw new IllegalStateException("Something went wrong, while trying get delivered products for period");
  }

  @NotNull
  public List<Pair<Integer, Integer>> companiesWithProductsCountCondition(@NotNull Map<Integer, Integer> productsCondition) {
    try {
      final var keys = new ArrayList<>(productsCondition.keySet());
      final var conditionsCount = keys.size();
      final String conditionSQL;
      if (conditionsCount <= 0) {
        conditionSQL = "FALSE ";
      } else {
        final var singleConditionSQL = "PRODUCT_ID = ? AND SUM(COUNT) >= ? ";
        conditionSQL = IntStream.range(0, conditionsCount - 1)
            .mapToObj(i -> singleConditionSQL + "OR ")
            .reduce("", (res, str) -> res += str) + singleConditionSQL;
      }

      final var sql = String.format(companiesWithProductsConditionSQL, conditionSQL);
      System.out.println(sql);
      final var statement = connection.prepareStatement(sql);
      IntStream.range(0, conditionsCount).forEach(i -> {
        final var key = keys.get(i);
        try {
          statement.setInt(2 * i + 1, key);
          statement.setInt(2 * (i + 1), productsCondition.get(key));
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
      statement.setInt(2 * conditionsCount + 1, conditionsCount);

      System.out.println(sql);
      final var resSet = statement.executeQuery();
      final var ret = new ArrayList<Pair<Integer, Integer>>();
      while (resSet.next()) {
        ret.add(Pair.of(resSet.getInt("COMPANY_ID"), resSet.getInt("CONDITION_SUCCEEDED_COUNT")));
      }
      return ret;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    throw new IllegalStateException("Something went wrong, while trying get companies by conditions");
  }
}