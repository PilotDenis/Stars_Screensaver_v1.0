package com.ball;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Model {
    public static float INITIAL_Z_COORD = -3;
    public static float MOTION_SPEED = 0.001f;
    private static final int PLANET_MAX_RADIUS = 50;

    List<Point> points = Collections.synchronizedList(new ArrayList<Point>());

    public void update(long elapsedTime) {
        float birthChance = 0.3f;
        int r;
        int cr = (int) (155 + random(0, 100));
        int cg = (int) (75 + random(0, 180));
        int cb = (int) (random(0, 50));

        synchronized (points) {
            r = (int) random(0, PLANET_MAX_RADIUS);
            if (random(0, 1) < birthChance) {
                points.add(new Point(random(-1, 1), random(-1, 1), INITIAL_Z_COORD, r, new Color(cr, cg, cb)));
            }

//--        Вычисляем приближение планеты по оси Z
            ListIterator<Point> iterator = points.listIterator();
            while (iterator.hasNext()) {
                Point p = iterator.next();
                p.z += elapsedTime * MOTION_SPEED;

//--            Если координата z доросла до нуля или выше, то такую планету удаляем, так как она уже дошла до плоскости изображения
                if (p.z >= 0) {
                    iterator.remove();
                } else {
                    iterator.set(p);
                }
            }
        }
    }

    private float random(float from, float to) {
        return (float) (from + (to - from) * Math.random());
    }

    public List<Point> getPoints() {
        return points;
    }
}
