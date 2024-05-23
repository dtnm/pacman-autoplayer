// PacActor.java
// Used for PacMan
package src;

import ch.aplu.jgamegrid.*;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.util.*;
import java.util.random.RandomGenerator;

public class PacActor extends Actor implements GGKeyRepeatListener {
  private static final int nbSprites = 4;
  private int idSprite = 0;
  private int nbPills = 0;
  private int score = 0;
  private final Game game;
  private final ArrayList<Location> visitedList = new ArrayList<>();
  private List<String> propertyMoves = new ArrayList<>();
  private int propertyMoveIndex = 0;
  private final Random randomiser = new Random();
  private boolean isAuto = false;
  private IMovementStrategy movementStrategy;

  public PacActor(Game game) {
    super(true, "sprites/pacpix.gif", nbSprites);  // Rotatable
    this.game = game;
    this.movementStrategy = new ManualMovementStrategy();
  }

  public void setAuto(boolean auto) {
    isAuto = auto;
    if (isAuto) {
      this.movementStrategy = new TorusVerseMovementStrategy();
    } else {
      this.movementStrategy = new ManualMovementStrategy();
    }
  }

  public void setSeed(int seed) {
    randomiser.setSeed(seed);
  }
  public void setPropertyMoves(String propertyMoveString) {
    if (propertyMoveString != null) {
      this.propertyMoves = Arrays.asList(propertyMoveString.split(","));
    }
  }

  public void keyRepeated(int keyCode) {
    if (isAuto) {
      return;
    }
    if (isRemoved()) {  // Already removed
      return;
    }
    Location next = null;
    switch (keyCode) {
      case KeyEvent.VK_LEFT -> {
        next = getLocation().getNeighbourLocation(Location.WEST);
        setDirection(Location.WEST);
      }
      case KeyEvent.VK_UP -> {
        next = getLocation().getNeighbourLocation(Location.NORTH);
        setDirection(Location.NORTH);
      }
      case KeyEvent.VK_RIGHT -> {
        next = getLocation().getNeighbourLocation(Location.EAST);
        setDirection(Location.EAST);
      }
      case KeyEvent.VK_DOWN -> {
        next = getLocation().getNeighbourLocation(Location.SOUTH);
        setDirection(Location.SOUTH);
      }
    }
    if (next != null && canMove(next)) {
      setLocation(next);
      eatPill(next);
      checkPortal(next);
    }
  }

  void checkPortal(Location location) {
    ArrayList<Portal> portals = game.getPortals();
    for (Portal portal : portals) {
      if (portal.getLocation().equals(location)) {
        teleport(portal.getPortalType());
        break;
      }
    }
  }

  private void teleport(PortalType portalType) {
    ArrayList<Portal> portals = game.getPortals();
    for (Portal portal : portals) {
      if (portal.getPortalType().equals(portalType) && !portal.getLocation().equals(getLocation())) {
        setLocation(portal.getLocation());
        eatPill(portal.getLocation());
        break;
      }
    }
  }

  public void act() {
    show(idSprite);
    idSprite++;
    if (idSprite == nbSprites) {
      idSprite = 0;
    }

    if (isAuto) {
      moveInAutoMode();
//      System.out.println(game.getPillAndItemLocations());
    }
    this.game.getGameCallback().pacManLocationChanged(getLocation(), score, nbPills);
  }
  protected Location closestPillLocation() {
    int currentDistance = 1000;
    Location currentLocation = null;
    List<Location> pillAndItemLocations = game.getPillAndItemLocations();
//    System.out.println(pillAndItemLocations);
    for (Location location: pillAndItemLocations) {
      int distanceToPill = location.getDistanceTo(getLocation());
      if (distanceToPill < currentDistance) {
        currentLocation = location;
        currentDistance = distanceToPill;
      }
    }
    return currentLocation;
  }

  private void followPropertyMoves() {
    String currentMove = propertyMoves.get(propertyMoveIndex);
    switch (currentMove) {
      case "R" -> turn(90);
      case "L" -> turn(-90);
      case "M" -> {
        Location next = getNextMoveLocation();
        if (canMove(next)) {
          setLocation(next);
          eatPill(next);
        }
      }
    }
    propertyMoveIndex++;
  }

  public void moveInAutoMode() {
    movementStrategy.move(this);
  }
  void addVisitedList(Location location) {
    visitedList.add(location);
    int listLength = 10;
    if (visitedList.size() == listLength) {
      visitedList.remove(0);
    }
  }

  boolean canMove(Location location) {
    Color c = getBackground().getColor(location);
    return !c.equals(Color.gray) && location.getX() < game.getNumHorzCells()
            && location.getX() >= 0 && location.getY() < game.getNumVertCells() && location.getY() >= 0;
  }

  public int getNbPills() {
    return nbPills;
  }

  void eatPill(Location location) {
    Color c = getBackground().getColor(location);
    if (c.equals(Color.white)) {
      nbPills++;
      score++;
      getBackground().fillCell(location, Color.lightGray);
      game.getGameCallback().pacManEatPillsAndItems(location, "pills");
    } else if (c.equals(Color.yellow)) {
      nbPills++;
      score += 5;
      getBackground().fillCell(location, Color.lightGray);
      game.getGameCallback().pacManEatPillsAndItems(location, "gold");
      game.removeItem("gold", location);
    } else if (c.equals(Color.blue)) {
      getBackground().fillCell(location, Color.lightGray);
      game.getGameCallback().pacManEatPillsAndItems(location, "ice");
      game.removeItem("ice", location);
    }
    String title = "[PacMan in the Multiverse] Current score: " + score;
    gameGrid.setTitle(title);
  }

  protected List<Location> findShortestPath(Location start, Location goal) {
    PriorityQueue<Node> queue = new PriorityQueue<>();
    queue.add(new Node(start, 0, calculateManhattanDistance(start, goal)));

    // Create a map to track the visited locations and their parents
    // for reconstructing the path
    java.util.HashMap<Location, Location> parentMap = new java.util.HashMap<>();
    parentMap.put(start, null);

    // Create a set to track the visited locations
    Set<Location> visited = new HashSet<>();
    visited.add(start);

    while (!queue.isEmpty()) {
      Node current = queue.poll();
      Location currentLocation = current.location();
      if (currentLocation.equals(goal)) {
        return reconstructPath(parentMap, currentLocation);
      }

      List<Location> neighbors = getNeighbors(currentLocation);
      for (Location neighbor : neighbors) {
        if (!canMove(neighbor) || visited.contains(neighbor)) {
          continue;
        }

        int costSoFar = current.costSoFar() + 1;
        int estimatedTotalCost = costSoFar + calculateManhattanDistance(neighbor, goal);
        Node neighborNode = new Node(neighbor, costSoFar, estimatedTotalCost);

        queue.add(neighborNode);
        parentMap.put(neighbor, currentLocation);
        visited.add(neighbor);
      }
    }

    // If no path found, return an empty list
    return new ArrayList<>();
  }

  public RandomGenerator getRandomiser() {
    return null;
  }

  private record Node(Location location, int costSoFar,
                      int estimatedTotalCost) implements Comparable<Node> {
    @Override
    public int compareTo(Node other) {
      return Integer.compare(estimatedTotalCost, other.estimatedTotalCost);
    }
  }

  private int calculateManhattanDistance(Location source, Location target) {
    int dx = Math.abs(source.getX() - target.getX());
    int dy = Math.abs(source.getY() - target.getY());
    return dx + dy;
  }

  private List<Location> getNeighbors(Location location) {
    int x = location.getX();
    int y = location.getY();

    List<Location> neighbors = new ArrayList<>();
    neighbors.add(new Location(x - 1, y));     // Left
    neighbors.add(new Location(x + 1, y));     // Right
    neighbors.add(new Location(x, y - 1));     // Up
    neighbors.add(new Location(x, y + 1));     // Down
    neighbors.add(new Location(x - 1, y - 1)); // Diagonal: Top left
    neighbors.add(new Location(x + 1, y - 1)); // Diagonal: Top right
    neighbors.add(new Location(x - 1, y + 1)); // Diagonal: Bottom left
    neighbors.add(new Location(x + 1, y + 1)); // Diagonal: Bottom right

    return neighbors;
  }

  private List<Location> reconstructPath(java.util.HashMap<Location, Location> parentMap, Location goal) {
    List<Location> path = new ArrayList<>();
    Location current = goal;

    while (current != null) {
      path.add(0, current);
      current = parentMap.get(current);
    }

    return path;
  }
}