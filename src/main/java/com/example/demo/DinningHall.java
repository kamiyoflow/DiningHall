package com.example.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.ArrayList;

@SpringBootApplication

public class DinningHall {

    public static ArrayList<Waiter> getWaiters() {
        return generateWaiters();
    }

    public static ArrayList<Waiter> generateWaiters() {
        ArrayList<Waiter> waiters = new ArrayList<>();
        int amount = 4;
        for (int i = 0; i < amount; i++) {
            Waiter waiter = new Waiter(i);
            waiters.add(waiter);
        }
        return waiters;

    }

    public static void main(String[] args) {

        SpringApplication.run(DinningHall.class, args);
        while (true){
            for (Waiter waiter:generateWaiters()) {
                Thread thread = new Thread(waiter);
                thread.start();
            }
        }

    }
}
