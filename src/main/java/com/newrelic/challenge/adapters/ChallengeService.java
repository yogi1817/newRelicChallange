package com.newrelic.challenge.adapters;

import com.newrelic.challenge.dto.KeyValuePair;
import com.newrelic.challenge.dto.WordsCountResponse;
import com.newrelic.challenge.exception.InvalidRequestException;
import com.newrelic.challenge.ports.in.ReadAndCountThreeWords;
import lombok.extern.slf4j.Slf4j;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
public class ChallengeService implements ReadAndCountThreeWords {

    /**
     * This method doe the following
     * 1. read all files one at a time and line by line
     * 2. Parse the line and removes punctuations, quotes and spaces
     * 3. Concat 3 words into a string and adds it to the map with count 1
     * 4. If ay any time it finds the same word it increments the counter
     * 5. Filter top 100 and print them and return them back as response
     * @param fileNameList - List of file names to process
     * @return - List of files with the words and counts
     * @throws IOException - Throws wxception is no file is present
     */
    @Override
    public List<WordsCountResponse> processFileAndCountThreeWords(List<String> fileNameList) throws IOException {
        if(CollectionUtils.isEmpty(fileNameList)){
            throw new InvalidRequestException("Empty file list sent" , "");
        }

        List<String> wordsToProcess = new ArrayList<>();
        List<WordsCountResponse> wordsCountResponseList = new ArrayList<>();
        for (String filePath : fileNameList) {
            Path path = Paths.get(filePath);
            Map<String, Integer> threeWordsCountMap = new HashMap<>();
            try (Stream<String> stream = Files.lines(path)) {
                stream.forEach(s -> {

                    String lineWithoutPunctuationQuotesAndSpaces = formatReadLine(s);
                    if(!StringUtils.isEmpty(lineWithoutPunctuationQuotesAndSpaces)){
                        wordsToProcess.addAll(Arrays.asList(lineWithoutPunctuationQuotesAndSpaces.split(" ")));

                        while (wordsToProcess.size() >= 3) {
                            insertCountIntoMap(threeWordsCountMap, joinThreeWords(wordsToProcess.get(0), wordsToProcess.get(1), wordsToProcess.get(2)));
                            //By removing the first element we are still saving the last 2 elements
                            //This will help find 3rd word in new row.
                            wordsToProcess.remove(0);
                        }
                    }

                });
            }

            printAndSetResponse(wordsCountResponseList, filePath, threeWordsCountMap);
        }
        return wordsCountResponseList;
    }

    /**
     * This method removes all punctuations and also removes double quotes
     * @param nextLineRead - Line read by stream
     * @return - Return replaced string without punctuations
     */
    private String formatReadLine(String nextLineRead) {
        return nextLineRead.replaceAll("(\\p{Punct})", "")
                .replace("”"," ")
                .replace("“"," ")
                .replace("\""," ")
                .replaceAll(" +", " ")
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    /**
     * This method iterate through the Map and extract 100 records and print them and send it back as response
     * @param wordsCountResponseList - file with the words and counts
     * @param filePath - File name
     * @param threeWordsCountMap - Map to track of count
     */
    private void printAndSetResponse(List<WordsCountResponse> wordsCountResponseList, String filePath, Map<String, Integer> threeWordsCountMap) {
        WordsCountResponse wordsCountResponse = new WordsCountResponse();
        wordsCountResponse.setFileName(filePath);
        log.debug("Printing for file --> {}",  filePath);
        threeWordsCountMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(100)
                .forEach(entry -> {
                    log.debug(String.format("%-25s | %d",entry.getKey(), entry.getValue()));
                    wordsCountResponse.addWordsAndCountItem(new KeyValuePair()
                            .word(entry.getKey())
                            .count(entry.getValue()));
                });
        wordsCountResponseList.add(wordsCountResponse);
    }

    /**
     *
     * Return concat string of 3 words using a space between words
     * @param word1 First word in the three word string
     * @param word2 Second word in the three word string
     * @param word3 Third word in the three word string
     * @return Append 3 words
     */
    private String joinThreeWords(String word1, String word2, String word3) {
        return word1 + " " + word2 + " " + word3;
    }

    /**
     * Method to keep trac of repeated 3 words strings and there count
     * @param threeWordsCountMap - Map to track count of 3 words strings
     * @param threeWords - 3 word string to check and update count
     */
    private void insertCountIntoMap(Map<String, Integer> threeWordsCountMap, String threeWords) {
        if (threeWordsCountMap.containsKey(threeWords)) {
            threeWordsCountMap.put(threeWords, threeWordsCountMap.get(threeWords) + 1);
        } else {
            threeWordsCountMap.put(threeWords, 1);
        }
    }
}
