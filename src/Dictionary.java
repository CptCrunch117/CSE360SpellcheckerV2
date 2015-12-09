import java.io.*;
import java.util.Random;

/**
 * Created by Team 5
 */
public class Dictionary {


    private String[] dictionary;
    private String[] addedWords;
    private String[] incorrectWords;
    private int incSize;
    private int adSize;
    private int size;
    private static int addFilecount;
    private static int incFilecount;

    /**
     * No Args-Constructor, Instantiates everything.
     */
    public Dictionary(){
        this.dictionary = new String[5];
        this.addedWords = new String[5];
        this.incorrectWords = new String[50];
        this.size = 0;
        this.adSize = 0;
        this.incSize = 0;
        addFilecount = 0;
        incFilecount = 0;
        File file = new File("Words/Dictionary.txt");
        if(file.exists()) {
            FileReader read = null;
            try {
                read = new FileReader(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader readB = new BufferedReader(read);
            String line;
            try {
                while ((line = readB.readLine()) != null) {
                    this.insertWord(line);
                }
            } catch (IOException e) {
                //Handle error
            }
        }

    }

    /**
     * Constructor that
     * @param size
     */
    public Dictionary(int size){
        this.dictionary = new String[size];
        this.addedWords = new String[size];
        this.incorrectWords = new String[size];
        this.size = 0;
        this.adSize = 0;
        this.incSize = 0;
        addFilecount = 0;
        incFilecount = 0;
        File file = new File("Words/Dictionary.txt");
        if(file.exists()) {
            FileReader read = null;
            try {
                read = new FileReader(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader readB = new BufferedReader(read);
            String line;
            try {
                while ((line = readB.readLine()) != null) {
                    this.insertWord(line);
                }
            } catch (IOException e) {
                //Handle error
            }
        }
    }

    /**
     * Increments file number being processed for saving the addedWords file. This helps for naming different files in a given session.
     */
    public void incrementAddFile(){
        this.addFilecount+=1;
    }

    /**
     * Increments file number being processed for saving the ignoredWords file. This helps for naming different files in a given session.
     */
    public void incrementIncFile(){
        this.incFilecount+=1;
    }

    /**
     * This algorithm uses binary search on the String array to find the index that matches with the key (the word).
     * @param list The list to search
     * @param type The type of list, addedWords list = "add", ignoredWords list = "inc", and the Dictionary list = "dict"
     *             This helps determine also which size integer to use for the loop iteration in the search.
     * @param word The word is the string key to search in the list provided.
     * @return  returns an iteger representation of the found word's index in the list, and a value of -1 if not found in the list.
     */
    public int searchWord(String[] list,String type,String word){
        int size = 0;
        if(type.equalsIgnoreCase("dict")){
            size = this.size;
        }
        else if(type.equalsIgnoreCase("add")){
            size = this.adSize;
        }
        else if(type.equalsIgnoreCase("inc")){
            size = this.incSize;
        }
        int low = 0;
        int high = size-1;
        while (low <= high) {
            int mid = low + (high-low) / 2;
            if(word.toLowerCase().compareTo(list[mid].toLowerCase()) < 0){
                high = mid - 1;
            }
            else if (word.toLowerCase().compareTo(list[mid].toLowerCase()) > 0){
                low = mid + 1;
            }
            else{
                return mid;
            }
        }
        return -1;
    }

    /**
     * This private method expands the size of the array passed in by double the array's orignial length when passed in.
     * @param listForExpansion The String array to expand.
     * @param type The type of list, addedWords list = "add", ignoredWords list = "inc", and the Dictionary list = "dict"
     *             This helps determine also which size integer to use for the loop iteration in the search.
     */
    private void expandSize(String[] listForExpansion, String type){
        int newLength = listForExpansion.length*2;
        String[] d = new String[newLength];
        int size = 0;
        if(type.equalsIgnoreCase("dict")){
            size = this.size;
        }
        else if(type.equalsIgnoreCase("add")){
            size = this.adSize;
        }
        else if(type.equalsIgnoreCase("inc")){
            size = this.incSize;
        }
        for(int i = 0; i < size; i++)
        {
            d[i] = listForExpansion[i];
            if(type.equalsIgnoreCase("dict")){
                this.dictionary = d;
            }
            else if(type.equalsIgnoreCase("add")){
                this.addedWords = d;
            }
            else if(type.equalsIgnoreCase("inc")){
                this.incorrectWords = d;
            }
        }
    }

    /**
     * This method inserts a word into the dictionary string array in ascending alphabetical order.
     * @param word The word to insert into the Dictionary list.
     * @return Returns true if the word was successfully inserted into the Dictionary and false otherwise.
     */
    public boolean insertWord(String word){
        int exists = searchWord(this.getDictionary(), "dict", word);
        if(exists == -1){
            if (this.size == this.dictionary.length) {
                expandSize(this.dictionary, "dict");
            }
            int index = findInsertionPoint(word);
            if (index != -1) {
                int iter = this.size - index;
                if (iter >= 0) {
                    String temp;
                    this.size += 1;
                    temp = this.dictionary[index];
                    this.dictionary[index] = word;
                    String temp2 = "";
                    for (int i = index + 1; i < this.size; i++) {
                        if (this.dictionary[i] != null) {
                            temp2 = this.dictionary[i];
                        }
                        this.dictionary[i] = temp;
                        temp = temp2;
                    }
                    return true;
                } else {
                    this.size++;
                    this.dictionary[index] = word;
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * This method inserts a word into the ignored Words string array in ascending alphabetical order.
     * @param word The word to insert into the ignored Words list.
     * @return Returns true if the word was successfully inserted into the ignored Words list and false otherwise.
     */
    public boolean insertIncWord(String word){
        int exists = searchWord(this.getIncorrectWords(), "inc", word);
        if(exists == -1) {
            if (this.incSize == this.incorrectWords.length) {
                expandSize(this.incorrectWords, "inc");
            }
            int index = findIncInsertionPoint(word);
            if (index != -1) {
                int iter = this.incSize - index;
                if (iter >= 0) {
                    String temp;
                    this.incSize += 1;
                    temp = this.incorrectWords[index];
                    this.incorrectWords[index] = word;
                    String temp2 = "";
                    for (int i = index + 1; i < this.incSize; i++) {
                        if (this.incorrectWords[i] != null) {
                            temp2 = this.incorrectWords[i];
                        }
                        this.incorrectWords[i] = temp;
                        temp = temp2;
                    }
                    return true;
                } else {
                    this.incSize++;
                    this.incorrectWords[index] = word;
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * This method inserts a word into the added Words string array in ascending alphabetical order.
     * @param word The word to insert into the added Words list.
     * @return Returns true if the word was successfully inserted into the ignored Words list and false otherwise.
     */
    public boolean insertAdWord(String word){
        int exists = searchWord(this.getDictionary(),"add",word);
        if(exists == -1) {
            if (this.adSize == this.addedWords.length) {
                expandSize(this.addedWords, "add");
            }
            int index = findAdInsertionPoint(word);
            if (index != -1) {
                int iter = this.adSize - index;
                if (iter >= 0) {
                    String temp;
                    this.adSize += 1;
                    temp = this.addedWords[index];
                    this.addedWords[index] = word;
                    String temp2 = "";
                    for (int i = index + 1; i < this.adSize; i++) {
                        if (this.addedWords[i] != null) {
                            temp2 = this.addedWords[i];
                        }
                        this.addedWords[i] = temp;
                        temp = temp2;
                    }
                    return true;
                } else {
                    this.adSize++;
                    this.addedWords[index] = word;
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Finds the insertion point for the word being inserted using a modified Binary search, this allows insert to truly be O(logn) complexity.
     * @param word The word to insert into the Dictionary list.
     * @return Returns the integer representation of the index position the word should belong in the Dictionary String array
     */
    private int findInsertionPoint(String word){
        int min = 0;
        int max = this.size;
        int found = -1;
        if(max == 0){
            return max;
        }
        int mark = (min+max)/2;
        if(word.toLowerCase().compareTo(this.dictionary[mark].toLowerCase()) == 0){
            found  = mark;
        }
        else if(word.toLowerCase().compareTo(this.dictionary[mark].toLowerCase()) < 0){
            max = mark;
        }
        else{
            min = mark+1;
        }
        if(!(min < max)){
            found = (word.toLowerCase().compareTo(this.dictionary[mark].toLowerCase()) < 0) ? mark-1 : mark+1;
        }
        while(min <= max && found == -1 ){
            mark = (min+max)/2;
            if((min == mark) || (mark == max)){
                if(this.dictionary[mark] == null){
                    found = mark;
                }
                else if(word.toLowerCase().compareTo(this.dictionary[mark].toLowerCase()) == 0){
                    found  = mark;
                }
                else if(word.toLowerCase().compareTo(this.dictionary[mark].toLowerCase()) < 0){
                    found = mark;
                }
                else{
                    found = mark+1;
                }
            }
            else{
                mark = (min+max)/2;
                if(word.toLowerCase().compareTo(this.dictionary[mark].toLowerCase()) == 0){
                    found = mark;
                }
                else if(word.toLowerCase().compareTo(this.dictionary[mark].toLowerCase()) < 0){
                    max = mark;
                }
                else{
                    min = mark +1;
                }
            }
        }//END WHILE LOOP
        return found;
    }

    /**
     * Finds the insertion point for the word being inserted using a modified Binary search, this allows insert to truly be O(logn) complexity.
     * @param word The word to insert into the added Words list.
     * @return Returns the integer representation of the index position the word should belong in the added Words String array
     */
    private int findAdInsertionPoint(String word){
        int min = 0;
        int max = this.adSize;
        int found = -1;
        if(max == 0){
            return max;
        }
        int mark = (min+max)/2;
        if(word.toLowerCase().compareTo(this.addedWords[mark].toLowerCase()) == 0){
            found  = mark;
        }
        else if(word.toLowerCase().compareTo(this.addedWords[mark].toLowerCase()) < 0){
            max = mark;
        }
        else{
            min = mark+1;
        }
        if(!(min < max)){
            found = (word.toLowerCase().compareTo(this.addedWords[mark].toLowerCase()) < 0) ? mark-1 : mark+1;
        }
        while(min <= max && found == -1 ){
            mark = (min+max)/2;
            if((min == mark) || (mark == max)){
                if(this.addedWords[mark] == null){
                    found = mark;
                }
                else if(word.toLowerCase().compareTo(this.addedWords[mark].toLowerCase()) == 0){
                    found  = mark;
                }
                else if(word.toLowerCase().compareTo(this.addedWords[mark].toLowerCase()) < 0){
                    found = mark;
                }
                else{
                    found = mark+1;
                }
            }
            else{
                mark = (min+max)/2;
                if(word.toLowerCase().compareTo(this.addedWords[mark].toLowerCase()) == 0){
                    found = mark;
                }
                else if(word.toLowerCase().compareTo(this.addedWords[mark].toLowerCase()) < 0){
                    max = mark;
                }
                else{
                    min = mark +1;
                }
            }
        }//END WHILE LOOP
        return found;
    }

    /**
     * Finds the insertion point for the word being inserted using a modified Binary search, this allows insert to truly be O(logn) complexity.
     * @param word The word to insert into the ignored  Words list.
     * @return Returns the integer representation of the index position the word should belong in the ignored Words String array
     */
    private int findIncInsertionPoint(String word){
        int min = 0;
        int max = this.incSize;
        int found = -1;
        if(max == 0){
            return max;
        }
        int mark = (min+max)/2;
        if(word.toLowerCase().compareTo(this.incorrectWords[mark].toLowerCase()) == 0){
            found  = mark;
        }
        else if(word.toLowerCase().compareTo(this.incorrectWords[mark].toLowerCase()) < 0){
            max = mark;
        }
        else{
            min = mark+1;
        }
        if(!(min < max)){
            found = (word.toLowerCase().compareTo(this.incorrectWords[mark].toLowerCase()) < 0) ? mark-1 : mark+1;
        }
        while(min <= max && found == -1 ){
            mark = (min+max)/2;
            if((min == mark) || (mark == max)){
                if(this.incorrectWords[mark] == null){
                    found = mark;
                }
                else if(word.toLowerCase().compareTo(this.incorrectWords[mark].toLowerCase()) == 0){
                    found  = mark;
                }
                else if(word.toLowerCase().compareTo(this.incorrectWords[mark].toLowerCase()) < 0){
                    found = mark;
                }
                else{
                    found = mark+1;
                }
            }
            else{
                mark = (min+max)/2;
                if(word.toLowerCase().compareTo(this.incorrectWords[mark].toLowerCase()) == 0){
                    found = mark;
                }
                else if(word.toLowerCase().compareTo(this.incorrectWords[mark].toLowerCase()) < 0){
                    max = mark;
                }
                else{
                    min = mark +1;
                }
            }
        }//END WHILE LOOP
        return found;
    }

    /**
     * This algorithm Deletes a word from a list and mantains this alpahbetical order afterwards in a given list.
     * @param list The list to delete the word from.
     * @param word The word to delete from the given list.
     * @param type The type of list, addedWords list = "add", ignoredWords list = "inc", and the Dictionary list = "dict"
     *             This helps determine also which size integer to use for the loop iteration in the search.
     */
    public void delete(String[] list, String word, String type ){
        int exists = searchWord(list,type,word);
        int size = 0;
        if(type.equalsIgnoreCase("dict")){
            size = this.size;
        }
        else if(type.equalsIgnoreCase("add")){
            size = this.adSize;
        }
        else if(type.equalsIgnoreCase("inc")){
            size = this.incSize;
        }
        if(exists != -1){
            for(int i = exists; i < size; i++ ){
                if(list[i+1] != null) {
                    list[i] = list[i + 1];
                }
            }
            list[size-1] = null;
            if(type.equalsIgnoreCase("dict")){
                this.size-=1;
            }
            else if(type.equalsIgnoreCase("add")){
                this.adSize-=1;
            }
            else if(type.equalsIgnoreCase("inc")){
                this.incSize-=1;
            }
        }
    }

    /**
     * This algorithm clears and resets the addedWords, and ignoredWords string arrays (NOT the Dictionary array),
     * and resets values associated with these two lists.
     */
    public void clearArrays(){
        int adLength = this.addedWords.length;
        int incLength = this.incorrectWords.length;
        this.addedWords = new String[adLength];
        this.incorrectWords = new String[incLength];
        this.adSize = 0;
        this.incSize = 0;
    }

    //GETTERS AND TOSTRINGS

    /**
     * Makes a string representation of the incorrectWords list.
     * @return The string representation of the incorrectWords list.
     */
    public String incorrectWordsString(){
        String incorrectWords = "";
        for(int i = 0; i < this.incSize; i++){
            incorrectWords += this.incorrectWords[i]+"\n";
        }
        return incorrectWords;
    }

    /**
     * Makes a string representation of the addedWords list.
     * @return The string representation of the addedWords list.
     */
    public String addedWordsString(){
        String incorrectWords = "";
        for(int i = 0; i < this.adSize; i++){
            incorrectWords += this.addedWords[i]+"\n";
        }
        return incorrectWords;
    }

    /**
     * Returns the Dictionary array for public access
     * @return The Dictionary String array
     */
    public String[] getDictionary() {
        return dictionary;
    }

    /**
     * Returns the addedWords array for public access
     * @return The addedWords String array
     */
    public String[] getAddedWords() {
        return addedWords;
    }

    /**
     * Returns the incorrectWords array for public access
     * @return The incorrectWords String array.
     */
    public String[] getIncorrectWords() {
        return incorrectWords;
    }

    /**
     * Returns the number of words currently in the incorrectWords String array
     * @return integer representation of words currently in the incorrectWords String array
     */
    public int getIncSize() {
        return incSize;
    }

    /**
     * Returns the number of words currently in the addedWords String array
     * @return integer representation of words currently in the addedWords String array
     */
    public int getAdSize() {
        return adSize;
    }

    /**
     * Returns the number of words currently in the Dictionary String array
     * @return integer representation of words currently in the Dictionary String array
     */
    public int getSize() {
        return size;
    }

    @Override
    public String toString(){
        String diction = "";
        for(int i = 0; i < this.size; i++){
            diction += this.dictionary[i]+"\n";
        }
        return diction;
    }

    //FILE OPS

    /**
     * Generates a text file with all the words that were added to the Dictionary in alphabetical order.
     * @param optName The optional name the user can give [didn't implement in app but is implementable]
     * @return True if the file was successfully written and saved and False otherwise
     */
    public boolean addedToFile(String optName){
        if(adSize > 0) {
            String filename = "addedWords" + addFilecount + ".txt";
            if (optName != null) {
                filename = optName + ".txt";
            }
            File addedWords = new File("Words/"+filename);
            if (!addedWords.exists()) {
                FileWriter write;
                try {
                    write = new FileWriter(addedWords);
                } catch (IOException e) {
                    return false;
                }

                BufferedWriter writeB = new BufferedWriter(write);

                for (int i = 0; i < this.adSize; i++) {
                    try {
                        writeB.write(this.getAddedWords()[i] + "\n");
                    } catch (IOException e) {
                        return false;
                    }
                }
                try {
                    writeB.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Generates a text file with all the words that were ignored in alphabetical order.
     * @param optName The optional name the user can give [didn't implement in app but is implementable]
     * @return True if the file was successfully written and saved and False otherwise
     */
    public boolean incToFile(String optName){
        if(incSize > 0) {
            String filename = "incorrectWords" + incFilecount + ".txt";
            if (optName != null) {
                filename = optName + ".txt";
            }
            File incWords = new File("Words/"+filename);
            if (!incWords.exists()) {
                FileWriter write;
                try {
                    write = new FileWriter(incWords);
                } catch (IOException e) {
                    return false;
                }

                BufferedWriter writeB = new BufferedWriter(write);

                for (int i = 0; i < this.incSize; i++) {
                    try {
                        writeB.write(this.incorrectWords[i] + "\n");
                    } catch (IOException e) {
                        return false;
                    }
                }
                try {
                    writeB.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Generates an updated text file with all the words that are now in the Dictionary in alphabetical order.
     * @return True if the file was successfully written and saved and False otherwise
     */
    public boolean dictToFile()
    {
        File diction = new File("Words/Dictionary.txt");
        FileWriter write;
        try{
            write = new FileWriter(diction);
        }catch(IOException e){
            return false;
        }

        BufferedWriter writeB = new BufferedWriter(write);

        for(int i =0; i < this.size; i++){
            try {
                writeB.write(this.dictionary[i]+"\n");
            }catch(IOException e){
                return false;
            }
        }
        try {
            writeB.close();
        }catch(IOException e){
            return false;
        }
        return true;
    }



}
