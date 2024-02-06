
import java.util.Objects;


public class WordCounter {
    String focusWord;
    private int numOfSpam;
    private int numOfNoSpam;
    private int numOfFocusWordSpam;
    private int numOfFocusWordNoSpam;

    /**
     * This constructor stores String value of the given focus word, number of spam, non spam words
     * @param focusWord a string with given focus word
     */
    public WordCounter(String focusWord) {
        this.focusWord = focusWord;
        numOfSpam = 0;
        numOfNoSpam = 0;
        numOfFocusWordSpam = 0;
        numOfFocusWordNoSpam = 0;

    }

    //focusWord accessor method

    /**
     * Provides the current focus word given to the model
     * @return current focus word
     */
    public String getFocusWord() {
        return this.focusWord;
    }


    /**
     *This method adds to our word counter object a string for training
     * @param document line of text to be added to our train model
     */
    public void addSample(String document) {
        boolean spam = false;

        //splitting words of string document ad adding them to string array to easily read it
        String[] splitArray = document.split(" ");
        int spamIndicator = Integer.parseInt(splitArray[0]);

        //checking whether line is spam or not
        if (spamIndicator == 1) {
            spam = true;
        }

        //iterating string array and avoiding consideration of the first element (indicator)
        for (int i = 1; i < splitArray.length; i++) {

            if (!spam) {

                //counting number of spam words
                this.numOfNoSpam += 1;

                //counting number of non spam focus word
                if (Objects.equals(splitArray[i], this.focusWord)) {
                    this.numOfFocusWordNoSpam += 1;
                }
            } else if (spam) {
                //counting number of spam words
                this.numOfSpam += 1;

                //counting number of focus spam words
                if (Objects.equals(splitArray[i], this.focusWord)) {
                    this.numOfFocusWordSpam += 1;
                }
            }
        }
    }

    /**
     * This method checks whether our model is trained(processed enough of string data)
     * @return a boolean value which shows whether current word counter object is trained
     */
    public boolean isCounterTrained() {

        return numOfSpam > 0 && numOfNoSpam > 0 && numOfFocusWordNoSpam + numOfFocusWordSpam > 0;
    }

    /**
     * This method calculates and returns conditional non spam value and checks calls 'isCounterTrained' method to check whether current wordcounter is trained
     * @return conditional non spam value
     * throws IllegalStateException if we try to use untrained word counter
     */
    public double getConditionalNoSpam() {
        if (!isCounterTrained()) {
            throw new IllegalStateException();
        }
        return (numOfFocusWordNoSpam * 1.0) / numOfNoSpam;
    }

    /**
     *This method calculates conditional spam value and checks calls 'isCounterTrained' method to check whether current wordcounter is trained
     * @return conditional spam value score
     * throws IllegalStateException if we try to use untrained word counter
     */
    public double getConditionalSpam() {
        if (!isCounterTrained()) {
            throw new IllegalStateException();
        }
        return (numOfFocusWordSpam * 1.0) / numOfSpam;
    }
}