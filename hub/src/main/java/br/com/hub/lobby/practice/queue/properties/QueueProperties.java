package br.com.hub.lobby.practice.queue.properties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueueProperties {
    private String mode;
    private boolean ranked;

    public boolean compareProperties(Object obj) {

        if (obj == this) {
            return false;
        }

        if (!(obj instanceof QueueProperties)) return false;

        QueueProperties otherProperties = (QueueProperties) obj;

        if (otherProperties.getMode() == null) return false;

        return this.getMode().equals(otherProperties.getMode()) && (this.isRanked() && otherProperties.isRanked());
    }

    public int compareTo(Object obj) {
        if (!(obj instanceof QueueProperties)) return -1;

        QueueProperties otherProperties = (QueueProperties) obj;

        if (otherProperties.getMode() == null) return -1;

        boolean modeAreEquals = this.getMode().equals(otherProperties.getMode());
        boolean isSameRankedMode = this.isRanked() == otherProperties.isRanked();

        if (modeAreEquals && isSameRankedMode) {
            return 0;
        }
        return -1;
    }
}
