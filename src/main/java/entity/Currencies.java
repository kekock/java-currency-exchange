package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Currencies {
    private int id;
    private String code;
    private String fullName;
    private String sign;
}
