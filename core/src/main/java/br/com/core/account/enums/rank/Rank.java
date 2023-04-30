package br.com.core.account.enums.rank;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Rank {

    MEMBER(0, "member", "Membro", "", "§7", false, null, null, "membro", "member"),

    MINI_YOUTUBER(1, "mini_youtuber", "Mini YT", "[MiniYT]", "§b", true, BenefitLevel.BASIC, null, "miniyt", "mini_youtuber", "miniyoutuber", "youtubermini", "ytmini"),
    YOUTUBER(2, "youtuber", "Youtuber", "[YT]", "§b", true, BenefitLevel.BASIC, null, "youtuber", "yt"),
    YOUTUBER_PLUS(3, "youtuber_plus", "Youtuber+", "[YT+]", "§3", true, BenefitLevel.ELITE, null, "youtuber+", "youtuberplus", "youtubermais", "ytplus", "yt+", "+yt", "plusyt"),

    STREAMER(4, "streamer", "Streamer", "[Streamer]", "§5", true, BenefitLevel.BASIC, null, "streamer", "str", "stream"),

    BASIC(10, "basic", "Basic", "[Basic]", "§e", true, BenefitLevel.BASIC, null, "basic"),
    ELITE(11, "elite", "Elite", "[Elite]", "§6", true, BenefitLevel.PREMIUM, null, "premium"),
    PREMIUM(12, "premium", "Premium", "[Premium]", "§d", true, BenefitLevel.ELITE, null, "elite"),
    SUPERIOR(13, "superior", "Superior", "[Superior]", "§5", true, BenefitLevel.SUPERIOR, null, "superior"),

    HELPER(20, "helper", "Ajudante", "[Help]", "§a", false, null, StafferLevel.JUNIOR, "ajudante", "helper", "ajd", "help"),
    TRIAL_MODERATOR(21, "trial_moderator", "Trial Mod", "[TrialMod]", "§d", false, null, StafferLevel.JUNIOR, "trialmod", "trial"),
    MODERATOR(22, "moderator", "Moderador", "[Mod]", "§5", false, null, StafferLevel.MID, "mod", "moderator", "moderador"),
    SENIOR_MODERATOR(23, "senior_moderator", "Senior Mod", "[SrMod]", "§5", false, null, StafferLevel.MID, "srmod", "modplus", "seniormod", "mod+"),
    ADMINISTRATOR(24, "administrator", "Administrador", "[Admin]", "§c", false, null, StafferLevel.SENIOR, "admin", "ademir", "administrador", "administrator"),
    SENIOR_ADMINISTRATOR(25, "senior_administrator", "Senior Admin", "[SrAdmin]", "§c", false, null, StafferLevel.MANAGERIAL, "adminplus", "sradmin", "senioradmin", "admin+"),
    MANAGER(26, "manager", "Gerente", "[Gerente]", "§4", false, null, StafferLevel.MANAGERIAL, "manager", "gerente", "diretor"),
    OWNER(27, "owner", "Dono", "[Dono]", "§4", false, null, StafferLevel.EXECUTIVE, "dono", "owner", "rei", "chefia"),
    DEVELOPER(28, "developer", "Desenvolvedor", "[Dev]", "§b", false, null, StafferLevel.EXECUTIVE, "desenvolvedor", "dev", "developer", "verdadeirodono");

    private int id;
    private String name;
    private String displayName;
    private String chatDisplay;
    private String color;
    private boolean partner;
    private BenefitLevel benefitLevel;
    private StafferLevel stafferLevel;
    private String[] aliases;

    Rank(int id, String name, String displayName, String chatDisplay, String color, boolean partner, BenefitLevel benefitLevel, StafferLevel stafferLevel, String... aliases) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.chatDisplay = chatDisplay;
        this.color = color;
        this.partner = partner;
        this.benefitLevel = benefitLevel;
        this.stafferLevel = stafferLevel;
        this.aliases = aliases;
    }

    private Rank() {
    }

    public static final Rank[] values;

    static {
        values = values();
    }

    public static Rank getByName(String name) {
        return Arrays.stream(values()).filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Rank getById(int id) {
        return Arrays.stream(values()).filter(rank -> rank.getId() == id).findFirst().orElse(null);
    }

    public static Rank getByAliases(String name) {
        return Arrays.stream(values()).filter(rank -> Arrays.stream(rank.getAliases()).anyMatch(aliase -> aliase.equalsIgnoreCase(name))).findFirst().orElse(null);
    }

    public static List<Rank> getStaffers() {
        return Arrays.stream(values()).filter(rank -> rank.getStafferLevel() != null).collect(Collectors.toList());
    }

    public static List<Rank> getRanksByBenefitLevel(BenefitLevel benefitLevel) {
        return Arrays.stream(values()).filter(rank -> rank.getBenefitLevel().equals(benefitLevel)).collect(Collectors.toList());
    }

    public static List<Rank> getRanksByStafferLevel(StafferLevel stafferLevel) {
        return Arrays.stream(values()).filter(rank -> {

            if (rank.getStafferLevel() != null) {
                return rank.getStafferLevel().equals(stafferLevel);
            }

            return false;
        }).collect(Collectors.toList());
    }

    public static List<Rank> getPartners() {
        return Arrays.stream(values()).filter(Rank::isPartner).collect(Collectors.toList());
    }

    public boolean isAboveOrEquals(Rank rank) {
        return rank.getId() >= this.getId();
    }

    public enum BenefitLevel {
        BASIC, PREMIUM, ELITE, SUPERIOR;
    }

    public enum StafferLevel {
        JUNIOR, MID, SENIOR, MANAGERIAL, EXECUTIVE
    }
}
