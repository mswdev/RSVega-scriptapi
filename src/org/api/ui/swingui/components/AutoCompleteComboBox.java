package org.api.ui.swingui.components;

import org.api.ui.SPXStyle;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AutoCompleteComboBox extends JComboBox<Object> {

    private static final long serialVersionUID = -5325656472664803718L;
    private JTextField textField = null;
    private int caretPosition = 0;

    public AutoCompleteComboBox(Object[] elements, String placeHolder) {
        this(elements, placeHolder, 200, 25);
    }

    public AutoCompleteComboBox(Object[] elements, String placeHolder, int width, int height) {
        super(elements);
        setPreferredSize(new Dimension(width, height));
        listenForMatch(new BasicComboBoxEditor());
        setUI(getComboBoxUI());
        setEditable(true);
        setSelectedItem(placeHolder);
    }

    public BasicComboBoxUI getComboBoxUI() {
        return new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                final JButton button = new BasicArrowButton(BasicArrowButton.SOUTH, SPXStyle.SPX_WHITE.getColor(), SPXStyle.SPX_WHITE.getColor(), SPXStyle.SPX_WHITE.getColor(), SPXStyle.SPX_WHITE.getColor());
                button.setBackground(SPXStyle.SPX_RED.getColor());
                button.setBorder(BorderFactory.createLineBorder(SPXStyle.SPX_RED.getColor(), 3, true));
                return button;
            }
        };
    }

    public void setSelectedIndex(int index) {
        super.setSelectedIndex(index);
        textField.setText(getItemAt(index).toString());
        textField.setSelectionEnd(caretPosition + textField.getText().length());
        textField.moveCaretPosition(caretPosition);
    }

    private void listenForMatch(ComboBoxEditor editor) {
        super.setEditor(editor);
        textField = (JTextField) editor.getEditorComponent();

        textField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                final char key = event.getKeyChar();
                if (!Character.isLetterOrDigit(key))
                    return;

                if (Character.isSpaceChar(key))
                    return;

                caretPosition = textField.getCaretPosition();
                String text = "";
                try {
                    text = textField.getText(0, caretPosition).toLowerCase();
                } catch (javax.swing.text.BadLocationException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < getItemCount(); i++) {
                    final String ELEMENT = getItemAt(i).toString().toLowerCase();
                    if (ELEMENT.startsWith(text)) {
                        setSelectedIndex(i);

                        return;
                    }
                }
            }
        });
    }

}
