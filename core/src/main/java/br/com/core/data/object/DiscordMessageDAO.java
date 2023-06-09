package br.com.core.data.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiscordMessageDAO {
    private String nick;
    private String message;
}
