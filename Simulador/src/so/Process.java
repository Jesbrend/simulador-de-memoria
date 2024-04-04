package so;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import so.memory.AddressMemory;

public class Process {
    
    private static int count = 0;
    
    private String id;
    private int sizeInMemory;
    private AddressMemory adressInMemory;
    
    public Process(int sizeInMemory) {
        count++; 
        this.id = "P" + count; 
        this.sizeInMemory = sizeInMemory;
    }
    
    public Process() {
        Random rand = new Random();
        count++; 
        this.id = "P" + count; 
        List<Integer> givenList = Arrays.asList(5, 10, 25, 50, 100);
        this.sizeInMemory = givenList.get(rand.nextInt(givenList.size()));
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getSizeInMemory() {
        return sizeInMemory;
    }
    public void setSizeInMemory(int sizeInMemory) {
        this.sizeInMemory = sizeInMemory;
    }

    public AddressMemory getAdressInMemory() {
        return adressInMemory;
    }

    public void setAdressInMemory(AddressMemory adressInMemory) {
        this.adressInMemory = adressInMemory;
    }
    
    
}

