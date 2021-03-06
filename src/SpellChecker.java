import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

/**
 * Created by Team 5
 */
public class SpellChecker {
    
    private JFrame frame;
    private JMenuBar jBar;
    private JMenu menu;
    Dictionary dict;
    int iter = 0;
    int iterEnd = 0;
    boolean isInit = true;
    JTextArea txtrWordsWillAppear;
    public static void main(String[] args) throws IOException {


        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SpellChecker window = new SpellChecker();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }//END MAIN METHOD



	/**
	 * Create the application.
	 */
	public SpellChecker() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {


		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Lucida Grande", Font.PLAIN, 33));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		txtrWordsWillAppear = new JTextArea();
		txtrWordsWillAppear.setFont(new Font("Lucida Grande", Font.PLAIN, 33));
		txtrWordsWillAppear.setRows(2);
		txtrWordsWillAppear.setText("Welcome to SpellChecker!");
        txtrWordsWillAppear.setEditable(false);
		frame.getContentPane().add(txtrWordsWillAppear, BorderLayout.NORTH);
        jBar = new JMenuBar();
        menu = new JMenu("Help");
        menu.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                String str = "To get Started navigate to \"CSE360Spellchecker/TextFilesHere\"\n" +
                        "and add in text files you want to run later when using the program. Next launch the program\n" +
                        "and click \"New File\" button, in the prompt, enter the name of the text file (with extension)\n" +
                        "and click ok. If incorrect words are found, you have the choice to walk through each incorrect word\n" +
                        "found and either ignore or add them to the dictionary. To see the results, navigate to the folder\n" +
                        "\"CSE360Spellchecker/Words\" here the text files generated with addedWords and incorrectWords will\n" +
                        "be stored! Thank you for using the Spell Checker 5000 and enjoy!";
                JOptionPane.showMessageDialog(null, str);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                System.exit(0);
            }
        });
        jBar.add(menu);
        frame.setJMenuBar(jBar);
        frame.setTitle("SpellChecker 5000");

        dict = new Dictionary();





		JButton btnIgnore = new JButton("Ignore");
		btnIgnore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (iter < iterEnd) {
                    iter++;
                    txtrWordsWillAppear.setText(dict.getIncorrectWords()[iter]);
                    if (iter >= iterEnd) {
                        txtrWordsWillAppear.setText("All incorrect words have\n been sorted out.");
                    }
                }
            }
        });
		frame.getContentPane().add(btnIgnore, BorderLayout.WEST);
		
		JButton btnAddWord = new JButton("Add Word");
        btnAddWord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (iter < iterEnd) {
                    dict.insertWord(dict.getIncorrectWords()[iter]);
                    dict.insertAdWord(dict.getIncorrectWords()[iter]);
                    dict.delete(dict.getIncorrectWords(), dict.getIncorrectWords()[iter], "inc");
                    iterEnd = dict.getIncSize();
                    txtrWordsWillAppear.setText(dict.getIncorrectWords()[iter]);
                    if (iter >= iterEnd) {
                        txtrWordsWillAppear.setText("All incorrect words have\n been sorted out.");
                    }
                }
            }
        });

		frame.getContentPane().add(btnAddWord, BorderLayout.EAST);
		
		final JButton btnNext = new JButton("New File");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                if (!isInit) {
                    iterEnd = 0;
                    dict.dictToFile();
                    dict.addedToFile(null);
                    dict.incToFile(null);
                    dict.clearArrays();
                    txtrWordsWillAppear.setText("Processing File...");
                    dict.incrementAddFile();
                    dict.incrementIncFile();
                } else {
                    isInit = false;
                }
                String path = JOptionPane.showInputDialog("Enter File path");
                if (path != null) {
                    File file = new File("TextFIlesHere/"+path);
                    if (file.exists()) {
                        FileReader read = null;
                        try {
                            read = new FileReader(file);
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        BufferedReader readB = new BufferedReader(read);

                        String line;
                        try {
                            while ((line = readB.readLine()) != null) {
                                boolean verifyLetter = true;
                                char[] wordChar = line.toCharArray();
                                for (char c : wordChar) {
                                    if (!Character.isLetter(c)) {
                                        verifyLetter = false;
                                    }
                                }

                                if (verifyLetter) {  //Disregard if verifyLetter is false as Line does not just contain letters
                                    int correct = dict.searchWord(dict.getDictionary(), "dict", line);
                                    if (correct == -1) {
                                        dict.insertIncWord(line);
                                    }
                                }
                            }
                            iterEnd = dict.getIncSize();
                            if (iterEnd != 0) {
                                int dialogButton = JOptionPane.YES_NO_OPTION;
                                int x = JOptionPane.showConfirmDialog(null, "Would you like to add the incorrect words to the Dictionary?", "?", dialogButton);
                                if (x == JOptionPane.YES_OPTION) {
                                    txtrWordsWillAppear.setText(dict.getIncorrectWords()[iter]);
                                }
                                if (x == JOptionPane.NO_OPTION) {
                                    iterEnd = 0;
                                    dict.dictToFile();
                                    dict.addedToFile(null);
                                    dict.incToFile(null);
                                    txtrWordsWillAppear.setText("File Processed!");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "All Words match the Dictionary!");
                            }
                        } catch (IOException x) {
                            JOptionPane.showMessageDialog(null, "Could not read file");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "File Does Not Exist!");
                    }
                }
                else{
                    txtrWordsWillAppear.setText("Welcome to SpellChecker!");
                }
            }
        });

		frame.getContentPane().add(btnNext, BorderLayout.CENTER);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int x = JOptionPane.showConfirmDialog(frame, "Would you like to save the files before closing this application?", "Really Closing!?!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(x == 0){
                    dict.dictToFile();
                    dict.addedToFile(null);
                    dict.incToFile(null);
                    System.exit(0);
                }
                else if(x == 1){
                    //Don't close...?
                }
            }
        });
	}


    private class HelpListener implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            JOptionPane.showMessageDialog(null,"sup");
        }
    }

}

