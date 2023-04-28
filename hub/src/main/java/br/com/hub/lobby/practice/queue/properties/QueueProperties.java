package br.com.hub.lobby.practice.queue.properties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class QueueProperties {
    private String mode;
    private boolean ranked;

    private int elo;
    private int maxElo;
    private int minElo;

    public QueueProperties(String mode, boolean ranked, int elo) {
        this.mode = mode;
        this.ranked = ranked;
        this.elo = elo;
        this.maxElo = elo + 100;
        this.minElo = elo - 100;
    }

    public boolean compareProperties(Object obj) {

        if (obj == this) {
            return false;
        }

        if (!(obj instanceof QueueProperties)) return false;

        QueueProperties otherProperties = (QueueProperties) obj;

        if (otherProperties.getMode() == null) return false;
        boolean modeAreEquals = this.getMode().equals(otherProperties.getMode());
        boolean isSameRankedMode = this.isRanked() == otherProperties.isRanked();

        return modeAreEquals && isSameRankedMode;
    }

    public int compareTo(QueueProperties otherProperties) {
        if (otherProperties == null || otherProperties.getMode() == null) {
            throw new ClassCastException("O objeto a ser comparado não é uma instância de QueueProperties");
        }

        boolean modeAreEquals = this.getMode().equals(otherProperties.getMode());
        boolean isSameRankedMode = this.isRanked() == otherProperties.isRanked();

        if (modeAreEquals && isSameRankedMode && !this.isRanked()) {
            return 0;
        }

        if (modeAreEquals && isSameRankedMode && inEloRange(otherProperties)) {
            return 0;
        }

        return -1;
    }

    public boolean inEloRange(QueueProperties otherProperties) {
        return otherProperties.getMaxElo() >= this.getMinElo() && otherProperties.getMinElo() <= this.getMaxElo();
    }
}
