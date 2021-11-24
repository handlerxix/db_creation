package entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public final class Company {

  private @NotNull Integer id;

  private @NotNull String name;

  private @NotNull String taxNumber;

  private @NotNull String companyCheck;
}
