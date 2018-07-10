package haz.approach.uniqueid;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tester {

	private static Integer NO_OF_THREADS = 10000;

	public static void main(String[] args) {
		CountDownLatch startSignal = new CountDownLatch(1);
		final CountDownLatch latch = new CountDownLatch(NO_OF_THREADS);
		HZUniqueIDGenerator hz = new HZUniqueIDGenerator(); 

		ExecutorService executorService = Executors.newFixedThreadPool(500);

		for(int i=0; i<NO_OF_THREADS; i++){
			executorService.execute(new MyExecutor(startSignal, latch, hz));
		}

		long currtime = System.currentTimeMillis();
		System.out.println("Start time - "+currtime);
		startSignal.countDown();
		
		try{
			latch.await(); 
			long currtime2 = System.currentTimeMillis();
			System.out.println("End time - "+currtime2);
			System.out.println("Time taken in ms - "+(currtime2-currtime));


		}catch(InterruptedException ie){
			ie.printStackTrace();
		}
		executorService.shutdown();
		//long val=hz.allPrefixes.get("foo");
		//System.out.println(val);
	}

}

class MyExecutor implements Runnable{
	private final CountDownLatch startSignal;
	private final CountDownLatch latch;
	HZUniqueIDGenerator hz;
	public MyExecutor(CountDownLatch startsignal, CountDownLatch latch, HZUniqueIDGenerator hz) { 
		this.latch = latch;
		this.hz = hz;
		this.startSignal=startsignal;
	}

	@Override
	public void run() {
		try {
			startSignal.await();
			System.out.println("foo_"+hz.generateID("foo") +" thread : "+Thread.currentThread().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}  
		latch.countDown(); 
	}

}