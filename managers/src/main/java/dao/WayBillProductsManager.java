package dao;

import generated.Tables;
import generated.tables.WaybillProducts;
import generated.tables.records.WaybillProductsRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Result;

import java.sql.Connection;

public final class WayBillProductsManager extends Manager<WaybillProductsRecord> {

  private final @NotNull
  WaybillProducts WAYBILL_PRODUCTS = Tables.WAYBILL_PRODUCTS;

  public WayBillProductsManager(@NotNull Connection connection) {
    super(connection);
  }

  @Override
  public void insert(@NotNull WaybillProductsRecord rec) {
    getContext()
        .insertInto(WAYBILL_PRODUCTS,
            WAYBILL_PRODUCTS.ID,
            WAYBILL_PRODUCTS.WAYBILL_ID,
            WAYBILL_PRODUCTS.PRODUCT_ID,
            WAYBILL_PRODUCTS.PRICE,
            WAYBILL_PRODUCTS.COUNT)
        .values(rec)
        .execute();
  }

  @Override
  public void update(@NotNull WaybillProductsRecord rec) {
    getContext()
        .update(WAYBILL_PRODUCTS)
        .set(WAYBILL_PRODUCTS.WAYBILL_ID, rec.field(WAYBILL_PRODUCTS.WAYBILL_ID))
        .set(WAYBILL_PRODUCTS.PRODUCT_ID, rec.field(WAYBILL_PRODUCTS.PRODUCT_ID))
        .set(WAYBILL_PRODUCTS.COUNT, rec.field(WAYBILL_PRODUCTS.COUNT))
        .set(WAYBILL_PRODUCTS.PRICE, rec.field(WAYBILL_PRODUCTS.PRICE))
        .execute();
  }

  @Override
  public void delete(@NotNull WaybillProductsRecord rec) {
    getContext()
        .deleteFrom(WAYBILL_PRODUCTS)
        .where(WAYBILL_PRODUCTS.ID.eq(rec.get(WAYBILL_PRODUCTS.ID)))
        .execute();
  }

  @Nullable
  @Override
  WaybillProductsRecord get(@NotNull Integer id) {
    return getContext()
        .selectFrom(WAYBILL_PRODUCTS)
        .where(WAYBILL_PRODUCTS.ID.eq(id))
        .fetchOne();
  }

  @NotNull
  @Override
  Result<WaybillProductsRecord> all() {
    return getContext()
        .selectFrom(WAYBILL_PRODUCTS)
        .fetch();
  }
}
