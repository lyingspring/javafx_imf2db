package fx_public;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Controller;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenFileChooser {
    private final Desktop desktop = Desktop.getDesktop();

    public String open() throws IOException {
        final FileChooser fileChooser = new FileChooser();
        String path="";
        fileChooser.setTitle("选择文件");
        fileChooser.setInitialDirectory(//打开默认路径
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(///设置过滤
                new FileChooser.ExtensionFilter("xls", "*.xls"),
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("xlsx", "*.xlsx")
        );
        Stage mainStage = null;
        File file = fileChooser.showOpenDialog(mainStage);
        if (file != null) {
            path=file.getCanonicalPath();
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
}
