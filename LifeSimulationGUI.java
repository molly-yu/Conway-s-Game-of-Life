  import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import javax.swing.event.*;  // Needed for ActionListener
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.*;

class LifeSimulationGUI extends JFrame implements ActionListener, ChangeListener, MouseListener
{
    static Colony colony = new Colony (0.1);
    static JSlider speedSldr = new JSlider ();
    static Timer t;
    int col, row, col2, row2;
    boolean lives;
    static JTextField savename;

    //======================================================== constructor
    public LifeSimulationGUI ()
    {
        // 1... Create/initialize components
        JButton simulateBtn = new JButton ("Simulate");
        simulateBtn.addActionListener (this);
        JButton populateBtn = new JButton ("Populate");
        populateBtn.addActionListener (this);
        JButton eradicateBtn = new JButton ("Eradicate");
        eradicateBtn.addActionListener (this);
        JButton loadBtn = new JButton("Load");
        loadBtn.addActionListener(this);
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(this);
        savename = new JTextField();

        savename.setEditable(true);
        savename.setPreferredSize(new Dimension(75, 30));

        speedSldr.addChangeListener (this);
        
        // 2... Create content pane, set layout
        JPanel content = new JPanel ();        // Create a content pane
        content.setLayout (new BorderLayout ()); // Use BorderLayout for panel
        JPanel north = new JPanel ();
        north.setLayout (new FlowLayout ()); // Use FlowLayout for input area

        DrawArea board = new DrawArea (500, 500);
        
        // 3... Add the components to the input area.
        north.add(populateBtn);
        north.add(eradicateBtn);
        north.add (simulateBtn);
        north.add (speedSldr);
        north.add(loadBtn);
        north.add(saveBtn);
        north.add(savename);

        content.add (north, "North"); // Input area
        content.add (board,BorderLayout.CENTER ); // Output area
        //content.addMouseListener(this);
        board.addMouseListener(this);

        // 4... Set this window's attributes.
        setContentPane (content);
        pack ();
        setTitle ("Life Simulation Demo");
        setSize (800, 670);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null);           // Center window.
    }

    public void stateChanged (ChangeEvent e)
    {
        if (t != null)
            t.setDelay (400 - 4 * speedSldr.getValue ()); // 0 to 400 ms
    }

    public void actionPerformed (ActionEvent e)
    {
        if (e.getActionCommand ().equals ("Simulate"))
        {
            Movement moveColony = new Movement (colony); // ActionListener
            t = new Timer (200, moveColony); // set up timer
            t.start (); // start simulation
        }
        if (e.getActionCommand ().equals ("Populate"))
        {
            lives = true;

        }
        if (e.getActionCommand ().equals ("Eradicate"))
        {
            lives = false;

        }
        if (e.getActionCommand().equals("Load")) {
             JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(null);
                File file = chooser.getSelectedFile();
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("You chose to open this file: " +
                            chooser.getSelectedFile().getName());
                }
            try {
                colony.load(file);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        
        if (e.getActionCommand().equals("Save")) {
             try {
                colony.save(savename.getText());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        repaint ();            // refresh display of deck
    }
    
     public void mousePressed(MouseEvent e) {
       col = e.getY()/5;
       row = e.getX()/5;
    }

    public void mouseReleased(MouseEvent e) {
       col2 = e.getY()/5;
       row2 = e.getX()/5;
       if(lives == true)       
       {
           colony.populate(col, row, col2, row2);
        }
       else if(lives == false)
       {
           colony.eradicate(col, row, col2, row2);
        }       
    }
     public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        int coll = e.getY()/5;
        int roww = e.getX()/5;
        if(lives == true)       
       {
           colony.populate(coll, roww, coll, roww);
        }
       else if(lives == false)
       {
           colony.eradicate(coll, roww, coll, roww);
        }       
    }
    

    class DrawArea extends JPanel
    {
        public DrawArea (int width, int height)
        {
            this.setMaximumSize (new Dimension (width, height)); // size
            this.setMinimumSize (new Dimension (width, height)); // size
            this.setPreferredSize (new Dimension (width, height)); // size
        }

        public void paintComponent (Graphics g)
        {
            colony.show (g);
        }
    }

    class Movement implements ActionListener
    {
        private Colony colony;

        public Movement (Colony col)
        {
            colony = col;
        }

        public void actionPerformed (ActionEvent event)
        {
            colony.advance ();
            repaint ();
        }
    }

    //======================================================== method main
    public static void main (String[] args)
    {
        LifeSimulationGUI window = new LifeSimulationGUI ();
        window.setVisible (true);
    }
}

class Colony
{
    private boolean grid[] [];

    public Colony (double density)
    {
        grid = new boolean [100] [100];
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
                grid [row] [col] = Math.random () < density;
    }

    public void show (Graphics g)
    {
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
            {
                if (grid [row] [col]) // life
                    g.setColor (Color.black);
                else
                    g.setColor (Color.white);
                g.fillRect (col * 5 + 2, row * 5 + 2, 5, 5); // draw life form
        }
    }

    public boolean live (int row, int col)
    {
        int count=0;
      
       try{
        if(grid[row-1][col-1]==true)
                count++;
            }catch(Exception e){}
            try{
        if(grid[row-1][col]==true)
                count++;
            }catch(Exception e){}
            try{
        if(grid[row-1][col+1]==true)
                count++;
            }catch(Exception e){}
            try{
        if(grid[row][col-1]==true)
                count++;
            }catch(Exception e){}
                try{
        if(grid[row][col+1]==true)
                count++;
            } catch(Exception e){}
                try{
        if(grid[row+1][col-1]==true)
                count++;
            } catch(Exception e){}
                try{
        if(grid[row+1][col]==true)
                count++;
            } catch(Exception e){}
                try{
        if(grid[row+1][col+1]==true)
                count++;
            }  catch(Exception e){}
        if (grid[row][col]==true)
        {
            if(count==2||count==3)
                return true;
            else
                return false;
            
        }
        else
        {
            if(count==3)
                return true;
            else
                return false;
        }
    }

    public void advance ()
    {
        boolean nextGen[] [] = new boolean [grid.length] [grid [0].length]; // create next generation of life forms
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
                nextGen [row] [col] = live (row, col); // determine life/death status
        grid = nextGen; // update life forms
    }
    
    public void populate(int row1, int col1, int row2, int col2)
    {
        if(col1>0&&col1<100&&col2>0&&col2<100&&row1>0&&row1<100&&row2>0&&row2<100){
        if(col1>col2)
        {
            int temp = col1;
            col1 = col2;
            col2 = temp;
        }
        if(row1>row2)
        {
            int temp = row1;
            row1 = row2;
            row2 = temp;
        }
        for(int i = row1; i <= row2; i++)
        {
            for(int j = col1; j<=col2; j++)
            {
                double rand = Math.random();
                if(rand<0.85)
                    grid[i][j] = true;
            }
        }         
    }
}
    
    public void eradicate(int row1, int col1, int row2, int col2)
    {
        if(col1>0&&col1<100&&col2>0&&col2<100&&row1>0&&row1<100&&row2>0&&row2<100){
        if(col1>col2)
        {
            int temp = col1;
            col1 = col2;
            col2 = temp;
        }
        if(row1>row2)
        {
            int temp = row1;
            row1 = row2;
            row2 = temp;
        }
        for(int i = row1; i <= row2; i++)
        {
            for(int j = col1; j<=col2; j++)
            {
                double rand = Math.random();
                if(rand<0.85)
                    grid[i][j] = false;
            }
        }
    }
}
    
    public void load(File file) throws IOException {
        FileReader reader = new FileReader(file);
        char str[] = new char [10000];
        reader.read(str);
        boolean data[][] = new boolean[100][100];
        for (int row = 0; row < 100; row++) {
            for (int col = 0; col < 100; col++) {
                if (str[row * 100 + col] == '1')
                    data[row][col] = true;
                else
                    data[row][col] = false;
            }
        }
        grid = data;
    }
    
    public void save (String file) throws IOException
    {
        char[][] data= new char[100][100];
        for (int row=0;row<grid.length;row++) {
            for (int col=0;col<grid[row].length;col++)
                if (grid[row][col])
                    data[row][col] = '1';
                else
                    data[row][col] = '0';
        }
       /* for (int row=0;row<data.length;row++) {
            for (int col=0;col<data[row].length;col++)
                System.out.print(data[row][col]);
            System.out.println();
        }
     */  String fileName = file+".txt" ;
        FileWriter writer = new FileWriter( fileName );
        for (int row=0;row<data.length;row++) {
            for (int col=0;col<data[row].length;col++)
                writer.write(data[row][col]);
        }
        writer.close();
                
    }
}


