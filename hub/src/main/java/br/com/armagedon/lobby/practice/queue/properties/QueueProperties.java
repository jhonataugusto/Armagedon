package br.com.armagedon.lobby.practice.queue.properties;

import lombok.Data;

@Data
public class QueueProperties {
    private String mode;
    private boolean ranked;

    public boolean compareProperties(Object obj) {

        if(obj == this) {
            return false;
        }

        if (!(obj instanceof QueueProperties)) return false;

        QueueProperties otherProperties = (QueueProperties) obj;

        if(otherProperties.getMode() == null) return false;

        return this.getMode().equals(otherProperties.getMode()) && (this.isRanked() && otherProperties.isRanked());
    }

    public int compareTo(Object obj) {
        if (!(obj instanceof QueueProperties)) return -1;

        QueueProperties otherProperties = (QueueProperties) obj;

        if(otherProperties.getMode() == null) return -1;

        if(this.getMode().equals(otherProperties.getMode()) && (this.isRanked() && otherProperties.isRanked())){
            return 0;
        }
        return -1;
    }}
