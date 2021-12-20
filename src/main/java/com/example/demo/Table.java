package com.example.demo;
import org.json.JSONException;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Table {

    private TableStatus tableStatus;
    private int tableID;
    public void setTableID(int tableID) {
        this.tableID = tableID;
    }
    public int getTableID() {
        return tableID;
    }
    private static final AtomicInteger counter = new AtomicInteger();
    private final ReentrantLock lock = new ReentrantLock();
    public ReentrantLock getLock() {
        return lock;
    }
    public void setTableStatus(TableStatus tableStatus) {
        this.tableStatus = tableStatus;
    }
    public TableStatus getTableStatus() {
        return tableStatus;
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableStatus=" + tableStatus +
                ", tableID=" + tableID +
                '}';
    }

    public static ArrayList<Table> generateTable(){
        int amount = 8;
        ArrayList<Table> tableArrayList = new ArrayList<>();
        for (int i = 1; i<= amount; i++){
            Table table = new Table();
            table.setTableStatus(TableStatus.Ready);
            table.setTableID(i);
            tableArrayList.add(table);
        }

        return tableArrayList;
    }

    public static Order generateOrder() throws JSONException {

        Order order = new Order();

        int priority = new Random().nextInt(5 - 1 + 1) + 1;
        int item_num = new Random().nextInt(5 - 1 + 1) + 1;

        Integer[] itemsID = new Integer[item_num];

        for (int i = 0; i < itemsID.length; i++){
            itemsID[i] = new Random().nextInt(10 - 1 + 1) + 1;
        }

        ArrayList<Integer>list = Table.getMaxTime(itemsID);

        Integer maxTime = Collections.max(list);

        double max_wait = 1.3 * maxTime;

        order.setOrderId((Table.nextValue()+1));
        order.setItems(itemsID);
        order.setMax_wait(max_wait);
        order.setPriority(priority);

        return order;
    }

    public static ArrayList<Integer> getMaxTime(Integer[] array){
        ArrayList<Integer> list = new ArrayList<>();
        int maxTime = 0;
        for (int i = 0; i<array.length; i++){
            switch (array[i]){
                case 1:
                    maxTime = 20;
                    break;
                case 2:
                    maxTime = 10;
                    break;
                case 3:
                    maxTime = 7;
                    break;
                case 4:
                    maxTime = 32;
                    break;
                case 5:
                    maxTime = 35;
                    break;
                case 6:
                    maxTime = 10;
                    break;
                case 7:
                    maxTime = 20;
                    break;
                case 8:
                    maxTime = 30;
                    break;
                case 9:
                    maxTime = 15;
                    break;
                case 10:
                    maxTime = 15;
                    break;
            }

            list.add(maxTime);
        }
        return list;
    }

    public static int nextValue() {
        return counter.getAndIncrement();
    }

}

