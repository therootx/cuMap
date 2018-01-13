package com.ahmet.showmustgoon;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Graph {

    private final Map<String, List<Vertex>> vertices;
    public Graph() {
        this.vertices = new HashMap<String, List<Vertex>>();
    }
    public void addVertex(String character, List<Vertex> vertex) {
        this.vertices.put(character, vertex);
    }

    public List<String> getShortestPath(String start, String finish) {
        final Map<String, Integer> distances = new HashMap<String, Integer>();
        final Map<String, Vertex> previous = new HashMap<String, Vertex>();
        PriorityQueue<Vertex> nodes = new PriorityQueue<Vertex>();

        for(String vertex : vertices.keySet()) {
            if (vertex .equals(start)) {
                distances.put(vertex, 0);
                nodes.add(new Vertex(vertex, 0));
            } else {
                distances.put(vertex, Integer.MAX_VALUE);
                nodes.add(new Vertex(vertex, Integer.MAX_VALUE));
            }
            previous.put(vertex, null);
        }
        final List<String> path = new ArrayList<String>();
        while (!nodes.isEmpty()) {
            Vertex smallest = nodes.poll();
            Log.e("Vertex Id ",smallest.getId());
            if (smallest.getId().equals(finish)) {
                while (previous.get(smallest.getId()) != null) {
                    path.add(smallest.getId());
                    smallest = previous.get(smallest.getId());
                }
                return path;
            }
            if (distances.get(smallest.getId()) == Integer.MAX_VALUE) {
                break;
            }
            for (Vertex neighbor : vertices.get(smallest.getId())) {
                Integer alt = distances.get(smallest.getId()) + neighbor.getDistance();
                if (alt < distances.get(neighbor.getId())) {
                    distances.put(neighbor.getId(), alt);
                    previous.put(neighbor.getId(), smallest);
                    forloop:
                    for(Vertex n : nodes) {
                        if (n.getId() == neighbor.getId()) {
                            nodes.remove(n);
                            n.setDistance(alt);
                            nodes.add(n);
                            break forloop;
                        }
                    }
                }
            }
        }
        return new ArrayList<String>(distances.keySet());
    }
}

class Vertex implements Comparable<Vertex> {

    private String id;
    private Integer distance;

    public Vertex(String id, Integer distance) {
        super();
        this.id = id;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((distance == null) ? 0 : distance.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (distance == null) {
            if (other.distance != null)
                return false;
        } else if (!distance.equals(other.distance))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Vertex [id=" + id + ", distance=" + distance + "]";
    }

    @Override
    public int compareTo(Vertex o) {
        if (this.distance < o.distance)
            return -1;
        else if (this.distance > o.distance)
            return 1;
        else
            return this.getId().compareTo(o.getId());
    }

}

