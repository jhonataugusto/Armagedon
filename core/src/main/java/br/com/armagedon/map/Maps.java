package br.com.armagedon.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Maps {
    GRASS("grass"),
    STONE("stone");
    private String name;
    private String displayName;

}
