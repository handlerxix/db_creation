package entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;

@Data
public final class WayBill {

  private @NotNull
  Integer id;

  private @NotNull
  Date date;

  private @NotNull
  Integer companyId;

}
