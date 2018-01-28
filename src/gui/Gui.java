package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * Created by henry on 12/2/17.
 */
public class Gui extends JFrame {
    JTextArea commandLog = new JTextArea();
    public Gui() {
        setLayout(new BorderLayout());
        add(commandLog, BorderLayout)
        setSize(400, 400);
        setVisible(true);

    }

    public void close() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
