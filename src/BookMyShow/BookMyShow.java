package BookMyShow;


import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BookMyShow {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private int availableSeats = 10;

    // Method to check the number of available seats (read operation)
    public void viewAvailableSeats() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " is viewing seats. Available seats: " + availableSeats);
        } finally {
            lock.readLock().unlock();
        }
    }

    // Method to book a seat (write operation)
    public void bookSeat() {
        lock.writeLock().lock();
        try {
            if (availableSeats > 0) {
                System.out.println(Thread.currentThread().getName() + " is booking a seat.");
                availableSeats--;
                System.out.println(Thread.currentThread().getName() + " booked a seat. Remaining seats: " + availableSeats);
            } else {
                System.out.println("No seats available to book.");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void main(String[] args) {
        BookMyShow bookMyShow = new BookMyShow();

        // Simulating multiple users viewing and booking seats
        Runnable viewTask = () -> {
            for (int i = 0; i < 5; i++) {
                bookMyShow.viewAvailableSeats();
                try {
                    Thread.sleep(50);  // Simulate time taken to view
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable bookTask = () -> {
            for (int i = 0; i < 5; i++) {
                bookMyShow.bookSeat();
                try {
                    Thread.sleep(50);  // Simulate time taken to book
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread user1 = new Thread(viewTask, "User1");
        Thread user2 = new Thread(viewTask, "User2");
        Thread user3 = new Thread(bookTask, "User3");

        user1.start();
        user2.start();
        user3.start();
    }
}
