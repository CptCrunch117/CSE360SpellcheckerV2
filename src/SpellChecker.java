import java.io.*;
import java.util.Scanner;

/**
 * Created by CptAmerica on 12/8/15.
 */
public class SpellChecker {
    
    private JFrame frame;
    private Dictionary dict;

    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SpellcheckerGUI window = new SpellcheckerGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        dict = new Dictionary();
        System.out.println(dict.toString());
        Scanner scan = new Scanner(System.in);
        String in;
        System.out.println(" would you like to enter a file? [y/n]");
        in  = scan.nextLine();
        while(in.equalsIgnoreCase("y")) {
            dict.clearArrays();
            String filename = scan.nextLine();
            File file = new File(filename);
            FileReader read = new FileReader(file);
            BufferedReader readB = new BufferedReader(read);

            String line;
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
            System.out.println("Incorrect Words:");
            System.out.println(dict.incorrectWordsString());
            System.out.println("Would you like to add any of these?");
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                int size = dict.getIncSize();
                for (int i = 0; i < dict.getIncSize(); i++) {
                    System.out.println("Would you like to add: " + dict.getIncorrectWords()[i] + " ? enter: [y/n]");
                    input = scan.nextLine();
                    if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                        dict.insertWord(dict.getIncorrectWords()[i]);
                        dict.insertAdWord(dict.getIncorrectWords()[i]);
                        dict.delete(dict.getIncorrectWords(), dict.getIncorrectWords()[i], "inc");
                        i -= 1;
                    }
                }
            }
            System.out.println(dict.dictToFile());
            System.out.println(dict.addedToFile(null));
            System.out.println(dict.incToFile(null));

            System.out.println(" would you like to enter another file? [y/n]");
            in  = scan.nextLine();
        }
        System.exit(0);
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
		
		JButton btnIgnore = new JButton("Ignore");
		btnIgnore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		frame.getContentPane().add(btnIgnore, BorderLayout.WEST);
		
		JButton btnAddWord = new JButton("Add Word");
		btnAddWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		frame.getContentPane().add(btnAddWord, BorderLayout.EAST);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		frame.getContentPane().add(btnNext, BorderLayout.CENTER);
	}


}
