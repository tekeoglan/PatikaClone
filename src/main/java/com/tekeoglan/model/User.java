package com.tekeoglan.model;

import com.tekeoglan.helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User {
    private UserType userType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public enum UserType {
        OPERATOR("operator"),
        USER("user"),
        STUDENT("student"),
        EDUCATOR("educator");

        private final String name;

        private UserType(String s) {
            this.name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    private int id;
    private String fullName;
    private String userName;
    private String password;

    public User() {
    }

    public User(int id, String fullName, String userName, String password, UserType type) {
        this.id = id;
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.userType = type;
    }

    public static ArrayList<User> getList() {
        ArrayList<User> userList = new ArrayList<>();
        String query = "select * from users";
        User obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new User();
                obj.setId(rs.getInt("user_id"));
                obj.setFullName(rs.getString("full_name"));
                obj.setUserName(rs.getString("user_name"));
                obj.setPassword(rs.getString("user_password"));
                UserType type = UserType.valueOf(rs.getString("account_type").toUpperCase());
                obj.setUserType(type);
                userList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userList;
    }

    public static ArrayList<User> getEducators() {
        ArrayList<User> userList = new ArrayList<>();
        String query = "select * from users where account_type = 'educator'";
        User obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new User();
                obj.setId(rs.getInt("user_id"));
                obj.setFullName(rs.getString("full_name"));
                obj.setUserName(rs.getString("user_name"));
                obj.setPassword(rs.getString("user_password"));
                UserType type = UserType.valueOf(rs.getString("account_type").toUpperCase());
                obj.setUserType(type);
                userList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userList;
    }

    public static boolean add(String fullName, String userName, String password, UserType type) {
        String query = "insert into users (full_name, user_name, user_password, account_type) values (?, ?, ?, ?::accounttype)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, fullName);
            pr.setString(2, userName);
            pr.setString(3, password);
            pr.setString(4, type.toString());
            boolean res = pr.executeUpdate() != -1;
            pr.close();
            return res;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean remove(int Id) {
        String query = "delete from users where user_id=?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, Id);
            boolean res = pr.executeUpdate() != -1;
            pr.close();
            return res;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean update(int id, String fullName, String userName, String password, UserType type) {
        String query = "update users set full_name=?, user_name=?, user_password=?, account_type=?::accounttype where user_id=?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, fullName);
            pr.setString(2, userName);
            pr.setString(3, password);
            pr.setString(4, type.toString());
            pr.setInt(5, id);
            boolean res = pr.executeUpdate() != -1;
            pr.close();
            return res;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static User login(String userName, String password) {
        String query = "select * from users where user_name = ? and user_password = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, userName);
            pr.setString(2, password);
            ResultSet rs = pr.executeQuery();
            User obj;
            if(rs.next()) {
                switch(rs.getString("account_type")) {
                    case "operator":
                        obj = new Operator();
                        break;
                    default:
                        obj = new User();
                        break;
                }
                obj.setId(rs.getInt("user_id"));
                obj.setFullName(rs.getString("full_name"));
                obj.setUserName(rs.getString("user_name"));
                obj.setPassword(rs.getString("user_password"));
                UserType type = UserType.valueOf(rs.getString("account_type").toUpperCase());
                obj.setUserType(type);
                pr.close();
                rs.close();
                return obj;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static ArrayList<User> searchUser(String query) {
        ArrayList<User> userList = new ArrayList<>();
        User obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new User();
                obj.setId(rs.getInt("user_id"));
                obj.setFullName(rs.getString("full_name"));
                obj.setUserName(rs.getString("user_name"));
                obj.setPassword(rs.getString("user_password"));
                UserType type = UserType.valueOf(rs.getString("account_type").toUpperCase());
                obj.setUserType(type);
                userList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userList;
    }

    public static String getSearchQuery(String fullName, String userName, String type) {
        String query = "select * from users where full_name like '%{{fullName}}%' and user_name like '%{{userName}}%'";
        query = query.replace("{{fullName}}", fullName);
        query = query.replace("{{userName}}", userName);
        if (!type.isEmpty()) {
            query += " and account_type = '{{type}}'";
            query = query.replace("{{type}}", type);
        }
        return query;
    }
}
