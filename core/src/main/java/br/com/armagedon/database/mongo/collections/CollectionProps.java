package br.com.armagedon.database.mongo.collections;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum CollectionProps {
    USERS("users");

    private String name;

    CollectionProps(String name) {
        this.name = name;
    }
}
