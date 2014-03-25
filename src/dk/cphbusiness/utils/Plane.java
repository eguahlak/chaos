package dk.cphbusiness.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import static java.lang.Math.*;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Plane extends JPanel implements MouseListener, MouseMotionListener {
  private PlaneObserver observer = null;
  private BufferedImage showing = null;
  private BufferedImage saving = null;
  
  private int width = 0;
  private int height = 0;
  private double factor = 0.005;
  private Point2D.Double origo = null;
  private Color paperColor = new Color(255, 255, 255);
  private Color axisColor = new Color(200, 200, 255);
  private Color gridColor = new Color(230, 240, 255);
  private Color lineColor = Color.RED;
  private boolean showAxis = true;
  private Point position = null; 
  
  public Plane() {
    addMouseListener(this);
    addMouseMotionListener(this);
    }

  public void setObserver(PlaneObserver value) {
    observer = value;
    factor = 0.005;
    origo = new Point2D.Double(-factor*width/2, -factor*height/2);
    clear();
    repaint();
    }
  
  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    this.width = width;
    this.height = height;
    if (showing == null)
        showing = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    if (origo == null) origo = new Point2D.Double(-factor*width/2, -factor*height/2);
    }

  private boolean outside(int x, int y) {
    if (showing == null) return true;
    if (x < 0 || showing.getWidth() <= x) return true;
    if (y < 0 || showing.getHeight() <= y) return true;
    return false;
    }
  
  public void setPixel(int x, int y, Color color) {
    if (outside(x, y)) return;
    showing.setRGB(x, y, color.getRGB());
    }
  
  public Color getPixel(int x, int y) {
    if (outside(x, y)) return Color.WHITE;
    return new Color(showing.getRGB(x, y));
    }

  public void setLineColor(Color value) {
    lineColor = value;
    }
  
  public Point moveTo(double x, double y) {
    if (position == null) position = new Point(0, 0);
    position.x = x;
    position.y = y;
    position.downdate();
    return position;
    }
  
  public Point lineTo(double x, double y) {
    if (position == null) position = new Point(0, 0);
    int px = getPixelXOf(x);
    int py = getPixelYOf(y);
    if (showing != null) {
      Graphics2D graphics = showing.createGraphics();
      graphics.setColor(lineColor);
      graphics.drawLine(position.px, position.py, px, py);
      graphics.dispose();
      }
    position.px = px;
    position.py = py;
    position.update();
    return position;
    }
  
  public void clear() {
    if (showing == null) return;
    Graphics2D graphics = showing.createGraphics();
    graphics.fillRect(0, 0, width - 1, height - 1);
    graphics.setColor(axisColor);
    double x0 = ceil(origo.x);
    double xw = floor(x0 + width*factor);
    double y0 = ceil(origo.y);
    double yh = floor(y0 + height*factor);
    while (x0 <= xw) {
      int px = getPixelXOf(x0);
      if (abs(x0) < 0.001) graphics.setColor(axisColor);
      else graphics.setColor(gridColor);
      graphics.drawLine(px, 0, px, height);
      x0 += 1.0;
      }
    while (y0 <= xw) {
      int py = getPixelYOf(y0);
      if (abs(y0) < 0.001) graphics.setColor(axisColor);
      else graphics.setColor(gridColor);
      graphics.drawLine(0, py, width, py);
      y0 += 1.0;
      }
    graphics.dispose();
    }
  
  @Override
  public void paintComponent(Graphics g) {
    if (showing == null) return;
    Graphics2D graphics = (Graphics2D)g.create();
    graphics.drawImage(showing, null, 0, 0);
    if (selection != null) {
      graphics.setColor(Color.YELLOW);
      graphics.drawRect(selection.x, selection.y, selection.width, selection.height);
      }
    graphics.dispose();
    }

  public double getFactor() {
    return factor;
    }

  public void setFactor(double factor) {
    this.factor = factor;
    }
  
  private int getPixelXOf(double x) {
    return (int)((x - origo.x)/factor);
    }
  
  private int getPixelYOf(double y) {
    return height - (int)((y - origo.y)/factor);
    }
  
  private double getXOf(int px) {
    return factor*px + origo.x;
    }
  
  private double getYOf(int py) {
    return factor*(height - py)+ origo.y;
    }
  
  public Box reposition(Box box) {
    double f = factor*(box.right - box.left)/width;
    origo.x = getXOf(box.left);
    origo.y = getYOf(box.bottom);
    factor = f;
    box.left = 0;
    box.top = 0;
    box.right = width - 1;
    box.bottom = height - 1;
    return box;
    }
  
  public Box getBox(double x, double y, double width, double height) {
    return new Box(x, y, width, height);
    }

  private Rectangle selection = null;
  
  @Override
  public void mouseClicked(MouseEvent me) { }

  @Override
  public void mousePressed(MouseEvent me) {
    if (SwingUtilities.isRightMouseButton(me)) {
      observer.pointSelected(new Plane.Point(me.getX(), me.getY()));
      }
    else selection = new Rectangle(me.getX(), me.getY(), 0, 0);
    }

  @Override
  public void mouseReleased(MouseEvent me) {
    if (selection != null && observer != null) {
      observer.boxSelected(new Box(selection));
      }
    selection = null;
    repaint();
    }

  @Override
  public void mouseEntered(MouseEvent me) { }

  @Override
  public void mouseExited(MouseEvent me) { }

  @Override
  public void mouseDragged(MouseEvent me) {
    if (selection == null) selection = new Rectangle(me.getX(), me.getY(), 0, 0);
    int x = me.getX();
    int y = me.getY();
    if (x < selection.x) {
      selection.width = selection.x - x;
      selection.x = x;
      }
    else selection.width = x - selection.x;
    if (y < selection.y) {
      selection.height = selection.y - y;
      selection.y = y;
      }
    else selection.height = y - selection.y;
    this.repaint();
    }

  @Override
  public void mouseMoved(MouseEvent me) {
    if (observer != null) observer.movedTo(new Point(me.getX(), me.getY()));
    }
  
  public class Point {
    int px;
    int py;
    private double x;
    private double y;
  
    Point(int px, int py) {
      this.px = px;
      this.py = py;
      update();
      }
    
    private void update() {
      x = getXOf(px);
      y = getYOf(py);
      }

    void downdate() {
      px = getPixelXOf(x);
      py = getPixelYOf(y);
      }
    
    public double getX() {
      return x;
      }

    public double getY() {
      return y;
      }
    
    public void setColor(Color color) {
      setPixel(px, py, color);
      }
    
    public Color getColor() {
      return getPixel(px, py);
      }
    
    public Plane getPlane() { return Plane.this; }
    }
  
  public class Box implements Iterable<Point> {
    private int left;
    private int top;
    private int right;
    private int bottom;

    public Box(double x, double y, double width, double height) {
      left = getPixelXOf(x);
      bottom = getPixelYOf(y);
      right = left + (int)(width/factor);
      top = bottom - (int)(height/factor);
      }
    
    public Box() {
      left = 0;
      top = 0;
      right = Plane.this.width - 1;
      bottom = Plane.this.height - 1;
      }
    
    private Box(Rectangle area) {
      this.left = area.x;
      this.top = area.y;
      this.right = area.x + area.width;
      this.bottom = area.y + area.height;
      }

    public Plane getPlane() {
      return Plane.this;
      }
    
    public Box fillPlane() {
      return reposition(this);
      }
    
    @Override
    public String toString() {
      return "("+left+"; "+top+") - ("+right+"; "+bottom+")";
      }
    
    @Override
    public Iterator<Point> iterator() {
      return new Iterator<Point>() {
        private Point current;
        {
          current = new Point(left - 1, top);
        }

        @Override
        public boolean hasNext() {
          return (current.px < right || current.py < bottom);
          }

        @Override
        public Point next() {
          if (current.px < right) current.px++;
          else if (current.py < bottom) {
            current.px = left;
            current.py++;
            }
          else throw new RuntimeException("Out of box");
          current.update();
          return current; 
          }

        @Override
        public void remove() {
          throw new UnsupportedOperationException("Remove is not supported");
          }
        
        };
      }
    
    }
  
  }
