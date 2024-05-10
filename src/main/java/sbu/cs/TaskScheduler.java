package sbu.cs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskScheduler
{
    public static class Task implements Runnable
    {
        /*
            ------------------------- You don't need to modify this part of the code -------------------------
         */
        String taskName;
        int processingTime;

        public Task(String taskName, int processingTime) {
            this.taskName = taskName;
            this.processingTime = processingTime;
        }
        /*
            ------------------------- You don't need to modify this part of the code -------------------------
         */

        @Override
        public void run() {
            try {
                Thread.sleep(processingTime);
                System.out.println("Task of " + Thread.currentThread().getName() + " Done.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            /*
            TODO
                Simulate utilizing CPU by sleeping the thread for the specified processingTime
             */
        }
    }

    public static ArrayList<String> doTasks(ArrayList<Task> tasks){
        ArrayList<String> finishedTasks = new ArrayList<>();
        //Sorted by processing time of tasks
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return Integer.compare(o1.processingTime, o2.processingTime);
            }
        });
        Collections.reverse(tasks);

        for(Task task : tasks){
            Thread thread = new Thread(task , task.taskName);
            thread.start();
            finishedTasks.add(task.taskName);
            try {
                thread.join();
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
        /*
        TODO
            Create a thread for each given task, And then start them based on which task has the highest priority
            (highest priority belongs to the tasks that take more time to be completed).
            You have to wait for each task to get done and then start the next task.
            Don't forget to add each task's name to the finishedTasks after it's completely finished.
         */

        return finishedTasks;
    }

    public static void main(String[] args){
        ArrayList<Task> tasks = new ArrayList<>();

        tasks.add(new Task("A", 100));
        tasks.add(new Task("B", 150));
        tasks.add(new Task("C", 200));
        tasks.add(new Task("E", 130));
        tasks.add(new Task("F", 300));
        ArrayList<String> finishedTasks = doTasks(tasks);
        System.out.println("Finished Tasks: " + finishedTasks);
    }
}
