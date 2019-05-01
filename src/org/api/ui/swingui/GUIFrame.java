package org.api.ui.swingui;

public class GUIFrame {

    private final GUI gui;

    GUIFrame(GUI gui) {
        this.gui = gui;
    }

    void initialize() {
        gui.getFrame().setTitle(gui.getName());
        gui.getFrame().setSize(gui.getWidth(), gui.getHeight());
        gui.getFrame().setLocationRelativeTo(null);
        gui.getFrame().setResizable(gui.getFrame().isResizable());
    }
}

