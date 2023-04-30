package br.com.core.data.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreferencesDAO {
    private String name;
    private boolean active;
}
