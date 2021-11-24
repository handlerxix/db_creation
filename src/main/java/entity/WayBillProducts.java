package entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public final class WayBillProducts {

  private @NotNull Integer id;

  private @NotNull Integer wayBillId;

  private @NotNull Integer productId;

  private @NotNull Integer price;

  private @NotNull Integer count;
}
