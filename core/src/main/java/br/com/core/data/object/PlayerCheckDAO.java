package br.com.core.data.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerCheckDAO {
    private String uuid;
    private double range;
}
