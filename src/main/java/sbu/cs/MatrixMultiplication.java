package sbu.cs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MatrixMultiplication {

    // You are allowed to change all code in the BlockMultiplier class
    public static class BlockMultiplier implements Runnable
    {
        List<List<Integer>> tempMatrixProduct;
        List<List<Integer>> matrix_A;
        List<List<Integer>> matrix_B;
        int firstRow;
        int lastRow;
        int firstCol;
        int lastCol;
        int test;
        Lock lock = new ReentrantLock();
        public BlockMultiplier(List<List<Integer>> tempMatrixProduct , List<List<Integer>> matrix_A , List<List<Integer>> matrix_B,
        int firstRow , int lastRow , int firstCol , int lastCol, Lock lock) {
            this.tempMatrixProduct = tempMatrixProduct;
            this.matrix_A = matrix_A;
            this.matrix_B = matrix_B;
            this.firstRow = firstRow;
            this.lastRow = lastRow;
            this.firstCol = firstCol;
            this.lastCol = lastCol;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = firstRow; i < lastRow; i++) {
                for (int j = firstCol; j < lastCol; j++) {
                    int sum = 0;
                    for (int k = 0; k < matrix_A.get(0).size(); k++) {
                        sum += matrix_A.get(i).get(k) * matrix_B.get(k).get(j);
                    }
                    tempMatrixProduct.get(i).set(j, sum);
                }
            }
            System.out.println(Thread.currentThread().getName() + " part of matrix done.");
        }
    }
    public static List<List<Integer>> ParallelizeMatMul(List<List<Integer>> matrix_A, List<List<Integer>> matrix_B)
    {
//        List<List<Integer>> finalMatrix = new ArrayList<>();
        int row = matrix_A.size();
        int col = matrix_B.get(0).size();
        List<List<Integer>> finalMatrix = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            finalMatrix.add(new ArrayList<>(Collections.nCopies(col, 0)));
        }

        Lock lock = new ReentrantLock();
        BlockMultiplier blockMultiplier1 = new BlockMultiplier(finalMatrix , matrix_A , matrix_B , 0 , row / 2 , 0 , col / 2  ,lock);
        BlockMultiplier blockMultiplier2 = new BlockMultiplier(finalMatrix , matrix_A , matrix_B , 0 , row / 2 , col / 2 , col  , lock);
        BlockMultiplier blockMultiplier3 = new BlockMultiplier(finalMatrix , matrix_A , matrix_B , row / 2 , row , 0 , col / 2 , lock);
        BlockMultiplier blockMultiplier4 = new BlockMultiplier(finalMatrix , matrix_A , matrix_B , row / 2 , row , col / 2 , col , lock);

        Thread thread1 = new Thread(blockMultiplier1 , "Top Left");
        Thread thread2 = new Thread(blockMultiplier2 , "Top Right");
        Thread thread3 = new Thread(blockMultiplier3 , "Down Left");
        Thread thread4 = new Thread(blockMultiplier4 , "Down Right");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        try{
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }

        /*
        TODO
            Parallelize the matrix multiplication by dividing tasks between 4 threads.
            Each thread should calculate one block of the final matrix product. Each block should be a quarter of the final matrix.
            Combine the 4 resulting blocks to create the final matrix product and return it.
         */
        return finalMatrix;
    }

    public static void main(String[] args) {
        // Test your code here
        List<List<Integer>> matrix_A;
        List<List<Integer>> matrix_B;
        List<List<Integer>> matrix_C;
        matrix_A = readMatrixFromCSV("src/test/resources/matrix_A.csv"); // 100 X 200
        matrix_B = readMatrixFromCSV("src/test/resources/matrix_B.csv"); // 200 X 100
        matrix_C = ParallelizeMatMul(matrix_A , matrix_B);
        System.out.println("The Result:");
        for (List<Integer> row : matrix_C) {
            for (Integer value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

    private static List<List<Integer>> readMatrixFromCSV(String filename) {
        List<List<Integer>> matrix = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                List<Integer> row = new ArrayList<>();
                for (String value : values) {
                    row.add(Integer.parseInt(value));
                }
                matrix.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }
}
