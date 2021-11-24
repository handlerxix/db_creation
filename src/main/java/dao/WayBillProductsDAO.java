package dao;

import entity.WayBillProducts;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class WayBillProductsDAO extends DAO<WayBillProducts> {

  public WayBillProductsDAO(@NotNull Connection connection) {
    super(connection, "WAYBILL_PRODUCTS");
  }

  @Override
  public void insert(@NotNull WayBillProducts entity) {
    try {
      String insertSQL = "INSERT INTO " + getTableName() + " (WAYBILL_ID, PRODUCT_ID, PRICE, COUNT) VALUES (?, ?, ?, ?)";
      final var statement = connection.prepareStatement(insertSQL);
      statement.setInt(1, entity.getWayBillId());
      statement.setInt(2, entity.getProductId());
      statement.setInt(3, entity.getPrice());
      statement.setInt(4, entity.getCount());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(@NotNull WayBillProducts entity) {
    try {
      String updateSQL = "UPDATE " + getTableName() + " SET " +
          "WAYBILL_ID = ?, PRODUCT_ID = ?, PRICE = ?, COUNT = ? WHERE ID = ?";
      final var statement = connection.prepareStatement(updateSQL);
      statement.setInt(1, entity.getWayBillId());
      statement.setInt(2, entity.getProductId());
      statement.setInt(3, entity.getPrice());
      statement.setInt(4, entity.getCount());
      statement.setInt(5, entity.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void delete(@NotNull WayBillProducts entity) {
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
  WayBillProducts resultToEntity(@NotNull ResultSet resultSet) {
    try {
      return new WayBillProducts(resultSet.getInt("ID"),
          resultSet.getInt("WAYBILL_ID"),
          resultSet.getInt("PRODUCT_ID"),
          resultSet.getInt("PRICE"),
          resultSet.getInt("COUNT"));
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    throw new IllegalStateException("Unable to convert ResultSet to WatBillProducts");
  }
}
