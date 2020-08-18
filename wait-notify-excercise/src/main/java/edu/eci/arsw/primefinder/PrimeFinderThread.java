package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	AtomicInteger counter;
	boolean inPause;
	
	private List<Integer> primes;
	
	public PrimeFinderThread(int a, int b, AtomicInteger primesCounter) {
		super();
		this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
		counter = primesCounter;
		inPause =false;
	}

        @Override
	public void run(){
            for (int i= a;i < b;i++){						
                if (isPrime(i)){
					System.out.println(i);
					primes.add(i);
					counter.getAndIncrement();
					while(inPause){
						synchronized (this){
							try {
								wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
                }
            }
	}

	public void pauseSearch() {
	    inPause = true;
    }

    public synchronized  void playSearch(){
		inPause = false;
		notify();
	}
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}

}
