import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SleepingBarber  {
	
	public static void main (String a[]) throws InterruptedException {	
		
        int ndeBarbeiros=2, clienteId=1, nDeClientes=100, nDeCadeiras;	
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Entre com o numero de barbeiros:");			
    	ndeBarbeiros=sc.nextInt();
    	
    	System.out.println("Entre com o numero de salas de esperas"			
    			+ " cadeiras(N):");
    	nDeCadeiras=sc.nextInt();
    	

    	
		ExecutorService exec = Executors.newFixedThreadPool(12);		
    	Bshop shop = new Bshop(ndeBarbeiros, nDeCadeiras);				
    	Random r = new Random();  										
       	    	
        System.out.println("\n a loja de barberaria foi aberta com  "
        		+ndeBarbeiros+" barber(s)\n");
        
        long startTime  = System.currentTimeMillis();					
        
        for(int i=1; i<=ndeBarbeiros;i++) {								
        	
        	Barber barber = new Barber(shop, i);	
        	Thread thbarber = new Thread(barber);
            exec.execute(thbarber);
        }
        
        for(int i=0;i<nDeClientes;i++) {								
        
            Customer customer = new Customer(shop);
            customer.setInTime(new Date());
            Thread thcustomer = new Thread(customer);
            customer.setclienteId(clienteId++);
            exec.execute(thcustomer);
            
            try {
            	
            	double val = r.nextGaussian() * 2000 + 2000;			
            	int millisDelay = Math.abs((int) Math.round(val));		
            	Thread.sleep(millisDelay);								
            }
            catch(InterruptedException iex) {
            
                iex.printStackTrace();
            }
            
        }
        
        exec.shutdown();												
        exec.awaitTermination(12, SECONDS);								
 
        long elapsedTime = System.currentTimeMillis() - startTime;		
        
        System.out.println("\nA loja foi fechada");
        System.out.println("\nTotal de tempo passado em segundos"
        		+ " servindo "+nDeClientes+" clientes "
        		+ndeBarbeiros+" barbeiro com "+nDeCadeiras+
        		" cadeiras na sala de espera: "
        		+TimeUnit.MILLISECONDS
        	    .toSeconds(elapsedTime));
        System.out.println("\nTotal de clientes: "+nDeClientes+
        		"\ntotal de clientes atentidos: "+shop.getTotalHairCuts()
        		+"\ntotal de clientes perdidos: "+shop.getCustomerLost());
               
        sc.close();
    }
}
 

class Barber implements Runnable {										

    Bshop shop;
    int barberId;
 
    public Barber(Bshop shop, int barberId) {
    
        this.shop = shop;
        this.barberId = barberId;
    }
    
    public void run() {
    
        while(true) {
        
            shop.cutHair(barberId);
        }
    }
}

class Customer implements Runnable {

    int clienteId;
    Date inTime;
 
    Bshop shop;
 
    public Customer(Bshop shop) {
    
        this.shop = shop;
    }
 
    public int getclienteId() {										
        return clienteId;
    }
 
    public Date getInTime() {
        return inTime;
    }
 
    public void setclienteId(int clienteId) {
        this.clienteId = clienteId;
    }
 
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }
 
    public void run() {													
    
        goForHairCut();
    }
    private synchronized void goForHairCut() {							
    
        shop.add(this);
    }
}
 
class Bshop {

	private final AtomicInteger totalHairCuts = new AtomicInteger(0);
	private final AtomicInteger customersLost = new AtomicInteger(0);
	int nchair, ndeBarbeiros, availableBarbers;
    List<Customer> listCustomer;
    
    Random r = new Random();	 
    
    public Bshop(int ndeBarbeiros, int nDeCadeiras){
    
        this.nchair = nDeCadeiras;														
        listCustomer = new LinkedList<Customer>();						
        this.ndeBarbeiros = ndeBarbeiros;									
        availableBarbers = ndeBarbeiros;
    }
 
    public AtomicInteger getTotalHairCuts() {
    	
    	totalHairCuts.get();
    	return totalHairCuts;
    }
    
    public AtomicInteger getCustomerLost() {
    	
    	customersLost.get();
    	return customersLost;
    }
    
    public void cutHair(int barberId)
    {
        Customer customer;
        synchronized (listCustomer) {									
        															 	
            while(listCustomer.size()==0) {
            
                System.out.println("\nbarbeiro "+barberId+" esta esperando "
                		+ " pelo cliente e dorme em sua cadeira ");
                
                try {
                
                    listCustomer.wait();								
                }
                catch(InterruptedException iex) {
                
                    iex.printStackTrace();
                }
            }
            
            customer = (Customer)((LinkedList<?>)listCustomer).poll();	 
            
            System.out.println("Cliente "+customer.getclienteId()+
            		" acha o barbeiro dormindo e acorda "
            		+ "o barbeiro "+barberId);
        }
        
        int millisDelay=0;
                
        try {
        	
        	availableBarbers--; 										
        																
            System.out.println("barbeiro "+barberId+" cortando o cabelo do "+
            		customer.getclienteId()+ " entao o cliente dorme");
        	
            double val = r.nextGaussian() * 2000 + 4000;				
        	millisDelay = Math.abs((int) Math.round(val));				
        	Thread.sleep(millisDelay);
        	
        	System.out.println("\nCorte de cabelo completado do "+
        			customer.getclienteId()+" pelo barber " + 
        			barberId +" em "+millisDelay+ " milliseconds.");
        
        	totalHairCuts.incrementAndGet();
            															
            if(listCustomer.size()>0) {									
            	System.out.println("Barbeiro "+barberId+					
            			" acorda um cliente na "					
            			+ "sala de espera");		
            }
            
            availableBarbers++;											
        }
        catch(InterruptedException iex) {
        
            iex.printStackTrace();
        }
        
    }
 
    public void add(Customer customer) {
    
        System.out.println("\nCliente "+customer.getclienteId()+
        		" entra pela entrada da frente da loja "
        		+customer.getInTime());
 
        synchronized (listCustomer) {
        
            if(listCustomer.size() == nchair) {							
            
                System.out.println("\nNenhuma cadeira esta liberada"
                		+ " para o cliente "+customer.getclienteId()+
                		" entao o cliente sai da loja");
                
              customersLost.incrementAndGet();
                
                return;
            }
            else if (availableBarbers > 0) {							
            															
            	((LinkedList<Customer>)listCustomer).offer(customer);
				listCustomer.notify();
			}
            else {														
            															
            	((LinkedList<Customer>)listCustomer).offer(customer);
                
            	System.out.println("Todos os barbeiros estao ocupados entao "+
            			customer.getclienteId()+
                		" pega uma cadeira na sala de espera ");
                 
                if(listCustomer.size()==1)
                    listCustomer.notify();
            }
        }
    }
}
