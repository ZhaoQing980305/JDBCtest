package com.asiainfo.controller;

import com.asiainfo.Utils.jdbcUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class testInt {
    private static Connection conn;
    private static PreparedStatement pstmt;
    private static String sql = "INSERT INTO dept VALUES (?,?)";

    public static void main(String[] args) {
        try {
            // 获取连接
            conn = jdbcUtils.getConnection();
            //读取从ftp上下载的文件中的数据并加入到数据库表中
            InputStream inputStream = new FileInputStream
                    ("D:\\桌面\\testNumber\\test600.txt");
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputReader);
            // 读取一行
            String line;
            // 设置手动提交 
            conn.setAutoCommit(false);
            //记录导入条数
            int count = 0;
            pstmt = conn.prepareStatement(sql);
            //开始时间
            long start = System.currentTimeMillis();
            while ((line = bufferReader.readLine()) != null) {
                // 按照相应规则截取字符串
                String a[] = line.split("\t");
                String s = "";
                for (int i = 1; i < a.length; i++) {
                    s += a[i] + " ";
                }
                // 去掉字符串开头和结尾的空格
                String ss = s.trim();
                pstmt.setInt(1, Integer.parseInt(a[0]));
                pstmt.setInt(2, Integer.parseInt(ss));
                // 加入批量处理 
                pstmt.addBatch();
                count++;
                if (count % 100000 == 0) {
                    pstmt.executeBatch();
                    pstmt.clearBatch();
                }
            }
            // 执行批量处理 
            pstmt.executeBatch();
            conn.commit(); // 提交 
            System.out.println("数量=" + count);
            System.out.println("运行时间=" + (System.currentTimeMillis() - start) / 1000 + "秒");
            conn.close();
        } catch (Exception e) {
            try {
                if (conn != null) {
                    // 事务回滚
                    conn.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // 释放资源
            jdbcUtils.close(pstmt, conn);
        }
    }
}
