package com.tekeoglan.model;

import com.tekeoglan.helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Course {
    private int id;
    private String educatorName;
    private String patikaName;
    private String name;
    private String language;

    public Course(int id, String name, String language, String educatorName, String patikaName) {
        this.id = id;
        this.educatorName = educatorName;
        this.patikaName = patikaName;
        this.name = name;
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPatikaName() {
        return patikaName;
    }

    public void setPatikaName(String patikaName) {
        this.patikaName = patikaName;
    }

    public String getEducatorName() {
        return educatorName;
    }

    public void setEducatorName(String educatorName) {
        this.educatorName = educatorName;
    }

    public static ArrayList<Course> getList() {
        ArrayList<Course> courses = new ArrayList<>();

        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("select courses.course_id, courses.course_name, courses.course_language, users.full_name as educator_name, patika.name as patika_name from courses\n" +
                    "join users on courses.user_id = users.user_id\n" +
                    "join patika on courses.patika_id = patika.id");

            while (rs.next()) {
                int id = rs.getInt("course_id");
                String name = rs.getString("course_name");
                String lang = rs.getString("course_language");
                String educator = rs.getString("educator_name");
                String patika = rs.getString("patika_name");
                obj = new Course(id, name, lang, educator, patika);
                courses.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return courses;
    }

    public static boolean add(String name, String language, int patikaID, int educatorID) {
        String query = "insert into courses(course_name, course_language, user_id, patika_id) values(?,?,?,?)";
        PreparedStatement pr;
        try {
            pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setString(2, language);
            pr.setInt(3, educatorID);
            pr.setInt(4, patikaID);
            boolean res = pr.executeUpdate() != -1;
            pr.close();
            return res;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
