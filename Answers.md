## Question 1
Because there is no `thread.join` it doesn't sleep 10 sec and it immediately goes for next line and it prints both catch and finally
* `Thread was interrupted`
* `Thread will be finished here!!!`
## Question 2
The outcome will be:
* `Running in: main`
- The reason is each thread extends from main thread.
## Question 3
First it prints:
* `Running in: Thread_0`
* And after that it prints
* `Back to: main`
* The reason is that when thread_0’s task has finished, the thread will be terminated, and control will return to the main thread, similar to question 2.
