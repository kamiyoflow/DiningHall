package com.example.demo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import static com.example.demo.DinningHall.getWaiters;
import static com.example.demo.Waiter.getTables;

@RestController
@SpringBootApplication
@Service
public class DinningHallController{

	private Order order;
	private static ArrayList<Order> receivedOrders = new ArrayList<>();
	private static ArrayList<Waiter> waiters = getWaiters();
	private static ArrayList<Table> dtables =  getTables();


	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}


	@RequestMapping(value = "/post", produces = "application/json", method=RequestMethod.POST)
	public Order getData(@RequestBody Order order) {
		return order ;
	}


	@RequestMapping( value = "/distribution", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	public synchronized void postOrder(@RequestBody Order order){
		this.order = order;
		receivedOrders.add(this.order);
	}

	@RequestMapping(value = "/distribution", consumes = "application/json", produces = "application/json", method = RequestMethod.GET)
	public static ArrayList<Order> getOrder(){
		System.out.println("List size:"+ receivedOrders.size());
		distributeOrders();
		return receivedOrders;
	}

	public static void distributeOrders(){

		while (receivedOrders.isEmpty()){}
		long totalTime;
		ArrayList<Integer> marks = new ArrayList<>();
		for (Order order: receivedOrders) {
			for (Waiter waiter: waiters) {
				if(order.getWaiterId() == waiter.getId()){
					for (Table table: dtables) {
						if(order.getTableId()==table.getTableID()){
							long servingTime =  System.nanoTime();
							totalTime = order.getPickUpTime() - servingTime;
							marks.add(calculateMark(totalTime,order));
							Table updatedTable = new Table();
							updatedTable.setTableStatus(TableStatus.Ready);
							updatedTable.setTableID(table.getTableID());
							dtables.set(dtables.indexOf(table),updatedTable);
							Waiter.setTables(dtables);

						}
					}
				}
			}
		}
		System.out.println("Average restaurant mark:" + calculateAverage(marks));
	}

	public static int calculateMark(long totalTime, Order order){
		double time = (double) totalTime + order.getCooking_time();
		int mark;
		if(time <= order.getMax_wait()){
			mark = 5;

		}else if (time <= order.getMax_wait() * 1.1)
		{ mark = 4;

		}else if (time <= order.getMax_wait() * 1.2){
			mark = 3;


		}else if(time <= order.getMax_wait() * 1.3){
			mark = 2;

		}else if(time <= order.getMax_wait() * 1.4){
			mark = 1;

		}else{
			mark = 0;

		}
		return mark;
	}

	public static Integer calculateAverage(ArrayList<Integer> marks){
		int sum = 0;
		for (Integer mark: marks) {
			sum+=mark;
		}
		return sum/marks.size();
	}


}
