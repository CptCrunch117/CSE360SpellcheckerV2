import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

/**
 * Created by CptAmerica on 12/8/15.
 */
public class SpellChecker {
    
    private JFrame frame;
    Dictionary dict;
    int iter = 0;
    boolean isInit = true;
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


    }



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
		
		JTextArea txtrWordsWillAppear = new JTextArea();
		txtrWordsWillAppear.setFont(new Font("Lucida Grande", Font.PLAIN, 33));
		txtrWordsWillAppear.setRows(2);
		txtrWordsWillAppear.setText("Words Will Appear Here");
		frame.getContentPane().add(txtrWordsWillAppear, BorderLayout.NORTH);




        dict = new Dictionary();






		JButton btnIgnore = new JButton("Ignore");
		btnIgnore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                iter++;
			}
		});
		frame.getContentPane().add(btnIgnore, BorderLayout.WEST);
		
		JButton btnAddWord = new JButton("Add Word");
		btnAddWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                dict.insertWord(dict.getIncorrectWords()[iter]);
                dict.insertAdWord(dict.getIncorrectWords()[iter]);
                dict.delete(dict.getIncorrectWords(), dict.getIncorrectWords()[iter], "inc");
                iter -= 1;
			}
		});

		frame.getContentPane().add(btnAddWord, BorderLayout.EAST);
		
		final JButton btnNext = new JButton("New File");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                if(!isInit){
                    dict.dictToFile();
                    dict.addedToFile(null);
                    dict.incToFile(null);
                    dict.clearArrays();
                }
                String path = JOptionPane.showInputDialog("Enter File path");
                File file = new File(path);
                if(file.exists()) {
                    FileReader read = null;
                    try {
                        read = new FileReader(file);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    BufferedReader readB = new BufferedReader(read);

                    String line;
                    try{
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
                        iter = dict.getIncSize();
                    }catch(IOException x){
                        JOptionPane.showMessageDialog(null,"Could not read file");
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"File Does Not Exist!");
                }
            }
		});

		frame.getContentPane().add(btnNext, BorderLayout.CENTER);
	}

}

