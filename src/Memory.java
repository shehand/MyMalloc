
import java.lang.reflect.Array;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Sono
 */
public class Memory {

    static byte Memory[] = new byte[25000];
    static byte initialized = 0; // memory is not initialized;
    static int initBlock = 0; // initializing block
//    static int headerSize = 200;
    private static int arrayListSize = 0;
    private static int remainSize = 25000 - arrayListSize;
    static int head = 0;
    private static ArrayList<int[]> freeBlocks = new ArrayList<>();
    

    void myMalloc(int size, int memoryID) {
        // memory is not initialized
        if (Memory[0] == 0 && initialized == 0) {
            System.out.println("Memory is initializing ... ");
            initialized = 1;
            Memory[initBlock] = 1;
            myMalloc(size, memoryID);

            System.out.println("Availabel Space for Allocations :" + (24999 - arrayListSize) + " MB");
        } else {
            // variable to check whether a suitable space to allocate is found or not
            int memoryDetected = 0;

            System.out.println("Searching for free space...");

            while (memoryDetected == 0) {

                int[] previouseBlock = freeBlocks.get(0);
                
                // check through the free blocks for a sutable apace
                for (int j = 0; j < freeBlocks.size(); j++) {
                    int tmp[] = freeBlocks.get(j);

                    if (size <= tmp[0]) {
                        memoryDetected = tmp[1];
                        if (previouseBlock[0] > tmp[0]) {
                            previouseBlock = tmp;
                        } else {
                            memoryDetected = previouseBlock[1];
                        }
                    }
                }

                if (memoryDetected == 0) {
                    memoryDetected = -1;
                }
            }

            if (memoryDetected != -1) {
                for(int i=0;i<size;i++){
                    Memory[memoryDetected+i] = 1;
                }
                freeBlocks.clear();
                calculateFreeBlocks();
            } else {
                System.out.println("Sorry! Can't find a suitable memory location for memory id : "+memoryID);
            }
        }
    }

    static void calculateFreeBlocks() {
        int begin = 0, end = 0;
        for (int i = 1; i < remainSize - 1; i++) {
            if (Memory[i] == 1 && Memory[i + 1] == 0) {
                begin = i + 1;
            }
            if (Memory[i] == 0 && Memory[i + 1] == 1) {
                end = i;
            }

            if (begin != end) {
                int size = end - begin;
                int tmp[] = {size, begin, end};
                freeBlocks.add(tmp);
            }
        }
        freeBlocks.trimToSize();
        arrayListSize = freeBlocks.size() * 3;
    }
    
    void free(int memoryID){
        if(freeBlocks.isEmpty()){
            System.out.println("Memory is already empty!");
        }else{
            
        }
    }

}
