package dk.cphbusiness.chaos;

import dk.cphbusiness.utils.Plane;
import dk.cphbusiness.utils.PlaneObserver;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.GroupLayout;

public class ChaosViewer extends JFrame {
  private JMenuBar menu;
  private JMenu fileMenu;
  private JMenu doMenu;
  private JMenu modelMenu;
  private JMenu typeMenu;
  private JMenuItem exitMenuItem;
  private JMenuItem nextMenuItem;
  private JMenuItem previousMenuItem;
  private JMenuItem firstMenuItem;
  private Plane canvas;
  private final Map<String, PlaneObserver> models = new HashMap<>();
  private PlaneObserver model;

  public ChaosViewer() {
    initialize();
    setModel(models.get("Line fractals"));
    }
  
  private void setModel(PlaneObserver model) {
    typeMenu.removeAll();
    String[] typeNames = model.getSubtypes();
    for (int index = 0; index < typeNames.length; index++) {
      JMenuItem typeMenuItem = new JMenuItem(typeNames[index]);
      typeMenuItem.addActionListener(new TypeSelector(index));
      typeMenu.add(typeMenuItem);
      }
    canvas.setObserver(model);
    model.boxSelected(canvas.new Box());
    canvas.repaint();
    //model.first(canvas);
    }
  
  private class TypeSelector implements ActionListener {
    private final int type;
    
    public TypeSelector(int type) {
      this.type = type;
      }

    @Override
    public void actionPerformed(ActionEvent ae) {
      model.setSubtype(type);
      model.first(canvas);
      }
    
    }
  
  private class ModelSelector implements ActionListener {
    private final String modelName;

    public ModelSelector(String modelName) {
      this.modelName = modelName;
      }

    @Override
    public void actionPerformed(ActionEvent ae) {
      System.out.println("Model "+modelName+" selected");
      model = models.get(modelName);
      setModel(model);
      }
    
    }
  
  private void addModel(String name, PlaneObserver model) {
    models.put(name, model);
    JMenuItem modelMenuItem = new JMenuItem(name);
    modelMenuItem.addActionListener(new ModelSelector(name));
    modelMenu.add(modelMenuItem);
    }
  
  private void initialize() {
    canvas = new Plane();
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    menu = new JMenuBar();
    fileMenu = new JMenu("File");
    fileMenu.setMnemonic(KeyEvent.VK_F);
    exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.setMnemonic(KeyEvent.VK_X);
    exitMenuItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          System.exit(0);
          }
        });
    fileMenu.add(exitMenuItem);
    menu.add(fileMenu);
    doMenu = new JMenu("Go");
    nextMenuItem = new JMenuItem("Next");
    nextMenuItem.setMnemonic(KeyEvent.VK_SPACE);
    nextMenuItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
          if (model != null) model.next(canvas);
          }
        });
    previousMenuItem = new JMenuItem("Previous");
    previousMenuItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
          if (model != null) model.previous(canvas);
          }
        });
    firstMenuItem = new JMenuItem("First");
    firstMenuItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
          if (model != null) model.first(canvas);
          }
        });
    doMenu.add(firstMenuItem);
    doMenu.add(previousMenuItem);
    doMenu.add(nextMenuItem);
    menu.add(doMenu);
    modelMenu = new JMenu("Models");
    // Add models here
    addModel("Mandelbrot", new MandelbrotDrawer());
    addModel("Line fractals", new FractalDrawer());
    menu.add(modelMenu);
    typeMenu = new JMenu("Type");
    menu.add(typeMenu);
    setJMenuBar(menu);

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup().addComponent(canvas, 1000, 1000, 1000)
        //layout.createParallelGroup().add(canvas, 1000, 1000, 1000)
        );
    layout.setVerticalGroup(
        layout.createParallelGroup().addComponent(canvas, 800, 800, 800)
        // layout.createParallelGroup().add(canvas, 800, 800, 800)
        );
    pack();
    }
  
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
          new ChaosViewer().setVisible(true);
          }
        });
    }

  }
