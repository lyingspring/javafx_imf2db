package fx_public;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public  class  FXAlert {
    public static void Alert_Info(String str){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(str);
        alert.showAndWait();
    }

    public static void Alert_WARNING(String str,String headmsg){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("警告");
        alert.setHeaderText(headmsg);
        alert.setContentText(str);
        alert.showAndWait();
    }

    public static void Alert_ERR(String str,String headmsg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(headmsg);
        alert.setContentText(str);
        alert.showAndWait();
    }
    public static boolean Alert_CONFIRMATION(String str,String headmsg){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("选择");
        alert.setHeaderText(headmsg);
        alert.setContentText(str);

        Optional result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
}
