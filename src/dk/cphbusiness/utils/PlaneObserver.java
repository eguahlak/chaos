/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.utils;

/**
 *
 * @author anders
 */
public interface PlaneObserver {
  void pointSelected(Plane.Point point);
  void boxSelected(Plane.Box box);
  void next(Plane plane);
  void previous(Plane plane);
  void first(Plane plane);
  void setSubtype(int type);
  String[] getSubtypes();
  void movedTo(Plane.Point point);
  }
