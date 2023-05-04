import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class IndividualRequestSimulation {

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
        double compileTime = 1.0 / 50;
        double networkTime = 0.5;
        double indexFileSize = 100;

        FileWriter fw = new FileWriter("output.txt");
        BufferedWriter bw = new BufferedWriter(fw);

        // For each year to be examined
        int bcAge = 50;
        ArrayList<Double> splits = new ArrayList<Double>();

        // For each search time to be examined
        for (int searchLength = 5; searchLength < 51; searchLength += 5) {
            bw.write("\n");
            for (int run = 1; run <= 30; run++) {
                // Make sure year not negative, if search time greater than bcAge
                double compiled = 0.0;
                for (int split = bcAge - searchLength; split < bcAge; split += 5) {
                    double avgSearchTime = 0;
                    double historicalSearchTime = 0.0;
                    double currentSearchTime = 0.0;
                    for (int year = split; year < split + 5; year++) {
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
                            historicalSearchTime += extractTime * size + exportTime * size;
                            if (searchLength >= 10)
                                compiled += size;
                        }
                    }
                    avgSearchTime += Math.max(currentSearchTime, historicalSearchTime);
                    avgSearchTime += searchLength <= 5 ? 0 : (networkTime * 2);
                    avgSearchTime += compiled * compileTime;
                    avgSearchTime += (indexFileSize * extractTime);
                    splits.add(avgSearchTime);
                }
                double totalTime = Collections.max(splits);
                if (run == 1)
                    bw.write("\t" + totalTime + "\t");
                else
                    bw.write(totalTime + "\t");
                splits.clear();
            }

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