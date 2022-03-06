import java.util.*;

/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/
public class AStarState
{
    /** This is a reference to the map that the A* algorithm is navigating. **/
    private Map2D map;

    /** Создаём мапу со всеми открытыми точками и их расположением **/
    private Map<Location, Waypoint> open_waypoints
            = new HashMap<Location, Waypoint> ();

    /** Создаём мапу со всеми закрытыми точками и их расположением **/
    private Map<Location, Waypoint> closed_waypoints
            = new HashMap<Location, Waypoint> ();

    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;
    }

    /** Returns the map that the A* pathfinder is navigating. **/
    public Map2D getMap()
    {
        return map;
    }

    /** Возвращает текущее количество открытых точек **/
    public int numOpenWaypoints()
    {
        return open_waypoints.size();
    }
    /** Возвращает открытую точку с минимальной стоимостью **/
    public Waypoint getMinOpenWaypoint() // Если нет открытых точек, возвращает <code>null</code>.
    {
        if (numOpenWaypoints() == 0)
            return null;
        Set open_waypoint_keys = open_waypoints.keySet();
        Iterator i = open_waypoint_keys.iterator();
        Waypoint best = null;
        float best_cost = Float.MAX_VALUE;
        // Сканируем все открытые точки
        while (i.hasNext())
        {
            // Сохраняем текущее местоположение
            Location location = (Location)i.next();
            // Сохраняем текующую точку
            Waypoint waypoint = open_waypoints.get(location);
            // Записываем совокупную стоимость для текущей точки
            float waypoint_total_cost = waypoint.getTotalCost();
            // Если совокупная стоимость для текущей точки лучше или хуже,
            // чем стоимость записанной ранее лучшей точки,
            // заменяем прошлую точку и её стоимость новой точкой и её стоимостью
            if (waypoint_total_cost < best_cost)
            {
                best = open_waypoints.get(location);
                best_cost = waypoint_total_cost;
            }
        }
        // Возвращает точку с минимальной стоимостью
        return best;
    }
    /** Добавляет точку в список открытых точек **/
    public boolean addOpenWaypoint(Waypoint newWP)
    {
        // Находит местоположение новой точки
        Location location = newWP.getLocation();
        if (open_waypoints.containsKey(location))
        {
            // Сравнивает, меньше ли стоимость новой точки, относительно текущей
            Waypoint current_waypoint = open_waypoints.get(location);
            if (newWP.getPreviousCost() < current_waypoint.getPreviousCost())
            {
                // Заменяет старую точку и возвращает true
                open_waypoints.put(location, newWP);
                return true;
            }
            // Если у новой точки предыдущая стоимость не меньше, чем у текущей, возвращает false
            return false;
        }
        // Если условия выше не соблюдаются, просто добавляет новую точку в список открытых точек и возвращает true
        open_waypoints.put(location, newWP);
        return true;
    }
    /** Возвращает true, если в списке закрытых точек содержится точка назначения **/
    public boolean isLocationClosed(Location loc)
    {
        return closed_waypoints.containsKey(loc);
    }
    /** Перемещает точку назначения из списка открытых точек в список закрытых точек **/
    public void closeWaypoint(Location loc)
    {
        Waypoint waypoint = open_waypoints.remove(loc);
        closed_waypoints.put(loc, waypoint);
    }
}

