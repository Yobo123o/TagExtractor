import java.io.*;
import java.util.*;

/**
 * TagExtractorLogic handles reading files, filtering words, counting tag frequency,
 * and saving output to a file.
 */
public class TagExtractorLogic {

    /**
     * Extracts tags from the input file while filtering out stop words.
     * @param inputFile The file to process
     * @param stopWordsFile The stop word list
     * @return Map of tags and their frequency
     */
    public static Map<String, Integer> extractTags(File inputFile, File stopWordsFile) {
        Set<String> stopWords = loadStopWords(stopWordsFile);
        Map<String, Integer> tagMap = new TreeMap<>();

        try (Scanner in = new Scanner(inputFile)) {
            in.useDelimiter("[^a-zA-Z]+");
            while (in.hasNext()) {
                String word = in.next().toLowerCase();
                if (!stopWords.contains(word) && !word.isEmpty()) {
                    tagMap.put(word, tagMap.getOrDefault(word, 0) + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tagMap;
    }

    /**
     * Loads stop words from a file into a Set.
     * @param file The stop word file
     * @return A Set of stop words
     */
    public static Set<String> loadStopWords(File file) {
        Set<String> stopWords = new TreeSet<>();
        try (Scanner in = new Scanner(file)) {
            while (in.hasNextLine()) {
                stopWords.add(in.nextLine().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stopWords;
    }

    /**
     * Saves the tag frequency output to a file.
     * @param text The output to write
     * @param file The file to write to
     * @return true if successful
     */
    public static boolean saveTags(String text, File file) {
        try (PrintWriter out = new PrintWriter(file)) {
            out.print(text);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
