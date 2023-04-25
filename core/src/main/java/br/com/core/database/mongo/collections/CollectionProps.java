package br.com.core.database.mongo.collections;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum CollectionProps {
    USERS("users"),
    KITS("practice_kits");

    private String name;

    CollectionProps(String name) {
        this.name = name;
    }
}
