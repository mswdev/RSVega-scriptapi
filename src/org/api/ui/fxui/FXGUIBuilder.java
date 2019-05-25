package org.api.ui.fxui;

public class FXGUIBuilder /*extends Application */ {

    /*private final FXGUI fxGui;
    private Stage stage;
    private Scene scene;
    private boolean isInvoking = true;*/

    public FXGUIBuilder(FXGUI fxGui) {
        /*this.fxGui = fxGui;*/
    }

    /*@Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        FXMLLoader loader = new FXMLLoader();
        loader.setClassLoader(getClass().getClassLoader());
        byte[] fxmlBytes = null;
        switch (fx_gui.getFXMLType()) {
            case URL:
                final URLConnection connection = new URL(fx_gui.getFXML()).openConnection();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                fxmlBytes = reader.lines().collect(Collectors.joining("\n")).getBytes();
                break;
            case FILE:
                final Path path = Paths.getBankCache(fx_gui.getFXML());
                fxmlBytes = Files.readAllBytes(path);
                break;
            case STRING:
                fxmlBytes = fxGui.getFXML().getBytes();
                break;
        }

        final Parent root = loader.load(new ByteArrayInputStream(fxml_bytes));
        scene = new Scene(root, fxGui.getWidth(), fxGui.getHeight());

        stage.setTitle(fx_gui.getTitle());
        stage.setResizable(fx_gui.isResizable());
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);

        if (fx_gui.showOnInvoke())
            stage.show();

        Platform.setImplicitExit(false);
        isInvoking = false;
    }*/

    public void invokeGUI() {
        /*new JFXPanel();
        Platform.runLater(() -> {
            try {
                start(new Stage());
                isInvoking = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/
    }

    public FXGUI getGUI() {
        /*return fxGui;*/
        return null;
    }

    public void show() {
        /*if (stage == null)
            return;

        Platform.runLater(() -> stage.show());*/
    }

    public void hide() {
        /*if (stage == null)
            return;

        Platform.runLater(() -> stage.hide());*/
    }

    public void close() {
        /*if (stage == null)
            return;

        Platform.runLater(() -> stage.close());*/
    }

    public boolean isInvoked() {
       /* if (fx_gui == null)
            return false;

        return isInvoking || stage.isShowing();*/

        return false;
    }
}
