package src;

import ch.aplu.jgamegrid.Actor;

public class Portal extends Actor {
    private PortalType portalType;
    public Portal(PortalType portalType) {
        super("sprites/data/" + portalType.getImageName());
        this.portalType = portalType;
    }
    public PortalType getPortalType() {
        return portalType;
    }
}