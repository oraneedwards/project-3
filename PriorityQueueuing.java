/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package priorityqueuing;

/**
 *
 * @author Renee
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.text.*;
import java.util.logging.Level;
import java.util.logging.Logger;




public class PriorityQueuing {
    
 
    static double serviceRate = 0.0005;
    static Customer one = new Customer(0.0, 600.0, 0, new ArrayList(), 1);
    static Customer two = new Customer(0.0, 500.0, 0, new ArrayList(), 2);

    // A data structure to store customers.
    static LinkedList<Customer> queue;
    static LinkedList<Customer> queue2;
    // A data structure for simulation: list of forthcoming events.
    static PriorityQueue<Event> eventList;

    // The system clock, which we'll advance from event to event.
    static double clock;
    static double clock2;
    // Report
    static int numArrivals = 0; 
    static int numArrivals1 = 0; // How many arrived?
    static int numArrivals2 = 0; 
    static int numDepartures = 0;
    static int numDepartures1 =0;   //How many left input source 1
    static int numDepartures2 =0; // How many left input source 2
    static double totalWaitTime, avgWaitTime;      // For time spent in queue.
    static double totalSystemTime, avgSystemTime;  // For time spent in system.
  

    void init (Customer c)
    {
        queue = new LinkedList<Customer> ();
        queue2 = new LinkedList<Customer> ();
        eventList = new PriorityQueue<Event> ();
        
        clock = 0.0;
        clock2 = 0.0;
        numArrivals = numDepartures = 0;
        totalWaitTime = totalSystemTime = 0.0;
        c.arrivalTime = 0.0;
        c.numDepartures = 0;
        c.responseTime = new  ArrayList<Double> ();
        nextArrival (c);
    }


    void sim (int maxCustomers, Customer c)
    {
        init (c);

        while (numArrivals < 1000) 
        {
           
           Event e = eventList.poll ();
          
            clock = e.eventTime;
             
            
            if (e.type == Event.ARRIVAL) {
                processArrival (e, c);
                }
            
            else {
                processDeparture (e);
            }
         }
        

       report ();
    }

   

    void processArrival (Event e, Customer c)
    {
	

        if(c.getType() == 1)
        {
         
          one.arrivalTime = clock;
          queue.add(one);
          numArrivals ++;
          numArrivals1++;
       //System.out.println(queue2.size());
          if (queue.size() == 1) {
	     //This is the only customer => schedule a departure.
	    nextDeparture ();
	 }
          nextArrival (c);

	}


        
        if(c.getType() == 2)
        {
           //two = new Customer(clock, c.getArrivalRate(), c.getNumDepartures(), c.getresponseTime(), 2);
            two.arrivalTime = clock;
            queue2.add (two);
            numArrivals ++;
            numArrivals2++;
            if (queue.size() == 2) {
	     //This is the only customer => schedule a departure.
	    nextDeparture ();
	    }
            nextArrival (c);
           //System.out.println(queue2.size());

	}



    }


    static void processDeparture (Event e)
    {
	
        
        if(!queue.isEmpty())
        {
            Customer c = queue.removeFirst ();
            numDepartures ++;
            numDepartures1 ++;

            
            double timeInSystem = clock - c.getArrivalTime();
            one.getresponseTime().add(timeInSystem);
     
       
        // Maintain total (for average, to be computed later).
           // totalSystemTime += timeInSystem;
              nextDeparture ();
            
        }
        
	if(!queue2.isEmpty())
        {
            Customer c2 = queue2.removeFirst ();
            numDepartures ++;
            numDepartures2 ++;
            
            double timeInSystem = clock2 - c2.getArrivalTime();
            two.getresponseTime().add(timeInSystem);
            
       
        // Maintain total (for average, to be computed later).
            //totalSystemTime += timeInSystem;
             nextDeparture ();
            
        }
        
    }


    static void nextArrival (Customer c)
    {
	// The next arrival occurs when we add an interrarrival to the the current time.
	double nextArrivalTime = clock + randomInterarrivalTime(c.arrivalRate);
	eventList.add (new Event (nextArrivalTime, Event.ARRIVAL));
        //System.out.println("Arrival" + c.type + "\t" + eventList.size());
    
    }
    static void nextDeparture ()
    {
	double nextDepartureTime = clock + serviceRate;
	eventList.add (new Event (nextDepartureTime, Event.DEPARTURE));
         //System.out.println(eventList.size());
    }


    static double randomInterarrivalTime (double arrivalRate)
    {
	return exponential (arrivalRate);
    }


    static double serviceTime ()
    {
	return serviceRate;
    }


    static double exponential (double gamma)
    {
        Random generator = new Random(System.currentTimeMillis());
        double randomX = generator.nextDouble()* 1;
        return (1.0 / gamma) * (-Math.log(1.0-RandTool.uniform()));
    }

    static void report ()
    {
	if (numDepartures == 0) {
	    return;
	}
	
	avgSystemTime = totalSystemTime / numDepartures;
    }


    public String toString ()
    {
        String results = "Simulation results:";
        results += "\n  numArrivals:     " + numArrivals;
        results += "\n  numDepartures:   " + numDepartures;
        results += "\n  avg System Time: " + avgSystemTime;
        return results;
    }
    
    

    ///////////////////////////////////////////////////////////////////////
    // main

    public static void main (String[] argv) throws IOException
    {
        FileWriter out = new FileWriter("C:\\Users\\Mickey\\Desktop\\Question.xls");
        BufferedWriter bw = new BufferedWriter(out);
        bw.write("Customer\t");
        bw.write("Arrival Rate\t");
        bw.write("Response Time");
        bw.write("\n");
        int steps = 100;
        ArrayList<Thread> threads = new <Thread>ArrayList();
      
////        for (int i = 0; i< 10; i++)
////        {
////           
////            for(int j=0; j<10; j++)
////            {
              PriorityQueuing queue = new PriorityQueuing (); 
              PriorityQueuing queue2 = new PriorityQueuing (); 
                
                Thread arrivalThread = new Thread() 
                {
                @Override
                    public void run() 
                    {

                        try
                        {

                            queue.sim (1000, one);
                            //System.out.println ("queue" + queue);

                        }
                        catch (Exception ex)
                   {
                       ex.printStackTrace();
                   }



                    }
                };   
                
               Thread arrivalThread2 = new Thread() 
                {
                @Override
                    public void run() 
                    {

                        try
                        {

                         queue2.sim (1000, two);
                         //System.out.println ("queue2" + queue);

                        }
                        catch (Exception ex)
                   {
                       ex.printStackTrace();
                   }



                    }
                };  
       
                arrivalThread.start();
                arrivalThread2.start();
                threads.add(arrivalThread);
                threads.add(arrivalThread2);


                  threads.stream().forEach((t) -> {
                            try {
                                t.join();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(PriorityQueuing.class.getName()).log(Level.SEVERE, null, ex);
                            }
                  });

                 System.out.println(numDepartures1);
                 System.out.println(numDepartures2);
                for (int o =0; o<one.getresponseTime().size(); o++) {
                    bw.write("one" + "\t" + one.getArrivalRate() + "\t" + one.getresponseTime().get(o));
                    bw.write("\n");
                }
                for (int t =0; t<two.getresponseTime().size(); t++) {
                     bw.write("two" + "\t" + two.getArrivalRate() + "\t" + two.getresponseTime().get(t));
                     bw.write("\n");
               }  
//            }
//             one.arrivalRate = one.getArrivalRate() + steps;
//             
//             //one = new Customer(0.0, one.arrivalRate, 0, new ArrayList(), 1);
//        
//        }
//                
//         bw.close();
   
   
    }
}


// Class Customer (one instance per customer) stores whatever we 
// need for each customer. Since we collect statistics on waiting 
// time at the time of departure, we need to record when a 
// customer arrives.

class Customer {
    double arrivalTime;
    double arrivalRate;
    int numDepartures;
    int type;
    ArrayList<Double> responseTime;
    public Customer (double arrivalTime, double arrivalRate, int numDepartures, ArrayList<Double> responseTime, int type)
    {
        this.arrivalTime = arrivalTime;
        this.arrivalRate = arrivalRate;
        this.numDepartures = numDepartures;
        this.responseTime = responseTime;
        this.type = type;
    }
    public Customer ()
    {
        
    }
  

    double getArrivalTime()
    {
        return arrivalTime;
    }
    
    double getArrivalRate()
    {
        return arrivalRate;
    }
    
    int getNumDepartures()
    {
        return numDepartures;
    }
    
    int getType()
    {
        return type;
    }
    
    ArrayList<Double> getresponseTime()
    {
        return responseTime;
    }
}


// Class Event has everything we need for an event: the type of
// event, and when it occurs. To use Java's PriorityQueue, we need
// have this class implement the Comparable interface where
// one event is "less" if it occurs sooner.

class Event implements Comparable {
    public static int ARRIVAL = 1;
    public static int DEPARTURE = 2;
    int type = -1;                     // Arrival or departure.
    double eventTime;                  // When it occurs.

    public Event (double eventTime, int type)
    {
	this.eventTime = eventTime;
	this.type = type;
    }


    public int compareTo (Object obj)
    {
        Event e = (Event) obj;
        if (eventTime < e.eventTime) {
            return -1;
        }
        else if (eventTime > e.eventTime) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public boolean equals (Object obj)
    {
        return (compareTo(obj) == 0);
    }

}

