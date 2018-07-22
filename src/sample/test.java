package sample;

import importfile.oracle_jdbc;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class test {
//    public static void main(String[] args) throws Exception {
//
//        String filename;
//        int startrow;
//        String cells;
//        String tablename;
//
//
//        File file = new File(System.getProperty("user.dir").toString());
//        System.out.println("----------------");
//        final String regex = "^(.*\\.xls.?|.*\\.txt|.*\\.csv)";
//        String[] paths2 = file.list(new FilenameFilter() {
//            private Pattern pattern = Pattern.compile(regex);
//
//            @Override
//            public boolean accept(File dir, String name) {
//                return pattern.matcher(name).matches();
//            }
//        });
//        Arrays.sort(paths2, String.CASE_INSENSITIVE_ORDER);//排序
//        for (String path : paths2) {
//            System.out.println(path);
//          //  insertDBBatch(startrow, cells, tablename, path, db);
//        }
//
//    }


    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
            if (i == 2) {
                Runnable myRunnable = new MyRunnable(); // 创建一个Runnable实现类的对象
                Thread thread1 = new Thread(myRunnable); // 将myRunnable作为Thread target创建新的线程
                Thread thread2 = new Thread(myRunnable);
                thread1.start(); // 调用start()方法使得线程进入就绪状态
                thread2.start();
            }
            Thread.sleep(3000);
        }
}

    static class MyRunnable implements Runnable {

        private boolean stop;

        @Override
        public void run() {
            for (int i = 0; i < 100 && !stop; i++) {
                System.out.println(Thread.currentThread().getName() + " w  " + i);
            }
        }

        public void stopThread() {
            this.stop = true;
        }

    }



}
