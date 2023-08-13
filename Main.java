import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.canvas.Canvas;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Canvas canvas;

    private volatile boolean running;

    private Runner runner;
    private Button btn;

    public void start(Stage stage) {
        canvas = new Canvas(640, 480);
        redraw();
        btn = new Button("Start!");
        btn.setStyle("-fx-font-size:16px; -fx-padding:8px; -fx-border-color:black;-fx-border-width:3px ");
        btn.setOnAction(e -> doStartOrStop());
        HBox bottom = new HBox(btn);
        HBox.setMargin(btn, new javafx.geometry.Insets(10));
        bottom.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane(canvas);
        root.setBottom(bottom);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("logo.png")));
        stage.setTitle("Click On Start Button To Start The Art Generation!");
        stage.setResizable(false);
        stage.show();
    }

    private class Runner extends Thread {
        public void run() {
            while (running) {
                Platform.runLater(() -> redraw());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Runner interrupted");
                }
            }
        }
    }

    private void redraw() {
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        if (!running) {
            graphics.setFill(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            return;
        }
        Color randomGray = Color.hsb(1, 0, Math.random());
        graphics.setFill(randomGray);
        graphics.fillRect(0, 0, width, height);
        int artType = (int) (3 * Math.random());

        switch (artType) {
            case 0:
                graphics.setLineWidth(2);
                for (int i = 0; i < 500; i++) {
                    int x1 = (int) (width * Math.random());
                    int y1 = (int) (height * Math.random());
                    int x2 = (int) (width * Math.random());
                    int y2 = (int) (height * Math.random());
                    Color randomHue = Color.hsb(360 * Math.random(), 1, 1);
                    graphics.setStroke(randomHue);
                    graphics.strokeLine(x1, y1, x2, y2);
                }
                break;
            case 1:
                for (int i = 0; i < 200; i++) {
                    int centerX = (int) (width * Math.random());
                    int centerY = (int) (height * Math.random());
                    Color randomHue = Color.hsb(360 * Math.random(), 1, 1);
                    graphics.setStroke(randomHue);
                    graphics.strokeLine(centerX - 50, centerY - 50, 100, 100);
                }
                break;
            default:
                graphics.setStroke(Color.BLACK);
                graphics.setLineWidth(4);
                for (int i = 0; i < 25; i++) {
                    int centerX = (int) (width * Math.random());
                    int centerY = (int) (height * Math.random());
                    int size = 30 + (int) (170 * Math.random());
                    Color randomColor = Color.hsb(360 * Math.random(), Math.random(), Math.random());
                    graphics.setFill(randomColor);
                    graphics.fillRect(centerX - size / 2, centerY - size / 2, size, size);
                    graphics.strokeRect(centerX - size / 2, centerY - size / 2, artType, height);
                }
                break;

        }
    }

    private void doStartOrStop() {
        if (running == false) {
            btn.setText("Stop!");
            runner = new Runner();
            running = true;
            runner.start();
        } else {
            btn.setDisable(true);
            running = false;
            redraw();
            runner.interrupt();

            try {
                runner.join(1000);
            } catch (InterruptedException e) {

            }
            runner = null;
            btn.setText("Start!");
            btn.setDisable(false);

        }
    }

}