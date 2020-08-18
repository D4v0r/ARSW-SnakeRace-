/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;
    private final int NDATA = MAXVALUE / NTHREADS;

    private ArrayList<PrimeFinderThread> threads;
    private AtomicInteger primesCounter;
    
    private Control() {
        super();
        threads = new ArrayList<>();
        primesCounter = new AtomicInteger();

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            threads.add( new PrimeFinderThread(i*NDATA, (i+1)*NDATA, primesCounter));
        }
        threads.add(new PrimeFinderThread(i*NDATA, MAXVALUE + 1, primesCounter));
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        threads.forEach(Thread::start);
        long startTime = System.currentTimeMillis();
        while(threads.stream().anyMatch(Thread::isAlive)){
                        if(System.currentTimeMillis() - startTime >= TMILISECONDS){
                            threads.forEach(PrimeFinderThread::pauseSearch);
                            System.out.println("Has found " + primesCounter + " primes until this moment.");
                            System.out.println("> press ENTER to continue ...");
                            Scanner input = new Scanner(System.in);
                            String key = input.nextLine();
                            while(!key.isEmpty()){
                                System.out.println("> press ENTER to continue ...");
                                key = input.nextLine();
                            }
                            threads.forEach(PrimeFinderThread::playSearch);
                            startTime = System.currentTimeMillis();
                        }
        }
    }
}
    

