package dao;

import generated.Tables;
import generated.tables.Company;
import generated.tables.Waybill;
import generated.tables.WaybillProducts;
import generated.tables.records.CompanyRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.jooq.impl.DSL.*;

public final class CompanyManager extends Manager<CompanyRecord> {

  private final @NotNull
  Company COMPANY = Tables.COMPANY;

  private final @NotNull
  Waybill WAYBILL = Tables.WAYBILL;

  private final @NotNull
  WaybillProducts WAYBILL_PRODUCTS = Tables.WAYBILL_PRODUCTS;

  public CompanyManager(@NotNull Connection connection) {
    super(connection);
  }

  @Override
  public void insert(@NotNull CompanyRecord rec) {
    getContext()
        .insertInto(COMPANY, COMPANY.ID, COMPANY.NAME, COMPANY.INDIVIDUAL_TAX_NUMBER, COMPANY.COMPANY_CHECK)
        .values(rec)
        .execute();
  }

  @Override
  public void update(@NotNull CompanyRecord rec) {
    getContext()
        .update(COMPANY)
        .set(COMPANY.NAME, rec.field(COMPANY.NAME))
        .set(COMPANY.INDIVIDUAL_TAX_NUMBER, rec.field(COMPANY.INDIVIDUAL_TAX_NUMBER))
        .set(COMPANY.COMPANY_CHECK, rec.field(COMPANY.COMPANY_CHECK))
        .execute();
  }

  @Override
  public void delete(@NotNull CompanyRecord rec) {
    getContext()
        .deleteFrom(COMPANY)
        .where(COMPANY.ID.eq(rec.get(COMPANY.ID)))
        .execute();
  }

  @NotNull
  public Result<Record3<Integer, String, BigDecimal>> topCompaniesByProductsCount(@NotNull Integer topNumber) {
    return getContext()
        .select(
            WAYBILL.COMPANY_ID,
            COMPANY.NAME,
            sum(when(WAYBILL_PRODUCTS.COUNT.isNull(), 0).otherwise(WAYBILL_PRODUCTS.COUNT)).as("PRODUCTS_COUNTS")
        )
        .from(COMPANY)
        .leftJoin(WAYBILL).on(COMPANY.ID.eq(WAYBILL.COMPANY_ID))
        .leftJoin(WAYBILL_PRODUCTS).on(WAYBILL.ID.eq(WAYBILL_PRODUCTS.WAYBILL_ID))
        .groupBy(WAYBILL.COMPANY_ID, COMPANY.NAME)
        .orderBy(
            sum(when(WAYBILL_PRODUCTS.COUNT.isNull(), 0).otherwise(WAYBILL_PRODUCTS.COUNT)
            ).desc())
        .limit(topNumber)
        .fetch();
  }

  @NotNull
  public Result<Record3<Integer, Integer, BigDecimal>> companiesProductsForRange(@NotNull String after, @NotNull String before) {
    return getContext()
        .select(
            COMPANY.ID.as("COMPANY_ID"),
            field("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType()),
            sum(field("COUNT", WAYBILL_PRODUCTS.COUNT.getType())).as("SUM")
        )
        .from(COMPANY.leftJoin(
                select(
                    WAYBILL.COMPANY_ID,
                    WAYBILL_PRODUCTS.PRODUCT_ID,
                    WAYBILL_PRODUCTS.COUNT
                ).from(
                        WAYBILL.leftJoin(WAYBILL_PRODUCTS)
                            .on(field(WAYBILL.ID).eq(WAYBILL_PRODUCTS.WAYBILL_ID))
                    )
                    .where(WAYBILL.DATE.ge(OffsetDateTime.parse(after))
                        .and(WAYBILL.DATE.le(OffsetDateTime.parse(before)))
                    ))
            .on(field("COMPANY_ID").eq(COMPANY.ID))
        )
        .groupBy(COMPANY.ID, field("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType()))
        .fetch();
  }

  @NotNull
  public List<Record> companiesWithProductsCountCondition(@NotNull Map<Integer, Integer> productsCondition) {
    final var keys = new ArrayList<>(productsCondition.keySet());
    final var conditionsCount = keys.size();
    if (conditionsCount <= 0) {
      return new ArrayList<>();
    }

    final var havingCondition = IntStream.range(1, conditionsCount)
        .mapToObj(i -> {
          final var key = keys.get(i);
          return haveCondition(key, productsCondition.get(key));
        })
        .reduce(haveCondition(keys.get(0), productsCondition.get(keys.get(0))), Condition::or);

    return new ArrayList<>(
        getContext()
            .select(
                field("COMPANY_ID", WAYBILL.COMPANY_ID.getType()),
                count(field("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType())).as("CONDITION_SUCCEEDED_COUNT"))
            .from(
                getContext()
                    .select(
                        field("COMPANY_ID", WAYBILL.COMPANY_ID.getType()),
                        field("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType()))
                    .from(COMPANY
                        .leftJoin(WAYBILL
                            .leftJoin(WAYBILL_PRODUCTS).on(WAYBILL.ID.eq(WAYBILL_PRODUCTS.WAYBILL_ID))
                        ).on(COMPANY.ID.eq(WAYBILL.COMPANY_ID))
                    )
                    .groupBy(
                        field("COMPANY_ID"),
                        field("PRODUCT_ID")
                    )
                    .having(havingCondition)
            )
            .groupBy(field("COMPANY_ID"))
            .having(count(field("PRODUCT_ID", WAYBILL_PRODUCTS.PRODUCT_ID.getType())).ge(conditionsCount))
            .fetch());
  }

  @Nullable
  @Override
  CompanyRecord get(@NotNull Integer id) {
    return getContext()
        .selectFrom(COMPANY)
        .where(COMPANY.ID.eq(id))
        .fetchOne();
  }

  @NotNull
  @Override
  Result<CompanyRecord> all() {
    return getContext()
        .selectFrom(COMPANY)
        .fetch();
  }

  @NotNull
  private Condition haveCondition(@NotNull Integer productId, @NotNull Integer minValue) {
    return WAYBILL_PRODUCTS.PRODUCT_ID.eq(productId)
        .and(sum(WAYBILL_PRODUCTS.COUNT).ge(BigDecimal.valueOf(minValue)));
  }
}