/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memorymanagement;

import java.util.Scanner;
import mymalloc.*;

/**
 *
 * @author Sono
 */
public class MemoryManagement {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         Scanner s = new Scanner(System.in);

        System.out.println("Type 'I' for initialize the memory and start allocating");
        String choice = s.nextLine();
        System.out.println("Type 'Q' to exit from the programme");
        System.out.println("To print the memory stack type 'P'");
        System.out.println("Allocate? or Free? (Type 'F' for free and 'A' for Allocate)");

        boolean ch = true;
        Memory mem = new Memory();
        String choose;
        while (ch) {
            if (choice.equals("I")) {
                choose = s.nextLine();
                if (choose.equals("A")) {
                    System.out.println("Insert Memory ID:");
                    int memoryID = s.nextInt();
                    System.out.println("Insert Memory Allocating size : ");
                    int size = s.nextInt();
                    mem.myMalloc(size, memoryID);
                } else if (choose.equals("F")) {
                    System.out.println("Insert Memory ID:");
                    int memoryID = s.nextInt();
                    mem.myFree(memoryID);
                } else if (choose.equals("Q")) {
                    ch = false;
                    break;
                } else if (choose.equals("P")) {
                    mem.printMemoryStack();
                }

            } else {
                ch = false;
            }
        }

        System.out.println("Finished!");
    }
    
}
