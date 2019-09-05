package com.asiainfo.Utils;

import java.io.FileInputStream;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

public class jdbcUtils {
    /**
     * 创建用于存储连接数据库所需的静态资源变量
     */
    private static String driver;//驱动
    private static String url;//url
    private static String uname;//用户名
    private static String pwd;//密码

    //文件的读取，只需要读取一次即可拿到这些值，使用静态代码块
    static {
        try {
            //1.创建用于读取Properties文件的类对象
            Properties pro = new Properties();
            //2.创建当前类的加载器
            ClassLoader classLoader = jdbcUtils.class.getClassLoader();
            //3.获取资源文件URL对象
            URL u = classLoader.getResource("jdbc.properties");
            String path = u.getPath();
            //4.加载配置文件
            pro.load(new FileInputStream(path));
            //5.根据key获取value
            driver = pro.getProperty("jdbc.driver");
            url = pro.getProperty("jdbc.url");
            uname = pro.getProperty("jdbc.uname");
            pwd = pro.getProperty("jdbc.pwd");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //公共获取数据库连接的方法
    public static Connection getConnection() throws Exception {
        //加载驱动
        Class.forName(driver);
        return DriverManager.getConnection(url, uname, pwd);
    }

    //公共的释放资源方法
    public static void close(PreparedStatement pstmt, Connection con) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


