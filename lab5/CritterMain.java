// CSE 142 Homework 8 (Critters)
// Authors: Marty Stepp, Stuart Reges
//
// Provides the main method for the simulation.
//
// YOU DON'T NEED TO EDIT THIS FILE FOR YOUR ASSIGNMENT.
//
import java.awt.*;
import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.security.*;
import java.util.*;
import java.util.List;
import java.util.zip.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class CritterMain {
   public static void main(String[] args) {
      CritterGui.createGui();
   }
}


// CSE 142 Homework 8 (Critters)
// Authors: Marty Stepp, Stuart Reges
//
// The overall graphical user interface for the Critter simulation.
//

class CritterGui implements ActionListener, ChangeListener,
                            Observer, WindowListener {
	// class constants
   public static final String SAVE_STATE_FILE_NAME = "_critters_network_settings.txt";
   public static final boolean PRINT_EXCEPTIONS = true;
   public static final boolean SHOULD_SAVE_SETTINGS = true;
   public static final boolean DEFAULT_NETWORK_ENABLED = true;
   public static final boolean DEFAULT_DEBUG = false;

   private static final String TITLE = "CSE 142 Critters";
   private static final long serialVersionUID = 0;
   private static final int DELAY = 100; // default MS between redraws
   private static final int MAX_CLASS_NAME_LENGTH = 24;
   private static final boolean TOURNAMENT_MODE = false;

	// constants for saving/loading GUI state
   private static final String LAST_HOST_NAME_KEY = "lastHostName";
   private static final String FPS_KEY = "fps";
   private static final String ACCEPT_KEY = "accept";
   private static final String BACKGROUND_COLORS_KEY = "backgroundColors";
   // private static final String DEBUG_KEY = "debug";
   private static final String ALWAYS_VALUE = "always";
   private static final String ASK_VALUE = "ask";
   private static final String NEVER_VALUE = "never";

	// constant for loading files from the course web site
   private static final String ZIP_CODE_BASE = "http://webster.cs.washington.edu/facebook/critters/huskies/huskies.zip";
   private static final Font STATUS_FONT = new Font("monospaced", Font.PLAIN, 12);
   private static final Font CLASS_FONT = new Font("sansserif", Font.BOLD, 12);

   static {
      try {
      	// make the GUI look like your operating system
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      
      	// set security manager so students can't do crazy things
         if (TOURNAMENT_MODE) {
            System.setSecurityManager(new CritterSecurityManager());
         }
      } catch (Exception e) {
      }
   }

	// This is basically the main method that makes the GUI and starts the program.
	// I am "hiding" it here for student readability.
   public static void createGui() {
      CritterClassVerifier.checkForSillyMethods();
      CritterGui gui = CritterClassVerifier.initialSettings();
   
      if (gui == null) {
      	// user canceled
         try {
            System.exit(0);
         } catch (Exception e) {
         }
      } else {
         gui.start(); // run the GUI
      }
   }

	// fields
   private CritterModel model;
   private CritterPanel panel;
   private JFrame frame;
   private javax.swing.Timer timer;
   private JButton go, stop, tick, reset, loadFromWeb;
   private JSlider slider;
   private JComponent east;
   private JRadioButton always, never, ask;
   private String lastHostName = "";
   private JLabel moves;
   private JCheckBox backgroundColors;
   private JCheckBox debug;
   private boolean enableNetwork;
   private boolean enableLoadFromWeb;
   private boolean enableSendRequest;

	// receives critters from others and lets me voluntarily send mine out
	// packets = [hostname, classname, critter text]
   private CritterNetworkManager networkSenderListener;

	// lets me host my wolf so others can reach out and request it from me
	// packets = [hostname, classname requested]
   private CritterNetworkManager networkServer;

	// keeps track of which classes already have a ClassPanel on the east side
	// of the window, so we know when we need to add a new one (on network receive etc)
   private Map<String, ClassPanel> counts;

	// Constructs a new GUI to display the given model of critters.
   public CritterGui(CritterModel model) {
      this(model, false);
   }

   public CritterGui(CritterModel model, boolean network) {
      this.model = model;
      model.addObserver(this);
   
      enableNetwork = enableLoadFromWeb = enableSendRequest = network;
   
   	// try to load settings from disk (fail silently)
      Properties props = null;
      if (SHOULD_SAVE_SETTINGS) {
         try {
            props = loadConfiguration();
         } catch (IOException ioe) {
         } catch (SecurityException ioe) {
            // don't print security exceptions
         }
      }
   
      if (!network) {
         try {
            System.setSecurityManager(new CritterSecurityManager());
         } catch (SecurityException e) {}
      }
   
   	// set up network listeners
      networkSenderListener = new CritterNetworkManager();
      networkSenderListener.getReceiveEvent().addObserver(this);
      networkSenderListener.getErrorEvent().addObserver(this);
      networkServer = new CritterNetworkManager(CritterNetworkManager.DEFAULT_PORT_2);
      networkServer.getReceiveEvent().addObserver(this);
      networkServer.getErrorEvent().addObserver(this);
   
   	// set up critter picture panel and set size
      panel = new CritterPanel(model, true);
      panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
   
   	// add the animation timer
      timer = new javax.swing.Timer(DELAY, this);
      timer.setCoalesce(true);
   
   	// east panel to store critter class info
      counts = new TreeMap<String, ClassPanel>();
   	// east = new JPanel(new GridLayout(0, 1));
      east = new JPanel();
      east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
   
   	// FlowLayout wrapper so that ClassPanels aren't stretched vertically
      JPanel eastWrapper = new JPanel();
      eastWrapper.setLayout(new FlowLayout()); // new BoxLayout(eastWrapper, BoxLayout.Y_AXIS));
      eastWrapper.add(east);
      JScrollPane scrollPane = new JScrollPane(eastWrapper);
      scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
   
   	// east.setBorder(BorderFactory.createTitledBorder("Critter classes:"));
      if (enableLoadFromWeb) {
         loadFromWeb = GuiFactory.createButton("Load from Web...", 'L', this, east);
         loadFromWeb.setAlignmentX(0.5f);
      }
   
   	// timer controls
      JPanel southcenter = new JPanel();
      go = GuiFactory.createButton("Go", 'G', this, southcenter);
      go.setBackground(Color.GREEN);
      stop = GuiFactory.createButton("Stop", 'S', this, southcenter);
      stop.setBackground(new Color(255, 96, 96));
      tick = GuiFactory.createButton("Tick", 'T', this, southcenter);
      tick.setBackground(Color.YELLOW);
      reset = GuiFactory.createButton("Reset", 'R', this, southcenter);
   
      Container southCenterHolder = Box.createVerticalBox();
      southCenterHolder.add(southcenter);
      Container southCenterCheckboxArea = new JPanel();
      backgroundColors = GuiFactory.createCheckBox("Husky background colors", 'H', this, southCenterCheckboxArea);
      backgroundColors.setAlignmentX(1.0f);
      debug = GuiFactory.createCheckBox("Debug", 'D', this, southCenterCheckboxArea);
      debug.setAlignmentX(1.0f);
      southCenterHolder.add(southCenterCheckboxArea);
   
   	// slider for animation speed
      JPanel southwest = new JPanel();
      southwest.add(new JLabel("Speed:"));
      slider = GuiFactory.createSlider(1, 41, 1000 / DELAY, 20, 5, this, southwest);
      moves = new JLabel("     0 moves");
      moves.setFont(STATUS_FONT);
      southwest.add(moves);
   
   	// checkbox 
      JPanel southeast = new JPanel(); // new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
      southeast.setBorder(BorderFactory.createTitledBorder("Accept requests:"));
      ButtonGroup group = new ButtonGroup();
      always = GuiFactory.createRadioButton("Always", 'A', false, group, this, southeast);
      always.setToolTipText("When selected, automatically accepts critters sent to you "
                            + "and automatically shares requested critters.");
      ask = GuiFactory.createRadioButton("Ask", 'k', true, group, this, southeast);
      ask.setToolTipText("When selected, prompts you when critters are sent to you "
                         + "and when requested to share your critters.");
      never = GuiFactory.createRadioButton("Never", 'N', false, group, this, southeast);
      never.setToolTipText("When selected, never accepts critters sent to you "
                           + "and refuses all requests to share your critters.");
   
   	// south panel to hold various widgets
      Container south = new JPanel(new BorderLayout());
      south.add(southCenterHolder);
      south.add(southwest, BorderLayout.WEST);
   
      if (enableSendRequest) {
         south.add(southeast, BorderLayout.EAST);
      } else {
         south.add(Box.createHorizontalStrut(southwest.getPreferredSize().width), BorderLayout.EAST);
      }
   
      JPanel center = new JPanel();
      center.add(panel);
   
   	// use saved settings, if any (fail silently)
      if (props != null) {
         try {
            slider.setValue(Integer.parseInt(props.getProperty(FPS_KEY)));
            String accept = props.getProperty(ACCEPT_KEY);
            if (accept.equals(ALWAYS_VALUE)) {
               always.setSelected(true);
            } else if (accept.equals(NEVER_VALUE)) {
               never.setSelected(true);
            } else if (accept.equals(ASK_VALUE)) {
               ask.setSelected(true);
            }
            backgroundColors.setSelected(Boolean.parseBoolean(props.getProperty(BACKGROUND_COLORS_KEY, "true")));
            // debug.setSelected(Boolean.parseBoolean(props.getProperty(DEBUG_KEY, "false")));
            debug.setSelected(model.isDebug());
            lastHostName = props.getProperty(LAST_HOST_NAME_KEY, "");
         	// timer.setDelay(Integer.parseInt(props.getProperty(FPS_KEY)));
         } catch (Exception e) {}
      }
   
   	// enable or disable background colors behind critters
      panel.setBackgroundColors(backgroundColors.isSelected());
      model.setDebug(debug.isSelected());
   
   	// create frame and do layout
      frame = new JFrame();
   
      if (enableNetwork) {
         frame.setTitle(TITLE + ": " + CritterNetworkManager.getHostName()
                        + " " + CritterNetworkManager.getIpAddresses());
      } else {
         frame.setTitle(TITLE);
      }
   
   	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.addWindowListener(this);
   	// frame.setResizable(false);
      frame.add(center, BorderLayout.CENTER);
      frame.add(south, BorderLayout.SOUTH);
      frame.add(scrollPane, BorderLayout.EAST);
   
      GuiFactory.center(frame);
      doEnabling();
   }

	// Responds to action events in the GUI.
   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == go) {
         timer.start();
      } else if (src == stop) {
         timer.stop();
      } else if (src == timer || (src == tick && !timer.isRunning())) {
         model.update();
      } else if (src == reset) {
         model.reset();
      } else if (src == loadFromWeb) {
         Thread thread = new Thread(new ZipDownloader(ZIP_CODE_BASE, model,
                                                      frame, loadFromWeb));
         thread.start();
      } else if (src == backgroundColors) {
         panel.setBackgroundColors(backgroundColors.isSelected());
         panel.repaint();
      
         for (ClassPanel cpanel : counts.values()) {
            cpanel.updateBorder();
            cpanel.updateBackground();
         }
      } else if (src == debug) {
         model.setDebug(debug.isSelected());
      }
   
      doEnabling();
   }

	// Responds to change events on the slider.
   public void stateChanged(ChangeEvent e) {
      int fps = slider.getValue();
      timer.setDelay(1000 / fps);
   	// timer.setInitialDelay(1000 / fps);
   	// timer.restart();
   }

	// Starts the simulation. Assumes all critters have already been added.
   public void start() {
      setCounts();
   
   	// frame.pack();
      GuiFactory.center(frame);
      frame.setVisible(true);
   
   	// start network listeners
      try {
         if (enableNetwork) {
            networkSenderListener.start();
            networkServer.start();
         }
      } catch (java.net.BindException e) {
         JOptionPane
            .showMessageDialog(
                               frame,
                               "Error: The network is already in use.\n\n"
                               + "If you want to be able to send and receive critters over the network,\n"
                               + "please close all instances of the Critters GUI and run it again.",
                               "Network in use", JOptionPane.ERROR_MESSAGE);
      } catch (IOException e) {
         JOptionPane.showMessageDialog(frame,
                                       "Error starting network listener:\n" + e, "Network error",
                                       JOptionPane.ERROR_MESSAGE);
         if (PRINT_EXCEPTIONS) {
            e.printStackTrace();
         }
      }
   
      frame.toFront();
   }

	// Responds to Observable updates in the model.
   public void update(Observable o, Object arg) {
      if (o == model) {
      	// model is notifying us of an update
         if (arg == CritterModel.Event.ADD
             || arg == CritterModel.Event.REMOVE_ALL
             || arg == CritterModel.Event.UPDATE
             || arg == CritterModel.Event.RESET) {
            setCounts();
            moves.setText(Util.padNumber(model.getMoveCount(), 6)
                          + " moves");
         }
      	// TODO: remove overall gui as observer of model?
      } else if (o == networkSenderListener.getReceiveEvent() && arg != null) {
      	// we received a message (a class to load)
         if (never.isSelected()) {
            return;
         }
      
         String[] strings = (String[]) arg;
         loadClassText(strings);
      	// setCounts();
      } else if (o == networkServer.getReceiveEvent() && arg != null) {
      	// we received a message (a request to send our wolf)
         if (never.isSelected()) {
         	// refuse all requests
            return;
         }
      
         String[] strings = (String[]) arg;
         String hostName = strings[0];
         String className = strings[1];
      
         if (ask.isSelected()) {
         	// "Always Accept" not checked, so ask to confirm
            int choice = JOptionPane
               .showConfirmDialog(frame, "Host \"" + hostName
                                  + "\" requests your " + className
                                  + " class.  Send it?", "Critter send request",
                                  JOptionPane.YES_NO_OPTION,
                                  JOptionPane.QUESTION_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) {
            	// refuse request (send back a null answer)
               sendJavaFile(null, hostName);
               return;
            }
         }
      
      	// user confirmed, so send the class data
         sendJavaFile(className, hostName);
      } else if (o == networkSenderListener.getErrorEvent()
                 || o == networkServer.getErrorEvent()) {
      	// something failed in a network send/receive attempt
         Exception e = (Exception) arg;
         String message;
         if (e instanceof UnknownHostException) {
            message = "Cannot reach target computer:\n\n" + e;
         } else if (e instanceof ConnectException) {
            message = "Target computer refused connection:\n\n" + e;
         } else if (e instanceof IOException) {
            message = "I/O error:\n" + e;
         } else {
            message = e.toString();
         }
         JOptionPane.showMessageDialog(frame, message, "network error",
                                       JOptionPane.ERROR_MESSAGE);
         if (PRINT_EXCEPTIONS) {
            e.printStackTrace();
         }
      }
   }

	// Called when the window is about to close.
	// Used to save the GUI's settings.
   public void windowClosing(WindowEvent e) {
      if (SHOULD_SAVE_SETTINGS) {
         try {
            networkServer.stop();
            networkSenderListener.stop();
            saveConfiguration();
         } catch (SecurityException sex) {
            // don't print applet security exceptions
         } catch (Exception ex) {
            if (PRINT_EXCEPTIONS) {
               ex.printStackTrace();
            }
         }
      }
   
      try {
         System.exit(0);
      } catch (Exception ex) {
      }
   }

	// Required to implement WindowListener interface.
   public void windowActivated(WindowEvent e) {
   }

   public void windowClosed(WindowEvent e) {
   }

   public void windowDeactivated(WindowEvent e) {
   }

   public void windowDeiconified(WindowEvent e) {
   }

   public void windowIconified(WindowEvent e) {
   }

   public void windowOpened(WindowEvent e) {
   }

	// Sets which buttons can be clicked at any given moment.
   private void doEnabling() {
      go.setEnabled(!timer.isRunning());
      stop.setEnabled(timer.isRunning());
      tick.setEnabled(!timer.isRunning());
      reset.setEnabled(!timer.isRunning());
   }

	// Helper method to read an integer input from a set of choices.
   private int getInput(String message, Object defaultValue, Object... choices) {
      Object countStr = JOptionPane.showInputDialog(frame, message,
                                                    "Question", JOptionPane.QUESTION_MESSAGE, null, choices,
                                                    defaultValue);
      if (countStr == null) {
         return -1;
      }
      try {
         return Integer.parseInt(countStr.toString());
      } catch (NumberFormatException e) {
         return -1;
      }
   }

	// Helper method to read a String input (with the given initial String in the field)
	// and return a default value if an empty string is entered.
   private String getInput(String message, String initialValue,
                           String defaultValue) {
      String input = (String) JOptionPane.showInputDialog(frame, message,
                                                          "Question", JOptionPane.QUESTION_MESSAGE, null, null,
                                                          initialValue);
      if (input != null && input.length() == 0) {
         input = defaultValue;
      }
      return input;
   }

	/*
   // Loads a class received over the network.
   // The given array contains [host name, class name, encoded base64 classfile text]
   @SuppressWarnings("unchecked")
   private void loadClassEncoded(String[] strings) {
   String hostName = strings[0];
   String className = strings[1];
   String encodedFileText = strings[2];
	 
   if (encodedFileText == null) {
   // they refused our request and sent back a null
   JOptionPane.showMessageDialog(frame, hostName + " refused the request.", 
   "Request refused", JOptionPane.ERROR_MESSAGE);
   return;
   }
	 
   // find out how many critters to add to the world
   int count = DEFAULT_NUMBER_OF_CRITTERS;
   if (ask.isSelected()) {
   count = getInput("Received " + className + " from host \"" + hostName + 
   "\".\nHow many to add? (Or Cancel to refuse this class)", 
   DEFAULT_NUMBER_OF_CRITTERS,
   0, 1, 25, 50, 100);
   if (count < 0) {
   return;
   }
   }
	 
   // try to compile and load the received class, add it to simulation
   try {
   Class<? extends Critter> critterClass = (Class<? extends Critter>) ClassUtils
   .writeAndLoadEncodedClass(encodedFileText, className);
   model.add(count, critterClass);
   } catch (CritterModel.TooManyCrittersException e) {
   JOptionPane.showMessageDialog(frame,
   "Error: Not enough room to add all critters",
   "Too many critters", JOptionPane.ERROR_MESSAGE);
   } catch (CritterModel.InvalidCritterClassException e) {
   JOptionPane.showMessageDialog(frame,
   "Problem with critter class:\n" + e + "\n\n" +
   "This is probably DrJava's fault; DrJava has some issues with dynamically loading code over the network.\n" +
   "If you've got a " + className + ClassUtils.CLASS_EXTENSION + " file in your program's folder, the file got to you successfully, but\n" +
   "DrJava wasn't able to load it into the simulator.\n\n" +
   "Try closing and re-running the simulator; this will give DrJava a chance to see the new animal type and load it.\n" +
   "Hopefully, when you re-run the simulator, the " + className + " type will appear.",
	 
   "Problem with critter class",
   JOptionPane.ERROR_MESSAGE);
   } catch (ClassNotFoundException cnfe) {
   JOptionPane.showMessageDialog(frame, "Error loading class:\n" + cnfe, "Error",
   JOptionPane.ERROR_MESSAGE);
   cnfe.printStackTrace();
   } catch (IOException ioe) {
   JOptionPane.showMessageDialog(frame, "Error loading class:\n" + ioe, "Error",
   JOptionPane.ERROR_MESSAGE);
   ioe.printStackTrace();
   }
   }
   */

	// Loads a class received over the network.
	// The given array contains [host name, class name, class text]
   @SuppressWarnings("unchecked")
      private void loadClassText(String[] strings) {
      String hostName = strings[0];
      String className = strings[1];
      String fileText = strings[2];
   
      if (fileText == null) {
      	// they refused our request and sent back a null
         JOptionPane.showMessageDialog(frame, hostName
                                       + " refused the request.", "Request refused",
                                       JOptionPane.ERROR_MESSAGE);
         return;
      }
   
   	// find out how many critters to add to the world
      int count = CritterModel.DEFAULT_CRITTER_COUNT;
      if (ask.isSelected()) {
         count = getInput("Received " + className + " from host \""
                          + hostName
                          + "\".\nHow many to add? (Or Cancel to refuse this class)",
                          CritterModel.DEFAULT_CRITTER_COUNT, 0, 1, 25, 50, 100);
         if (count < 0) {
            return;
         }
      }
   
   	// try to compile and load the received class, add it to simulation
      try {
         Class<? extends Critter> critterClass = (Class<? extends Critter>) ClassUtils
            .writeAndLoadClass(fileText, className, true);
         model.add(count, critterClass);
      } catch (CritterModel.TooManyCrittersException e) {
         JOptionPane.showMessageDialog(frame,
                                       "Error: Not enough room to add all critters",
                                       "Too many critters", JOptionPane.ERROR_MESSAGE);
      } catch (CritterModel.InvalidCritterClassException e) {
         if (ClassUtils.isDrJavasFault(className)) {
            JOptionPane.showMessageDialog(frame, className
                                          + " received; simulator must be restarted.\n"
                                          + "Try closing the GUI and re-running CritterMain.",
                                          "Restart required", JOptionPane.INFORMATION_MESSAGE);
         } else {
            JOptionPane
               .showMessageDialog(
                                  frame,
                                  "Problem with critter class:\n"
                                  + e
                                  + "\n\n"
                                  + "This is probably DrJava's fault; DrJava has some issues with dynamically loading code over the network.\n"
                                  + "If you've got a "
                                  + className
                                  + ClassUtils.CLASS_EXTENSION
                                  + " file in your program's folder, the file got to you successfully, but\n"
                                  + "DrJava wasn't able to load it into the simulator.\n\n"
                                  + "Try closing and re-running the simulator; this will give DrJava a chance to see the new animal type and load it.\n"
                                  + "Hopefully, when you re-run the simulator, the "
                                  + className + " type will appear.",
               
                                  "Problem with critter class",
                                  JOptionPane.ERROR_MESSAGE);
            if (PRINT_EXCEPTIONS) {
               e.printStackTrace();
            }
         }
      } catch (ClassNotFoundException e) {
         JOptionPane
            .showMessageDialog(
                               frame,
                               "Unable to find the Java compiler.\n"
                               + "If you aren't using DrJava, try running the simulator from there.",
                               "Error", JOptionPane.ERROR_MESSAGE);
         if (PRINT_EXCEPTIONS) {
            e.printStackTrace();
         }
      } catch (Exception e) {
         JOptionPane.showMessageDialog(frame, "Error loading class:\n" + e,
                                       "Error", JOptionPane.ERROR_MESSAGE);
         if (PRINT_EXCEPTIONS) {
            e.printStackTrace();
         }
      }
   }

   private Properties loadConfiguration() throws IOException {
      Properties prop = new Properties();
      prop.load(new FileInputStream(SAVE_STATE_FILE_NAME));
      return prop;
   }

   private void saveConfiguration() throws IOException {
      Properties prop = new Properties();
      int fps = slider.getValue();
      prop.setProperty(LAST_HOST_NAME_KEY, lastHostName);
      prop.setProperty(FPS_KEY, String.valueOf(fps));
      if (always.isSelected()) {
         prop.setProperty(ACCEPT_KEY, ALWAYS_VALUE);
      } else if (ask.isSelected()) {
         prop.setProperty(ACCEPT_KEY, ASK_VALUE);
      } else if (never.isSelected()) {
         prop.setProperty(ACCEPT_KEY, NEVER_VALUE);
      }
      prop.setProperty(BACKGROUND_COLORS_KEY, String.valueOf(backgroundColors.isSelected()));
      // prop.setProperty(DEBUG_KEY, String.valueOf(debug.isSelected()));
      prop.store(new PrintStream(SAVE_STATE_FILE_NAME), "CSE 142 Critters saved network settings");
   }

	/*
   // sends the given class code to the given host computer
   // if className is null, sends null text to signify request refused
   private void sendClassFile(String className, String hostName) {
   try {
   String encodedFileText = null;
   String newClassName = className;
	 
   if (className != null) {
   // new class name = old one + current user name?
   // e.g. "Wolf" --> "Wolf_Stepp"
   String userName = System.getProperty("user.name");
   if (userName.length() > 0) {
   userName = userName.substring(0, 1).toUpperCase() + userName.substring(1).toLowerCase();
   }
   newClassName += "_" + userName;

   encodedFileText = ClassUtils.renameCompileEncode(className, newClassName);
   }
   networkSenderListener.sendText(hostName, newClassName, encodedFileText);
   } catch (IOException e) {
   JOptionPane.showMessageDialog(frame,
   "Error reading file:\n" + e, "I/O Error",
   JOptionPane.ERROR_MESSAGE);
   } catch (ClassNotFoundException e) {
   JOptionPane.showMessageDialog(frame,
   "Error preparing class to send:\n" + e, "Class Error",
   JOptionPane.ERROR_MESSAGE);
   }
   }
   */

	// sends the given class code to the given host computer
	// if className is null, sends null text to signify request refused
   private void sendJavaFile(String className, String hostName) {
      try {
         String fileText = null;
         String newClassName = className;
         if (className != null) {
         	// new class name = old one + current user name?
         	// e.g. "Wolf" --> "Wolf_Stepp"
            String userName = System.getProperty("user.name");
            if (userName.length() > 0) {
               userName = userName.substring(0, 1).toUpperCase()
                  + userName.substring(1).toLowerCase();
            }
            newClassName += "_" + userName;
         
         	// rename and read
            fileText = ClassUtils.readAndRename(className, newClassName);
         }
         networkSenderListener.sendText(hostName, newClassName, fileText);
      } catch (IOException ioe) {
         JOptionPane.showMessageDialog(frame, "Error reading file:\n" + ioe,
                                       "I/O Error", JOptionPane.ERROR_MESSAGE);
      }
   }

	// Adds right-hand column of labels showing how many of each type are alive.
	// Updates the counter labels to store the current count information.
   private void setCounts() {
      Set<String> classNames = model.getClassNames();
      if (classNames.size() == counts.size()) {
         return; // nothing to do
      }
   
      for (ClassPanel cpanel : counts.values()) {
         east.remove(cpanel);
      }
      counts.clear();
   
      panel.ensureAllColors();
   
      boolean packed = false;
      int count = 0;
      for (String className : classNames) {
         ClassPanel cpanel = new ClassPanel(className);
         east.add(cpanel);
         counts.put(className, cpanel);
      	
         if (!packed && count >= 3) {
            east.validate();
            frame.pack();
            frame.setSize(frame.getWidth() + 20, frame.getHeight());
            packed = true;
         }
      }
   	
      if (!packed) {
         east.validate();
         frame.pack();
          
         // buffer because for some reason Swing underestimates east's width
         frame.setSize(frame.getWidth() + 20, frame.getHeight());
         packed = true;
      }
      east.validate();
   }

	// One of the east panels representing a critter class.
   private class ClassPanel extends JPanel implements ActionListener, Observer {
      private static final long serialVersionUID = 0;
   
   	// fields
      private String className;
      private JButton send, request, delete;
      private JLabel statusLabel;
      private JPanel center;
      private Color oldBackground;
      private TitledBorder border;
   
   	// Constructs a new ClassPanel to hold info about the given critter class.
      public ClassPanel(String className) {
         this.className = className;
         oldBackground = getBackground();
      
         border = BorderFactory.createTitledBorder(Util.truncate(className,
                                                                 MAX_CLASS_NAME_LENGTH));
         border.setTitleFont(CLASS_FONT);
      	
         updateBorder();
         setBorder(border);
      
         model.addObserver(this);
         this.setToolTipText(className);
         statusLabel = new JLabel(" ");
         statusLabel.setFont(STATUS_FONT);
         updateLabel();
      
         setLayout(new BorderLayout(0, 0));
         add(statusLabel, BorderLayout.NORTH);
         if (ClassUtils.isNetworkClass(className)) {
            delete = GuiFactory.createButton("Remove", '\0', this, null);
            delete.setForeground(Color.RED.darker());
            delete.setToolTipText("Remove all animals of type " + className);
            add(delete, BorderLayout.CENTER);
         } else {
            center = new JPanel();
            if (enableSendRequest && this.className.startsWith("Husky")) {
               send = GuiFactory.createButton("Send", '\0', this, center);
               request = GuiFactory
                  .createButton("Get", '\0', this, center);
               add(center, BorderLayout.CENTER);
            }
         }
      }
   
   	// Handles action events in this panel.
      public void actionPerformed(ActionEvent e) {
         Object src = e.getSource();
         if (src == send) {
            if (ClassUtils.isNetworkClass(className)) {
               return; // skip network classes
            }
         
         	// ask what computer to send to
            String hostName = getInput("Send your " + className
                                       + " to what computer name or IP address? \n",
                                       lastHostName, "localhost");
            if (hostName != null) {
            	// send class to computer
               lastHostName = (hostName.equals("localhost") ? ""
                               : hostName);
               sendJavaFile(className, hostName);
            }
         } else if (src == request) {
         	// ask what computer to request from
            String hostName = getInput("Request " + className
                                       + " from what computer name or IP address?\n ",
                                       lastHostName, "localhost");
            if (hostName != null) {
            	// request class from computer
               lastHostName = (hostName.equals("localhost") ? ""
                               : hostName);
               networkServer.requestClass(hostName, className);
            }
         } else if (src == delete) {
         	// delete this network class from the system
            String classFileName = className + ClassUtils.CLASS_EXTENSION;
            model.removeAll(className);
         
         	// set the .class file to be deleted when the VM exits
         	// BUG: if they then send us that same file while GUI is running,
         	// I'm unable to cancel the delete-on-exit request and it'll
         	// get deleted when you close your GUI.  Oh well.
            int choice = JOptionPane
               .showConfirmDialog(frame, className
                                  + " was removed.\nShould I delete the "
                                  + classFileName + " file from your disk?",
                                  "Delete class?", JOptionPane.YES_NO_OPTION,
                                  JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
               new File(classFileName).deleteOnExit();
            }
         }
      }
   
   	// Handles Observable updates from the model.
      public void update(Observable o, Object arg) {
         updateLabel();
         updateBackground();
      }
   
      private void updateBackground() {
         if (className.equals(model.getWinningClassName())) {
            setBackground(Color.YELLOW);
            if (center != null) {
               center.setBackground(Color.YELLOW);
            }
         } else {
            setBackground(oldBackground);
            if (center != null) {
               center.setBackground(oldBackground);
            }
         }
      }
   
      private void updateBorder() {
         Color bgColor = panel.getColor(className);
         if (backgroundColors.isSelected() && bgColor != null) {
         	// border.setTitleColor(bgColor);
            border.setBorder(BorderFactory.createLineBorder(bgColor, 5));
         } else {
         	// border.setTitleColor(Color.BLACK);
            border.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
         }
         setBorder(border);
         validate();
         repaint();
      }
   
   	// Updates the text status about this critter class. 
      private void updateLabel() {
         int count = model.getCount(className);
         int kills = model.getKills(className);
         int food = model.getFoodEaten(className);
         int foodPenalty = model.getFoodPenalty(className);
      	// int deaths = model.getDeaths(className);
         int total = count + kills + food;
         String status = "<html>" + 
            Util.padNumber(count, 4, true) + " alive<br>" +
            "+" + Util.padNumber(kills, 3, true) + " kills<br>" +
            "+" + Util.padNumber(food, 3, true)  + " food<br>";
      	// "-" + padString(deaths, 3, true) + " deaths<br>" +
         if (total > 999) {
            status += "<b>= ZOMG!!!1</b>";
         } else {
            status += "<b>=" + Util.padNumber(total, 3, true)
               + " TOTAL</b>";
         }
      
         if (foodPenalty > 0) {
            status += "<br><font color='#990000'><b>"
               + Util.padNumber(foodPenalty, 4, true)
               + " sleep</b></font>";
         }
         status += "</html>";
         statusLabel.setText(status);
      }
   }
}


// CSE 142 Homework 8 (Critters)
// Authors: Marty Stepp, Stuart Reges, Steve Gribble
//
// Utility class to make sure the student doesn't do silly things
// in his/her critter code.
//


class CritterClassVerifier {
   private static final String[] CLASSES_TO_CHECK_METHODS = { "Bear", "Lion",
                                                              "Tiger" };

	// Pops up an error message if the student has any methods
	// with the wrong signature or mis-capitalized names.
   public static void checkForSillyMethods() {
      List<String> errors = new ArrayList<String>();
   
   	// methods inherited from class Object
      Set<String> objectMethods = new HashSet<String>();
      objectMethods.add("equals");
      objectMethods.add("getClass");
      objectMethods.add("hashCode");
      objectMethods.add("notify");
      objectMethods.add("notifyAll");
      objectMethods.add("wait");
   
   	// grab all inherited methods
      Set<Method> critterMethods = new HashSet<Method>();
      Map<String, Method> critterMethodMap = new HashMap<String, Method>();
      Map<String, Method> critterMethodMapLC = new HashMap<String, Method>();
      try {
         for (Method method : Critter.class.getMethods()) {
            critterMethods.add(method);
            String methodName = method.getName();
            critterMethodMap.put(methodName, method);
            critterMethodMapLC.put(methodName.toLowerCase(), method);
         	// System.out.println("Critter method " + methodName + ": " +
         	// method);
         }
      } catch (Exception e) {
      	// e.printStackTrace();
      }
   
      for (String className : CLASSES_TO_CHECK_METHODS) {
         try {
            Class<?> clazz = Class.forName(className);
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != Critter.class) {
               errors.add("Class " + className
                          + " is supposed to extend Critter, but doesn't.");
               continue;
            }
            for (Method method : clazz.getMethods()) {
            	// System.out.println(className + " method: " + method);
               String methodName = method.getName();
               if (objectMethods.contains(methodName)) {
                  continue; // inherited from class Object
               }
            
               String methodNameLC = methodName.toLowerCase();
               if (critterMethodMapLC.containsKey(methodNameLC)) {
                  Method critterMethod = critterMethodMapLC
                     .get(methodNameLC);
                  if (!critterMethodMap.containsKey(methodName)) {
                     errors
                        .add("Class "
                             + className
                             + " has incorrect capitalization for method "
                             + methodName + ":\nexpected: "
                             + critterMethod + "\nactual: "
                             + method);
                  }
                  if (!Arrays.equals(critterMethod.getParameterTypes(),
                                     method.getParameterTypes())) {
                     errors.add("Class " + className
                                + " has incorrect parameters for method "
                                + methodName + ":\nexpected: "
                                + critterMethod + "\nactual: " + method);
                  }
               }
            }
         } catch (Exception e) {
         	// e.printStackTrace();
         }
      }
   
   	// System.out.println("ERRORS: " + errors);
      String errorString = "";
      for (String error : errors) {
         errorString += "\n\n" + error;
      }
   
      if (!errors.isEmpty()) {
         JOptionPane.showMessageDialog(null,
                                       "I found some possible errors in your critter classes:"
                                       + errorString, "Possible critter errors",
                                       JOptionPane.WARNING_MESSAGE);
      }
   }

   public static CritterGui initialSettings() {
      List<String> names = new ArrayList<String>();
      names.add("Width");
      names.add("Height");
      names.add("Number of each critter");
      names.add("Network features");
      names.add("Debug output");
      names.add(null);
   
      List<Class<?>> types = new ArrayList<Class<?>>();
      types.add(Integer.TYPE);
      types.add(Integer.TYPE);
      types.add(Integer.TYPE);
      types.add(Boolean.TYPE);
      types.add(Boolean.TYPE);
      types.add(null);
   
      List<Object> initialValues = new ArrayList<Object>();
      initialValues.add(new Integer(CritterModel.DEFAULT_WIDTH));
      initialValues.add(new Integer(CritterModel.DEFAULT_HEIGHT));
      initialValues.add(new Integer(CritterModel.DEFAULT_CRITTER_COUNT));
      initialValues.add(new Boolean(CritterGui.DEFAULT_NETWORK_ENABLED));
      initialValues.add(new Boolean(CritterGui.DEFAULT_DEBUG));
      initialValues.add(null);
   
      for (Class<? extends Critter> critterClass : ClassUtils.getClasses(Critter.class, ".")) {
         names.add(critterClass.getName());
         types.add(Boolean.TYPE);
         initialValues.add(new Boolean(true));
      }
   
      boolean ok = InputPane.showInputDialog(null, "Critters settings", null,
                                             names.toArray(new String[0]), types.toArray(new Class<?>[0]),
                                             initialValues.toArray(new Object[0]));
      if (ok) {
         int width = InputPane.getInt("Width");
         int height = InputPane.getInt("Height");
         int count = InputPane.getInt("Number of each critter");
         boolean network = InputPane.getBoolean("Network features");
         boolean debug = InputPane.getBoolean("Debug output");
      
         CritterModel model = new CritterModel(width, height, debug);  // create simulation
      
         for (Class<? extends Critter> critterClass : ClassUtils.getClasses(Critter.class, ".")) {
            boolean checked = InputPane.getBoolean(critterClass.getName());
            if (checked) {
               model.add(count, critterClass);
            }
         }
      
         CritterGui gui = new CritterGui(model, network);
         return gui;
      } else {
      	// user canceled
         return null;
      }
   }
}


// CSE 142 Homework 8 (Critters)
// Authors: Marty Stepp, Stuart Reges, Steve Gribble
//
// The model of all critters in the simulation.
// The main work is done by the update method, which moves all critters
// and initiates the various fights and interactions between them.
// 
// Performance profiled with Java HProf.
// To profile heap memory/object usage:
//   java -Xrunhprof CritterMain
// To profile CPU cycles:
//   java -Xrunhprof:cpu=old,thread=y,depth=10,cutoff=0,format=a CritterMain
// View HProf data with HPjmeter software (Google for it).
//
// 
class CritterModel extends Observable implements Iterable<Critter> {
	// class constants
   public static final String EMPTY = " "; // how to display empty squares
   public static final int DEFAULT_CRITTER_COUNT = 25;
   public static final int DEFAULT_WIDTH = 60;
   public static final int DEFAULT_HEIGHT = 50;

	// how many pieces of food an animal type must eat before being blocked
	// (set to 0 to disable this feature)
   public static final int CRITTER_GLUTTON_COUNT = 2;
   public static final int CRITTER_CLASS_GLUTTON_COUNT = 0; // 20;
   public static final int CRITTER_MOVE_FATIGUE_COUNT = 0;

	// largest value that will be passed to the constructor of a critter that 
	// takes an int as a parameter
   private static final int INT_PARAM_MAX = 9;
   private static final int GLUTTON_PENALTY = 20;
   private static final int MATING_PENALTY = 40;
   private static final int FOOD_PERCENTAGE = 5;  // what % squares have food?
   private static final int FOOD_RESPAWN_INTERVAL = 50;
   private static final String FOOD = ".";  // how will food be drawn?
   private static final String RANDOM_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   private static final Color FOOD_COLOR = Color.BLACK;
   private static final Color BROWN = new Color(128, 128, 64);

   public static boolean isHuskyClass(Class<? extends Critter> clazz) {
      return clazz != null && clazz.getName().startsWith("Husky");
   }

	// fields
   private final int height;
   private final int width;
   private final Critter[][] grid;
   private final String[][] display;
   private final Color[][] colorDisplay;
   private final boolean[][] food;
   private final Random rand;
   private final List<Critter> critterList;
   private final Map<Critter, Point> locationMap;
   private final SortedMap<String, CritterState> classStateMap;
   private final Map<Critter, CritterState> critterStateMap;
   private int moveCount;
   private Point infoPoint = new Point();
   private boolean debug = false;

	// Constructs a new model of the given size.
   public CritterModel(int width, int height) {
      this(width, height, false);
   }
	
   // Constructs a new model of the given size.
   public CritterModel(int width, int height, boolean debug) {
   	// check for invalid model size
      if (width <= 0 || height <= 0) {
         throw new IllegalArgumentException();
      }
   
      this.width = width;
      this.height = height;
      this.debug = debug;
   	
      rand = new Random();
      moveCount = 0;
      grid = new Critter[width][height];
      food = new boolean[width][height];
      display = new String[width][height];
      colorDisplay = new Color[width][height];
   
   	// initialize various data structures
      critterList = new ArrayList<Critter>();
      locationMap = new HashMap<Critter, Point>();
      classStateMap = new TreeMap<String, CritterState>();
   
   	// important to use IdentityHashMap so they can't trick me by overriding hashCode
      critterStateMap = new IdentityHashMap<Critter, CritterState>();
   
      createRandomFood();
      updateDisplay(Event.NEW);
   }

	// Adds the given number of critters of the given type to the simulation.
   public synchronized void add(int number, Class<? extends Critter> critterClass) {
   	// count # of critters of each class
      String className = critterClass.getName();
      if (!classStateMap.containsKey(className)) {
         classStateMap.put(className, new CritterState(critterClass));
      }
   
      try {
      	// call private helper add method many times
         for (int i = 0; i < number; i++) {
            add(critterClass);
         }
      } catch (IllegalAccessException e) {
         throw new InvalidCritterClassException(e);
      } catch (InvocationTargetException e) {
         throw new InvalidCritterClassException(e);
      } catch (InstantiationException e) {
         throw new InvalidCritterClassException(e);
      }
   
      updateDisplay(Event.ADD);
   }

	// Returns the color that should be displayed on the given (x, y) location,
	// or black if nothing is there.
   public Color getColor(int x, int y) {
      return colorDisplay[x][y];
   }

	// Returns a set of all names of Critter classes that exist in this model.
   public Set<String> getClassNames() {
      return Collections.unmodifiableSet(classStateMap.keySet());
   }

	// Returns a set of [class name, count] entry pairs in this model.
   public Set<Map.Entry<String, CritterState>> getClassStates() {
      return Collections.unmodifiableSet(classStateMap.entrySet());
   }

	// Returns how many critters of the given type exist in the world.
   public int getCount(String className) {
      if (classStateMap.containsKey(className)) {
         return classStateMap.get(className).count;
      } else {
         return 0;
      }
   }

	// Returns the String of text to display at the given (x, y) location.
   public Class<? extends Critter> getCritterClass(int x, int y) {
      if (grid[x][y] == null) {
         return null;
      } else {
         return grid[x][y].getClass();
      }
   }

	// Returns how many critters of the given type have died.
   public int getDeaths(String className) {
      if (classStateMap.containsKey(className)) {
         return classStateMap.get(className).deaths;
      } else {
         return 0;
      }
   }

	// Returns how many critters of the given type exist in the world.
   public int getFoodEaten(String className) {
      if (classStateMap.containsKey(className)) {
         return classStateMap.get(className).foodEaten;
      } else {
         return 0;
      }
   }

	// Returns how many critters of the given type exist in the world.
   public int getFoodPenalty(String className) {
      if (classStateMap.containsKey(className)) {
         return classStateMap.get(className).foodPenalty;
      } else {
         return 0;
      }
   }

	// Returns the height of this model.
   public int getHeight() {
      return height;
   }

	// Returns how many critters of the given type exist in the world.
   public int getKills(String className) {
      if (classStateMap.containsKey(className)) {
         return classStateMap.get(className).kills;
      } else {
         return 0;
      }
   }

	// Returns number of updates made to this model.
   public int getMoveCount() {
      return moveCount;
   }

	// Returns the String of text to display at the given (x, y) location.
   public String getString(int x, int y) {
      return display[x][y];
   }

	// Returns the total number of critters in this model.
   public int getTotalCritterCount() {
      return locationMap.keySet().size();
   }

	// Returns the width of this model.
   public int getWidth() {
      return width;
   }

	// Returns the name of the critter class with the highest score.
   public String getWinningClassName() {
      int max = 0;
      String maxClassName = "";
      for (Map.Entry<String, CritterState> entry : classStateMap.entrySet()) {
         CritterState state = entry.getValue();
         int total = state.count + state.kills + state.foodEaten;
         if (total > max) {
            max = total;
            maxClassName = entry.getKey();
         } else if (total == max) {
         	// combine the names just to make it not match
            maxClassName += " " + entry.getKey();
         }
      }
      return maxClassName;
   }

   // Returns whether a critter at the given square is asleep.
   public boolean isAsleep(int x, int y) {
      if (grid[x][y] == null) {
         return false;
      }
      CritterState state = critterStateMap.get(grid[x][y]);
      return state != null && state.isAsleep();
   }

   // Returns whether a critter at the given square is asleep.
   public boolean isBaby(int x, int y) {
      if (grid[x][y] == null) {
         return false;
      }
      CritterState state = critterStateMap.get(grid[x][y]);
      return state != null && state.isBaby();
   }
    
   // Returns whether the model is printing debug messages
   public boolean isDebug() {
      return debug;
   }

   // Returns whether a critter at the given square is asleep.
   public boolean isMating(int x, int y) {
      if (grid[x][y] == null) {
         return false;
      }
      CritterState state = critterStateMap.get(grid[x][y]);
      return state != null && state.isMating();
   }

	// Returns an iterator of the critters in this model.
   public Iterator<Critter> iterator() {
      return Collections.unmodifiableList(critterList).iterator();
   }

	// Restarts the model and reloads the critters.
   public synchronized void reset() {
      createRandomFood();
   	
   	/*
   	// remove any babies born through mating
   	List<Critter> crittersToRemove = new ArrayList<Critter>();
   	for (Critter critter : critterList) {
      if (critterStateMap.get(critter).isBaby(false)) {
      crittersToRemove.add(critter);
      }
   	}
   	*/
   	
      for (Critter critter : critterList) {
         critterStateMap.remove(critter);
         Point location = locationMap.remove(critter);
         if (location != null && grid[location.x][location.y] == critter) {
            grid[location.x][location.y] = null;
         }
      }
      critterList.clear();
   	
   	// reset state for all classes
      for (Map.Entry<String, CritterState> entry : classStateMap.entrySet()) {
         String className = entry.getKey();
      
      	// wipe the class entry for this animal type
         CritterState classState = entry.getValue();
      
      	// remove all animals of this type
         int count = entry.getValue().initialCount;
         removeAll(className, false);
         classState.reset();
      
      	// add them back
         Class<? extends Critter> critterClass = entry.getValue().critterClass;
         add(count, critterClass);
      }
   	
   	// reset class-based state (hmm, is this redundant with the above?)
      for (CritterState state : critterStateMap.values()) {
         state.reset();
      }
      moveCount = 0;
   
      for (Critter critter : critterList) {
         critter.reset();
      }
   	
      setChanged();
      notifyObservers(Event.RESET);
   }

	// Removes all critters of the given type from the simulation.
   public synchronized void removeAll(String className) {
      removeAll(className, true);
   }

	// Removes all critters of the given type from the simulation;
	// if permanent is true, they won't revive even after a reset.
   private synchronized void removeAll(String className, boolean permanent) {
      for (Iterator<Critter> i = critterList.iterator(); i.hasNext();) {
         Critter critter = i.next();
         if (critter.getClass().getName().equals(className)) {
         	// delete this critter
            i.remove();
            Point location = locationMap.remove(critter);
            if (grid[location.x][location.y] == critter) {
               grid[location.x][location.y] = null;
            	// display[location.x][location.y] = EMPTY;
            }
         }
      }
      if (permanent) {
      	// TODO: this might cause a ConcurrentModificationException
      	// if done while game is running...
         classStateMap.remove(className);
      }
   
      updateDisplay(Event.REMOVE_ALL);
   }
	
   public void setDebug(boolean debug) {
      this.debug = debug;
   }

	// Moves the position of all critters and handles collisions.
   public synchronized void update() {
      moveCount++;
   
      if (debug) System.out.println("\nBeginning simulation move #" + moveCount);
   	
   	// reorder the list to be fair about move/collision order
      Collections.shuffle(critterList);
   
   	// move each critter to its new position
      for (int i = 0; i < critterList.size(); i++) {
         Critter critter1 = critterList.get(i);
         CritterState classState1 = classStateMap.get(critter1.getClass().getName());
         CritterState critterState1 = critterStateMap.get(critter1);
         Point location = locationMap.get(critter1);
      
      	// fill the Critter with data about the game state
         if (debug) System.out.println("  processing critter: " + Util.toString(critter1) + " at " + Util.toString(location));
         critter1.setAlive(true);
         critter1.setX(location.x);
         critter1.setY(location.y);
         critter1.setWidth(width);
         critter1.setHeight(height);
         critter1.setNeighbor(Critter.Direction.CENTER, display[location.x][location.y]);
         for (Critter.Direction dir : Critter.Direction.values()) {
            infoPoint.x = location.x;
            infoPoint.y = location.y;
            movePoint(infoPoint, dir);
            critter1.setNeighbor(dir, display[infoPoint.x][infoPoint.y]);
         }
      
         if (classState1.isAsleep() || critterState1.isAsleep() ||
             classState1.isMating() || critterState1.isMating()) {
         	// this critter doesn't get to move; he is sleeping
         	// from eating too much food or something
            if (debug) System.out.println("    asleep or mating; skipping");
            continue;
         }
      
      	// move the critter
         grid[location.x][location.y] = null;
         String critter1ToString = display[location.x][location.y];
         display[location.x][location.y] = EMPTY;
      
         if (debug) System.out.print("    calling getMove ... ");
      
         Critter.Direction move = critter1.getMove();   // get actual move
         if (debug) System.out.println("returned " + move);
         Point locationCopy = movePoint(new Point(location.x, location.y), move);
      	// movePoint(location, move);
      	
      	// see if anybody else is in the square critter1 moved onto
         Critter critter2 = grid[locationCopy.x][locationCopy.y];
         Critter winner = critter1;
         String winnerToString = critter1ToString;
      	
         if (critter2 == null) {
            movePoint(location, move);
            if (debug) System.out.println("    moving critter to " + Util.toString(location));
            critter1.setX(location.x);
            critter1.setY(location.y);
         } else {
            // if two critters from same species want to move together, mate!
            // (also don't actually move the moving animal; leave them still)
            if (critter1.getClass() == critter2.getClass()) {
               CritterState critterState2 = critterStateMap.get(critter2);
               if (!critterState1.hasMate() && !critterState2.hasMate()) {
                  // they fall in love!
                  if (debug) System.out.println("    mating begins at " + Util.toString(location) + " between " + Util.toString(critter1) + " and " + Util.toString(critter2));
                  critterState1.mate = critter2;
                  critterState2.mate = critter1;
                  critterState1.matePenalty = MATING_PENALTY;
                  critterState2.matePenalty = MATING_PENALTY;
                        
                  // notify the critters that they be gettin' it on
                  critter1.mate();
                  critter2.mate();
               }
            } else {
            	// square is occupied by an enemy animal; fight!
               if (debug) System.out.println("    moving critter to " + Util.toString(location));
               movePoint(location, move);
               critter1.setX(location.x);
               critter1.setY(location.y);
               String critter2ToString = display[location.x][location.y];
            	
               if (debug) System.out.println("    fight with " + Util.toString(critter2));
               winner = fight(critter1, critter2);
            
               Critter loser = (winner == critter1) ? critter2 : critter1;
               if (debug) System.out.println("      winner=" + Util.toString(winner) + ", loser=" + Util.toString(loser));
            
               locationMap.remove(loser);
               int indexToRemove;
               if (winner == critter1) {
                  indexToRemove = Util.indexOfSafe(critterList, critter2);
                  winnerToString = critter1ToString;
               } else {
                  indexToRemove = i;
                  winnerToString = critter2ToString;
               }
               critterList.remove(indexToRemove);
               if (indexToRemove <= i) {
                  i--; // so we won't skip a critter by mistake
               }
            
            	// TODO: update the grid and display fields if necessary
            	// put null color, "." on location of loser
            	// problem: if winner is still there, should put his color/toString,
            	// but then we'll get them again when we call updateDisplay...
            	// should only call them once per update
            
            	// decrement various counters for each critter type
               String winnerClassName = winner.getClass().getName();
               String loserClassName = loser.getClass().getName();
               classStateMap.get(loserClassName).deaths++;
               classStateMap.get(loserClassName).count--;
               if (!winnerClassName.equals(loserClassName)) {
                  classStateMap.get(winnerClassName).kills++;
               }
            	
               loser.setAlive(false);
            }
         }
         grid[location.x][location.y] = winner;
         display[location.x][location.y] = winnerToString;
      
         if (winner == critter1) {
         	// critter is still alive
            critterState1.moves++;
            if (CRITTER_MOVE_FATIGUE_COUNT > 0 && critterState1.moves % CRITTER_MOVE_FATIGUE_COUNT == 0) {
               critterState1.foodPenalty = GLUTTON_PENALTY;
               if (debug) System.out.println("    moved too much; falling asleep for " + GLUTTON_PENALTY + " moves");
               critter1.setAwake(false);
               critter1.sleep();
            } else if (food[location.x][location.y]) {
            	// check whether this critter should eat food
               if (debug) System.out.print("    food found; calling eat ... ");
               boolean critterEat = critter1.eat();
               if (debug) System.out.println("returned " + critterEat);
               if (critterEat) {
                  food[location.x][location.y] = false;
                  classState1.foodEaten++;
                  if (!critterStateMap.containsKey(critter1)) {
                     throw new IllegalStateException("Unknown critter.  This should not happen: " + critter1);
                  }
                  critterState1.foodEaten++;
               
               	// possibly penalize the critter for eating too much
                  if (CRITTER_GLUTTON_COUNT > 0 && critterState1.foodEaten % CRITTER_GLUTTON_COUNT == 0) {
                     critterState1.foodPenalty = GLUTTON_PENALTY;
                     if (debug) System.out.println("    ate too much; falling asleep for " + GLUTTON_PENALTY + " moves");
                     critter1.setAwake(false);
                     critter1.sleep();
                  }
               
               	// possibly penalize the species as a whole for eating too much
                  if (CRITTER_CLASS_GLUTTON_COUNT > 0 && classState1.foodEaten % CRITTER_CLASS_GLUTTON_COUNT == 0) {
                     classState1.foodPenalty = GLUTTON_PENALTY;
                     if (debug) System.out.println("    class ate too much; falling asleep for " + GLUTTON_PENALTY + " moves");
                     critter1.setAwake(false);
                     critter1.sleep();
                  }
               }
            }
         }
      }
   
   	// any sleeping classes come one step closer to waking up
      for (CritterState state : classStateMap.values()) {
         if (state.isAsleep()) {
            state.foodPenalty--;
            if (!state.isAsleep()) {
            	// notify all the critters that they've woken up
               if (debug) System.out.println("  waking up all critters of type " + state.critterClass.getName());
               for (Critter critter : critterList) {
                  if (critter.getClass() == state.critterClass) {
                     if (debug) System.out.println("    waking up " + Util.toString(critter));
                     critter.setAwake(true);
                     critter.wakeup();
                  }
               }
            }
         }
      }
   	
   	// now process all individual critter animals' state
   	// (I make a copy of the map values so I won't get a ConcurrentModificationException
   	// if I add a baby to the game board in the middle of the foreach loop)
      List<CritterState> critterStates = new ArrayList<CritterState>(critterStateMap.values());
      for (CritterState state : critterStates) {
         if (state.isAsleep()) {
            // any sleeping animals come one step closer to waking up
            state.foodPenalty--;
         
         	// wake him up, if neither he nor his species is asleep
            if (state.isAsleep()) {
               if (debug) System.out.println("  " + state.foodPenalty + " moves until wakeup for " + Util.toString(state.critter));
            }else if (!classStateMap.get(state.critterClass.getName()).isAsleep()) {
               if (debug) System.out.println("  waking up " + Util.toString(state.critter));
               state.critter.setAwake(true);
               state.critter.wakeup();
            }
         }
         if (state.isMating()) {
            state.matePenalty--;
            if (state.isMating()) {
               if (debug) System.out.println("  " + state.matePenalty + " moves until done mating for " + Util.toString(state.critter) + " and " + Util.toString(state.mate));
            } else {
               // new baby born!
               CritterState state2 = critterStateMap.get(state.mate);
                    
               // critter 1 and 2 should be next to each other
               Point location1 = locationMap.get(state.critter);
               Point location2 = locationMap.get(state2.critter);
                    
               if (location1 == null) {
                  throw new RuntimeException(location2 + ": null location 1 for " + state.critterClass.getName() + " " + state.critter + " " + state.critter.hashCode() + ": " + locationMap);
               } else if (location2 == null) {
                  throw new RuntimeException(location1 + ": null location 2 for " + state2.critterClass.getName() + " " + state2.critter + " " + state2.critter.hashCode() + ": " + locationMap);
               }
                    
               // pick a random location for the baby to be born
               // (prefer a random spot that borders the parents)
               Set<Point> neighbors = getOpenNeighbors(location1);
               neighbors.addAll(getOpenNeighbors(location2));
               List<Point> neighborsList = new ArrayList<Point>(neighbors);
               Collections.shuffle(neighborsList);
               Point babyLocation = neighborsList.isEmpty() ? randomOpenLocation() : neighborsList.get(0);
                    
               if (debug) System.out.println("  done mating for " + Util.toString(state.critter) + " and " + Util.toString(state.mate));
               if (debug) System.out.println("    baby born at " + Util.toString(babyLocation));
               try {
                  Critter baby = add(state.critterClass, babyLocation);   // add the baby!
                  CritterState babyState = critterStateMap.get(baby);
                  babyState.daddy = state.critter;
                        
                  // adjust the class's state not to count babies in the initial count
                  CritterState speciesState = classStateMap.get(baby.getClass().getName());
                  speciesState.initialCount--;
               } catch (IllegalAccessException e) {
                  System.out.println(e);
               } catch (InvocationTargetException e) {
                  System.out.println(e);
               } catch (InstantiationException e) {
                  System.out.println(e);
               }
            
               // notify the critters that the boom shaka laka is over
               state.matePenalty = 0;
               state2.matePenalty = 0;
               state.critter.mateEnd();
               state2.critter.mateEnd();
            }
         }
      }
   
   	// respawn new food periodically
      if (moveCount % FOOD_RESPAWN_INTERVAL == 0) {
         Point open = randomOpenLocation();
         if (debug) System.out.println("  creating new food at " + Util.toString(open));
         food[open.x][open.y] = true;
      }
   
      updateDisplay(Event.UPDATE, true);
   }
	
   private Set<Point> getOpenNeighbors(Point location) {
      // pick random place for the baby to appear
      Set<Point> neighbors = new HashSet<Point>();
      for (int x = location.x - 1; x <= location.x + 1; x++) {
         for (int y = location.y - 1; y <= location.y + 1; y++) {
            int realX = (x + width) % width;
            int realY = (y + height) % height;
            if (grid[realX][realY] == null) {
               neighbors.add(new Point(realX, realY));
            }
         }
      }
      return neighbors;
   }

	// Adds a single instance of the given type to this model.
	// If the critter's constructor needs any parameters, gives random values.
   private Critter add(Class<? extends Critter> critterClass)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
      Point location = randomOpenLocation();
      return add(critterClass, location);
   }

	// Adds a single instance of the given type to this model.
   // If the critter's constructor needs any parameters, gives random values.
   private Critter add(Class<? extends Critter> critterClass, Point location)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
      if (getTotalCritterCount() >= width * height) {
         throw new TooManyCrittersException();
      }
   
   	// create critter
      Constructor<? extends Critter> ctor = getConstructor(critterClass);
      Object[] params = createRandomParameters(critterClass, ctor);
   	// Critter critter = ctor.newInstance(params);
      Object obj = ctor.newInstance(params);
      if (debug) System.out.println("Constructed new " + critterClass.getName() + " (id " + obj.hashCode() + ") at " + Util.toString(location) + " with parameter(s): " + Arrays.toString(params));
        
      Critter critter;
      if (obj instanceof Critter) {
         critter = (Critter) obj;
      } else if (Util.usingDrJava()) {
         throw new DrJavaSucksException(critterClass.getName()
                                        + " has been downloaded.  Close and re-run the simulator to use it.");
      } else {
         throw new InvalidCritterClassException(critterClass.getName()
                                                + " is not a valid Critter and cannot be loaded.");
      }
   
      critterList.add(critter);
   
   	// place critter on board
      locationMap.put(critter, location);
      grid[location.x][location.y] = critter;
   
   	// count # of critters of each class
      String className = critter.getClass().getName();
      CritterState state = classStateMap.get(className);
      state.count++;
      state.initialCount++;
   
   	// count various things about each critter object
      CritterState objectState = new CritterState(critterClass, critter);
      critterStateMap.put(critter, objectState);
   	
      return critter;
   }

	// Fills the board with food in randomly chosen locations.
   private void createRandomFood() {
   	// clear out any previous food
      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            food[x][y] = false;
         }
      }
   
   	// randomly fill some fraction of all squares
      int squaresToFill = FOOD_PERCENTAGE * width * height / 100;
      for (int i = 0; i < squaresToFill; i++) {
         int randomX = rand.nextInt(width);
         int randomY = rand.nextInt(height);
         food[randomX][randomY] = true;
      }
   }

	// Fills and returns an array of random values of the proper types
	// for the given constructor.
   private Object[] createRandomParameters(
                                           Class<? extends Critter> critterClass,
                                           Constructor<? extends Critter> ctor) {
      Class<?>[] paramTypes = ctor.getParameterTypes();
      Object[] params = new Object[paramTypes.length];
   
   	// build random parameters
      for (int j = 0; j < params.length; j++) {
         if (paramTypes[j] == Integer.TYPE) {
         	// int parameters are things like hunger;
         	// randomly choose a value from 0 through MAX
            params[j] = new Integer(rand.nextInt(INT_PARAM_MAX) + 1);
         } else if (paramTypes[j] == Boolean.TYPE) {
            params[j] = rand.nextBoolean();
         } else if (paramTypes[j] == String.class) {
            int r = rand.nextInt(RANDOM_LETTERS.length());
            params[j] = RANDOM_LETTERS.substring(r, r + 1);
         } else if (paramTypes[j] == Color.class) {
            params[j] = randomColor();
         } else if (paramTypes[j] == Critter.Attack.class) {
         	// special case: Elephant's int parameter is an attack
            int rIndex = rand.nextInt(Critter.Attack.values().length);
            params[j] = Critter.Attack.values()[rIndex];
         } else if (paramTypes[j] == Critter.Direction.class) {
         	// special case: Elephant's int parameter is an attack
            int rIndex = rand.nextInt(Critter.Direction.values().length);
            params[j] = Critter.Direction.values()[rIndex];
         } else {
            throw new InvalidCritterClassException("when constructing "
                                                   + critterClass + ":\nbad constructor parameter type: "
                                                   + paramTypes[j]);
         }
      }
      return params;
   }

	// Conducts a fight between the given two critters.
	// Returns which critter won the game.  The other must die!
   private Critter fight(Critter critter1, Critter critter2) {
      Critter.Attack weapon1 = critter1.fight(critter2.toString());
      Critter.Attack weapon2 = critter2.fight(critter1.toString());
   
      Critter winner;
   
   	// special case: if one of the animals is sleeping, it dies
      CritterState classState1 = classStateMap.get(critter1.getClass().getName());
      CritterState classState2 = classStateMap.get(critter2.getClass().getName());
      if (classState1 == classState2) {
         throw new RuntimeException("BUG: Should not get here.  Two " + critter1.getClass().getName() + "s fighting!");
      }
   	
      CritterState state1 = critterStateMap.get(critter1);
      CritterState state2 = critterStateMap.get(critter2);
      if (((classState1.isAsleep() || state1.isAsleep()) && (classState2.isAsleep() || state2.isAsleep())) || 
          (state1.isMating() && state2.isMating())) {
      	// shouldn't get here
         throw new IllegalStateException("Fight between two sleeping/mating critters.  This should not happen!");
      } else if (classState1.isAsleep() || state1.isAsleep() || state1.isMating()) {
         winner = critter2;
      } else if (classState2.isAsleep() || state2.isAsleep() || state2.isMating()) {
         winner = critter1;
      } else {
      	// let's randomly decide that if both animals forfeit, player 1 wins
         if (weapon1 == weapon2) {
         	// tie
            winner = (Math.random() < 0.5) ? critter1 : critter2;
         } else if ((weapon2 == Critter.Attack.FORFEIT)
                    || (weapon1 == Critter.Attack.ROAR && weapon2 == Critter.Attack.SCRATCH)
                    || (weapon1 == Critter.Attack.SCRATCH && weapon2 == Critter.Attack.POUNCE)
                    || (weapon1 == Critter.Attack.POUNCE && weapon2 == Critter.Attack.ROAR)) {
         	// player 1 wins
            winner = critter1;
         } else {
         	// player 2 wins
            winner = critter2;
         }
      }
   
   	// inform the critters that they have won/lost
      Critter loser = (winner == critter1) ? critter2 : critter1;
      winner.win();
      loser.lose();
   	
   	// if the loser was mating, inform the mate to go back to normal
      CritterState loserState = (winner == critter1) ? state2 : state1;
      if (loserState.isMating()) {
         loserState.matePenalty = 0;
         CritterState mateState = critterStateMap.get(loserState.mate);
         mateState.matePenalty = 0;
         loserState.mate.mateEnd();
      }
   	
      return winner;
   }

	// Gets and returns the constructor for the given class by reflection.
   @SuppressWarnings("unchecked")
      private Constructor<? extends Critter> getConstructor(Class<? extends Critter> critterClass) {
   	// TODO: change to getConstructor() (no warning)
      Constructor<? extends Critter>[] ctors = (Constructor<? extends Critter>[]) critterClass.getConstructors();
      if (ctors.length != 1) {
         throw new InvalidCritterClassException(
                                                "wrong number of constructors (" + ctors.length + ") for "
                                                + critterClass + "; must have only one constructor");
      }
      return ctors[0];
   }

	// Translates a point's coordinates 1 unit in a particular direction.
   private Point movePoint(Point p, Critter.Direction direction) {
      if (direction == Critter.Direction.NORTH) {
         p.y = (p.y - 1 + height) % height;
      } else if (direction == Critter.Direction.SOUTH) {
         p.y = (p.y + 1) % height;
      } else if (direction == Critter.Direction.EAST) {
         p.x = (p.x + 1) % width;
      } else if (direction == Critter.Direction.WEST) {
         p.x = (p.x - 1 + width) % width;
      } // else direction == Critter.CENTER
      return p;
   }

	// Returns a random color.
   private Color randomColor() {
   	// return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
      double r = Math.random();
      if (r < 0.333) {
         return Color.WHITE;
      } else if (r < 0.667) {
         return Color.BLACK;
      } else {
         return BROWN;
      }
   }

	// Returns a random point that is unoccupied by any critters. 
   private Point randomOpenLocation() {
   	// TODO: If board is completely full of animals, throw exception
      if (critterList.size() >= width * height) {
         throw new TooManyCrittersException();
      }
       
      Point p = new Point();
      do {
         p.x = rand.nextInt(width);
         p.y = rand.nextInt(height);
      } while (grid[p.x][p.y] != null);
      return p;
   }

	// Updates the internal string array representing the text to display.
	// Also notifies observers of a new event.
	// Doesn't throw exceptions if colors or toStrings are null.
   private void updateDisplay(Event event) {
      updateDisplay(event, false);
   }

	// Updates the internal string array representing the text to display.
	// Also notifies observers of a new event.
	// Possibly throws exceptions if colors or toStrings are null.
   private void updateDisplay(Event event, boolean throwOnNull) {
      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            updateDisplaySquare(x, y, throwOnNull);
         }
      }
   
      setChanged();
      notifyObservers(event);
   }
	
   // Updates a square of the internal string array representing the text to display.
   // Possibly throws exceptions if colors or toStrings are null.
   private void updateDisplaySquare(int x, int y, boolean throwOnNull) {
      if (grid[x][y] == null) {
         if (food[x][y]) {
            display[x][y] = FOOD;
            colorDisplay[x][y] = FOOD_COLOR;
         } else {
            display[x][y] = EMPTY;
            colorDisplay[x][y] = Color.BLACK;
         }
      } else {
         display[x][y] = grid[x][y].toString();
         if (throwOnNull && display[x][y] == null) {
            throw new IllegalArgumentException(grid[x][y].getClass().getName() + 
                                               " returned a null toString result.");
         }
      
         colorDisplay[x][y] = grid[x][y].getColor();
         if (throwOnNull && colorDisplay[x][y] == null) {
            throw new IllegalArgumentException(grid[x][y].getClass().getName() + 
                                               " returned a null getColor result.");
         }
      }
   }

	// Inner class to represent the state of a particular critter class or object.
   public static class CritterState {
      private Class<? extends Critter> critterClass;
      private Critter critter;
      private Critter daddy;
      private int count;
      private int initialCount;
      private int kills;
      private int deaths;
      private int moves;
      private int foodEaten;
      private int foodPenalty;
      private int matePenalty;
      private Critter mate;
   
   	// Constructs object to represent state of the given class.
      public CritterState(Class<? extends Critter> critterClass) {
         this(critterClass, null);
      }
   
   	// Constructs object to represent state of the given class.
      public CritterState(Class<? extends Critter> critterClass, Critter critter) {
         this.critterClass = critterClass;
         this.critter = critter;
      }
   
   	// Returns how many animals are alive.
      public int getCount() {
         return count;
      }
   
   	// Returns how many times an animal of this type has died.
      public int getDeaths() {
         return deaths;
      }
   
   	// Returns how many times an animal of this type has eaten food.
      public int getFoodEaten() {
         return foodEaten;
      }
   
   	// Returns how many moves this type is currently being penalized
   	// for eating too much food.
      public int getFoodPenalty() {
         return foodPenalty;
      }
   
   	// Returns how many animals of this type have ever been created.
      public int getInitialCount() {
         return initialCount;
      }
   
   	// Returns how many animals this type has killed.
      public int getKills() {
         return kills;
      }
   
      // Returns how many moves this type is currently frozen during mating.
      public int getMatePenalty() {
         return matePenalty;
      }
   
   	// Returns how many times this critter has moved.
      public int getMoves() {
         return moves;
      }
   	
   	// Returns true if this animal has a love partner and has mated.
      public boolean hasMate() {
         return mate != null;
      }
   
      // Returns true if this class is asleep.
      public boolean isAsleep() {
         return foodPenalty > 0;
      }
        
      public boolean isBaby() {
         return isBaby(true);
      }
   
      public boolean isBaby(boolean considerMoves) {
         if (considerMoves) {
            return moves < MATING_PENALTY && daddy != null;
         } else {
            return daddy != null;
         }
      }
   
      // Returns true if this class is currently mating.
      public boolean isMating() {
         return mate != null && matePenalty > 0;
      }
   
   	// Resets the state of this type.
      public void reset() {
         count = 0;
         deaths = 0;
         foodEaten = 0;
         foodPenalty = 0;
         initialCount = 0;
         kills = 0;
         moves = 0;
         mate = null;
         matePenalty = 0;
         daddy = null;
      }
   
      public String toString() {
         return Util.toString(this);
      }
   	
   	// TODO: Comment out
      //		public Class<? extends Critter> getCritterClass() {
      //		    return critterClass;
      //		}
      //		
      //		public Critter getCritter() {
      //		    return critter;
      //		}
      //		
      //		public Critter getMate() {
      //		    return mate;
      //		}
   }

	// Used to signal various types to observers
   public enum Event {
      ADD, NEW, REMOVE_ALL, RESET, UPDATE
   }

	// An exception thrown when the model is unable to instantiate a critter
	// class because of DrJava blowing so much.
   public static class DrJavaSucksException extends RuntimeException {
      private static final long serialVersionUID = 0;
   
      public DrJavaSucksException(Exception e) {
         super(e);
      }
   
      public DrJavaSucksException(String message) {
         super(message);
      }
   }

	// An exception thrown when the model is unable to instantiate a class.
   public static class InvalidCritterClassException extends RuntimeException {
      private static final long serialVersionUID = 0;
   
      public InvalidCritterClassException(Exception e) {
         super(e);
      }
   
      public InvalidCritterClassException(String message) {
         super(message);
      }
   }

	// An exception thrown when a bad direction integer is passed.
   public static class InvalidDirectionException extends RuntimeException {
      private static final long serialVersionUID = 0;
   
      public InvalidDirectionException(int direction) {
         super(String.valueOf(direction));
      }
   
      public InvalidDirectionException(String message) {
         super(message);
      }
   }

	// An exception thrown when the model becomes too full to fit more critters.
   public static class TooManyCrittersException extends RuntimeException {
      private static final long serialVersionUID = 0;
   }
}


// CSE 142 Homework 8 (Critters)
// Author: Marty Stepp
//
// Handles network responsibilities in the critter game.
//

// Critter-specific usage of the network manager object.
class CritterNetworkManager extends NetworkManager {
	// class constants
   public static final int DEFAULT_PORT_1 = 5142;

   public static final int DEFAULT_PORT_2 = 5143;

	// Constructs a CritterNetworkManager at the default port.
   public CritterNetworkManager() {
      super(DEFAULT_PORT_1);
   }

	// Constructs a CritterNetworkManager at the given port.
   public CritterNetworkManager(int port) {
      super(port);
   }

	// Sends out a message to the given host requesting that they send
	// their class with the given name back to us.
   public void requestClass(String host, String className) {
      send(host, getHostName(), className);
   }

	// Sends out the text of the given class to the given host.
   public void sendText(String host, String className, String fileText) {
      send(host, getHostName(), className, fileText);
   }

	/*
   // Sends out the text of the given class to the given host.
   public void sendClass(String host, String className, byte[] bytes) {
   String encodedText = Base64.encodeToString(bytes);
   send(host, getHostName(), className, encodedText);
   }
   */
}

// General reusable version of event-driven network manager object.
class NetworkManager {
	// class constants
   private static final boolean DEBUG = false;

   private static String hostName;

   private static String ipAddress;

   private static String ipAddresses = "";

   static {
   	// use host name to name classes sent over the wire
   	// also grab IP address(es) of local computer
      try {
         InetAddress localhost = InetAddress.getLocalHost();
         hostName = localhost.getHostName();
         ipAddress = localhost.getHostAddress();
      
         for (InetAddress addr : InetAddress.getAllByName(hostName)) {
            ipAddresses += " " + addr.getHostAddress();
         }
         ipAddresses = ipAddresses.trim();
      } catch (UnknownHostException e) {
         hostName = "unknown_host";
         ipAddress = ipAddresses = "127.0.0.1";
      } catch (SecurityException e) {
         hostName = "unknown_host";
         ipAddress = ipAddresses = "127.0.0.1";
      }
   }

	// Returns this computer's host name.
   public static String getHostName() {
      return hostName;
   }

	// Returns this computer's IP address.
   public static String getIpAddress() {
      return ipAddress;
   }

	// Returns this computer's IP address.
   public static String getIpAddresses() {
      return ipAddresses;
   }

	// fields
   private int port;

   private boolean shouldContinue;

   private List<Message> outQueue; // queue of messages to send

   private Thread sendThread;

   private Thread receiveThread;

   private ServerSocket receiveSrvSock;

   private Event<String[]> receive; // events to represent messages received

   private Event<Exception> error; // and errors that occur

	// Constructs a network manager at the given port.
   public NetworkManager(int port) {
      this.port = port;
      shouldContinue = true;
      outQueue = Collections.synchronizedList(new LinkedList<Message>());
      receive = new Event<String[]>();
      error = new Event<Exception>();
   }

	// Returns this network manager's observable error event object,
	// so observers can be notified about errors and respond to them.
   public Event<? extends Exception> getErrorEvent() {
      return error;
   }

	// Returns this network manager's port.
   public int getPort() {
      return port;
   }

	// Returns this network manager's observable receive event object,
	// so observers can be notified when messages are received.
   public Event<String[]> getReceiveEvent() {
      return receive;
   }

	// Sends a message to the given host containing the given strings.
   public void send(String host, String... strings) {
      synchronized (outQueue) {
         Message message = new Message(host, strings);
         outQueue.add(message);
      }
   }

	// Notifies this network manager that you want it to shut down
	// and stop listening for messages.
   @SuppressWarnings("deprecation")
      public void stop() {
      shouldContinue = false;
      try {
         if (receiveSrvSock != null) {
            receiveSrvSock.close();
         }
      } catch (Exception e) {
      }
   
      if (sendThread != null) {
         sendThread.stop();
         sendThread = null;
      }
      if (receiveThread != null) {
         receiveThread.stop();
         receiveThread = null;
      }
   }

	// Starts listening for messages on the network.
   public void start() throws IOException {
      shouldContinue = true;
      if (sendThread == null) {
         sendThread = new Thread(new SendRunnable());
         receiveThread = new Thread(new ReceiveRunnable());
      }
   
      sendThread.start();
      receiveThread.start();
   }

	// A helper class to represent observable events.
   class Event<T> extends Observable {
   	// Convenience method to replace notifyObservers.
      public void fire() {
         notifyObservers(null);
      }
   
   	// Convenience method to replace notifyObservers.
      public void fire(T arg) {
         setChanged();
         super.notifyObservers(arg);
      }
   }

	// Listening thread that waits for messages to arrive.
   private class ReceiveRunnable implements Runnable {
      public ReceiveRunnable() throws IOException {
         receiveSrvSock = new ServerSocket(port);
      }
   
   	// Runs the listening thread.
      public void run() {
         try {
            while (shouldContinue) {
            	// wait for a message to arrive
               Socket sock = receiveSrvSock.accept();
               InputStream stream = sock.getInputStream();
               ObjectInputStream ois = new ObjectInputStream(stream);
            
            	// read message
               String[] strings = (String[]) ois.readObject();
               if (DEBUG)
                  System.out.println("Received on port " + port + ":\n"
                                     + Arrays.toString(strings));
            
            	// notify observers that message has arrived
            
               receive.fire(strings);
            }
         } catch (Exception e) {
         	// notify observers that an error occurred
            error.fire(e);
         }
      }
   }

	// Sending thread that outputs messages from the output queue to the
	// network.
   private class SendRunnable implements Runnable {
      public void run() {
         while (shouldContinue) {
         	// test-and-test-and-set paradigm
            if (!outQueue.isEmpty()) {
               synchronized (outQueue) {
                  if (!outQueue.isEmpty()) {
                  	// grab message from the queue
                     Message message = outQueue.remove(0);
                  
                     try {
                     	// send the message
                        Socket sock = new Socket(message.host, port);
                        OutputStream stream = sock.getOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(
                                                                        stream);
                        oos.writeObject(message.strings);
                        if (DEBUG)
                           System.out.println("Sent on port " + port
                                              + ":\n" + message);
                     } catch (IOException e) {
                     	// notify observers that an error occurred
                        error.fire(e);
                     }
                  }
               }
            }
         
         	// wait 1 second between checks for messages so this thread
         	// won't drain 100% of the CPU
            try {
               Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
         }
      }
   }

	// Convenience classto represent messages about to be sent over the network.
   private static class Message implements Serializable {
   	// class constants
      private static final long serialVersionUID = 0;
   
   	// fields
      public String host;
   
      public String[] strings;
   
   	// Constructs a message to the given host containing the given strings.
      public Message(String host, String[] strings) {
         this.host = host;
         this.strings = strings;
      }
   
   	// Returns debug text about this message.
      public String toString() {
         return "Message{host=" + host + ", strings="
            + Arrays.toString(strings) + "}";
      }
   }
}


// CSE 142 Homework 8 (Critters)
// Authors: Marty Stepp, Stuart Reges
//
// A drawing surface that draws the state of all critters in the simulation.
//

class CritterPanel extends JPanel implements Observer {
	// class constants
   private static final long serialVersionUID = 0;

   private static final boolean ANTI_ALIAS = false;
   private static final Color BACKGROUND_COLOR = new Color(220, 255, 220);
   private static final int FONT_SIZE = 12;
   private static final Font FONT = new Font("Monospaced", Font.BOLD, FONT_SIZE + 4);
   private static final int MIN_COLOR = 192;  // darkest bg color out of 255
   private static final List<Color> PREDEFINED_COLORS = new LinkedList<Color>();

   static {
      PREDEFINED_COLORS.add(new Color(255, 220, 220));
      PREDEFINED_COLORS.add(new Color(200, 200, 255));
      PREDEFINED_COLORS.add(new Color(200, 255, 200));
      PREDEFINED_COLORS.add(new Color(255, 200, 100));
      PREDEFINED_COLORS.add(new Color(200, 255, 255));
      PREDEFINED_COLORS.add(new Color(255, 255, 100));
   }

	// fields
   private CritterModel model;
   private Map<String, Color> colorMap;
   private boolean backgroundColors;

	// Constucts a new panel to display the given model.
   public CritterPanel(CritterModel model, boolean backgroundColors) {
      this.model = model;
      model.addObserver(this);
   
      colorMap = new HashMap<String, Color>();
      this.backgroundColors = backgroundColors;
   
      setFont(FONT);
      setBackground(BACKGROUND_COLOR);
      setPreferredSize(new Dimension(FONT_SIZE * model.getWidth() + 1,
                                     FONT_SIZE * (model.getHeight()) + FONT_SIZE / 2));
   
   	// pre-decide colors so that east GUI labels look right
      ensureAllColors();
   }

	// Ensures that any currently visible Husky class has been assigned a
	// background color.
   public void ensureAllColors() {
      if (backgroundColors) {
         for (int x = 0; x < model.getWidth(); x++) {
            for (int y = 0; y < model.getHeight(); y++) {
               Class<? extends Critter> clazz = model.getCritterClass(x, y);
               if (CritterModel.isHuskyClass(clazz)) {
                  ensureColorExists(clazz);
               }
            }
         }
      }
   }

   public Color getColor(String className) {
      return colorMap.get(className);
   }

	// Paints the critters on the panel.
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
   
   	// anti-aliasing
      if (ANTI_ALIAS) {
         Graphics2D g2 = (Graphics2D) g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      }
   
   	// because font is monospaced, all widths should be the same;
   	// so we can get char width from any char (in this case x)
   
   	// draw all critters
      for (int x = 0; x < model.getWidth(); x++) {
         for (int y = 0; y < model.getHeight(); y++) {
            // if sleeping/mating, draw a "zzz" bubble or heart
            int drawX = getDrawX(x);
            int drawY = getDrawY(y);
            if (model.isAsleep(x, y)) {
               drawBubble(g, "z", drawX, drawY);
            } else if (model.isMating(x, y)) {
               drawHeart(g, drawX, drawY);
            }
         }
      }
   
      for (int x = 0; x < model.getWidth(); x++) {
         for (int y = 0; y < model.getHeight(); y++) {
            int drawX = getDrawX(x);
            int drawY = getDrawY(y);
            Color color = model.getColor(x, y);
         
         	// possibly draw a background color behind the critter
            if (backgroundColors) {
               Class<? extends Critter> clazz = model.getCritterClass(x, y);
               if (CritterModel.isHuskyClass(clazz)) {
                  Color bgColor = ensureColorExists(clazz);
                  g.setColor(bgColor);
                  g.fillRect(drawX - 1, drawY - FONT_SIZE + 1, FONT_SIZE, FONT_SIZE + 1);
               }
            }
         
         	// draw the critter's toString representation
            String critterString = model.getString(x, y);
            if (model.isBaby(x, y)) {
               critterString = critterString.toLowerCase();
            }
            drawShadowedString(g, critterString, color, drawX, drawY);
         }
      }
   }

   public void setBackgroundColors(boolean backgroundColors) {
      this.backgroundColors = backgroundColors;
      repaint();
   }

	// Responds to Observable updates to the model.
   public void update(Observable o, Object arg) {
      repaint();
   }
	
   private void drawHeart(Graphics g, int x, int y) {
      // heart (mating)
      g.setColor(Color.PINK);
      int heartX = x + FONT_SIZE / 3;
      int heartY = y - 5 * FONT_SIZE / 4;
      Polygon heart = new Polygon();
      heart.addPoint(heartX, heartY + 2);
      heart.addPoint(heartX + 2, heartY);
      heart.addPoint(heartX + 5, heartY);
      heart.addPoint(heartX + 7, heartY + 2);
      heart.addPoint(heartX + 9, heartY);
      heart.addPoint(heartX + 12, heartY);
      heart.addPoint(heartX + 14, heartY + 2);
      heart.addPoint(heartX + 14, heartY + 5);
      heart.addPoint(heartX + 7, heartY + 11);
      heart.addPoint(heartX, heartY + 5);
      g.fillPolygon(heart);
   }
	
   private void drawBubble(Graphics g, String text, int x, int y) {
      int bubbleX = x + FONT_SIZE / 2;
      int bubbleY = y - 3 * FONT_SIZE / 2;
      int bubbleSize = FONT_SIZE;
       
      g.setColor(Color.WHITE);
      g.fillOval(bubbleX, bubbleY, bubbleSize, bubbleSize);
      g.setColor(Color.GRAY);
      g.drawOval(bubbleX, bubbleY, bubbleSize, bubbleSize);
       
      // draw text in bubble
      Font oldFont = g.getFont();
      Font newFont = oldFont.deriveFont(11f);
      Rectangle2D bounds = g.getFontMetrics().getStringBounds(text, g);
      int textX = (int) (bubbleX + bubbleSize / 2 - bounds.getWidth() / 2 + 2);
      int textY = (int) (bubbleY + bubbleSize / 2 + 11f / 2 - 2);
       
      g.setColor(Color.BLACK);
      g.setFont(newFont);
      g.drawString(text, textX, textY);
      g.setFont(oldFont);
   }

	// Draws the given text with a dark shadow beneath it.
   private void drawShadowedString(Graphics g, String s, Color c, int x, int y) {
      if (s == null) {
         return;
      }
      g.setColor(Color.BLACK);
      drawStringSpaced(g, s, x + 1, y + 1);
      if (c != null) {
         g.setColor(c);
      }
      drawStringSpaced(g, s, x, y);
   }
	
	// draws each letter evenly spaced.
   private void drawStringSpaced(Graphics g, String s, int x, int y) {
      for (int i = 0; i < s.length(); i++) {
         g.drawString(s.substring(i, i + 1), x, y);
         x += FONT_SIZE;
      }
   }

   private Color ensureColorExists(Class<? extends Critter> clazz) {
      Color bgColor = getColor(clazz.getName());
      if (bgColor == null) {
         if (PREDEFINED_COLORS.isEmpty()) {
            bgColor = new Color(
                                (int) (Math.random() * (256 - MIN_COLOR)) + MIN_COLOR,
                                (int) (Math.random() * (256 - MIN_COLOR)) + MIN_COLOR,
                                (int) (Math.random() * (256 - MIN_COLOR)) + MIN_COLOR);
         } else {
            bgColor = PREDEFINED_COLORS.remove(0);
         }
         colorMap.put(clazz.getName(), bgColor);
      }
      return bgColor;
   }

	// Returns the RGB opposite of the given color.
   public Color getReverseColor(Color c) {
      return new Color(~c.getRGB());
   }
	
   private int getDrawX(int x) {
      return x * FONT_SIZE + 2;
   }
	
   private int getDrawY(int y) {
      return (y + 1) * FONT_SIZE;
   }
}


// CSE 142 Homework 8 (Critters)
// Authors: Marty Stepp and Stuart Reges
//
// Provides a secure environment for the critters to play in.
// Forbids them from reading files or network resources.
//

class CritterSecurityManager extends SecurityManager {
   public void checkAccept(String host, int port) {
   }

   public void checkConnect(String host, int port) {
   }

   public void checkConnect(String host, int port, Object context) {
   }

   public void checkCreateClassLoader() {
   }

   public void checkDelete(String file) {
      throw new SecurityException("cannot write: " + file);
   }

   public void checkExec(String cmd) {
      throw new SecurityException("cannot exec: " + cmd);
   }

   public void checkLink(String lib) {
   }

   public void checkListen(int port) {
   	// if (port != CritterNetworkManager.DEFAULT_PORT_1
   	// && port != CritterNetworkManager.DEFAULT_PORT_2) {
   	// throw new SecurityException("Invalid port: " + port);
   	// }
      throw new SecurityException("cannot listen: " + port);
   }

   public void checkMemberAccess(Class<?> clazz, int which) {
      if (which != Modifier.PUBLIC) {
      	// throw new SecurityException("cannot access member: " + clazz + ",
      	// " + which);
      }
   }

   public void checkPackageAccess(String pkg) {
   }

   public void checkPackageDefinition(String pkg) {
   }

   public void checkPermission(Permission perm) {
   }

   public void checkPropertiesAccess() {
   }

   public void checkPropertyAccess(String property) {
   }

   public void checkRead(FileDescriptor fd) {
   }

   public void checkRead(String file) {
   }

   public void checkRead(String file, Object context) {
   }

	// so the windows won't have a "Java Applet Window" warning
   public boolean checkTopLevelWindow(Object window) {
      return true;
   }

   public void checkWrite(FileDescriptor fd) {
   	// throw new SecurityException("cannot write: " + fd);
   }

   public void checkWrite(String file) {
      if (!file.equals(CritterGui.SAVE_STATE_FILE_NAME)
          && !file.equals(InputPane.SAVE_STATE_FILE_NAME)) {
         throw new SecurityException("cannot write: " + file);
      }
   }
}

// CSE 142 Homework 8 (Critters)
// Author: Marty Stepp
//
// A bunch of methods used to dynamically load critter classes sent across
// the web.  Useful for running 1-on-1 critter tournaments.
//

class ClassUtils {
	// class constants
   public static final String CLASS_EXTENSION = ".class";

   public static final String JAVA_EXTENSION = ".java";

   private static final FileFilter CLASS_FILTER = new ExtensionFilter(
                                                                      CLASS_EXTENSION);

   private static final boolean SHOULD_CACHE = false;

	// Adds 25 of each critter class type to the given model.
	// The only critter-specific code in here; a bit of a cheat
	// so that CritterMain.java doesn't have to have this icky code in it.
   public static void addAllCritterClasses(CritterModel model, int count) {
      for (Class<? extends Critter> critterClass : ClassUtils.getClasses(
                                                                         Critter.class, ".")) {
         model.add(count, critterClass);
      }
   }

	// Adds 25 of each Wolf class type to the given model.
   public static void addOtherWolfClasses(CritterModel model, int count) {
      for (Class<? extends Critter> critterClass : ClassUtils.getClasses(
                                                                         Critter.class, ".")) {
         if (isNetworkClass(critterClass.getName())) {
            model.add(count, critterClass);
         }
      }
   }

	// Returns whether the given name represents an inner class (has a $ sign).
   public static boolean isInnerClass(String className) {
      return className.indexOf('$') >= 0;
   }

	// Returns whether the given class is one that came from the network.
	// Excludes inner classes (ones with $ in their name).
   public static boolean isNetworkClass(String className) {
      return className.indexOf('_') >= 0 && !isInnerClass(className);
   }

	// DrJava makes nightmares for me ...
   public static boolean isDrJavasFault(String className) {
      return new File(className + ClassUtils.CLASS_EXTENSION).exists()
         && System.getProperties().toString().toLowerCase().indexOf(
                                                                    "drjava") >= 0;
   }

	/*
	 * // Reads the .java file for the given "old" class, renames it to the
	 * "new" class name, // compiles the newly created .java file, reads the
	 * bytes of the .class file // just made, base64 encodes those bytes into a
	 * String, and returns that. Phew! public static String
	 * renameCompileEncode(String oldClassName, String newClassName) throws
	 * ClassNotFoundException, IOException { String fileText =
	 * readEntireFile(oldClassName + JAVA_EXTENSION); String newJavaFileName =
	 * renameAndWriteJavaFile(fileText, oldClassName, newClassName, false);
	 * 
	 * String classFileName = compile(newJavaFileName); new
	 * File(newJavaFileName).delete();
	 * 
	 * byte[] bytes = readEntireFileBytes(classFileName); new
	 * File(classFileName).deleteOnExit();
	 * 
	 * String encodedFileText = Base64.encodeToString(bytes); return
	 * encodedFileText; }
	 *  // An overall method that takes the given file text and dumps it // to
	 * the disk, renames it, recompiles it, loads the class into the JVM, // and
	 * returns the associated Class object. public static Class<?>
	 * doEverythingText(String fileText, String oldClassName, String
	 * newClassName, boolean useTempFolder) throws ClassNotFoundException {
	 * newClassName = sanitizeClassName(newClassName); String newFileName =
	 * renameAndWriteJavaFile(fileText, oldClassName, newClassName,
	 * useTempFolder); String classFileName = compile(newFileName); new
	 * File(newFileName).delete(); if (useTempFolder) { new
	 * File(classFileName).deleteOnExit(); } return loadClass(classFileName); }
	 */

   public static Class<?> writeAndLoadClass(String fileText, String className,
                                            boolean useTempFolder) throws IOException, ClassNotFoundException,
                                                                          IllegalAccessException, InvocationTargetException,
                                                                          NoSuchMethodException {
   	// write the modified text to a new file (possibly in temp dir)
      String javaFileName = className + JAVA_EXTENSION;
      if (useTempFolder) {
         javaFileName = System.getProperty("java.io.tmpdir")
            + File.separatorChar + javaFileName;
      }
      writeEntireFile(fileText, javaFileName);
   
      String classFileName = compile(javaFileName);
      new File(javaFileName).delete();
   
   	// move class to current directory
      new File(classFileName).renameTo(new File("." + File.separatorChar
                                                + className + CLASS_EXTENSION));
   
      return loadClass(classFileName);
   }

	/*
	 * public static Class<?> writeAndLoadEncodedClass(String encodedClassText,
	 * String className) throws ClassNotFoundException, IOException { byte[]
	 * bytes = Base64.decodeToBytes(encodedClassText); String classFileName =
	 * className + CLASS_EXTENSION; writeBytes(bytes, classFileName); //
	 * newClassName = sanitizeClassName(newClassName); return
	 * loadClass(classFileName); }
	 */

	// Compiles the .java source file with the given file name,
	// and returns the file name of the newly compiled .class file.
	// Throws a RuntimeException if the compilation fails.
	// TODO: *** make this use JDK 1.6's new JavaCompiler interface
   public static String compile(String fileName)
      throws ClassNotFoundException, NoSuchMethodException,
             IllegalAccessException, InvocationTargetException {
      String folderName = getFolder(fileName);
      String sep = System.getProperty("path.separator");
      String[] args = { "-classpath", "." + sep + folderName, fileName };
   	// int result = com.sun.tools.javac.Main.compile(args);
      Class<?> compilerClass = Class.forName("com.sun.tools.javac.Main");
      Method compileMethod = compilerClass.getMethod("compile", new String[0]
                                                     .getClass());
      compileMethod.invoke(null, (Object) args);
      int result = 0;
      if (result != 0) {
         throw new RuntimeException("Compilation failed: error code "
                                    + result);
      }
   
      return removeExtension(fileName) + CLASS_EXTENSION;
   }

	// Downloads the file at the given URL and saves it to the local disk.
	// For example "www.foo.com/bar/baz/Mumble.txt" saves to "Mumble.txt".
	// Returns the file name of the saved local file.
   public static String downloadFile(URL url) throws IOException {
      String fileName = removeFolder(url.getFile());
      if (!SHOULD_CACHE || !(new File(fileName).exists())) {
         OutputStream output = new PrintStream(fileName);
         InputStream input = new BufferedInputStream(url.openStream());
         byte[] buffer = new byte[512000];
         int numRead;
         long numWritten = 0;
         while ((numRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, numRead);
            numWritten += numRead;
         }
         output.close();
      }
      return fileName;
   }

	// Loads all classes that extend the given class from the given folder.
   @SuppressWarnings("unchecked")
      public static <T> List<Class<? extends T>> getClasses(Class<T> superClass, String folderName) {
      try {
         List<Class<? extends T>> list = new ArrayList<Class<? extends T>>();
         File folder = new File(folderName);
         if (!folder.exists() || !folder.canRead()) {
            return list;
         }
      
         for (File file : folder.listFiles(CLASS_FILTER)) {
            String fileName = file.getName();
            if (file.canRead() && !file.isDirectory() && fileName.endsWith(CLASS_EXTENSION)) {
               try {
                  Class<?> existingClass = Class.forName(removeExtension(fileName));
                  if (!existingClass.isInterface()
                      && !Modifier.isAbstract(existingClass.getModifiers())
                      && superClass.isAssignableFrom(existingClass)) {
                  	// then this is a concrete class that implements the interface
                     list.add((Class<? extends T>) existingClass);
                  }
               } catch (IncompatibleClassChangeError icce) {
                  icce.printStackTrace();
               } catch (Throwable t) {
                  System.out.println("error reading " + fileName + ":");
                  t.printStackTrace();
               }
            }
         }
      	
         Collections.sort(list, new ClassComparator());
         return list;
      } catch (SecurityException e) {
      	// probably running as an applet; return default classes
         try {
            if (Class.forName("Husky") != null) {
               return Arrays.asList(
                                    (Class<? extends T>) Class.forName("Bear"),
                                    (Class<? extends T>) Class.forName("Husky"),
                                    (Class<? extends T>) Class.forName("Lion"),
                                    (Class<? extends T>) Class.forName("Tiger"));
            }
         } catch (ClassNotFoundException e2) {
            try {
               return Arrays.asList((Class<? extends T>) Class.forName("Bear"),
                                    (Class<? extends T>) Class.forName("Lion"),
                                    (Class<? extends T>) Class.forName("Tiger"));
            } catch (ClassNotFoundException e3) {}
         }
      }
        
      // I give up!
      return new ArrayList<Class<? extends T>>();
   }

	// Downloads the contents of the .zip file at the given URL, and
	// returns them as a map from filenames to the bytes of each file.
   public static Map<String, byte[]> getZipFileContents(URL url)
      throws IOException {
      String fileName = downloadFile(url);
      ZipFile zip = new ZipFile(fileName);
      Map<String, byte[]> zipFilesMap = new TreeMap<String, byte[]>();
   
   	// read each file entry from the zip archive
      for (Enumeration<? extends ZipEntry> enu = zip.entries(); enu
              .hasMoreElements();) {
         ZipEntry ze = enu.nextElement();
         if (ze.isDirectory()) {
            continue;
         }
      
         int size = (int) ze.getSize();
         if (size < 0) {
            continue; // -1 means unknown size.
         }
      
         InputStream input = zip.getInputStream(ze);
         byte[] b = new byte[size];
         int offset = 0;
         while (size - offset > 0) {
            int bytesRead = input.read(b, offset, size - offset);
            if (bytesRead < 0) {
               break;
            }
            offset += bytesRead;
         }
      
         zipFilesMap.put(ze.getName(), b);
      }
      return zipFilesMap;
   }

	// Returns whether the given class implements the given interface.
   public static boolean classImplements(Class<?> clazz, Class<?> interfaceType) {
      for (Class<?> c : clazz.getInterfaces()) {
         if (c == interfaceType) {
            return true;
         }
      }
      return false;
   }

	// Dynamically loads the compiled .class file with the given file name
	// into our JVM and returns its Class object.
	// Throws various reflectiony exceptions if the file is bad.
   public static Class<?> loadClass(String fileName)
      throws ClassNotFoundException {
      String folderName = getFolder(fileName);
      File folder = new File(folderName);
      ClassLoader loader = ClassLoader.getSystemClassLoader();
   
      ClassLoader urlLoader = loader;
      try {
         URL fileUrl = new URL("file:" + System.getProperty("user.dir")
                               + File.separator + fileName);
      
         File currentDir = new File(System.getProperty("user.dir"));
         urlLoader = URLClassLoader.newInstance(
                                                new URL[] { folder.toURI().toURL(),
                                                            currentDir.toURI().toURL(), fileUrl }, loader);
      } catch (MalformedURLException mfurle) {
         mfurle.printStackTrace(); // this will never happen
      }
   
      String className = removeExtension(removeFolder(fileName));
      try {
         Class<?> clazz = urlLoader.loadClass(className);
         return clazz;
      } catch (IncompatibleClassChangeError icce) {
         throw new RuntimeException("Unable to load the class: " + icce);
      }
   }

	// Reads the given file's text fully and returns it as a String.
   public static String readEntireFile(String fileName) throws IOException {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));
      StringBuilder text = new StringBuilder();
      while (reader.ready()) {
         text.append((char) reader.read());
      }
      return text.toString();
   }

   public static byte[] readEntireFileBytes(String fileName)
      throws IOException {
      File file = new File(fileName);
      ByteArrayOutputStream out = new ByteArrayOutputStream((int) file
                                                            .length());
   
      InputStream stream = new FileInputStream(fileName);
      BufferedReader reader = new BufferedReader(
                                                 new InputStreamReader(stream));
      while (reader.ready()) {
         out.write(reader.read());
      }
      return out.toByteArray();
   }

   public static String readAndRename(String oldClassName, String newClassName)
      throws IOException {
      String fileName = oldClassName + JAVA_EXTENSION;
      String fileText = readEntireFile(fileName);
   
   	// replace the class name in the code
      fileText = fileText.replaceAll(oldClassName, newClassName);
      return fileText;
   }

	// Treats fileText as the text of a Java source file, and
	// replaces occurrences of its class name with the given new class name,
	// then writes it to a new .java file with that name.
	// Returns the new file name.
   public static String renameAndWriteJavaFile(String fileText,
                                               String oldClassName, String newClassName, boolean useTempFolder) {
   	// replace the class name in the code
      fileText = fileText.replaceAll(oldClassName, newClassName);
   
   	// write the modified text to a new file
      String newFileName = newClassName + JAVA_EXTENSION;
      if (useTempFolder) {
         newFileName = System.getProperty("java.io.tmpdir") + newFileName;
      }
      writeEntireFile(fileText, newFileName);
      return newFileName;
   }

   public static void writeEntireFile(String text, String fileName) {
      try {
         PrintStream output = new PrintStream(fileName);
         output.print(text);
         output.close();
      } catch (FileNotFoundException fnfe) {
         fnfe.printStackTrace(); // this will never happen; we're writing the
         // file!
      }
   }

	// Removes any characters from given text that wouldn't be acceptable
	// in a Java class name.
	// Not perfect (e.g., doesn't prevent names that start with a number).
   public static String sanitizeClassName(String text) {
      text = text.replaceAll("[^A-Za-z0-9_$]+", "_");
      return text;
   }

   public static void writeBytes(byte[] bytes, String fileName)
      throws IOException {
      FileOutputStream output = new FileOutputStream(fileName);
      output.write(bytes);
      output.close();
   }

	// pre: no folders in fileName (no "foo/bar/Baz.java")
   private static String getFolder(String fileName) {
      int slash = fileName.lastIndexOf(File.separatorChar);
      if (slash < 0) {
         slash = fileName.lastIndexOf("/"); // fallback
      }
   
      if (slash >= 0) {
         return fileName.substring(0, slash + 1);
      } else {
         return "./";
      }
   }

	// pre: no folders in fileName (no "foo/bar/Baz.java")
   private static String removeExtension(String fileName) {
      int dot = fileName.lastIndexOf(".");
      if (dot >= 0) {
         fileName = fileName.substring(0, dot);
      }
      return fileName;
   }

	// pre: no folders in fileName (no "foo/bar/Baz.java")
   private static String removeFolder(String fileName) {
      int slash = fileName.lastIndexOf(File.separatorChar);
      if (slash < 0) {
         slash = fileName.lastIndexOf("/"); // fallback
      }
   
      if (slash >= 0) {
         fileName = fileName.substring(slash + 1);
      }
      return fileName;
   }
	
	// For sorting class reflection objects by name.
   public static class ClassComparator implements Comparator<Class<?>> {
      public int compare(Class<?> c1, Class<?> c2) {
         return c1.getName().compareTo(c2.getName());
      }
   }

	// inner class to filter files by extension
   public static class ExtensionFilter implements FileFilter {
      private String extension;
   
      public ExtensionFilter(String extension) {
         this.extension = extension;
      }
   
      public boolean accept(File f) {
         return f != null && f.exists() && f.canRead()
            && f.getName().endsWith(extension);
      }
   }
}



// Lots of helper methods for creating GUI components.
class GuiFactory {
	// Moves the given window to the center of the screen.
   public static void center(Component comp) {
      Dimension size = comp.getSize();
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      comp.setLocation(Math.max(0, (screen.width - size.width) / 2), Math
                       .max(0, (screen.height - 24 - size.height) / 2));
   }

	// Helper method to create a JButton with the given properties.
   public static JButton createButton(String text, char mnemonic,
                                      ActionListener listen, Container panel) {
      JButton button = new JButton(text);
      if (mnemonic != '\0') {
         button.setMnemonic(mnemonic);
      }
      button.addActionListener(listen);
      if (panel != null) {
         panel.add(button);
      }
      return button;
   }

	// Helper method to create a JCheckBox with the given properties.
   public static JCheckBox createCheckBox(String text, char mnemonic, ActionListener listen, Container panel) {
      JCheckBox box = new JCheckBox(text);
      if (mnemonic != '\0') {
         box.setMnemonic(mnemonic);
      }
      box.addActionListener(listen);
      if (panel != null) {
         panel.add(box);
      }
      return box;
   }

	// Helper method to create a JRadioButton with the given properties.
   public static JRadioButton createRadioButton(String text, char mnemonic,
                                                boolean selected, ButtonGroup group, ActionListener listen,
                                                Container panel) {
      JRadioButton button = new JRadioButton(text, selected);
      if (mnemonic != '\0') {
         button.setMnemonic(mnemonic);
      }
      button.addActionListener(listen);
      if (panel != null) {
         panel.add(button);
      }
      if (group != null) {
         group.add(button);
      }
      return button;
   }

	// Helper method to create a JSlider with the given properties.
   public static JSlider createSlider(int min, int max, int initial,
                                      int majorTick, int minorTick, ChangeListener listen, Container panel) {
      JSlider slider = new JSlider(min, max, initial);
      slider.setMajorTickSpacing(majorTick);
      slider.setMinorTickSpacing(minorTick);
      slider.setSnapToTicks(true);
      slider.setPaintTicks(true);
   	// slider.setPaintLabels(true);
      Dimension size = slider.getPreferredSize();
      slider.setPreferredSize(new Dimension(size.width / 2, size.height));
      slider.addChangeListener(listen);
      if (panel != null) {
         panel.add(slider);
      }
      return slider;
   }
}



// A more general input window that can pop up and ask for several values.
// Used to initialize the GUI and model settings.
class InputPane {
   public static final String SAVE_STATE_FILE_NAME = "_critters_saved_settings.txt";

   private static Map<String, Object> values = new TreeMap<String, Object>();

   private static Properties savedSettings = new Properties();

	// load settings
   static {
      try {
         savedSettings.load(new FileInputStream(SAVE_STATE_FILE_NAME));
      } catch (Exception ioe) {
      }
   }

   public static boolean getBoolean(String name) {
      Boolean b = (Boolean) values.get(name);
      return b.booleanValue();
   }

   public static int getInt(String name) {
      Integer i = (Integer) values.get(name);
      return i.intValue();
   }

   public static String getString(String name) {
      String s = (String) values.get(name);
      return s;
   }

   public static Map<String, Object> getValues() {
      return values;
   }

   public static void showInputDialog(Frame parent, String title,
                                      String message, String[] names, Class<?>[] types) {
      showInputDialog(parent, title, message, names, types, null);
   }

	// Shows a dialog box with the given settings in it.
	// Returns true if OK was clicked and false if the dialog was canceled.
   public static boolean showInputDialog(Frame parent, String title,
                                         String message, final String[] names, final Class<?>[] types,
                                         final Object[] initialValues) {
   
   	// hack to get a reference to a boolean value
      final boolean[] result = { true };
   
      final JDialog dialog = new JDialog(parent, title, true);
      final JPanel west = new JPanel(new GridLayout(0, 1));
   	// final Container center = new JScrollPane(new JPanel(new GridLayout(0, 1)));
      final Container center = new JPanel(new GridLayout(0, 1));
      final JComponent[] comps = new JComponent[names.length];
   
      for (int i = 0; i < names.length; i++) {
         west.add(new JLabel(names[i]));
         if (types[i] == Boolean.TYPE) {
            JCheckBox box = new JCheckBox();
            if (savedSettings.containsKey(names[i])) {
               box.setSelected(Boolean.parseBoolean(savedSettings
                                                    .getProperty(names[i])));
            } else {
               box.setSelected(initialValues != null
                               && initialValues[i].toString().equals("true"));
            }
            comps[i] = box;
            center.add(box);
         } else if (types[i] != null) {
            int width = 10;
            if (types[i] == Integer.TYPE || types[i] == Double.TYPE) {
               width = 4;
            }
            JTextField field = new JTextField(width);
            if (savedSettings.containsKey(names[i])) {
               field.setText(savedSettings.getProperty(names[i]));
            } else if (initialValues != null) {
               field.setText(initialValues[i].toString());
            }
            comps[i] = field;
            center.add(field);
         } else {
         	// null type means blank slot
            center.add(new JPanel());
         }
      }
   
      JPanel south = new JPanel();
      JButton ok = new JButton("OK");
      ok.setMnemonic('O');
      ok.requestFocus();
      ok.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               dialog.setVisible(false);
            }
         });
      south.add(ok);
   
      KeyListener key = 
         new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
               if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                  result[0] = false;
                  dialog.setVisible(false);
               }
            }
         };
   
      if (initialValues != null) {
         JButton reset = new JButton("Reset");
         reset.setMnemonic('R');
         reset.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  for (int i = 0; i < names.length; i++) {
                     if (types[i] == Boolean.TYPE) {
                        JCheckBox box = (JCheckBox) comps[i];
                        box.setSelected(initialValues != null
                                        && initialValues[i].toString().equals(
                                                                              "true"));
                     } else if (types[i] != null) {
                        JTextField field = (JTextField) comps[i];
                        field.setText(initialValues[i].toString());
                     }
                  }
               }
            });
         south.add(reset);
         reset.addKeyListener(key);
      }
   
      JButton checkAll = new JButton("All");
      checkAll.setMnemonic('A');
      checkAll.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               for (int i = 0; i < center.getComponentCount(); i++) {
                  Component comp = center.getComponent(i);
                  if (comp instanceof JCheckBox) {
                     ((JCheckBox) comp).setSelected(true);
                  }
               }
            }
         });
      south.add(checkAll);
      checkAll.addKeyListener(key);
   
      JButton uncheckAll = new JButton("None");
      uncheckAll.setMnemonic('N');
      uncheckAll.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               for (int i = 0; i < center.getComponentCount(); i++) {
                  Component comp = center.getComponent(i);
                  if (comp instanceof JCheckBox) {
                     ((JCheckBox) comp).setSelected(false);
                  }
               }
            }
         });
      south.add(uncheckAll);
      uncheckAll.addKeyListener(key);
   
      dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
      dialog.addWindowListener(
         new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
               result[0] = false;
               dialog.setVisible(false);
            }
         });
      dialog.addKeyListener(key);
      ok.addKeyListener(key);
      dialog.getContentPane().setLayout(new BorderLayout(10, 5));
      ((JComponent) dialog.getContentPane()).setBorder(BorderFactory
                                                       .createEmptyBorder(10, 10, 10, 10));
   
      if (message != null) {
         JLabel messageLabel = new JLabel(message);
         dialog.add(messageLabel, BorderLayout.NORTH);
      }
   	
      Container westCenter = new JPanel(new BorderLayout());
      westCenter.add(west, BorderLayout.WEST);
      westCenter.add(center);
   	
   	// dialog.add(west, BorderLayout.WEST);
   	// dialog.add(new JScrollPane(center));
      dialog.add(new JScrollPane(westCenter));
      dialog.add(south, BorderLayout.SOUTH);
      dialog.pack();
      GuiFactory.center(dialog);
   
   	// actually show the dialog box on the screen
      ok.requestFocus();
      dialog.setVisible(true);
      ok.requestFocus();
   
   	// by this point, the dialog has been closed by the user
      values.clear();
   
   	// store all the user's settings in the map for later
      for (int i = 0; i < names.length; i++) {
         if (types[i] == Boolean.TYPE) {
            JCheckBox box = (JCheckBox) comps[i];
            values.put(names[i], new Boolean(box.isSelected()));
            if (savedSettings != null) {
               savedSettings.setProperty(names[i], String.valueOf(box
                                                                  .isSelected()));
            }
         } else if (types[i] == Integer.TYPE) {
            JTextField field = (JTextField) comps[i];
            String text = field.getText();
            int value = 0;
            if (initialValues != null) {
               Integer integer = (Integer) initialValues[i];
               value = integer.intValue();
            }
         
            try {
               value = Integer.parseInt(text);
            } catch (Exception e) {
            }
         
            values.put(names[i], new Integer(value));
            if (savedSettings != null) {
               savedSettings.setProperty(names[i], text);
            }
         } else if (types[i] != null) {
            JTextField field = (JTextField) comps[i];
            values.put(names[i], field.getText());
            if (savedSettings != null) {
               savedSettings.setProperty(names[i], field.getText());
            }
         }
      }
   
      try {
         savedSettings.store(new FileOutputStream(SAVE_STATE_FILE_NAME),
                             "CSE 142 Critters saved settings");
      } catch (Exception ioe) {
      }
   
      return result[0];
   }
}


// CSE 142 Homework 8 (Critters)
// Authors: Marty Stepp, Stuart Reges
//
// A small class to pop up a list of items and let the user select them.
//

class ListOptionPane extends JDialog implements ActionListener {
   private static final long serialVersionUID = 0;

   private JList list;

   private JButton ok, cancel;

   private boolean pressedOk = false;

   public ListOptionPane(JFrame frame, Collection<String> items) {
      super(frame, true);
      setTitle("Load...");
   
      list = new JList(items.toArray());
      list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      list.setVisibleRowCount(Math.min(12, items.size()));
   
      JPanel south = new JPanel();
      ok = GuiFactory.createButton("OK", 'O', this, south);
      cancel = GuiFactory.createButton("Cancel", 'C', this, south);
   
      add(new JScrollPane(list));
      add(south, BorderLayout.SOUTH);
      pack();
      setLocation(frame.getX() + (frame.getWidth() - getWidth()) / 2, frame
                  .getY()
                  + (frame.getHeight() - getHeight()) / 2);
   }

   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == ok) {
         pressedOk = true;
      } else if (src == cancel) {
         setVisible(false);
      	// dispose();
      }
      setVisible(false);
   }

   public Object[] getSelectedValues() {
      return list.getSelectedValues();
   }

   public boolean pressedOk() {
      return pressedOk;
   }
}



class Util {
   public static <T> int indexOfSafe(List<T> list, T value) {
      for (int i = 0; i < list.size(); i++) {
         if (list.get(i) == value) {
            return i;
         }
      }
      return -1;
   }

   public static String padNumber(int n, int length) {
      return padNumber(n, length, false);
   }

   public static String padNumber(int n, int length, boolean html) {
      String s = "" + n;
      int len = s.length();
      while (len < length) {
         if (html) {
            s = "&nbsp;" + s;
         } else {
            s = " " + s;
         }
         len++;
      }
      return s;
   }
	
   public static String toString(Critter critter) {
      return "(" + critter.getClass().getName() + ", \"" + critter + "\", id " + critter.hashCode() + ")";
   }
	
   public static String toString(Object o) {
      if (o == null) {
         return "null";
      }
      StringBuilder sb = new StringBuilder();
      Class<?> clazz = o.getClass();
      sb.append(clazz.getName() + "{");
      boolean first = true;
      for (Field field : clazz.getDeclaredFields()) {
         if (!first) {
            sb.append(",");
         }
         String fieldName = field.getName();
         sb.append(fieldName + "=");
         try {
            sb.append(field.get(o));
         } catch (IllegalAccessException e) {
            // private field; see if there's an accessor for this field
            String capitalized = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
               Method method = clazz.getMethod("get" + capitalized);
               sb.append(method.invoke(o));
            } catch (Exception ex) {}
            try {
               Method method = clazz.getMethod("is" + capitalized);
               sb.append(method.invoke(o));
            } catch (Exception ex) {}
         } catch (Exception e) {
            sb.append(e.getClass().getName());
         }
         first = false;
      }
      sb.append("}");
      return sb.toString();
   }
	
   public static String toString(Point p) {
      return "(" + p.x + ", " + p.y + ")";
   }

   public static String truncate(String className, int length) {
      if (className.length() <= length) {
         return className;
      } else {
         return className.substring(0, length) + "~";
      }
   }

	// returns true if the simulator is running from inside DrJava.
   public static boolean usingDrJava() {
      try {
         return System.getProperty("drjava.debug.port") != null
            || System.getProperty("java.class.path").toLowerCase()
            .indexOf("drjava") >= 0;
      } catch (SecurityException e) {
      	// running as an applet, or something
         return false;
      }
   }
}



class ZipDownloader implements Runnable {
   private String zipFile;

   private CritterModel model;

   private JFrame frame;

   private JButton button;

   public ZipDownloader(String zipFile, CritterModel model, JFrame frame,
                        JButton button) {
      this.zipFile = zipFile;
      this.model = model;
      this.frame = frame;
      this.button = button;
   }

   @SuppressWarnings("unchecked")
      public void run() {
      button.setEnabled(false);
      try {
         String filename = JOptionPane.showInputDialog("File to read?",
                                                       zipFile);
         if (filename == null || (filename = filename.trim()).length() == 0) {
            return;
         }
         Map<String, byte[]> zipFilesMap = ClassUtils
            .getZipFileContents(new URL(filename));
      
      	// remove ".class" from file names
         Set<String> classNames = new TreeSet<String>();
         for (String fileName : zipFilesMap.keySet()) {
            classNames.add(fileName.replaceAll("\\.class", ""));
         }
      
      	// filter out inner classes from list
         Set<String> innerClasses = new TreeSet<String>();
         for (Iterator<String> i = classNames.iterator(); i.hasNext();) {
            String className = i.next();
            if (ClassUtils.isInnerClass(className)) {
               i.remove();
            	// String outerClassName = className.substring(0, className.indexOf('$'));
               innerClasses.add(className);
            }
         }
      
      	// show dialog box to user so they can select wolf file(s)
         ListOptionPane dialog = new ListOptionPane(frame, classNames);
         dialog.setVisible(true);
         if (!dialog.pressedOk()) {
            return;
         }
         Object[] selectedItems = dialog.getSelectedValues();
      
      	// include any necessary inner classes in list of files to download
         Set<String> selectedClasses = new TreeSet<String>();
         for (Object item : selectedItems) {
            String className = item.toString();
            selectedClasses.add(className);
         
         	// include any inner classes associated with this class
            for (String innerClassName : innerClasses) {
               if (innerClassName.startsWith(className)) {
                  selectedClasses.add(innerClassName);
               }
            }
         }
      
      	// write the selected classes as files to disk
         for (String className : selectedClasses) {
            String fileName = className + ClassUtils.CLASS_EXTENSION;
            byte[] bytes = zipFilesMap.get(fileName);
            ClassUtils.writeBytes(bytes, fileName);
         }
      
      	// load the classes into the JVM
         for (String className : selectedClasses) {
            if (ClassUtils.isInnerClass(className)) {
               continue;
            }
            String fileName = className + ClassUtils.CLASS_EXTENSION;
            Class<? extends Critter> critterClass = (Class<? extends Critter>) ClassUtils
               .loadClass(fileName);
            model.add(CritterModel.DEFAULT_CRITTER_COUNT, critterClass);
         }
      } catch (IOException e) {
         JOptionPane.showMessageDialog(frame,
                                       "Error downloading ZIP data:\n" + e, "I/O error",
                                       JOptionPane.ERROR_MESSAGE);
         if (CritterGui.PRINT_EXCEPTIONS) {
            e.printStackTrace();
         }
      } catch (Throwable t) {
         JOptionPane.showMessageDialog(frame, "Error loading class data:\n"
                                       + t, "Class loading error", JOptionPane.ERROR_MESSAGE);
         if (CritterGui.PRINT_EXCEPTIONS) {
            t.printStackTrace();
         }
      } finally {
         button.setEnabled(true);
      }
   }
}


