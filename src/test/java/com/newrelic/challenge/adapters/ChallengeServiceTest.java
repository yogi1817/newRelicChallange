package com.newrelic.challenge.adapters;

import com.newrelic.challenge.dto.WordsCountResponse;
import com.newrelic.challenge.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

    @MockBean
    private ChallengeService challengeService = new ChallengeService();

    @Test
    void countThreeLetterWords_When_ThirdWordIsInNextLine() throws IOException {
        List<String> input = List.of("src/test/resources/input/TestCase1.txt");
        List<WordsCountResponse> wordsCountResponseList = challengeService.processFileAndCountThreeWords(input);
        assertEquals(wordsCountResponseList.get(0).getFileName(), "src/test/resources/input/TestCase1.txt");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getWord(),
                "the project gutenberg");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getCount(),
                2);
    }

    @Test
    void countThreeLetterWords_When_CaseInsensitive() throws IOException {
        List<String> input = List.of("src/test/resources/input/TestCase1.txt");
        List<WordsCountResponse> wordsCountResponseList = challengeService.processFileAndCountThreeWords(input);
        assertEquals(wordsCountResponseList.get(0).getFileName(), "src/test/resources/input/TestCase1.txt");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getWord(),
                "the project gutenberg");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getCount(),
                2);
    }

    @Test
    void countThreeLetterWords_When_ApostrophePresents_DoNotCountIfApostropheHasLeadingLetters() throws IOException {
        List<String> input = List.of("src/test/resources/input/TestCase2.txt");
        List<WordsCountResponse> wordsCountResponseList = challengeService.processFileAndCountThreeWords(input);
        assertEquals(wordsCountResponseList.get(0).getFileName(), "src/test/resources/input/TestCase2.txt");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getWord(),
                "the project gutenberg");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getCount(),
                2);
    }

    @Test
    void countThreeLetterWords_When_ApostrophePresents_IgnoreIfWordEndsWithPunctuations () throws IOException {
        List<String> input = List.of("src/test/resources/input/TestCase3.txt");
        List<WordsCountResponse> wordsCountResponseList = challengeService.processFileAndCountThreeWords(input);
        assertEquals(wordsCountResponseList.get(0).getFileName(), "src/test/resources/input/TestCase3.txt");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getWord(),
                "the project gutenberg");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getCount(),
                6);
    }

    @Test
    void countThreeLetterWords_When_QuotesPresents_IgnoreQuotesAndSpaces () throws IOException {
        List<String> input = List.of("src/test/resources/input/TestCase4.txt");
        List<WordsCountResponse> wordsCountResponseList = challengeService.processFileAndCountThreeWords(input);
        assertEquals(wordsCountResponseList.get(0).getFileName(), "src/test/resources/input/TestCase4.txt");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getWord(),
                "the project gutenberg");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getCount(),
                10);
    }

    @Test
    void countThreeLetterWords_When_HyphensPresents_IfLettersPresentAfterHyphensConsiderANewWord () throws IOException {
        List<String> input = List.of("src/test/resources/input/TestCase5.txt");
        List<WordsCountResponse> wordsCountResponseList = challengeService.processFileAndCountThreeWords(input);
        assertEquals(wordsCountResponseList.get(0).getFileName(), "src/test/resources/input/TestCase5.txt");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getWord(),
                "the project gutenberg");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getCount(),
                10);
    }

    @Test
    void countThreeLetterWords_When_HyphensPresents_IfHyphenIsLastLetterCountIt () throws IOException {
        List<String> input = List.of("src/test/resources/input/TestCase6.txt");
        List<WordsCountResponse> wordsCountResponseList = challengeService.processFileAndCountThreeWords(input);
        assertEquals(wordsCountResponseList.get(0).getFileName(), "src/test/resources/input/TestCase6.txt");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getWord(),
                "the project gutenberg");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getCount(),
                11);
    }

    @Test
    void countThreeLetterWords_When_BigFileRead_VerifyMostOccurredCount () throws IOException {
        List<String> input = List.of("src/test/resources/input/TestCase7.txt");
        List<WordsCountResponse> wordsCountResponseList = challengeService.processFileAndCountThreeWords(input);
        assertEquals(wordsCountResponseList.get(0).getFileName(), "src/test/resources/input/TestCase7.txt");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getWord(),
                "the sperm whale");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().get(0).getCount(),
                86);
    }

    @Test
    void countThreeLetterWords_When_BigFileRead_VerifyOnly100RecordsPresent () throws IOException {
        List<String> input = List.of("src/test/resources/input/TestCase7.txt");
        List<WordsCountResponse> wordsCountResponseList = challengeService.processFileAndCountThreeWords(input);
        assertEquals(wordsCountResponseList.get(0).getFileName(), "src/test/resources/input/TestCase7.txt");
        assertEquals(wordsCountResponseList.get(0).getWordsAndCount().size(), 100);
    }

    @Test
    void countThreeLetterWords_When_ReadFiles_BothFilePresentInResponse () throws IOException {
        List<String> input = Arrays.asList("src/test/resources/input/TestCase7.txt", "src/test/resources/input/TestCase6.txt");
        List<WordsCountResponse> wordsCountResponseList = challengeService.processFileAndCountThreeWords(input);
        assertEquals(wordsCountResponseList.get(0).getFileName(), "src/test/resources/input/TestCase7.txt");
        assertEquals(wordsCountResponseList.get(1).getFileName(), "src/test/resources/input/TestCase6.txt");
    }

    @Test
    void countThreeLetterWords_When_InvalidFile_ThrowsIOException() {
        List<String> input = List.of("invalidPath");
        assertThrows(IOException.class,
                ()-> challengeService.processFileAndCountThreeWords(input));
    }

    @Test
    void countThreeLetterWords_When_ReadEmptyFiles_ReturnsFileNameAndNullCounts () throws IOException {
        List<String> input = List.of("src/test/resources/input/TestCase8.txt");
        List<WordsCountResponse> wordsCountResponseList = challengeService.processFileAndCountThreeWords(input);
        assertEquals(wordsCountResponseList.get(0).getFileName(), "src/test/resources/input/TestCase8.txt");
        assertNull(wordsCountResponseList.get(0).getWordsAndCount());
    }

    @Test
    void countThreeLetterWords_When_ReadFileWithOneWordOnly_ReturnsFileNameAndNullCounts () throws IOException {
        List<String> input = List.of("src/test/resources/input/TestCase9.txt");
        List<WordsCountResponse> wordsCountResponseList = challengeService.processFileAndCountThreeWords(input);
        assertEquals(wordsCountResponseList.get(0).getFileName(), "src/test/resources/input/TestCase9.txt");
        assertNull(wordsCountResponseList.get(0).getWordsAndCount());
    }

    @Test
    void countThreeLetterWords_When_NoFilePathSent_ReturnsNullResponse () throws IOException {
        List<String> input = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                ()-> challengeService.processFileAndCountThreeWords(input));
    }
}
