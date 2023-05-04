import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OldIndividualRequestSimulation {

    public static void main(String[] args) throws IOException {
        simulation();
    }

    public static void simulation() throws IOException {
        // Inputs
        int visitsPerYearLB = 1;
        int visitsPerYearUB = 7;
        double imageSizeLB = 1.0;
        double imageSizeUB = 3.0;
        int imageCountLB = 1;
        int imageCountUB = 5;
        double imageProb = 0.05;
        double textSizeLB = 0.003;
        double textSizeUB = 0.004;
        double growthRate = 1.03;
        double extractTime = 1.0 / 50;
        double exportTime = 1.0 / 60;
        double networkTime = 0.5;
        double indexFileSize = 100;

        FileWriter fw = new FileWriter("output.txt");
        BufferedWriter bw = new BufferedWriter(fw);

        // For each year to be examined
        int bcAge = 50;

        bw.write("Blockchain Age: " + bcAge + "\n");
        // For each search time to be examined
        for (int searchLength = 5; searchLength < 51; searchLength += 5) {
            for (int run = 1; run <= 30; run++) {
                double avgSearchTime = 0;
                double historicalSearchTime = 0.0;
                double currentSearchTime = 0.0;
                // Make sure year not negative, if search time greater than bcAge
                int check = Math.max(bcAge - searchLength, 0);
                for (int year = check; year < bcAge; year++) {
                    double size = 0.0;
                    // For each visit
                    int visits = getRandom(visitsPerYearLB, visitsPerYearUB);
                    for (int visit = 0; visit < visits; visit++) {
                        if (Math.random() <= imageProb) {
                            int images = getRandom(imageCountLB, imageCountUB);
                            for (int image = 0; image < images; image++) {
                                size += getRandom(imageSizeLB, imageSizeUB) * Math.pow(growthRate, year / 5);
                            }
                        }
                        size += getRandom(textSizeLB, textSizeUB) * Math.pow(growthRate, year / 5);
                    }
                    // Check for searching in current blockchain
                    if (bcAge - year <= 5) {
                        currentSearchTime += extractTime * size;
                    } else {
                        historicalSearchTime += extractTime * size
                                + exportTime * size;
                    }
                }
                avgSearchTime += Math.max(currentSearchTime, historicalSearchTime);
                avgSearchTime += searchLength <= 5 ? 0 : networkTime;
                avgSearchTime += searchLength <= 5 ? (indexFileSize * extractTime)
                        : searchLength <= bcAge ? (Math.ceil(searchLength / 5.0) - 1) * (indexFileSize * extractTime)
                                : (Math.ceil(bcAge / 5.0) - 1) * (indexFileSize * extractTime);
                bw.write(avgSearchTime + "\t");
            }
            bw.write("\n");
        }
        // }
        bw.close();
        fw.close();
    }

    public static int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static double getRandom(double min, double max) {
        return (double) (Math.random() * (max - min) + min);
    }
}