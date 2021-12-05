package dao;

import generated.public_.Tables;
import generated.public_.tables.Product;
import generated.public_.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import javax.inject.Inject;
import java.sql.Connection;

public final class ProductManager {

  private final @NotNull
  DSLContext context;

  @Inject
  ProductManager(@NotNull Connection connection) {
    this.context = DSL.using(connection);
  }

  private final @NotNull
  Product PRODUCT = Tables.PRODUCT;

  public @NotNull
  Result<Record> allProducts() {
    return context.select().from(PRODUCT).fetch();
  }

  public void add(@NotNull String newProduct) {
    context.insertInto(PRODUCT, PRODUCT.NAME).values(newProduct).execute();
  }

  public void rename(@NotNull ProductRecord rec) {
    context
        .update(PRODUCT)
        .set(PRODUCT.NAME, rec.get("NAME", String.class))
        .where(PRODUCT.ID.eq(rec.get("ID", Integer.class)))
        .execute();
  }
}
