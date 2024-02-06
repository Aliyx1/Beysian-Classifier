public class ConfusionMatrix{
    int trueNegative;
    int truePositive;
    int falseNegative;
    int falsePositive;

    /**
     * This constructor keeps 4 values which are number of times our model classified a text of line correctly and incorrectly
     */
    public ConfusionMatrix() {
        this.truePositive = 0;
        this.trueNegative = 0;
        this.falsePositive = 0;
        this.falseNegative = 0;
    }

    //Methods to access each score in other classes
    public int getTrueNegatives() {
        return trueNegative;
    }
    public int getTruePositives() {
        return truePositive;
    }
    public int getFalseNegatives() {
        return falseNegative;
    }
    public int getFalsePositives() {
        return falsePositive;
    }

}