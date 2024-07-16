package util;

public class SentimentAnalyzer {

    public static double analyzeSentiment(String comment) {
        String[] positiveWords = {"good", "great", "excellent", "amazing", "delicious"};
        String[] negativeWords = {"bad", "terrible", "awful", "disgusting", "poor"};

        comment = comment.toLowerCase();
        double score = 0;

        for (String word : positiveWords) {
            if (comment.contains(word)) {
                score += 1;
            }
        }
        for (String word : negativeWords) {
            if (comment.contains(word)) {
                score -= 1;
            }
        }
        return score;
    }
}
