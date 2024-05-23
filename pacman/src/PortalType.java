package src;

public enum PortalType {
    PortalYellow,
    PortalDarkGray,
    PortalDarkGold,
    PortalWhite;

    public String getImageName() {
        switch (this) {
            case PortalYellow: return "j_portalYellowTile.png";
            case PortalDarkGray: return "l_portalDarkGrayTile.png";
            case PortalDarkGold: return "k_portalDarkGoldTile.png";
            case PortalWhite: return "i_portalWhiteTile.png";
            default: {
                assert false;
            }
        }
        return null;
    }
}