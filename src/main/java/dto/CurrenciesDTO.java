package dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrenciesDTO {

    private int id;
    private String name;
    private String code;
    private String sign;
}