import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class NaiveBayes {
    private int numOfNoSpam;
    private int numOfSpam;
    private double spamScore;
    private double noSpamScore;

    private ArrayList<WordCounter> counters;

    //initializing the constructor and the variables in it (also creating as many WordCounter objects as we need)

    /**
     * This constructor initiates arrayList with all WordCounter objects. Also stores value of spam and non spam scores, number of spam and non spam words
     * @param focusWord focus words provided
     */

    public NaiveBayes(String[] focusWord) {
        counters = new ArrayList<>();
        numOfNoSpam = 0;
        numOfSpam = 0;
        spamScore = 0;
        noSpamScore = 0;
        for (String s : focusWord) {
            counters.add(new WordCounter(s));
        }

    }

    /**
     *This method is adding training samples to our naiveBayes object
     * @param document document which is provided with sample lines of classified text
     */
    public void addSample(String document) {

        //adding sample document to each WordCounter
        for (WordCounter wordCounter : counters) {
            wordCounter.addSample(document);
        }

        //splitting words of string document and adding them to string array to easily read it

        String[] docList = document.split(" ");

        //counting number of spam and non spam words
        if (Integer.parseInt(docList[0]) == 1) {
            numOfSpam++;
        } else {
            numOfNoSpam++;
        }
    }


    /**
     * This method gives classification to each string line provided
     *
     * @param unclassifiedDocument document provided with lines of text which need to be classified
     * @return returns boolean value whether we consider a given line of text as a spam or not spam
     */
    public boolean classify(String unclassifiedDocument) {
        //Calculating a spam and a non spam score for a line which doesn't contain a focusword
        noSpamScore = (numOfNoSpam * 1.0) / (numOfNoSpam + numOfSpam);
        spamScore = (numOfSpam * 1.0) / (numOfSpam + numOfNoSpam);

        //splitting words of string unclassifiedDocument and adding them to string array to easily read it
        String[] splitArray = unclassifiedDocument.split(" ");

        //Calculating a new spam and a non spam score for a line which contains a focusword
        for (String s : splitArray) {
            for (WordCounter w : counters) {
                if (s.equals(w.getFocusWord())) {
                    this.spamScore = spamScore * w.getConditionalSpam();
                    this.noSpamScore = noSpamScore * w.getConditionalNoSpam();
                }
            }
        }

        //If spam score > no spam score, then the line classifies as spam
        return spamScore > noSpamScore;
    }

    //Reading file and calling addSample method for each line to train classifier

    /**
     * This method reads given file and calls addSample method for each line to train classifier
     * @param trainingFile a file provided with a text to train our classifier
     * @throws IOException this method might throw and InOrOut exception if there's a problem with reading the file
     */
    public void trainClassifier(File trainingFile) throws IOException {
        try (Scanner scanner = new Scanner(trainingFile)){
            while (scanner.hasNext()) {
                String row = scanner.nextLine();
                addSample(row);
            }
        }
    }

    //Reading file to classify each line in it as spam/noSpam and writing the results in the output file

    /**
     * This method reads given file (lines of string) and prints classification in the output file
     * @param input a file with a text data which needs to be classified
     * @param output a file with a data which has been already classified with our 'AI'
     * @throws IOException might trow an exception if there's some problem with reading our outputting files
     */
    public void classifyFile(File input, File output) throws IOException {
        //Creating scanner and a writer
        try (Scanner scanner = new Scanner(input); PrintWriter writer = new PrintWriter(output)) {

            //Reading file input and writing result in file output
            while (scanner.hasNext()) {
                String row = scanner.nextLine();
                if (classify(row)) {
                    writer.println("1");
                } else {
                    writer.println("0");
                }

            }
        }
    }

    /**
     * This method valuates the accuracy of our algorithm
     * @param testdata a file provided with a test data for our model
     * @return returns a ConfusionMatrix object with saved test result of classification algorithm
     * @throws IOException might throw an exception if there's some problem with reading of a file
     */
    public ConfusionMatrix computeAccuracy(File testdata) throws IOException {
        //Creating a confusion matrix
        ConfusionMatrix conf = new ConfusionMatrix();

        //Creating scanner to read a file with the testdata
        try(Scanner scanner = new Scanner(testdata)) {
            while (scanner.hasNext()) {

                //Splitting each line in testdata file into words and adding them to a new string array
                String row = scanner.nextLine();
                String[] arraySplit = row.split(" ");



                //Counting number of times our algorithm gives a right and wrong classifications
                if (arraySplit[0].equals("1") && classify(row)) {
                    conf.truePositive += 1;
                } else if (arraySplit[0].equals("1") && !classify(row)) {
                    conf.falseNegative += 1;
                } else if (arraySplit[0].equals("0") && !classify(row)) {
                    conf.trueNegative += 1;
                } else if (arraySplit[0].equals("0") && classify(row)) {
                    conf.falsePositive += 1;
                }
            }
        }
        return conf;
    }
}