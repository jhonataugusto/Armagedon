package br.com.core.data.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EloDAO {
    private String name;
    private int elo;
}
