package so.memory;

import so.Process;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MemoryManager {
    
    private int pageSize;
    private String[] physicMemory;
    private Hashtable<String, List<FrameMemory>> logicalMemory;
    private Strategy strategy;
    private int[][] emptySpaces;

    public MemoryManager(Strategy strategy) {
        this.strategy = strategy;
        this.physicMemory = new String[128];
        this.logicalMemory = new Hashtable<>();
        int emptySpaceCount = sizePhysicMemoryEmpty();
        this.emptySpaces = new int[emptySpaceCount][2];
        this.pageSize = 2; 
    }


  
    public int sizePhysicMemoryEmpty() {
        int emptySpace = 0;
        for (int i = 0; i < physicMemory.length; i++) {
            if (physicMemory[i] == null) {
                emptySpace++;
            }
        }
        return emptySpace;
    }


    public void spaceInPhysicMemory() {
        int actualSize = 0;
        int count = 0;
        for (int i = 0; i < physicMemory.length; i++) {
            if (i == (physicMemory.length - 1)) {
                if (actualSize > 0) {
                    if (physicMemory[i] == null) {
                        actualSize++;
                        emptySpaces[count][0] = actualSize;
                        emptySpaces[count][1] = i;
                        count++;
                    }
                }
            } else if (physicMemory[i] == null) {
                actualSize++;
            } else {
                if (actualSize > 0) {
                    emptySpaces[count][0] = actualSize;
                    emptySpaces[count][1] = i - 1; 
                    actualSize = 0;
                    count++;
                }
            }
        }
    }

  
    public void writeProcess(Process p) {
        try {
            switch (this.strategy) {
                case FIRST_FIT:
                    writeUsingFirstFit(p);
                    break;
                case BEST_FIT:
                    writeUsingBestFit(p);
                    break;
                case WORST_FIT:
                    writeUsingWorstFit(p);
                    break;
                case PAGING:
                    writeUsingPaging(p);
                    break;
                default:
                    throw new IllegalArgumentException("INCORRECT STRATEGY");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    private void writeUsingPaging(Process p) {
        List<FrameMemory> frames = getFrames(p);
        if (frames == null) {
            System.out.println("Não há espaço suficiente em memória");
        } else {
            for (FrameMemory frame : frames) {
                for (int j = frame.getPageNumber(); j < frame.getOffset(); j++) {
                    this.physicMemory[j] = p.getId();
                }
            }
        }
        this.logicalMemory.put(p.getId(), frames);
    }
    

    private void delete(Process p) {
        List<FrameMemory> frames = this.logicalMemory.get(p.getId());
        if (frames == null) {
            System.out.println("Não há espaço suficiente em memória");
        } else {
            for (FrameMemory frame : frames) {
                for (int j = frame.getPageNumber(); j < frame.getOffset(); j++) {
                    this.physicMemory[j] = null;
                }
            }
        }
        this.logicalMemory.remove(p.getId());
    }


    private List<FrameMemory> getFrames(Process p) {
        List<FrameMemory> frames = new ArrayList<>();
        int increment = 0;
        for (int page = 0; page < this.physicMemory.length; page += this.pageSize) {
            if (this.physicMemory[page] == null) {
                int offset = page + this.pageSize;
                frames.add(new FrameMemory(page, offset));
                increment += this.pageSize;
                if (increment == p.getSizeInMemory()) {
                    return frames;
                }
            }
        }
        return null;
    }
    

    private int[][] biggerSpacePhysicMemory() {
        spaceInPhysicMemory();
        int[][] bigSpace = new int[1][2];
        for (int[] emptySpace : emptySpaces) {
            if (emptySpace[0] > bigSpace[0][0]) {
                bigSpace[0][0] = emptySpace[0];
                bigSpace[0][1] = emptySpace[1];
            }
        }
        return bigSpace;
    }

    private void writeUsingWorstFit(Process p) {
        int[][] bigSpace = biggerSpacePhysicMemory();
        int sizeArray = sizePhysicMemoryEmpty();
        if (p.getSizeInMemory() <= sizeArray) {
            if (p.getSizeInMemory() <= bigSpace[0][0]) {
                int start = Math.max(0, bigSpace[0][1] - bigSpace[0][0]);
                int count = 0;
                do {
                    physicMemory[(start + count)] = p.getId();
                    count++;
                } while (count < p.getSizeInMemory());
                printStatusMemory();
            } else {
                memoryFull(p);
            }
        } else {
            memoryFull(p);
        }
    }

    private void writeUsingBestFit(Process p) {
        spaceInPhysicMemory();
        int lowerNumber = Integer.MAX_VALUE;
        int position = 0;
        int[][] bigSpace = biggerSpacePhysicMemory();
        if (p.getSizeInMemory() <= bigSpace[0][0]) {
            for (int i = 0; i < physicMemory.length; i++) {
                if (emptySpaces[i][0] > p.getSizeInMemory()) {
                    int differentNumber = Math.abs(emptySpaces[i][0] - p.getSizeInMemory());
                    if (differentNumber < lowerNumber) {
                        lowerNumber = emptySpaces[i][0];
                        position = emptySpaces[i][1];
                    }
                }
            }
            int start = Math.max(0, position - lowerNumber);
            int count = 0;
            do {
                physicMemory[start + count] = p.getId();
                count++;
            } while (count < p.getSizeInMemory());
            printStatusMemory();
        } else {
            memoryFull(p);
        }
    }
    
    private void writeUsingFirstFit(Process p) {
        int[][] bigSpace = biggerSpacePhysicMemory();
        spaceInPhysicMemory();
        if (p.getSizeInMemory() <= bigSpace[0][0]) {
            for (int i = 0; i < physicMemory.length; i++) {
                if (p.getSizeInMemory() <= emptySpaces[i][0]) {
                    int start = Math.abs(emptySpaces[i][0] - emptySpaces[i][1]);
                    int count = 0;
                    do {
                        physicMemory[start + count] = p.getId();
                        count++;
                    } while (count < p.getSizeInMemory());
                }
            }
            printStatusMemory();
        } else {
            memoryFull(p);
        }
    }


    private void printStatusMemory() {
        for (String page : physicMemory) {
            System.out.print((page != null ? page : "-") + " | ");
        }
        System.out.println();
    }

 
    public void deleteProcess(Process p) {
        for (int i = 0; i < physicMemory.length; i++) {
            if (p.getId().equals(physicMemory[i])) {
                physicMemory[i] = null;
            }
        }
        printStatusMemory();
    }

  
    public void memoryFull(Process p) {
        System.out.printf("O PROCESSO (%s) NÃO FOI ENCONTRADO, A MEMÓRIA ESTÁ CHEIA!%n", p.getId());
    }

}

