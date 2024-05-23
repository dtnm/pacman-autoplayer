package src;

import ch.aplu.jgamegrid.Location;
import java.util.*;

public class ShortestPathMovementStrategy implements IMovementStrategy {
    @Override
    public void move(PacActor pacActor) {
        Location closestPill = pacActor.closestPillLocation();
        if (closestPill != null) {
            List<Location> path = pacActor.findShortestPath(pacActor.getLocation(), closestPill);
            if (path.size() > 1) {
                Location nextLocation = path.get(1);
                pacActor.setLocation(nextLocation);
                pacActor.eatPill(nextLocation);
                pacActor.checkPortal(nextLocation);
                pacActor.addVisitedList(nextLocation);
                return;
            }
        }

        // If no path found or no pills remaining, fall back to random movement
        RandomMovementStrategy randomMovementStrategy = new RandomMovementStrategy();
        randomMovementStrategy.move(pacActor);
    }
}