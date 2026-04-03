import config.Config;
import logic.Logic;
import ui.Panel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Config config = new Config();
        Logic logic = new Logic();

        JFrame frame = new JFrame(config.getFrameTitle());
        frame.add(new Panel(config, logic));
        frame.setSize(config.getFrameWidth(), config.getFrameHeight());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
