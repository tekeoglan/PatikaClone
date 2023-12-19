package com.tekeoglan.view;

import com.tekeoglan.helper.Config;
import com.tekeoglan.helper.Helper;
import com.tekeoglan.model.Operator;
import com.tekeoglan.model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGui extends JFrame {
    private JPanel wrapper;
    private JPanel wrapper_top;
    private JPanel wrapper_bottom;
    private JLabel lbl_welcome;
    private JLabel lbl_username;
    private JTextField txt_username;
    private JLabel lbl_password;
    private JPasswordField txt_password;
    private JButton btn_login;

    public LoginGui() {
        add(wrapper);
        setSize(400,400);
        setLocation(Helper.getScreenCenterCoordinates('x', getSize()), Helper.getScreenCenterCoordinates('y', getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
        btn_login.addActionListener(e -> {
            if(txt_username.getText().isEmpty() || txt_password.getPassword().length == 0)  {
               JOptionPane.showMessageDialog(null, "Fill the blank spces");
            } else {
                User user = User.login(txt_username.getText().trim(), new String(txt_password.getPassword()));
                if(user == null) {
                    JOptionPane.showMessageDialog(null, "Invalid username or password!!");
                } else {
                    switch (user.getUserType()) {
                        case OPERATOR:
                            OperatorGUI operatorGUI = new OperatorGUI((Operator) user);
                            break;
                        default:
                            break;
                    }
                    dispose();
                }
            }
        });
    }
}
