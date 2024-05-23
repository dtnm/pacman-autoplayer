package src;

import java.util.ArrayList;

public abstract class CompositeMovementStrategy implements IMovementStrategy {
    private ArrayList<IMovementStrategy> list = new ArrayList<>();
    @Override
    public void move(PacActor pacActor) {
    }

    public void add(IMovementStrategy strategy){
        this.list.add(strategy);
    }
}
