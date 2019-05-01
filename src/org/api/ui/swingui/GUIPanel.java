package org.api.ui.swingui;

import org.api.ui.SPXStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GUIPanel {

    private final ImageIcon logo = new ImageIcon();
    private final JLabel logoLabel = new JLabel();
    private final JButton startButton = new JButton();
    private final JSeparator separator = new JSeparator();
    private final GUI gui;

    GUIPanel(GUI gui) {
        this.gui = gui;
    }

    void initialize() {
        gui.getPanel().setLayout(new GridBagLayout());
        gui.getPanel().setBackground(SPXStyle.SPX_GRAY.getColor());

        logo.setImage(gui.getLogo());

        gui.getConstraints().gridwidth = 2;
        gui.getConstraints().insets = new Insets(10, 0, 15, 0);
        gui.getConstraints().weighty = 1;
        gui.getConstraints().weightx = 1;

        gui.getConstraints().gridx = 0;
        gui.getConstraints().gridy = 0;
        gui.getConstraints().anchor = GridBagConstraints.NORTH;
        logoLabel.setIcon(logo);
        gui.getPanel().add(logoLabel, gui.getConstraints());

        gui.getConstraints().gridx = 0;
        gui.getConstraints().gridy = 2;
        gui.getConstraints().anchor = GridBagConstraints.SOUTH;
        startButton.setText("START");
        startButton.setPreferredSize(new Dimension(100, 30));
        startButton.setBackground(SPXStyle.SPX_RED.getColor());
        startButton.setForeground(SPXStyle.SPX_WHITE.getColor());
        startButton.setOpaque(true);
        startButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        startButton.setFocusPainted(false);
        startButton.addActionListener(setStartButtonActionListener());
        startButton.addMouseListener(setStartButtonMouseListener());
        gui.getPanel().add(startButton, gui.getConstraints());

        gui.initialize();

        gui.getConstraints().insets = new Insets(80, 15, 0, 15);
        gui.getConstraints().gridx = 0;
        gui.getConstraints().gridy = 0;
        gui.getConstraints().fill = GridBagConstraints.HORIZONTAL;
        gui.getConstraints().anchor = GridBagConstraints.NORTH;
        gui.getPanel().add(separator, gui.getConstraints());
    }

    private ActionListener setStartButtonActionListener() {
        return gui.onStart();
    }

    private MouseListener setStartButtonMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        };
    }
}

