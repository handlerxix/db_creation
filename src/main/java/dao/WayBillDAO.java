package dao;

import entity.WayBill;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class WayBillDAO extends DAO<WayBill> {

  public WayBillDAO(@NotNull Connection connection) {
    super(connection, "WAYBILL");
  }

  @Override
  public void insert(@NotNull WayBill entity) {
    try {
      String insertSQL = "INSERT INTO " + getTableName() + " (DATE, COMPANY_ID) VALUES (?, ?)";
      final var statement = connection.prepareStatement(insertSQL);
      statement.setDate(1, entity.getDate());
      statement.setInt(2, entity.getCompanyId());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(@NotNull WayBill entity) {
    try {
      String updateSQL = "UPDATE " + getTableName() + " SET DATE = ?, COMPANY_ID = ?, COMPANY_CHECK = ? WHERE ID = ?";
      final var statement = connection.prepareStatement(updateSQL);
      statement.setDate(1, entity.getDate());
      statement.setInt(2, entity.getCompanyId());
      statement.setInt(3, entity.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void delete(@NotNull WayBill entity) {
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
  WayBill resultToEntity(@NotNull ResultSet resultSet) {
    try {
      return new WayBill(resultSet.getInt("ID"),
          resultSet.getDate("DATE"),
          resultSet.getInt("COMPANY_ID"));
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    throw new IllegalStateException("Unable to convert ResultSet to WayBill");
  }
}
