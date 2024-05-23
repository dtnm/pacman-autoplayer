package src;

public class TorusVerseMovementStrategy extends CompositeMovementStrategy {
    ShortestPathMovementStrategy shortestPathMovementStrategy = new ShortestPathMovementStrategy();

    @Override

    public void move(PacActor pacActor) {
        shortestPathMovementStrategy.move(pacActor);
    }
}
