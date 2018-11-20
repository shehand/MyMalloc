
package mymalloc;

import java.util.ArrayList;

/**
 *
 * @author Shehan Dhaleesha
 */
public class Memory {

    static byte Memory[] = new byte[25000]; // total memory size
    static byte initialized = 0; // memory is not initialized;
    static int initBlock = 0; // initializing block
    private static int arrayListSize = 0; // total array list size that can be used to allocate
    private static int remainSize = 24998 - arrayListSize; // remaining memory size
    static int head = 0; // identify to find that the memory is already allocated or not
    private static ArrayList<int[]> freeBlocks = new ArrayList<>(); // array list to hold free blocks
    private static ArrayList<int[]> allocatedBlocks = new ArrayList<>(); // array list to hold allocated memory slots

    public void myMalloc(int size, int memoryID) {
        // memory is not initialized
        if (Memory[0] == 0 && initialized == 0) {
            System.out.println("Memory is initializing ... ");
            initialized = 1;
            Memory[initBlock] = 1;
            Memory[24999] = 1; // for hold freeblock arraylist
            Memory[24998] = 1; // for hold allocated blocks arraylist

            int tmp[] = {24997, 1, 24997};
            freeBlocks.add(tmp);
            System.out.println("Availabel Space for Allocations :" + (24997) + " MB");

            myMalloc(size, memoryID);
        } else {
            // variable to check whether a suitable space to allocate is found or not
            int memoryDetected = 0;

            System.out.println("Searching for free space...");

            while (memoryDetected == 0) { // searching for a suitable memory location

                int[] previouseBlock = {24999, 1, 24999}; // to hold tempory a selected slot until finalize the most suitable memory location

                // check through the free blocks for a suitable apace (best fit algorithm)
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
                    memoryDetected = -1; // no suitable memory location
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
        // find the memory slot by seachin through the array

        for (int i = 1; i < remainSize - 1; i++) {

            if (Memory[i] == 0) {
                begin = i;
                while (Memory[i] == 0) {
                    i++;
                }
                end = i - 1;
            }
            if (begin != end) {
                int size = end - begin;
                int tmp[] = {size, begin, end};
                freeBlocks.add(tmp);
            }
        }

        freeBlocks.trimToSize();
        allocatedBlocks.trimToSize();
        // calculate the array lists sizes to reduce them from the memory
        arrayListSize += freeBlocks.size() * 4 + allocatedBlocks.size() * 4;
    }

    public void myFree(int memoryID) {
        // if memory is not initialize cant free any memory slot
        if (initialized == 0) {
            System.out.println("Memory is already empty!");
        } else {
            int cleared = -1;
            // searching fot the memory id and clear it
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
                // remove the list item from the array list
                allocatedBlocks.remove(cleared);
                // calculate free blocks againt when done clearing
                freeBlocks.clear();
                calculateFreeBlocks();
            }
        }
    }

    // method to print the memory stack
    public void printMemoryStack() {
        System.out.println("Initializing Memory Allocation : " + 1 + " MB");

        for (int i = 0; i < allocatedBlocks.size(); i++) {
            int tmp[] = allocatedBlocks.get(i);
            System.out.println("Memory ID : " + tmp[0] + " From Register ID : " + tmp[1] + " To Register ID : " + (tmp[1] + tmp[2]));
        }

        System.out.println("Number of Allocated Memory slots : " + allocatedBlocks.size());
        System.out.println("Number of Free Memory slots : " + freeBlocks.size());
    }
}
