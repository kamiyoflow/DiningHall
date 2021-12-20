package com.example.demo;
import org.json.JSONException;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class Waiter implements Runnable{

    private Order order;
    private final int id;
    private final RestTemplate restTemplate = new RestTemplate();
    private static ArrayList<Table> tables = Table.generateTable();
    public static void setTables(ArrayList<Table> tables) {
      Waiter.tables = tables;
    }
    public void setOrder(Order order) {
        this.order = order;
    }

    public Waiter(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ArrayList<Table> getTables() {
        return tables;
    }

    @Override
    public  void run() {
      getTheOrder();
    }

    public void getTheOrder(){

        int min = 2;
        int max = 4;
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);

        for (Table table: tables) {
            if (table.getLock().tryLock()){
                if (table.getTableStatus()==TableStatus.Ready){
                    Table updatedTable = new Table();
                    updatedTable.setTableStatus(TableStatus.Waiting);
                    updatedTable.setTableID(table.getTableID());
                    tables.set(tables.indexOf(table),updatedTable);
                    Waiter.setTables(tables);

                    try {
                        TimeUnit.SECONDS.sleep(random_int);
                        setOrder(Table.generateOrder());
                        order.setTableId(table.getTableID());
                        order.setWaiterId(id);
                        order.setPickUpTime(System.nanoTime());
                        sendOrder(order);
                        System.out.println("order was send");

                    } catch (JSONException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public synchronized void sendOrder(Order order) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Order> entity = new HttpEntity<>(order, headers);
        restTemplate.exchange(
        "http://localhost:8008/order", HttpMethod.POST, entity, Order.class).getBody(); }


    @Override
    public String toString() {
        return "Waiter{" +
                ", id=" + id +
                '}';
    }

}
