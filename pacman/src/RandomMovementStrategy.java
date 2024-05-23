package src;

import ch.aplu.jgamegrid.Location;

import java.util.Random;

public class RandomMovementStrategy implements IMovementStrategy {
    private Random randomiser = new Random();
    @Override
    public void move(PacActor pacActor) {
        double oldDirection = pacActor.getDirection();
        int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;
        pacActor.setDirection(oldDirection);
        pacActor.turn(sign * 90);  // Try to turn left/right
        Location next = pacActor.getNextMoveLocation();
        if (pacActor.canMove(next)) {
            pacActor.setLocation(next);
        } else {
            pacActor.setDirection(oldDirection);
            next = pacActor.getNextMoveLocation();
            if (pacActor.canMove(next)) { // Try to move forward
                pacActor.setLocation(next);
            } else {
                pacActor.setDirection(oldDirection);
                pacActor.turn(-sign * 90);  // Try to turn right/left
                next = pacActor.getNextMoveLocation();
                if (pacActor.canMove(next)) {
                    pacActor.setLocation(next);
                } else {
                    pacActor.setDirection(oldDirection);
                    pacActor.turn(180);  // Turn backward
                    next = pacActor.getNextMoveLocation();
                    pacActor.setLocation(next);
                }
            }
        }
        pacActor.eatPill(next);
        pacActor.checkPortal(next);
        pacActor.addVisitedList(next);
    }
}
