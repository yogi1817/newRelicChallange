package com.newrelic.challenge.config;

import com.newrelic.challenge.exception.InvalidRequestException;
import com.newrelic.challenge.ports.in.ReadAndCountThreeWords;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class CommandLine implements CommandLineRunner {

    private final ReadAndCountThreeWords readAndCountThreeWords;

    /**
     * Reading the arguments from command line run
     * Converts them into string and send it to service layer for processing
     * @param args - Input from command line
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        /*if (args == null || args.length == 0) {
            processInputFromCommandLine();
        }else if (args != null && args.length > 0) {*/
            try{
                readAndCountThreeWords.processFileAndCountThreeWords(Arrays.stream(args).collect(Collectors.toList()))
                        .forEach(wordsCountResponse -> {
                                System.out.println();
                                System.out.println("Printing file "+ wordsCountResponse.getFileName());
                                wordsCountResponse.getWordsAndCount().forEach(keyValuePair ->
                                        System.out.println(String.format("%-25s | %d",keyValuePair.getWord(), keyValuePair.getCount())));
                        });
            }catch (InvalidRequestException invalidRequestException){
                log.error("Not input passed in commandline");
            }
        /*}*/
    }

    //Commenting out this code because Spring boot will stop at Stdin and will not run untill an input is provided.
    //This can be achieved if I will use plain java and not spring boot framework and this will stop the spring boot run.
/*    private void processInputFromCommandLine(){
        System.out.println("Enter File to be scanned");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        try {
            readAndCountThreeWords.processFileAndCountThreeWords(List.of(line))
                    .get(0).getWordsAndCount().forEach(keyValuePair ->
                            System.out.println(String.format("%-25s | %d",keyValuePair.getWord(), keyValuePair.getCount())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
