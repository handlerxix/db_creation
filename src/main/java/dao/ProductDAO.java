package dao;

import entity.Product;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ProductDAO extends DAO<Product> {

  private @NotNull
  String sourceQuery =
      "FROM PRODUCT " +
          "JOIN WAYBILL_PRODUCTS ON PRODUCT.ID = PRODUCT_ID " +
          "JOIN WAYBILL ON WAYBILL.ID = WAYBILL_ID " +
          "WHERE DATE >= ? AND DATE <= ? " +
          "GROUP BY PRODUCT_ID, DATE ORDER BY PRODUCT_ID, DATE";

  private @NotNull
  String productsByDaysSQL =
      "SELECT PRODUCT_ID, DATE, SUM(COUNT) AS PRODUCT_COUNT, SUM(PRICE) AS PRODUCT_PRICE "+ sourceQuery;

  private @NotNull
  String avgPriceFoePeriodSQL =
      "SELECT PRODUCT_ID, (CAST(SUM(PRICE) AS FLOAT)/SUM(COUNT)) AS AVG_PRICE "+ sourceQuery;

  public ProductDAO(@NotNull Connection connection) {
    super(connection, "PRODUCT");
  }

  @Override
  public void insert(@NotNull Product entity) {
    try {
      String insertSQL = "INSERT INTO " + getTableName() + " (NAME, INNER_CODE) VALUES (?, ?)";
      final var statement = connection.prepareStatement(insertSQL);
      statement.setString(1, entity.getName());
      statement.setString(2, entity.getInnerCode());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(@NotNull Product entity) {
    try {
      String updateSQL = "UPDATE " + getTableName() + " SET NAME = ?, INNER_CODE = ? WHERE ID = ?";
      final var statement = connection.prepareStatement(updateSQL);
      statement.setString(1, entity.getName());
      statement.setString(2, entity.getInnerCode());
      statement.setInt(3, entity.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void delete(@NotNull Product entity) {
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
  Product resultToEntity(@NotNull ResultSet resultSet) {
    try {
      return new Product(resultSet.getInt("ID"),
          resultSet.getString("NAME"),
          resultSet.getString("INNER_CODE"));
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    throw new IllegalStateException("Unable to convert ResultSet to Product");
  }

  @NotNull
  public List<List<String>> productByDate(@NotNull String after, @NotNull String before) {
    try {
      final var statement = connection.prepareStatement(productsByDaysSQL);
      statement.setDate(1, Date.valueOf(after));
      statement.setDate(2, Date.valueOf(before));
      final var ret = new ArrayList<List<String>>();
      final var resSet = statement.executeQuery();
      while (resSet.next()) {
        final var l = new ArrayList<String>();
        l.add(String.valueOf(resSet.getInt("PRODUCT_ID")));
        l.add(String.valueOf(resSet.getDate("DATE")));
        l.add(String.valueOf(resSet.getInt("PRODUCT_COUNT")));
        l.add(String.valueOf(resSet.getInt("PRODUCT_PRICE")));
        ret.add(l);
      }
      return ret;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    throw new IllegalStateException("Something went wrong, while trying get products by date info");
  }

  @NotNull
  public List<Pair<Integer, Float>> avgProductPeriodPrice(@NotNull String after, @NotNull String before) {
    try {
      final var statement = connection.prepareStatement(avgPriceFoePeriodSQL);
      statement.setDate(1, Date.valueOf(after));
      statement.setDate(2, Date.valueOf(before));
      final var ret = new ArrayList<Pair<Integer, Float>>();
      final var resSet = statement.executeQuery();
      while (resSet.next()) {
        ret.add(Pair.of(resSet.getInt("PRODUCT_ID"), resSet.getFloat("AVG_PRICE")));
      }
      return ret;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    throw new IllegalStateException("Something went wrong, while trying get products by date info");
  }
}
