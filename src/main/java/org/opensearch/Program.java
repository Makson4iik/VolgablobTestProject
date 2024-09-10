package org.opensearch;

public class Program {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("There are too few input arguments.");
        }

        try {
            BatteryStats.HandlePrint(args[0], args[1]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}