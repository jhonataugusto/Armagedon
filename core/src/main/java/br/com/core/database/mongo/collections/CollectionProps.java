package br.com.core.database.mongo.collections;

import br.com.core.account.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum CollectionProps {
    USERS("users"),
    DUELS("duels");

    private String name;

    CollectionProps(String name) {
        this.name = name;
    }
}
