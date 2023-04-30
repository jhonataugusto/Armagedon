package br.com.core.data.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventoryDAO {
    private String gamemodeName;
    private String inventoryEncoded;
}
