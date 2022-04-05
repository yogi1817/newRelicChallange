package com.newrelic.challenge.endpoints;

import com.newrelic.challenge.api.NewrelicApiDelegate;
import com.newrelic.challenge.dto.WordsCountRequest;
import com.newrelic.challenge.dto.WordsCountResponse;
import com.newrelic.challenge.ports.in.ReadAndCountThreeWords;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChallengeController implements NewrelicApiDelegate {

    private final ReadAndCountThreeWords readAndCountThreeWords;

    @Override
    public ResponseEntity<List<WordsCountResponse>> processFileAndCountThreeWords(WordsCountRequest wordsCountRequest) {
        try {
            return ResponseEntity.ok(readAndCountThreeWords.processFileAndCountThreeWords(wordsCountRequest.getFileNames()));
        } catch (IOException e) {
            log.error("Error reading file "+ e.getMessage());
        }

        return ResponseEntity.badRequest().build();
    }
}
