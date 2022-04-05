package com.newrelic.challenge.ports.in;

import com.newrelic.challenge.dto.WordsCountResponse;

import java.io.IOException;
import java.util.List;

public interface ReadAndCountThreeWords {
    List<WordsCountResponse> processFileAndCountThreeWords(List<String> fileName) throws IOException;
}
