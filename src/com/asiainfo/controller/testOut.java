package com.asiainfo.controller;

import com.asiainfo.Utils.jdbcUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class testOut {
    /**
     * 将已有数据导出为txt格式
     */
    private static Connection conn;
    private static PreparedStatement pstmt;
    private static String sql = "SELECT * FROM test200";

    public static void main(String[] args) throws Exception {
        //导出数据所在位置及文件名
        File file = new File("D:\\桌面\\testNumber\\test200.txt");
        //开始时间
        Long startTime = System.currentTimeMillis();
        //判断文件是否存在
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        try {
            //获取连接
            conn = jdbcUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery(sql);
            //输出结果集
            while (rs.next()) {
                fileWriter.write(String.valueOf(rs.getInt(1)) + "\t");
                fileWriter.write(rs.getString(2) + "\r\n");
                fileWriter.flush();
            }
            System.out.println("已导出数据！");
            //结束时间
            Long endTime = System.currentTimeMillis();
            System.out.println("用时：" + (endTime - startTime) / 1000 + "S");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            jdbcUtils.close(pstmt, conn);
            fileWriter.close();
        }
    }
}


