
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
    private static ArrayList<int[]> allocatedBlocks = new ArrayList<>();

    void myMalloc(int size, int memoryID) {
        // memory is not initialized
        if (Memory[0] == 0 && initialized == 0) {
            System.out.println("Memory is initializing ... ");
            initialized = 1;
            Memory[initBlock] = 1;
            Memory[24999] = 1; // for hold freeblock arraylist
            Memory[24998] = 1; // for hold allocated blocks arraylist

            int tmp[] = {24999, 1, 25000};
            freeBlocks.add(tmp);
            System.out.println("Availabel Space for Allocations :" + (24997 - arrayListSize) + " MB");

            myMalloc(size, memoryID);
        } else {
            // variable to check whether a suitable space to allocate is found or not
            int memoryDetected = 0;

            System.out.println("Searching for free space...");

            while (memoryDetected == 0) {

                int[] previouseBlock = freeBlocks.get(0);

                // check through the free blocks for a suitable apace
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
                for (int i = 0; i < size; i++) {
                    Memory[memoryDetected + i] = 1;
                }
                int tmp[] = {memoryID, memoryDetected, size};
                allocatedBlocks.add(tmp);
                // calculate free spaces again when done allocating
                freeBlocks.clear();
                calculateFreeBlocks();
                System.out.println("Memory has been allocated for memory id : " + memoryID);
            } else {
                System.out.println("Sorry! Can't find a suitable memory location for memory id : " + memoryID);
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
        allocatedBlocks.trimToSize();
        arrayListSize = freeBlocks.size() * 3 + allocatedBlocks.size() * 3;
    }

    void myFree(int memoryID) {
        if (initialized == 0) {
            System.out.println("Memory is already empty!");
        } else {
            int cleared = -1;
            for (int i = 0; i < allocatedBlocks.size(); i++) {
                int tmp[] = allocatedBlocks.get(i);
                if (memoryID == tmp[0]) {
                    for (int j = 0; j < tmp[2]; j++) {
                        Memory[tmp[1] + j] = 0;
                    }
                    cleared = i;
                }
            }

            if (cleared == -1) {
                System.out.println("Sorry! Can't find the specific memory to clean");
            } else {
                System.out.println("Memory location for id :" + memoryID + " is successfully cleared");
                allocatedBlocks.remove(cleared);
            }
        }
    }

    void printMemoryStack() {

    }
}
