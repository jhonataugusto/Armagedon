package br.com.hub.util.mojang.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MojangApiResponse {
    private String uuid;
    private String username;
    @SerializedName("username_history")
    private List<UsernameHistory> usernameHistory;
    private Textures textures;
    private Boolean legacy;
    private Boolean demo;
    @SerializedName("created_at")
    private Date createdAt;

    @Getter
    @Setter
    public static class UsernameHistory {
        private String username;
        @SerializedName("changed_at")
        private Date changedAt;
    }

    @Getter
    @Setter
    public static class Textures {
        private Boolean slim;
        private Boolean custom;
        private Skin skin;
        private Cape cape;
        private Raw raw;
    }

    @Getter
    @Setter
    public static class Skin {
        private String url;
        private String data;
    }

    @Getter
    @Setter
    public static class Cape {
        private String url;
        private String data;
    }

    @Getter
    @Setter
    public static class Raw {
        private String value;
        private String signature;
    }
}