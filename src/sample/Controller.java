package sample;

import fx_public.ConsoleToTextarea;
import fx_public.FXAlert;
import fx_public.OpenFileChooser;
import importfile.Imain;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable {

    @FXML
    private TextField excelname;

    @FXML
    private TextField startrow;

    @FXML
    private TextField cellstr;

    @FXML
    private TextField dbtable;

    @FXML
    private Button btn1;

    @FXML
    private TextArea textarea1;

    @FXML
    private Button btn2;


    @FXML
    void chofile(ActionEvent event) throws IOException {
        OpenFileChooser opfile = new OpenFileChooser();
        excelname.setText(opfile.open());
        //opfile. openSaveDialog();
        // excelname.setText(opfile.opendir());
        //System.out.printf("123");
    }

    @FXML
    void openfile(ActionEvent event) throws IOException {
        OpenFileChooser opfile = new OpenFileChooser();
        excelname.setText(opfile.open());
        //opfile. openSaveDialog();
        // excelname.setText(opfile.opendir());
        //System.out.printf("123");
    }

    @FXML
    void opendir(ActionEvent event) throws IOException {
        OpenFileChooser opfile = new OpenFileChooser();
        excelname.setText(opfile.opendir());
    }

    @FXML
    void save(ActionEvent event) throws Exception {
        //判断URL是否合法
        if (!Imain.checkUrl(excelname.getText(), "^(.*\\.xls.?|.*\\.txt|.*\\.csv)")) {
            FXAlert.Alert_ERR("请检查路径是否正确或路径下是否有文件存在！", "URL错误");
            return;
        }
        ;

        //textarea1.setText(excelname.getCharacters().toString());
        Imain.importFile(excelname.getText(), Integer.parseInt(startrow.getText()), cellstr.getText(), dbtable.getText());
        //  System.setOut(printStream);

        FXAlert.Alert_Info("处理完成");
        excelname.setText("");
    }

    @FXML
    void getAuthorInfo(ActionEvent event) throws Exception {
        FXAlert.Alert_Info("author:maoxj \n\nEmail:281898533@qq.com \n\n2018-07-14 \n\nversion:beta 1.0");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ConsoleToTextarea aa = new ConsoleToTextarea();

        try {
            aa.setupConsoleOutput(textarea1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void dbconf(ActionEvent event) throws IOException {
        OpenFileChooser open=new OpenFileChooser();
        File file = new File(System.getProperty("user.dir").toString()
                + "\\oracle.properties");
        if(file.exists()){
            open.openFile(file);
        }else {
            file.createNewFile();
            FileOutputStream out=new FileOutputStream(file);
            PrintStream ps= new PrintStream(out, true,"utf-8");
            ps.println("url=jdbc:oracle:thin:@192.168.1.1:1521:sid");
            ps.println("user=xxx");
            ps.println("password=xxx");
            ps.close();
            open.openFile(file);
        }
    }
    @FXML
    void hyperlink1click(ActionEvent event) throws Exception{
        getAuthorInfo(event);
    }


}
