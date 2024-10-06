package ru.zotov.nbkitesttask.controllers;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.zotov.nbkitesttask.dto.UserRequest;
import ru.zotov.nbkitesttask.dto.UserResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserLoadTest {
    private final int SELECT_COUNT = 1_000_000;
    private final int CONNECTIONS_COUNT = 100;
    private final int USERS_COUNT = 100_000;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    static void setUp() {

    }

    @Test
    @Order(1)
    @DisplayName("Save 100000 users through 100 connections(threads)")
    public void saveUsers() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(CONNECTIONS_COUNT);
        List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());

        long totalStartTime = System.currentTimeMillis();

        for(int i = 0; i < USERS_COUNT; i++) {
            executor.submit(() -> {
                long requestStartTime = System.currentTimeMillis();

                UserRequest userRequest = produceRandomUserRequest();
                restTemplate.postForEntity("/user", userRequest, UserResponse.class);

                responseTimes.add(System.currentTimeMillis() - requestStartTime);
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long totalEndTime = System.currentTimeMillis();

        printReport(totalEndTime - totalStartTime, responseTimes);
    }

    @Test
    @Order(2)
    @DisplayName("Select 1000000 users through 100 connections(threads)")
    public void getUsersLoadTest() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(CONNECTIONS_COUNT);
        List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());
        Random random = new Random();

        long totalStartTime = System.currentTimeMillis();

        for (int i = 0; i < SELECT_COUNT; i++) {
            int userId = random.nextInt(USERS_COUNT-1)+1;
            executor.submit(() -> {
                long requestStartTime = System.currentTimeMillis();
                restTemplate.getForEntity("/user/" + userId, String.class);
                long requestEndTime = System.currentTimeMillis();

                responseTimes.add(requestEndTime - requestStartTime);
            });
        }

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.MINUTES);

        long totalEndTime = System.currentTimeMillis();

        printReport(totalEndTime - totalStartTime, responseTimes);
    }

    private UserRequest produceRandomUserRequest() {
        String generatedString = RandomStringUtils.random(10, true, false);

        return new UserRequest(
                generatedString,
                generatedString,
                generatedString + "@mail.com",
                30
        );
    }

    private long getMedian(List<Long> responseTimes) {
        return responseTimes.get(responseTimes.size()/2);
    }

    private long get95Percentile(List<Long> responseTimes) {
        return responseTimes.get((int) (responseTimes.size()*0.95));
    }

    private long get99Percentile(List<Long> responseTimes) {
        return responseTimes.get((int) (responseTimes.size()*0.99));
    }

    private void printReport(long totalTime, List<Long> responseTimes) {
        Collections.sort(responseTimes);

        System.out.println("-----Result-----");
        System.out.printf("Total time (ms): %d%n", totalTime);
        System.out.printf("Median (ms): %d%n", getMedian(responseTimes));
        System.out.printf("95 Percentile (ms): %d%n", get95Percentile(responseTimes));
        System.out.printf("99 Percentile (ms): %d%n", get99Percentile(responseTimes));
    }
}
