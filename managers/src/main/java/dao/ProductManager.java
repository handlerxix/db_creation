package dao;

import generated.Tables;
import generated.tables.Product;
import generated.tables.Waybill;
import generated.tables.WaybillProducts;
import generated.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Result;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.OffsetDateTime;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.sum;

public final class ProductManager extends Manager<ProductRecord> {

  private final @NotNull
  Waybill WAYBILL = Tables.WAYBILL;

  private final @NotNull
  WaybillProducts WAYBILL_PRODUCTS = Tables.WAYBILL_PRODUCTS;

  private final @NotNull
  Product PRODUCT = Tables.PRODUCT;

  public ProductManager(@NotNull Connection connection) {
    super(connection);
  }

  @Override
  public void insert(@NotNull ProductRecord rec) {
    getContext()
        .insertInto(PRODUCT, PRODUCT.ID, PRODUCT.NAME, PRODUCT.INNER_CODE)
        .values(rec)
        .execute();
  }

  @Override
  public void update(@NotNull ProductRecord rec) {
    getContext()
        .update(PRODUCT)
        .set(PRODUCT.NAME, rec.field(PRODUCT.NAME))
        .set(PRODUCT.INNER_CODE, rec.field(PRODUCT.INNER_CODE))
        .execute();
  }

  @Override
  public void delete(@NotNull ProductRecord rec) {
    getContext()
        .deleteFrom(PRODUCT)
        .where(PRODUCT.ID.eq(rec.get(PRODUCT.ID)))
        .execute();
  }

  @NotNull
  public Result<Record3<Integer, OffsetDateTime, BigDecimal>> productByDate(@NotNull String after, @NotNull String before) {
    return getContext()
        .select(
            field("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType()),
            field("DATE", WAYBILL.DATE.getType()),
            sum(field("COUNT", WAYBILL_PRODUCTS.COUNT.getType())).as("PRODUCT_COUNT")
        )
        .from(PRODUCT.join(
                WAYBILL_PRODUCTS
                    .join(WAYBILL).on(WAYBILL.ID.eq(WAYBILL_PRODUCTS.WAYBILL_ID))
            ).on(PRODUCT.ID.eq(WAYBILL_PRODUCTS.PRODUCT_ID))
        )
        .where(WAYBILL.DATE.ge(OffsetDateTime.parse(after))
            .and(WAYBILL.DATE.le(OffsetDateTime.parse(before)))
        ).groupBy(WAYBILL_PRODUCTS.PRODUCT_ID, WAYBILL.DATE)
        .orderBy(WAYBILL_PRODUCTS.PRODUCT_ID, WAYBILL.DATE)
        .fetch();
  }

  @NotNull
  public Result<Record2<Integer, Float>> avgProductPeriodPrice(@NotNull String after, @NotNull String before) {
    return getContext()
        .select(
            WAYBILL_PRODUCTS.PRODUCT_ID.as("PRODUCT_ID"),
            sum(WAYBILL_PRODUCTS.PRICE).cast(Float.class).div(sum(WAYBILL_PRODUCTS.COUNT)).as("AVG_PRICE"))
        .from(PRODUCT)
        .join(WAYBILL_PRODUCTS).on(PRODUCT.ID.eq(WAYBILL_PRODUCTS.PRODUCT_ID))
        .join(WAYBILL).on(WAYBILL.ID.eq(WAYBILL_PRODUCTS.WAYBILL_ID))
        .where(WAYBILL.DATE.ge(OffsetDateTime.parse(after))
            .and(WAYBILL.DATE.le(OffsetDateTime.parse(before))))
        .groupBy(WAYBILL_PRODUCTS.PRODUCT_ID, WAYBILL.DATE)
        .orderBy(WAYBILL_PRODUCTS.PRODUCT_ID, WAYBILL.DATE)
        .fetch();
  }

  @Nullable
  @Override
  ProductRecord get(@NotNull Integer id) {
    return getContext()
        .selectFrom(PRODUCT)
        .where(PRODUCT.ID.eq(id))
        .fetchOne();
  }

  @NotNull
  @Override
  Result<ProductRecord> all() {
    return getContext()
        .selectFrom(PRODUCT)
        .fetch();
  }
}
