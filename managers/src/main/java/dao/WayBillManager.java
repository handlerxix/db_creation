package dao;

import generated.Tables;
import generated.tables.Waybill;
import generated.tables.records.WaybillRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Result;

import java.sql.Connection;

public final class WayBillManager extends Manager<WaybillRecord> {

  private final @NotNull
  Waybill WAYBILL = Tables.WAYBILL;

  public WayBillManager(@NotNull Connection connection) {
    super(connection);
  }

  @Override
  public void insert(@NotNull WaybillRecord rec) {
    getContext()
        .insertInto(WAYBILL, WAYBILL.ID, WAYBILL.DATE, WAYBILL.COMPANY_ID)
        .values(rec)
        .execute();
  }

  @Override
  public void update(@NotNull WaybillRecord rec) {
    getContext()
        .update(WAYBILL)
        .set(WAYBILL.COMPANY_ID, rec.field(WAYBILL.COMPANY_ID))
        .set(WAYBILL.DATE, rec.field(WAYBILL.DATE))
        .execute();
  }

  @Override
  public void delete(@NotNull WaybillRecord rec) {
    getContext()
        .deleteFrom(WAYBILL)
        .where(WAYBILL.ID.eq(rec.get(WAYBILL.ID)))
        .execute();
  }

  @Nullable
  @Override
  WaybillRecord get(@NotNull Integer id) {
    return getContext()
        .selectFrom(WAYBILL)
        .where(WAYBILL.ID.eq(id))
        .fetchOne();
  }

  @NotNull
  @Override
  Result<WaybillRecord> all() {
    return getContext()
        .selectFrom(WAYBILL)
        .fetch();
  }
}
