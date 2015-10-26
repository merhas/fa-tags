package fr.xorus.software.fa;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by Xorus on 26/10/2015.
 */
public class GUI extends JFrame implements StatusUpdatable {
    JTextField user;
    JPasswordField password;
    JButton login;
    JTextField userToGrab;
    JButton grab;
    FATagStats fats;
    JProgressBar progressBar;
    JLabel status;

    public GUI(FATagStats fats) {
        super("FA Tag Stats");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.fats = fats;
        user = new JTextField();
        password = new JPasswordField();
        userToGrab = new JTextField();
        login = new JButton("Login");
        grab = new JButton("Generate tag stats from faves");
        progressBar = new JProgressBar();
        status = new JLabel("Disconnected");

        user.setSize(200, user.getHeight());
        password.setSize(200, password.getHeight());

        setLayout(new GridLayout(3, 1));

        JPanel loginPane = new JPanel();
        loginPane.setLayout(new GridLayout(3, 2));
        loginPane.add(new JLabel("FA username"));
        loginPane.add(user);
        loginPane.add(new JLabel("FA password"));
        loginPane.add(password);
        loginPane.add(login);

        JPanel grabPane = new JPanel();
        grabPane.setLayout(new GridLayout(2, 2));
        grabPane.add(new JLabel("FA user to grab"));
        grabPane.add(userToGrab);
        grabPane.add(grab);

        add(loginPane);
        add(grabPane);

        JPanel statusPane = new JPanel();
        statusPane.add(status, BorderLayout.EAST);
        statusPane.add(progressBar, BorderLayout.CENTER);

        add(statusPane, BorderLayout.SOUTH);

        userToGrab.setEnabled(false);
        grab.setEnabled(false);

        pack();

        login.addActionListener(e -> login());
        grab.addActionListener(e -> grab());

        Status.registerStatusUpdatable(this);
    }

    private void login() {
        Thread task = new Thread() {
            @Override
            public void run() {
                progressBar.setIndeterminate(true);
                fats.login(user.getText(), password.getText());
                progressBar.setIndeterminate(false);

                if(fats.logguedIn) {
                    user.setEnabled(false);
                    password.setEnabled(false);
                    login.setEnabled(false);
                    userToGrab.setText(fats.fa.user);
                    userToGrab.setEnabled(true);
                    grab.setEnabled(true);
                }
            }
        };

        task.start();
    }

    private void grab() {
        Thread task = new Thread() {
            @Override
            public void run() {
                grab.setEnabled(false);
                fats.generateTagStats(userToGrab.getText());
                grab.setEnabled(true);
            }
        };

        task.start();
    }

    @Override
    public void setStatus(String statusMessage) {
        this.status.setText(statusMessage);
    }

    @Override
    public void setStatus(String statusMessage, Integer progress) {
        progressBar.setValue(progress);
        status.setText(statusMessage);
    }
}
