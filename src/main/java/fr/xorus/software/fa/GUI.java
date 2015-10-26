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
    JCheckBox downloadFaves;

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
        status.setHorizontalAlignment(SwingConstants.CENTER);

        progressBar.setPreferredSize(new Dimension(100, 15));
        downloadFaves = new JCheckBox("Download images");

        user.setSize(200, user.getHeight());
        password.setSize(200, password.getHeight());

        setLayout(new GridLayout(5, 1));

        JPanel loginPane = new JPanel();
        loginPane.setLayout(new GridLayout(2, 2));
        loginPane.add(new JLabel("FA username"));
        loginPane.add(user);
        loginPane.add(new JLabel("FA password"));
        loginPane.add(password);

        add(loginPane);
        add(login);

        JPanel grabPane = new JPanel();
        grabPane.setLayout(new GridLayout(2, 2));
        grabPane.add(new JLabel("FA user to grab"));
        grabPane.add(userToGrab);
        grabPane.add(downloadFaves);

        add(grabPane);
        add(grab);

        JPanel statusPane = new JPanel();
        statusPane.setLayout(new GridLayout(2, 1));
        statusPane.add(status);
        statusPane.add(progressBar);

        add(statusPane, BorderLayout.SOUTH);

        userToGrab.setEnabled(false);
        grab.setEnabled(false);
        downloadFaves.setEnabled(false);

        pack();

        login.addActionListener(e -> login());
        grab.addActionListener(e -> grab());
        downloadFaves.addActionListener(e -> downloadCheck());

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
                    downloadFaves.setEnabled(true);
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
                downloadFaves.setEnabled(false);
                fats.generateTagStats(userToGrab.getText());
                grab.setEnabled(true);
                downloadFaves.setEnabled(true);
            }
        };

        task.start();
    }

    private void downloadCheck() {
        if(downloadFaves.isSelected()) {
            fats.downloadImages = true;
        } else {
            fats.downloadImages = false;
        }
    }

    @Override
    public void setStatus(String statusMessage) {
        this.status.setText("<html>" + statusMessage + "</html>");
    }

    @Override
    public void setStatus(String statusMessage, Integer progress) {
        progressBar.setValue(progress);
        setStatus(statusMessage);
    }
}
