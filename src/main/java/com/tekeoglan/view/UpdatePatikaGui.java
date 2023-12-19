package com.tekeoglan.view;

import com.tekeoglan.helper.Config;
import com.tekeoglan.helper.Helper;
import com.tekeoglan.model.Patika;

import javax.swing.*;

public class UpdatePatikaGui extends JFrame {
    private JPanel wrapper;
    private JLabel lbl_patika_update;
    private JTextField txt_patika_update;
    private JButton btn_patika_update;
    private Patika patika;

    public UpdatePatikaGui(Patika patika) {
        this.patika = patika;
        add(wrapper);
        setSize(300, 150);
        setLocation(Helper.getScreenCenterCoordinates('x', getSize()), Helper.getScreenCenterCoordinates('y', getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        lbl_patika_update.setText(patika.getName());
        btn_patika_update.addActionListener(e -> {
            if (txt_patika_update.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill the blank space!!");
            } else {
                if (Patika.update(this.patika.getId(), txt_patika_update.getText().trim())) {
                    JOptionPane.showMessageDialog(null, ":: Patika is updated ::");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Can't update patika!!");
                }
            }
        });
    }
}
