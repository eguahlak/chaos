/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.chaos;

import dk.cphbusiness.utils.Plane;
import dk.cphbusiness.utils.Plane.Point;
import dk.cphbusiness.utils.PlaneObserver;
import static java.lang.Math.sqrt;

/**
 *
 * @author anders
 */
public class FractalDrawer implements PlaneObserver {
  public static final int COAST = 0;
  public static final int SKIES = 1;
  public static final int TREE = 2;
  public static final int XMAS = 3;
  public static final int PARSIL = 4;
  
  
  private void drawFigure(Plane plane, int depth, double x0, double y0, double x1, double y1, double... points) {
    double dx = x1 - x0; // 2.0
    double dy = y1 - y0; // 0.0
    double l = sqrt(dx*dx + dy*dy); // 2.0
    for (int i = 0; i < points.length; i += 4) {
      double px = points[i];
      double py = points[i + 1];
      double xn = dx*px - dy*py + x0;
      double yn = dy*px + dx*py + y0;
      if (depth <= 1) plane.moveTo(xn, yn);
      double qx = points[i + 2];
      double qy = points[i + 3];
      double xm = dx*qx - dy*qy + x0;
      double ym = dy*qx + dx*qy + y0;
      if (depth <= 1) plane.lineTo(xm, ym);
      else drawFigure(plane, depth - 1, xn, yn, xm, ym, points);
      }
    }
  
  int initialDepth = 0;
  int subtype = COAST;
  
  private void draw(Plane plane, int depth) {
    plane.clear();
    switch (subtype) {
    case SKIES: 
      drawFigure(plane, initialDepth, -2.0, 0.0, 2.0, 0.0,
          0.0, 0.0, 0.6, 0.1,
          0.0, 0.0, 0.5, -0.3,
          0.5, -0.3, 1.0, 0.0
          );
      break;
    case TREE:
      drawFigure(plane, initialDepth, -2.0, 0.0, 2.0, 0.0,
          0.0, 0.0, 0.4, 0.0,
          0.4, 0.0, 0.5, 0.5,
          0.5, 0.5, 0.6, 0.0,
          0.6, 0.0, 1.0, 0.0
          );
      break;
    case COAST:
      drawFigure(plane, initialDepth, -2.0, 0.0, 2.0, 0.0,
          0.0, 0.0, 0.4, 0.2,
          0.4, 0.2, 0.6, -0.2,
          0.6, -0.2, 1.0, 0.0
          );
      break;
    case XMAS:
      drawFigure(plane, initialDepth, -2.0, 0.0, 2.0, 0.0,
          0.0, 0.0, 0.7, 0.1,
          0.0, 0.0, 0.7, -0.08,
          0.3, 0.02, 1.0, 0.0
          );
      break;
    case PARSIL:
      drawFigure(plane, initialDepth, -2.0, 0.0, 2.0, 0.0,
          0.0, 0.0, 0.5, 0.0,
          0.5, 0.0, 0.95, 0.2,
          0.5, 0.0, 0.95, -0.2,
          0.5, 0.0, 1.0, 0.0
          );
      break;
      }
    plane.repaint();
    }
  
  @Override
  public void next(Plane plane) {
    initialDepth++;
    draw(plane, initialDepth);
    }

  @Override
  public void first(Plane plane) {
    initialDepth = 1;
    draw(plane, initialDepth);
    }

  @Override
  public void previous(Plane plane) {
    initialDepth--;
    if (initialDepth < 1) initialDepth = 1;
    draw(plane, initialDepth);
    }
  
  @Override
  public void boxSelected(Plane.Box box) {
    }

  @Override
  public void setSubtype(int value) {
    subtype = value;
    }

  @Override
  public String[] getSubtypes() {
    return new String[] { "Coast", "Skies", "Tree", "X-mas", "Parsil" };
    }

  @Override
  public void movedTo(Point point) {
    }

  @Override
  public void pointSelected(Point point) {
    }
  
  }
