package com.tekeoglan.view;

import com.tekeoglan.helper.Config;
import com.tekeoglan.helper.Helper;
import com.tekeoglan.helper.Item;
import com.tekeoglan.model.Course;
import com.tekeoglan.model.Operator;
import com.tekeoglan.model.Patika;
import com.tekeoglan.model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OperatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane scr_user_list;
    private JTable tbl_user_list;
    private JPanel pnl_add_user;
    private JLabel lbl_add_user;
    private JLabel lbl_fullname;
    private JTextField txt_fullname;
    private JLabel lbl_username;
    private JTextField txt_username;
    private JLabel lbl_password;
    private JPasswordField txt_password;
    private JComboBox cmb_accounttype;
    private JLabel lbl_accounttype;
    private JButton btn_add_user;
    private JLabel lbl_delete_user;
    private JTextField txt_delete_user;
    private JButton btn_delete_user;
    private JPanel pnl_search;
    private JLabel lbl_search_by_fullname;
    private JTextField txt_search_by_fullname;
    private JLabel lbl_search_by_username;
    private JTextField txt_search_by_username;
    private JComboBox cmb_search_by_type;
    private JButton btn_search_user;
    private JPanel pnl_patikas;
    private JScrollPane scr_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JLabel lbl_patika_add;
    private JTextField txt_patika_add;
    private JButton btn_patika_add;
    private JPanel pnl_courses;
    private JScrollPane scr_courses;
    private JTable tbl_course_list;
    private JPanel pnl_course_add;
    private JLabel lbl_course_add_name;
    private JTextField txt_course_add_name;
    private JLabel lbl_course_add_language;
    private JTextField txt_course_add_language;
    private JLabel lbl_course_add_patika;
    private JComboBox cmb_course_add_patika;
    private JLabel lbl_course_add_educator;
    private JComboBox cmb_course_add_educator;
    private JButton btn_course_add;
    private DefaultTableModel mdl_user_list;
    private DefaultTableModel mdl_patika_list;
    private DefaultTableModel mdl_course_list;
    private JPopupMenu patikaMenu;
    private Object[] row_patika_list;
    private Object[] row_user_list;
    private Object[] row_course_list;
    private Operator operator;

    public OperatorGUI(Operator operator) {
        this.operator = operator;
        add(wrapper);
        setSize(1000, 500);
        setLocation(Helper.getScreenCenterCoordinates('x', getSize()), Helper.getScreenCenterCoordinates('y', getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Welcome : " + operator.getFullName());

        mdl_user_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_user_list = {"ID", "FULL NAME", "USER NAME", "PASSWORD", "USER TYPE"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        row_user_list = new Object[col_user_list.length];
        loadUserModel();

        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);

        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String selectedUserId = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString();
                txt_delete_user.setText(selectedUserId);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        });

        tbl_user_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int user_id = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString());
                String fullName = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 1).toString();
                String userName = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 2).toString();
                String password = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 3).toString();
                String type = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 4).toString();

                User.UserType account_type;
                try {
                    account_type = User.UserType.valueOf(type.toUpperCase());
                } catch (IllegalArgumentException exception) {
                    JOptionPane.showMessageDialog(null, "Please give a valid account type.");
                    throw new RuntimeException(exception);
                }

                if (User.update(user_id, fullName, userName, password, account_type)) {
                    JOptionPane.showMessageDialog(null, "User updated.");
                } else {
                    JOptionPane.showMessageDialog(null, "User can't updated.");
                }
            }
        });

        //start patika list modifications
        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Update");
        JMenuItem deleteMenu = new JMenuItem("Delete");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int selectedId = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
            UpdatePatikaGui updatePatikaGui = new UpdatePatikaGui(Patika.fetch(selectedId));
            updatePatikaGui.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadPatikaModel();
                    loadCourseModel();
                }
            });
            updatePatikaGui.removeAll();
        });

        deleteMenu.addActionListener(e -> {
            int selectedId = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
            if (selectedId > 0 && Patika.remove(selectedId)) {
                JOptionPane.showMessageDialog(null, ":: Patika removed ::");
                loadPatikaModel();
                loadCourseModel();
            } else {
                JOptionPane.showMessageDialog(null, "Can't remove Patika");
            }
        });

        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID", "Patika Name"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(100);

        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selected_row, selected_row);
            }
        });
        //end patika list

        //start course_list modifications
        mdl_course_list = new DefaultTableModel();
        Object[] col_course_list = {"ID", "Course Name", "Course Language","Patika", "Educator"};
        mdl_course_list.setColumnIdentifiers(col_course_list);
        row_course_list = new Object[col_course_list.length];
        loadCourseModel();

        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);
        loadPatikaCombo();

        //end course_list

        btn_add_user.addActionListener(e -> {
            if (txt_username.getText().isEmpty() || txt_username.getText().isEmpty() || txt_password.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill the blank spaces!!");
            } else {
                String username = txt_username.getText();
                String fullName = txt_fullname.getText();
                String password = new String(txt_password.getPassword());
                String type = cmb_accounttype.getSelectedItem().toString();
                if (User.add(username, fullName, password, User.UserType.valueOf(type.toUpperCase()))) {
                    JOptionPane.showMessageDialog(null, "User added.");
                    loadUserModel();
                    txt_username.setText(null);
                    txt_fullname.setText(null);
                    txt_password.setText(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add User.");
                }
            }
        });
        btn_delete_user.addActionListener(e -> {
            if (txt_delete_user.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please give a valid user_id!!");
            } else {
                String userIdTxt = txt_delete_user.getText().trim();
                try {
                    int Id = Integer.parseInt(userIdTxt);
                    boolean response = User.remove(Id);
                    if (response) {
                        JOptionPane.showMessageDialog(null, "User removed!!");
                        loadUserModel();
                        loadCourseModel();
                        txt_delete_user.setText(null);
                    } else {
                        JOptionPane.showMessageDialog(null, "User can't removed!!");
                    }
                } catch (NumberFormatException err) {
                    JOptionPane.showMessageDialog(null, "Please give a valid user_id!!");
                    throw new RuntimeException(err);
                }
            }
        });

        btn_search_user.addActionListener(e -> {
            String fullName = txt_search_by_fullname.getText();
            String userName = txt_search_by_username.getText();
            String type = cmb_search_by_type.getSelectedItem().toString();
            String query = User.getSearchQuery(fullName, userName, type);
            ArrayList<User> filteredUsers = User.searchUser(query);
            loadUserModel(filteredUsers);
        });

        btn_logout.addActionListener(e -> {
            dispose();
        });

        btn_patika_add.addActionListener(e -> {
            if (txt_patika_add.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill the blank spaces!!");
            } else {
                if (Patika.add(txt_patika_add.getText().trim())) {
                    JOptionPane.showMessageDialog(null, "Patika is added!!");
                    loadPatikaModel();
                    txt_patika_add.setText(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Patika can't added!!");
                }
            }
        });

        btn_course_add.addActionListener(e -> {
           Item patikaItem = (Item) cmb_course_add_patika.getSelectedItem();
            Item userItem = (Item) cmb_course_add_educator.getSelectedItem();
            if(txt_course_add_name.getText().isEmpty() || txt_course_add_language.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill the blank spaces!!");
            } else {
                if(Course.add(txt_course_add_name.getText().trim(), txt_course_add_language.getText().trim(), patikaItem.getKey(), userItem.getKey())) {
                    JOptionPane.showMessageDialog(null, ":: Course is added ::");
                    loadCourseModel();
                    txt_course_add_language.setText(null);
                    txt_course_add_name.setText(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Can't add course.");
                }
            }
        });
    }

    private void loadCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);

        int i =0;
        for (Course obj : Course.getList()) {
            i = 0;
            row_course_list[i++] = obj.getId();
            row_course_list[i++] = obj.getName();
            row_course_list[i++] = obj.getLanguage();
            row_course_list[i++] = obj.getPatikaName();
            row_course_list[i++] = obj.getEducatorName();

            mdl_course_list.addRow(row_course_list);
        }
    }

    private void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Patika obj : Patika.getList()) {
            i = 0;
            row_patika_list[i++] = obj.getId();
            row_patika_list[i++] = obj.getName();
            mdl_patika_list.addRow(row_patika_list);
        }
        loadPatikaCombo();
    }

    public void loadUserModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj : User.getList()) {
            int i = 0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getFullName();
            row_user_list[i++] = obj.getUserName();
            row_user_list[i++] = obj.getPassword();
            row_user_list[i++] = obj.getUserType();

            mdl_user_list.addRow(row_user_list);
        }
        loadEducatorCombo();
    }

    public void loadUserModel(ArrayList<User> userArrayList) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj : userArrayList) {
            int i = 0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getFullName();
            row_user_list[i++] = obj.getUserName();
            row_user_list[i++] = obj.getPassword();
            row_user_list[i++] = obj.getUserType();

            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadPatikaCombo() {
        cmb_course_add_patika.removeAllItems();
        for(Patika obj : Patika.getList()) {
            System.out.println(obj.getId() + " " + obj.getName());
            cmb_course_add_patika.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    public void loadEducatorCombo() {
        cmb_course_add_educator.removeAllItems();
        for(User obj : User.getEducators()) {
            cmb_course_add_educator.addItem(new Item(obj.getId(), obj.getFullName()));
        }
    }
}
