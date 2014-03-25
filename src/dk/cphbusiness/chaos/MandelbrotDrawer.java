/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.chaos;

import dk.cphbusiness.utils.Complex;
import dk.cphbusiness.utils.Plane;
import dk.cphbusiness.utils.Plane.Box;
import dk.cphbusiness.utils.Plane.Point;
import dk.cphbusiness.utils.PlaneObserver;
import java.awt.Color;

/**
 *
 * @author anders
 */
public class MandelbrotDrawer implements PlaneObserver {
  private Color[] colors = new Color[1024];

  public MandelbrotDrawer() {
    for (int i = 0; i < 1024; i++) {
      colors[i] = new Color((127 + (i*16))%256, (i*7)%256, (i*3)%256);
      // colors[i] = new Color(i/4, i/4, i/4);
      }
    }

  @Override
  public void setSubtype(int type) { }

  @Override
  public void next(Plane plane) {
    System.out.println("MANDELBROT NEXT");
    }

  @Override
  public void first(Plane plane) { }

  @Override
  public void previous(Plane plane) { }
  
  @Override
  public void boxSelected(Box box) {
    box.fillPlane();
    Complex z = new Complex(0.0, 0.0);
    Complex c = new Complex(0.0, 0.0);
    for (Plane.Point point : box) {
      z.set(0.0, 0.0);
      c.set(point.getX(), point.getY());
      int k = 0;
      while (z.squareModulus() < 100.0 && k < 1024) {
        z = z.powered(2).added(c);
        k++;
        }
      if (k == 1024) point.setColor(Color.BLACK);
      else point.setColor(colors[k]);
      }
    }

  @Override
  public String[] getSubtypes() {
    return new String[0];
    }

  @Override
  public void movedTo(Point point) {
    }

  @Override
  public void pointSelected(Point point) {
    Complex c = new Complex(point.getX(), point.getY());
    Complex z = new Complex(point.getX(), point.getY());
    Plane plane = point.getPlane();
    plane.moveTo(z.getRe(), z.getIm());
    for (int i = 0; i < 10; i++) {
      z = z.powered(2).added(c);
      plane.lineTo(z.getRe(), z.getIm());
      }
    plane.repaint();
    }
  
  }
