package dao;

import generated.public_.Tables;
import generated.public_.tables.Product;
import generated.public_.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.sql.Connection;

public final class ProductManager {

  private final @NotNull
  DSLContext context;

  ProductManager(@NotNull Connection connection) {
    this.context = DSL.using(connection);
  }

  private final @NotNull
  Product PRODUCT = Tables.PRODUCT;

  public void insert(@NotNull ProductRecord rec) {
    context
        .insertInto(PRODUCT, PRODUCT.ID, PRODUCT.NAME)
        .values(rec)
        .execute();
  }

  public void update(@NotNull ProductRecord rec) {
    context
        .update(PRODUCT)
        .set(PRODUCT.NAME, rec.field(PRODUCT.NAME))
        .execute();
  }

  public void delete(@NotNull ProductRecord rec) {
    context
        .deleteFrom(PRODUCT)
        .where(PRODUCT.ID.eq(rec.get(PRODUCT.ID)))
        .execute();
  }

  @Nullable
  ProductRecord get(@NotNull Integer id) {
    return context
        .selectFrom(PRODUCT)
        .where(PRODUCT.ID.eq(id))
        .fetchOne();
  }

  @NotNull
  Result<ProductRecord> all() {
    return context
        .selectFrom(PRODUCT)
        .fetch();
  }
}
