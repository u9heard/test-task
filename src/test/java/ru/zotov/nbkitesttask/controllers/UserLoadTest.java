package ru.zotov.nbkitesttask.controllers;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import ru.zotov.nbkitesttask.dto.UserRequest;
import ru.zotov.nbkitesttask.dto.UserResponse;
import ru.zotov.nbkitesttask.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserLoadTest {
    private final int SELECT_COUNT = 1_000_000;
    private final int CONNECTIONS_COUNT = 100;
    private final int USERS_COUNT = 100_000;

    private static final RestTemplate restTemplate = new RestTemplate();

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
                restTemplate.postForEntity("http://localhost:8888/user", userRequest, UserResponse.class);

                responseTimes.add(System.currentTimeMillis() - requestStartTime);
            });
        }

        long totalEndTime = System.currentTimeMillis();

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        printReport(totalEndTime - totalStartTime, responseTimes);
    }

    @Test
    @Order(2)
    @DisplayName("Select 1000000 users through 100 connections(threads)")
    public void getUsersLoadTest() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());
        Random random = new Random();

        long totalStartTime = System.currentTimeMillis();

        for (int i = 0; i < SELECT_COUNT; i++) {
            int userId = random.nextInt(USERS_COUNT-1)+1;
            executor.submit(() -> {
                long requestStartTime = System.currentTimeMillis();
                restTemplate.getForEntity("http://localhost:8888/user/" + userId, String.class);
                long requestEndTime = System.currentTimeMillis();

                responseTimes.add(requestEndTime - requestStartTime);
            });
        }

        long totalEndTime = System.currentTimeMillis();

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
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

        long median = getMedian(responseTimes);
        long percentile95 = get95Percentile(responseTimes);
        long percentile99 = get99Percentile(responseTimes);

        System.out.println("-----Result-----");
        System.out.println(responseTimes.size());
        System.out.printf("Total time (ms): %d%n", totalTime);
        System.out.printf("Median (ms): %d%n", median);
        System.out.printf("95 Percentile (ms): %d%n", percentile95);
        System.out.printf("99 Percentile (ms): %d%n", percentile99);
    }
}
