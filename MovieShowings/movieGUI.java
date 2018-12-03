import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class movieGUI {
	//
	private static boolean isAdmin = false;
	//General JFrame Window Components
	private JLabel label;
	private JTabbedPane tabbedPane;
	private JFrame movieJF;
	private JPanel controlPane, logInPane, filterPane, movieFilterPane, cinemaFilterPane;
	private JPanel adminPane, adminAddCinemaPane, adminAddMoviePane, adminAddMovieTitle, adminAddMovieShowing, adminRemovePane;
	//J Components with no Functions
	private JLabel loginLabel, passwordL;
	private JPasswordField passwordF;
	private JTextField movieNameTF, cinemaNameTF, cinemaXTF, cinemaYTF; //Admin JTextFields
	private static JComboBox<String> movieJCBox;
	private JTextField xFilterTF, yFilterTF, radiusTF; //User JTextFields
	//J Components with Functions
	private JButton logInBtn, logOutBtn, cinemaFilterBtn, addMovieBtn, addShowingBtn, addCinemaBtn, removeBtn, refreshBtn;
	private JCheckBox gCB, pgCB, pg13CB, rCB, nc17CB, nrCB; //For Filters
	private JCheckBox agCB, apgCB, apg13CB, arCB, anc17CB, anrCB;  //For Adding
	protected static JComboBox<String> ratingJCBox, cinemaJCBox, timeJCBox;
	//J Table Related Variables
	protected static DefaultTableModel movieTableModel, cinemaTableModel;
	private static JTable movieJTable, cinemaJTable;

	//Global Variables acting as Temporary Storage for Methods
	private static String movieNameS, movieRatingS, movieTimeS; //Adding Movie Strings
	private static String cinemaS, cinemaXS, cinemaYS, radiusS; //Adding and Filtering Cinema Strings
	private static int xInt, yInt, rInt;            //Adding and Filtering Cinema ints
	protected static int xDB, yDB;                //Comparing XY Coords from DB with Cinema XY Coords

	protected static TableRowSorter<DefaultTableModel> movieTRS;
	protected static TableRowSorter<DefaultTableModel> cinemaTRS;

	//Emtpy Constructor
	public movieGUI(){
		initialize();
		//Room for Changes
	}

	private void initialize() {
		movieJF = new JFrame();
		movieJF.setTitle("Movies & Cinemas");
		movieJF.setLayout(new BorderLayout());
		movieJF.setSize(2000, 1000); //888, 300
		//movieJF.setSize(888, 300); //888, 300
		movieJF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Insets insets = new Insets(10, 10, 10, 10);

	//Panes

		controlPane = new JPanel(new BorderLayout());
		logInPane = new JPanel(); 
		logInPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

		filterPane = new JPanel(new BorderLayout());
		movieFilterPane = new JPanel();             
		movieFilterPane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
		cinemaFilterPane = new JPanel();            

		adminPane = new JPanel(new BorderLayout());   
		adminAddMoviePane = new JPanel(new BorderLayout());
		adminAddMovieTitle = new JPanel();
		adminAddMovieTitle.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
		adminAddMovieShowing = new JPanel();
		adminAddMovieShowing.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		adminAddCinemaPane = new JPanel();   
		
		adminAddCinemaPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		adminRemovePane = new JPanel();           

		//Padding for main panes
		Border padding = BorderFactory.createEmptyBorder(50, 50, 50, 50);   //New as of 11/6 Wanted to space things out a bit
		controlPane.setBorder(padding);
		filterPane.setBorder(padding);
		adminPane.setBorder(padding);

		//Handler
		btnHandlerClass bh = new btnHandlerClass();

		//loginPane TextFields and Password
		loginLabel = new JLabel("Log In:");
		loginLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		passwordL = new JLabel();
		passwordF = new JPasswordField(8);
		passwordF.setFont(new Font("Tahoma", Font.PLAIN, 25));
		logInBtn = new JButton("Log In");
		logInBtn.setFont(new Font("Tahoma", Font.PLAIN, 25));
		logOutBtn = new JButton("Log Out");
		logOutBtn.setFont(new Font("Tahoma", Font.PLAIN, 25));

		logInPane.add(loginLabel);
		logInPane.add(passwordL);
		logInPane.add(passwordF);
		logInPane.add(logInBtn);
		logInPane.add(logOutBtn);

		logOutBtn.setVisible(false); //LOGOUT is not visible initially

		logInBtn.addActionListener(bh);
		logOutBtn.addActionListener(bh);
		passwordF.addActionListener(new ActionListener() {      

			@Override
			public void actionPerformed(ActionEvent e) {
				doLogIn();
			}
		});

		controlPane.add(logInPane, BorderLayout.NORTH);

		//Movie Filter Components
		gCB = new JCheckBox("G");
		gCB.setFont(new Font("Tahoma", Font.PLAIN, 25));
		pgCB = new JCheckBox("PG");
		pgCB.setFont(new Font("Tahoma", Font.PLAIN, 25));
		pg13CB = new JCheckBox("PG-13");
		pg13CB.setFont(new Font("Tahoma", Font.PLAIN, 25));
		rCB = new JCheckBox("R");
		rCB.setFont(new Font("Tahoma", Font.PLAIN, 25));
		nc17CB = new JCheckBox("NC17");
		nc17CB.setFont(new Font("Tahoma", Font.PLAIN, 25));
		nrCB = new JCheckBox("NR");
		nrCB.setFont(new Font("Tahoma", Font.PLAIN, 25));
		label = new JLabel("Movie Filter: ");
		label.setFont(new Font("Tahoma", Font.PLAIN, 25));
		movieFilterPane.add(label);
		movieFilterPane.add(gCB);
		movieFilterPane.add(pgCB);
		movieFilterPane.add(pg13CB);
		movieFilterPane.add(rCB);
		movieFilterPane.add(nc17CB);
		movieFilterPane.add(nrCB);

		filterPane.add(movieFilterPane, BorderLayout.NORTH);

		selectionHandler handler = new selectionHandler();
		gCB.addItemListener(handler);
		pgCB.addItemListener(handler);
		pg13CB.addItemListener(handler);
		rCB.addItemListener(handler);
		nc17CB.addItemListener(handler);
		nrCB.addItemListener(handler);

		//Cinema Filter Components
		xFilterTF = new JTextField(3);
		xFilterTF.setFont(new Font("Tahoma", Font.PLAIN, 25));

		yFilterTF = new JTextField(3);
		yFilterTF.setFont(new Font("Tahoma", Font.PLAIN, 25));
		radiusTF = new JTextField(3);
		radiusTF.setFont(new Font("Tahoma", Font.PLAIN, 25));
		cinemaFilterBtn = new JButton("Filter");
		cinemaFilterBtn.setFont(new Font("Tahoma", Font.PLAIN, 25));


		label = new JLabel("Cinema Filter: ");
		label.setFont(new Font("Tahoma", Font.PLAIN, 25));
		cinemaFilterPane.add(label);

		label = new JLabel("x ");
		label.setFont(new Font("Tahoma", Font.PLAIN, 25));
		cinemaFilterPane.add(label);
		cinemaFilterPane.add(xFilterTF);

		label = new JLabel("y ");
		label.setFont(new Font("Tahoma", Font.PLAIN, 25));
		cinemaFilterPane.add(label);
		cinemaFilterPane.add(yFilterTF);

		label = new JLabel("Radius: ");
		label.setFont(new Font("Tahoma", Font.PLAIN, 25));
		cinemaFilterPane.add(label);
		cinemaFilterPane.add(radiusTF);

		cinemaFilterPane.add(cinemaFilterBtn);
		filterPane.add(cinemaFilterPane, BorderLayout.CENTER);
		cinemaFilterBtn.addActionListener(bh);

		controlPane.add(filterPane, BorderLayout.CENTER);

		//Refresh Button
		JPanel refreshPane = new JPanel(new BorderLayout());
		refreshBtn = new JButton("Refresh");
		refreshBtn.setFont(new Font("Tahoma", Font.PLAIN, 25));
		refreshBtn.addActionListener(bh);
		refreshBtn.setHorizontalAlignment(JButton.CENTER);
		refreshBtn.setVerticalAlignment(JButton.CENTER);
		refreshPane.add(refreshBtn, BorderLayout.CENTER);
		controlPane.add(refreshPane, BorderLayout.SOUTH);

//Administrator Add Movie
		//Administrator Add a Movie Title
		movieNameTF = new JTextField(10);
		movieNameTF.setFont(new Font("Tahoma", Font.PLAIN, 25));
		
		ratingJCBox = new JComboBox<String>(database.getRatingsA()); //DropList of Ratings
		ratingJCBox.setFont(new Font("Tahoma", Font.PLAIN, 25));
		
		addMovieBtn = new JButton("Add Movie");
		addMovieBtn.setFont(new Font("Tahoma", Font.PLAIN, 25));
		addMovieBtn.addActionListener(bh);

		label = new JLabel("Movie Name: ");
		label.setFont(new Font("Tahoma", Font.PLAIN, 25));
		adminAddMovieTitle.add(label);
		adminAddMovieTitle.add(movieNameTF);
		adminAddMovieTitle.add(ratingJCBox);
		adminAddMovieTitle.add(addMovieBtn);
		
		adminAddMoviePane.add(adminAddMovieTitle, BorderLayout.NORTH);
		
		//Administrator Add a Movie Showing
		movieJCBox = new JComboBox<String>(database.loadMovie()); //DropList of movies
		movieJCBox.setFont(new Font("Tahoma", Font.PLAIN, 25));
		
		cinemaJCBox = new JComboBox<String>(database.loadCinema()); //DropList of Cinemas
		cinemaJCBox.setFont(new Font("Tahoma", Font.PLAIN, 25));
		
		timeJCBox = new JComboBox<String>(database.loadTime());
		timeJCBox.setFont(new Font("Tahoma", Font.PLAIN, 25));
		
		addShowingBtn = new JButton("Add Showing");
		addShowingBtn.setFont(new Font("Tahoma", Font.PLAIN, 25));
		addShowingBtn.addActionListener(bh);
		
		label = new JLabel("Showing: ");
		label.setFont(new Font("Tahoma", Font.PLAIN, 25));
		adminAddMovieShowing.add(label);
		adminAddMovieShowing.add(movieJCBox);
		adminAddMovieShowing.add(cinemaJCBox);
		adminAddMovieShowing.add(timeJCBox);
		adminAddMovieShowing.add(addShowingBtn);

		
		adminAddMoviePane.add(adminAddMovieShowing, BorderLayout.SOUTH);

		
		adminPane.add(adminAddMoviePane, BorderLayout.NORTH); 
		
		//Administrator Add a Cinema Components
		cinemaNameTF = new JTextField(10);
		cinemaNameTF.setFont(new Font("Tahoma", Font.PLAIN, 25));
		cinemaNameTF.setToolTipText("Cinema Name");

		cinemaXTF = new JTextField(2);
		cinemaXTF.setFont(new Font("Tahoma", Font.PLAIN, 25));
		cinemaXTF.setText("0");

		cinemaYTF = new JTextField(2);
		cinemaYTF.setFont(new Font("Tahoma", Font.PLAIN, 25));
		cinemaYTF.setText("0");

		label = new JLabel("Cinema Name");
		label.setFont(new Font("Tahoma", Font.PLAIN, 25));
		adminAddCinemaPane.add(label);
		adminAddCinemaPane.add(cinemaNameTF);

		label = new JLabel("X Coord.");
		label.setFont(new Font("Tahoma", Font.PLAIN, 25));;
		adminAddCinemaPane.add(label);
		adminAddCinemaPane.add(cinemaXTF);

		label = new JLabel("Y Coord.");
		label.setFont(new Font("Tahoma", Font.PLAIN, 25));
		adminAddCinemaPane.add(label);
		adminAddCinemaPane.add(cinemaYTF);

		//Administrator Add Cinema Filter //11/8 New Things

		label = new JLabel("Ratings: ");
		label.setFont(new Font("Tahoma", Font.PLAIN, 25));
		agCB = new JCheckBox("G");
		agCB.setFont(new Font("Tahoma", Font.PLAIN, 25));
		apgCB = new JCheckBox("PG");
		apgCB.setFont(new Font("Tahoma", Font.PLAIN, 25));
		apg13CB = new JCheckBox("PG-13");
		apg13CB.setFont(new Font("Tahoma", Font.PLAIN, 25));
		arCB = new JCheckBox("R");
		arCB.setFont(new Font("Tahoma", Font.PLAIN, 25));
		anc17CB = new JCheckBox("NC17");
		anc17CB.setFont(new Font("Tahoma", Font.PLAIN, 25));
		anrCB = new JCheckBox("NR");
		anrCB.setFont(new Font("Tahoma", Font.PLAIN, 25));

		adminAddCinemaPane.add(label);
		adminAddCinemaPane.add(agCB);
		adminAddCinemaPane.add(apgCB);
		adminAddCinemaPane.add(apg13CB);
		adminAddCinemaPane.add(arCB);
		adminAddCinemaPane.add(anc17CB);
		adminAddCinemaPane.add(anrCB);

		//Administrator Add Cinema Button
		addCinemaBtn = new JButton("Add Cinema");
		addCinemaBtn.setFont(new Font("Tahoma", Font.PLAIN, 25));
		addCinemaBtn.addActionListener(bh);
		adminAddCinemaPane.add(addCinemaBtn);

		adminPane.add(adminAddCinemaPane, BorderLayout.CENTER);

		//Administrator Removal Components
		removeBtn = new JButton("Remove Selected Item");
		removeBtn.setFont(new Font("Tahoma", Font.PLAIN, 25));
		removeBtn.addActionListener(bh);
		adminRemovePane.add(removeBtn);
		adminPane.add(adminRemovePane, BorderLayout.SOUTH);

		adminPane.setVisible(false);


		//Movie JTable
		movieJTable = new JTable() {
			public boolean isCellEditable(int data, int columns) {
				return false;
			}
		};
		movieJTable.setFont(new Font("Tahoma", Font.PLAIN, 30));  //KARL
		movieJTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
		movieJTable.setAutoCreateRowSorter(true);
		movieJTable.setFillsViewportHeight(true);

		//Double Click in Movie JTable
		movieJTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				try {
					if(e.getClickCount() == 2){
						int i = movieJTable.getSelectedRow();
						getSelectedMovieData(i);  //Grabs data from selected row
						DisplayGUI movieInfo = new DisplayGUI(movieNameS, isAdmin, "m");
						refresh();  //11/13 Refresh Tables and Filters
						System.out.println("Double Click in movieJTable");
					}
				}
				catch(Exception noRowSelected1) {
					System.out.println("No item double clicked!");
				}

			}
		});

		//Cinema JTable
		cinemaJTable = new JTable() {
			public boolean isCellEditable(int data, int columns) {
				return false;
			}
		};
		cinemaJTable.setFont(new Font("Tahoma", Font.PLAIN, 30));  //KARL
		cinemaJTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
		cinemaJTable.setAutoCreateRowSorter(true);
		cinemaJTable.setFillsViewportHeight(true);

		//Double Click in Cinema JTable
		cinemaJTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if(e.getClickCount() == 2){
						int i = cinemaJTable.getSelectedRow();
						getSelectedCinemaData(i); //Grabs data from selected row
						DisplayGUI movieInfo = new DisplayGUI(cinemaS, isAdmin, "c");
						//cinemaS, cinemaXS, cinemaYS, isAdmin
						refresh();  //11/13 Refresh Tables and Filters
						System.out.println("Double Click on cinemaJTable");
					}
				}catch (Exception noRowSelected2) {
					System.out.println("No item double clicked!");
				}
			}
		});

		//JScrollPane to be placed within the TabbedPane
		JScrollPane movieJSP = new JScrollPane(movieJTable);
		movieJSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		JScrollPane cinemaJSP = new JScrollPane(cinemaJTable);
		cinemaJSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		//JTabbedPane to contain the JScrollPanes
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 30));
		tabbedPane.addTab("Movies", null, movieJSP, null);
		tabbedPane.addTab("Cinema", null, cinemaJSP, null);

		tabbedPane.setBorder(padding);                  //New as of 11/6 Wanted to space things out a bit


		//DefaultTableModel to format structure of JTables
		Object[] columnsM = {"Movie Title","Rating"};
		movieTableModel = new DefaultTableModel();
		movieTableModel.setColumnIdentifiers(columnsM);

		Object[] columnsC = {"Cinema","x","y"};
		cinemaTableModel = new DefaultTableModel();
		cinemaTableModel.setColumnIdentifiers(columnsC);

		//Back to JTable, setModels to the format above
		movieJTable.setModel(movieTableModel);
		movieJTable.setRowHeight(30);
		movieJTable.getTableHeader().setFont(new Font("SansSerif", Font.ITALIC, 25));

		cinemaJTable.setModel(cinemaTableModel);
		cinemaJTable.setRowHeight(30);
		cinemaJTable.getTableHeader().setFont(new Font("SansSerif", Font.ITALIC, 25));

		//LoadMovies from DB
		loadData();

		//Place Panes Accordingly
		movieJF.add(tabbedPane, BorderLayout.CENTER);
		movieJF.add(controlPane, BorderLayout.EAST);
		movieJF.add(adminPane, BorderLayout.SOUTH);

		movieJF.setVisible(true);
	}//End of initialize

	/*
	 * Methods and Functions
	 */

	//--METHODS FOR ADMIN
	private void doLogIn() {
		char[] passC = passwordF.getPassword();
		String passS = new String(passC);
		System.out.println("Password: "+passS);
		if (passS.equals("admin")) {
			isAdmin = true;
			adminPane.setVisible(true);
			logInBtn.setVisible(false);
			passwordF.setText(null);
			loginLabel.setVisible(false);
			passwordL.setVisible(false);
			passwordF.setVisible(false);
			logOutBtn.setVisible(true);
			movieJF.setSize(movieJF.getWidth(), movieJF.getHeight()+100);
			System.out.println("Log In Successful!");
		}
		else {
			System.out.println("Incorrect password.");
			messageBox.doMessage("Password is incorrect!");
		}
	}

	private void doLogOut() {
		adminPane.setVisible(false);
		logInBtn.setVisible(true);
		loginLabel.setVisible(true);
		passwordL.setVisible(true);
		passwordF.setVisible(true);
		logOutBtn.setVisible(false); 	  
		movieJF.setSize(movieJF.getWidth(), movieJF.getHeight()-100);
		System.out.println("Log Out Successful!");
		isAdmin = false;
	}


	//--METHODS FOR FILTERS--

	//I. FILTER MOVIES
	//Call to Filter
	private void filterMovies() {
		String regex = "no|";
		if(gCB.isSelected()) regex += "^(G)$|";
		if(pgCB.isSelected()) regex += "^(PG)$|";
		if(pg13CB.isSelected())regex += "^(PG-13)$|";
		if(rCB.isSelected())regex += "^(R)$|";
		if(nc17CB.isSelected())regex += "^(NC17)$|";
		if(nrCB.isSelected())regex += "^(NR)$|";
		regex+="ne";
		//Check the CheckBoxes if they are selected and append accordingly to the regex for filter

		if (regex.equals("no|ne")) regex = "^.*$";
		//If nothing is selected, regex is set to be all inclusive

		//Filter 2nd Column of JTable of Movies that conform to REGEX
		movieTRS = new TableRowSorter<DefaultTableModel>(movieTableModel);
		movieJTable.setRowSorter(movieTRS);
		movieTRS.setRowFilter(RowFilter.regexFilter(regex, 1));

		//System.out.println("Filter Table using regex: "+ regex); //Debugging
	}

	//II. FILTER CINEMAS
	//1) Call to Check Cinema Filter Inputs. If Valid, Call to Filter Cinema.
	private void doCinemaFilter() {
		if (getCinemaFilter()) {  //If data from x, y, and radius are valid, filter Cinemas
			xInt = Integer.parseInt(xFilterTF.getText()); //KARL
			yInt = Integer.parseInt(yFilterTF.getText()); //KARL
			rInt = Integer.parseInt(radiusTF.getText());  //KARL

			filterCinema();
			clearFilterCinemaInputs();
			//System.out.println("Filtered Cinemas using xy and radius: (" + xInt + "," +yInt + ") and radius of " + rInt +".");
		}
		else {} //Do nothing
	}
	//2) Check if Cinema Filter Inputs are Valid
	private boolean getCinemaFilter() {
		//Get TextField Strings
		cinemaXS = xFilterTF.getText();
		cinemaYS = yFilterTF.getText();
		radiusS = radiusTF.getText();

		//Check for Empty Text Field
		if (cinemaXS.equals("") || cinemaYS.equals("") || radiusS.equals("")) {
			messageBox.doMessage("Cinema Filter Error: Empty Field");
			return false;
		}

		//Attempt to Parse String Inputs before updating int variables
		try {
			xInt = Integer.parseInt(cinemaXS);
			yInt = Integer.parseInt(cinemaYS);
			rInt = Integer.parseInt(radiusS);
			return true;
		}
		catch(Exception eC){
			messageBox.doMessage("Cinema Filter Error: Invalid XY Coordinates or Radius");
			return false;
		}
	}//Retrieve values for filtering Cinemas
	//3) Filter Cinema
	private void filterCinema() {  //KARL
		//Enable RowSorter
		String regex = "no|";
		for(int i=0; i<cinemaTableModel.getRowCount();i++)
		{
			xDB = (int) cinemaTableModel.getValueAt(i, 1);
			yDB = (int) cinemaTableModel.getValueAt(i, 2);
			if(isNearBy()==true)
				regex += "^"+cinemaTableModel.getValueAt(i, 0)+"$|";
		}
		regex+="ne";
		cinemaTRS = new TableRowSorter<DefaultTableModel>(cinemaTableModel);
		cinemaJTable.setRowSorter(cinemaTRS);
		cinemaTRS.setRowFilter(RowFilter.regexFilter(regex, 0));
		//System.out.println("Filter Table using regex: "+ regex);
		//System.out.println("Filter Cinema Action Completed");

	}
	//3a) Check if Cinema Location is Within Boundaries
	private boolean isNearBy()
	{
		//System.out.println(xInt + " "  + yInt + "  " + xDB + " " + yDB+"  "+rInt); KARL
		return (rInt*rInt >=( Math.pow(Math.abs(xInt-xDB) , 2) + Math.pow(Math.abs(yInt-yDB),2) ) ); //KARL changed to rInt^2
	}



	//--METHODS FOR ADDING--

	//I. ADDING MOVIES
	//1) Get Data for Adding Movie
	private boolean getMovieData() {
		movieNameS = movieNameTF.getText();
		movieRatingS = ratingJCBox.getSelectedItem().toString();

		if (movieNameS.equals("")) {
			messageBox.doMessage("Movie Entry Error: Movie Title Missing");
			return false;
		}     

		return true;
	}//Retrieve values for adding Movie
	//1b) Get Data for Adding Showing
	private boolean getShowingData() {
		movieNameS = movieJCBox.getSelectedItem().toString();
		cinemaS = cinemaJCBox.getSelectedItem().toString(); 
		movieTimeS = timeJCBox.getSelectedItem().toString();

		if (movieNameS.equals("")) {
			messageBox.doMessage("Showing Entry Error: Movie Title Missing");
			return false;
		}     

		return true;
	}//Retrieve values for adding Showing
	//2) Call to Database to Add Movie
	private void doAddM() {
		if (getMovieData()) {   //If data for adding movie are valid, add Movie

			database.addMovieToDB(movieNameS, "" , movieRatingS, "");	//Add Movie with Movie Name and Movie Rating Only (name, cinema, rating, time)
			
			clearAddMovieInputs();
			refresh();  //Both JTables are Affected when Movies are Added

		}
	}
	//2b) Call to Database to Add Showing
	private void doAddS() {
		if (getShowingData()) {   //If data for adding movie are valid, add Showing

			database.addMovieToDB(movieNameS, cinemaS, "", movieTimeS);	//Add Showing with Movie Name, Cinema, and Time Only (name, cinema, rating, time)

			refresh();  //Both JTables are Affected when Movies are Added
		}
	}
	//3) Update the Movie DropList with New Movie
	protected static void addMovieJCBox(String movieString) {
		movieJCBox.addItem(movieString);        
	}//11/11 Add CinemaJCBox

	//II. ADDING CINEMAS
	//1) Get the Data from Text Fields
	private boolean getCinemaFromTF() {
		//Get TextField Strings
		cinemaS = cinemaNameTF.getText();
		cinemaXS = cinemaXTF.getText();
		cinemaYS = cinemaYTF.getText();

		//Check for Empty Text Field
		if (cinemaS.equals("") || cinemaXS.equals("") || cinemaYS.equals("")) {
			messageBox.doMessage("Add a Cinema Error: Empty Field");
			return false;
		}   

		//Attempt to Parse String Inputs before updating int variables
		try {
			xInt = Integer.parseInt( cinemaXS );
			yInt = Integer.parseInt( cinemaYS );
			return true;
		}
		catch(Exception badStrings) {
			messageBox.doMessage("Add a Cinema Error: Invalid XY Coordaintes");
			return false;
		}
	}//Retrieve values for adding Cinema
	//2) Call to Database to Add Cinema
	private void doAddC() {     //Contains all the steps that must happen before adding
		if (getCinemaFromTF()) {  //(1) Check if the TextField Inputs for XY Coordinates are Integers
			boolean isG = agCB.isSelected();
			boolean isPG = apgCB.isSelected();
			boolean isPG13 = apg13CB.isSelected();
			boolean isR = arCB.isSelected();
			boolean isNC17 = anc17CB.isSelected();
			boolean isNR = anrCB.isSelected();  //(2) Figure out which Check Boxes are Selected.

			
			if(!(isG || isPG || isPG13 || isR || isNC17 || isNR)) {
				messageBox.doMessage("Adding Cinema Error: A Cinema must Show At Least One Rating");
				System.out.println("Adding Cinema Error: A Cinema must Show At Least One Rating");
				return;
			}                                         // (3) If Statement to Check if at least one rating was selected

			database.addCinemaToDB(cinemaS, xInt, yInt, isG, isPG, isPG13, isR, isNC17, isNR);    // (4) Sent Data to Cinema to do final checks before adding to Database
			//    (i) If the Cinema Name is a duplicate
			//    (ii) If the address is already taken
			refresh(); //Only Cinema Table is Affected when Cinema is Added
			clearAddCinemaInputs(); //Reasoning: No need to keep last entered cinema there
			//System.out.println("Add Cinema Method Complete");
		}
	}   
	//3) Update the Cinema DropList with New Cinema
	protected static void addCinemaJCBox(String cinemaString) {
		cinemaJCBox.addItem(cinemaString);        
	}//11/11 Add CinemaJCBox


	//--METHODS FOR REMOVING

	//1) Call to Remove and Determine Origin of Removal
	private void doRemove() {
		//Check for which JTable is Visible, then Remove
		if(movieJTable.isShowing() && movieJTable.getSelectedRow()>=0) {
			removeSelectedData(movieJTable, movieTableModel, "M");
		}
		else if(cinemaJTable.isShowing() && cinemaJTable.getSelectedRow()>=0) {
			removeSelectedData(cinemaJTable, cinemaTableModel, "C");
		}
		else {
			System.out.println("Nothing was selected from movie/cinema table to be removed.");
		}
	}
	//2) Call to DB to Remove Data Depending on Origin
	private void removeSelectedData(JTable t, DefaultTableModel m, String locationCHAR)
	{
		int i = t.getSelectedRow();

		if(i >= 0)
		{
			int choice = messageBox.doMessage("Are you sure you want to delete this row?","Confirm Delete");
			if (choice==0) {

				if (locationCHAR.equals("M")) {             //1) Determine source *could also probably be achieved with JTable.isSelected()
					getSelectedMovieData(i);                //2) Update global string variables with the information of selected item from the JTable

					database.deleteMovie(movieNameS,"","");       //3) Call database to delete movie
				}
				else if (locationCHAR.equals("C")) {
					getSelectedCinemaData(i); 

					database.deleteCinema(cinemaS);
				}   
				refresh();
			}
			else
			{}//Do nothing
		}
		else{
			messageBox.doMessage("Delete Error");
		}
	}//Remove Selected Row by DB Removal and GUI Update
	//a) Get the Selected Data for Removal from Movie Table
	private void getSelectedMovieData(int selectedRowIndex)
	{
		movieNameS = movieJTable.getModel().getValueAt(movieJTable.getRowSorter().convertRowIndexToModel(selectedRowIndex), 0).toString();
		movieRatingS = movieJTable.getModel().getValueAt(movieJTable.getRowSorter().convertRowIndexToModel(selectedRowIndex), 1).toString();
	}//Retrieve Data from Movie Table for DisplayGUI
	//b) Get the Selected Data for Removal from Cinema Table
	private void getSelectedCinemaData(int selectedRowIndex)
	{
		cinemaS = cinemaJTable.getModel().getValueAt(cinemaJTable.getRowSorter().convertRowIndexToModel(selectedRowIndex), 0).toString();
		cinemaXS = cinemaJTable.getModel().getValueAt(cinemaJTable.getRowSorter().convertRowIndexToModel(selectedRowIndex), 1).toString();
		cinemaYS = cinemaJTable.getModel().getValueAt(cinemaJTable.getRowSorter().convertRowIndexToModel(selectedRowIndex), 2).toString();
	}//Retrieve values from Selected Cinema Table for DisplayGUI
	//3a) Update Movie DropList of Removed Movie
	protected static void removeMovieJCBox(String movieString) {
		movieJCBox.removeItem(movieString);   
	}//11/11 Add CinemaJCBox
	//3b) Update Movie DropList of Removed Cinema 
	protected static void removeCinemaJCBox(String cinemaString) {
		cinemaJCBox.removeItem(cinemaString);   
	}//11/11 Add CinemaJCBox


	//--METHODS FOR "GENERAL HOUSEKEEPING"

	//I. Load JTables with data from Database
	private void loadData() {
		database.loadDataToMovie();
		database.loadDataToCinema();
	}//Appends Data from DB onto All Table
	//II. Cleaning Up Filters
	//1) Unselects all the check boxes
	private void resetFilterMovieInputs() {
		gCB.setSelected(false);
		pgCB.setSelected(false);
		pg13CB.setSelected(false);
		rCB.setSelected(false);
		nc17CB.setSelected(false);
		nrCB.setSelected(false);
		filterMovies();
		System.out.println("resetFilterMovieInputs");
	}
	//2) Clear Cinema Filters
	private void clearFilterCinemaInputs() {
		xFilterTF.setText("");
		yFilterTF.setText("");
		radiusTF.setText("");                   // 11/8 JTextFields reset after filter
		System.out.println("clearFilterCinemaInputs");
	}
	//III. Cleaning Up Inputs
	//1) Resets the inputs for adding Movies
	private void clearAddMovieInputs() {
		movieNameTF.setText("");
		ratingJCBox.setSelectedIndex(0);
		System.out.println("clearAddMovieInptus");
	}
	//2) Clears the inputs for adding Showings
	private void clearAddShowingInputs() {
		movieJCBox.setSelectedIndex(0);
		cinemaJCBox.setSelectedIndex(0);
		timeJCBox.setSelectedIndex(0);                        // 11/8 Reset JTextfield and JComboBoxes after Adding movie
		System.out.println("clearAddShowingInptus");
	}
	//3) Clears the inputs for adding Cinemas
	private void clearAddCinemaInputs() {
		cinemaNameTF.setText("");
		cinemaXTF.setText("");
		cinemaYTF.setText("");
		agCB.setSelected(false);
		apgCB.setSelected(false);
		apg13CB.setSelected(false);
		arCB.setSelected(false);
		anc17CB.setSelected(false); 
		anrCB.setSelected(false);
		System.out.println("clearAddCinemaInputs");
	};
	//IV. JTable and Default Table Model Functions
	//1) Clears All Rows from JTable
	private static void clearBox(DefaultTableModel defaultTableModelObj) {
		if (defaultTableModelObj.getRowCount() > 0) {
			for (int i = defaultTableModelObj.getRowCount() - 1; i > -1; i--) {
				defaultTableModelObj.removeRow(i);
			}
		}   
	}//Clears specific table of all data
	//2) Calls refreshMovie and refreshCinema
	private void refresh() {
		refreshMovie();
		refreshCinema();
	}//Clear All Tables, then Loads All Tables from DB
	//2a) Call to resetFilterMovieInputs, clearBox() for Movies, Resets the Movie JTable Filter, Loads Most Recent Movies from DB
	private void refreshMovie() {
		resetFilterMovieInputs();                         //1)  Movie Filter Checkboxes are unchecked
		clearBox(movieTableModel);                        //2)  Empty Movie JTable
		movieTRS = new TableRowSorter<DefaultTableModel>(movieTableModel);    //3)  Re-declare the rowSorter
		movieJTable.setRowSorter(movieTRS);                   //4)  Reset the rowSorter
		database.loadDataToMovie();                       //5)  Reload the most recent data from the DB
		System.out.println("Movies Refreshed!");
	}//Clears Movie Table and Loads Movies from DB
	//2b) Call to resetFilterCinemaInputs, clearBox() for Cinemas, Resets the Cinema JTable Filter, Loads Most Recent Cinemas from DB
	private void refreshCinema() {
		clearFilterCinemaInputs();                        //1)  Cinema Filter Textfields are cleared
		clearBox(cinemaTableModel);                       //2)  Empty Cinema JTable
		cinemaTRS = new TableRowSorter<DefaultTableModel>(cinemaTableModel);  //3)  Re-declare the rowSorter
		cinemaJTable.setRowSorter(cinemaTRS);                 //4)  Reset the rowSorter
		database.loadDataToCinema();                      //5)  Reload the most recent data from the DB
		System.out.println("Cinema Refreshed!");
	}//Clears Cinema Table and Loads Cinemas from DB


	/*
	 * Action Handler Classes
	 * */

	//Handles All Button Related Functions
	private class btnHandlerClass implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Object sourceObj = event.getSource();
			if (sourceObj == refreshBtn) {
				System.out.println("RefreshBtn has been Clicked!");
				refresh();
				clearAddMovieInputs();
				clearAddShowingInputs();
				clearAddCinemaInputs();
			}
			else if (sourceObj == logInBtn) {
				System.out.println("Administrator Logging In!");
				doLogIn();
			}
			else if (sourceObj == logOutBtn) {
				System.out.println("Log Out Button Clicked!");
				doLogOut();
			}
			else if (sourceObj == cinemaFilterBtn) { //ASDFASDFASDF
				System.out.println("Cinema Filter Action Listened!");
				doCinemaFilter();
				tabbedPane.setSelectedIndex(1);
			}//If Cinema Filter Button is Clicked
			else if (sourceObj == addMovieBtn) {
				System.out.println("Adding a Movie!");
				doAddM();
				tabbedPane.setSelectedIndex(0);
			}//If Administer Add Movie Button is Clicked
			else if (sourceObj == addShowingBtn) {
				System.out.println("Adding a Showing");
				doAddS();
				tabbedPane.setSelectedIndex(0);
			}
			else if (sourceObj == addCinemaBtn) {
				System.out.println("Adding a Cinema!");
				doAddC();
				tabbedPane.setSelectedIndex(1);
			}//If Administer Add Cinema Button is Clicked
			else if (sourceObj == removeBtn) {
				System.out.println("Remove Button Clicked!");
				doRemove();
			}//If Administer Remove Button is Clicked
		}
	}

	//Class to Handle Movie Check Box Related Functions
	private class selectionHandler implements ItemListener{
		public void itemStateChanged(ItemEvent event) {
				filterMovies();
				tabbedPane.setSelectedIndex(0);
		}
	}//Any changes to checkboxes will alert this HandlerClass

}
