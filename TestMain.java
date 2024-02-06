import java.io.File;
import java.io.IOException;

public class TestMain
{

	public static void main(String [] args) throws IOException
	{
		testClassification();

	}


	public static void testClassification() throws IOException {
		String [] words = {"good", "bad"};
		NaiveBayes nb = new NaiveBayes(words);
		nb.trainClassifier(new File("traindata.txt"));
		ConfusionMatrix cm = nb.computeAccuracy(new File("testdata.txt"));
		System.out.println(cm.getTruePositives());
		System.out.println(cm.getFalsePositives());
		System.out.println(cm.getTrueNegatives());
		System.out.println(cm.getFalseNegatives());
	}


	
}
