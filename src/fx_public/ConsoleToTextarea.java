package fx_public;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;

import java.io.*;

/***
 * 将控制台信息实时输出到绑定的界面元素上
 */
public class ConsoleToTextarea {

    private volatile Service<String> backgroundThread;
    private boolean updated=false;
    private StringBuffer results=new StringBuffer();
    private boolean exported=false;
    /**
     *
     * @param str
     */
    public void appendText(String str) {

        results.append(str);

        updated = true;
    }

    /**
     *
     */
    public void setupConsoleOutput(TextArea consoleOutput) throws IOException {

        backgroundThread = new Service<String>() {

            protected Task<String> createTask() {

                return new Task<String>() {

                    protected String call() throws Exception {

                        while (true) {

                            if (isCancelled()) {

                                break;
                            }

                            if (updated) {

                                updateValue(results.toString());

                                updated = false;

                                Platform.runLater(() -> {

                                    consoleOutput.setScrollTop(Double.MAX_VALUE);
                                });
                            }

                            if (exported) {

                                consoleOutput.setScrollTop(Double.MAX_VALUE);

                                exported = false;
                            }

                            Thread.sleep(100);
                        }

                        return results.toString();
                    }
                };
            }
        };

        consoleOutput.textProperty().bind(backgroundThread.valueProperty());

        backgroundThread.start();

        backgroundThread.setOnCancelled(new EventHandler<WorkerStateEvent>() {

            public void handle(WorkerStateEvent event) {

            }
        });

        backgroundThread.restart();
        File file=new File("console.log");
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file) {
            @Override
            public void write(byte b[], int off, int len) throws IOException {
               // String ss=new String(b,"utf-8");
                byte aa[]=new byte[len];
                for (int i = 0; i <len ; i++) {
                    aa[i]=b[i];
                }
                appendText(new String(aa,"utf-8"));
                super.write(b,off,len);
            }

        };


        PrintStream ps = new PrintStream(out, true,"utf-8");

        System.setOut(ps);

        System.setErr(ps);
    }
}
