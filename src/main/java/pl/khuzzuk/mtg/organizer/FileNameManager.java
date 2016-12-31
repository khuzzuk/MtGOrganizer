package pl.khuzzuk.mtg.organizer;

public class FileNameManager {
    public static String getFileName(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("number can't be negative");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("db/pics/");
        for (int n = number*10; n < 1_000_000; n*=10) {
            builder.append("0");
        }
        builder.append(number);
        builder.append(".jpg");
        return builder.toString();
    }
}
