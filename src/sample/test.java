package sample;

import importfile.oracle_jdbc;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class test {
    public static void main(String[] args) throws Exception {

        String filename;
        int startrow;
        String cells;
        String tablename;


        File file = new File(System.getProperty("user.dir").toString());
        System.out.println("----------------");
        final String regex = "^(.*\\.xls.?|.*\\.txt|.*\\.csv)";
        String[] paths2 = file.list(new FilenameFilter() {
            private Pattern pattern = Pattern.compile(regex);

            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(name).matches();
            }
        });
        Arrays.sort(paths2, String.CASE_INSENSITIVE_ORDER);//排序
        for (String path : paths2) {
            System.out.println(path);
          //  insertDBBatch(startrow, cells, tablename, path, db);
        }

    }
}
