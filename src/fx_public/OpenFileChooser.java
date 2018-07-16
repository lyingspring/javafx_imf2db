package fx_public;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenFileChooser {
    private final Desktop desktop = Desktop.getDesktop();

    /**
     * 打开文件选择框
     * @return
     * @throws IOException
     */
    public String open() throws IOException {
        final FileChooser fileChooser = new FileChooser();
        String path = "";
        fileChooser.setTitle("选择文件");
        fileChooser.setInitialDirectory(//打开默认路径
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(///设置过滤
                new FileChooser.ExtensionFilter("all", "*.xls*","*.csv","*.txt"),
                new FileChooser.ExtensionFilter("xls", "*.xls"),
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("xlsx", "*.xlsx")
        );
        Stage mainStage = null;
        File file = fileChooser.showOpenDialog(mainStage);
        if (file != null) {
            path = file.getCanonicalPath();
            // openFile(file);//直接运行程序
        }
        return path;
    }

    private void openFile(File file) {
        EventQueue.invokeLater(() -> {
            try {
                desktop.open(file);
            } catch (IOException ex) {
                Logger.getLogger(Controller.
                        class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
    }
    /**
     * 打开文件夹选择框文件夹位置
     * @return
     * @throws IOException
     */
    public String opendir() throws IOException {
        {
            final FileChooser fileChooser = new FileChooser();
            String path = "";
            final DirectoryChooser directoryChooser=
            new DirectoryChooser();
            final File selectedDirectory =
                    directoryChooser.showDialog(null);
            if (selectedDirectory != null) {
                path=selectedDirectory.getAbsolutePath();
            }
            return path;
        }

    }

    /**
     * 保存对话框
     * @return
     */
    public  String openSaveDialog(){
        FileChooser fileChooser1 = new FileChooser();
        fileChooser1.setTitle("保存");
        fileChooser1.setInitialFileName("1.xls");//默认的文件名字
        fileChooser1.getExtensionFilters().addAll(///设置过滤
                 (new FileChooser.ExtensionFilter("excel", "*.xls")));
        //System.out.println(pic.getId());
        String filepath="";
        File file = fileChooser1.showSaveDialog(null);
        if (file != null) {
            try {
//                ImageIO.write(SwingFXUtils.fromFXImage(pic.getImage(),
//                        null), "png", file);
                filepath=  file.getCanonicalPath();
               // System.out.println(file.getCanonicalPath());
               // System.out.println(file.getAbsolutePath());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return filepath;
    }



    /**
     * 打开文件夹选择框文件夹位置(非系统风格窗口)
     * @return
     * @throws IOException
     */
    public String opendir2() throws IOException {
        FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
        System.out.println(fsv.getHomeDirectory());                //得到桌面路径
        String filePath = "";
        JFileChooser fileChooser = new JFileChooser(fsv.getHomeDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().getAbsolutePath();//这个就是你选择的文件夹的路径
        }
        return filePath;
    }


    /**
     * 打开选择文件选择框(非系统风格窗口)
      * @return
     */
    public String open2() {//选择文件
        int result = 0;
        File file = null;
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
        System.out.println(fsv.getHomeDirectory());                //得到桌面路径
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        fileChooser.setDialogTitle("请选择要上传的文件...");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        result = fileChooser.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == result) {
            path = fileChooser.getSelectedFile().getPath();
            System.out.println("path: " + path);
        }
        return path;
    }


}
