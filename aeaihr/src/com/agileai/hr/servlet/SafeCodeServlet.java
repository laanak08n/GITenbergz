package com.agileai.hr.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SafeCodeServlet extends HttpServlet{
	private static final long serialVersionUID = 5583431276675185381L;

	private Font getFont() {  
        Random random = new Random();  
        Font font[] = new Font[5];  
        font[0] = new Font("Arial", Font.PLAIN, 24);  
        font[1] = new Font("Arial", Font.PLAIN, 24);  
        font[2] = new Font("Arial", Font.PLAIN, 24);  
        font[3] = new Font("Arial", Font.PLAIN, 24);  
        font[4] = new Font("Arial", Font.PLAIN, 24);  
        return font[random.nextInt(5)];  
//        return font[1];
    }
  
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  
            throws ServletException, IOException {  
        resp.setContentType("image/jpeg");  
        resp.setHeader("Pragma", "No-cache");  
        resp.setHeader("Cache-Control", "No-cache");  
        resp.setDateHeader("Expires", 0);  
  
        OutputStream os = resp.getOutputStream();  
        int width = 83, height = 26;  
        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);  
  
        Graphics g = image.getGraphics();  
        Color c = g.getColor();  
        g.fillRect(0, 0, width, height);  
  
        char[] ch = "abcdefghjkmnpqrstuvwxyz23456789".toCharArray(); // 随即产生的字符串 不包括 i l(小写L) o（小写O） 1（数字1）0(数字0)  
        int length = ch.length;
        String sRand = "";  
        Random random = new Random();  
        for (int i = 0; i < 4; i++) {  
            g.setFont(getFont());  
            String rand = new Character(ch[random.nextInt(length)]).toString();  
            sRand += rand;
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));  
            g.drawString(rand, 20 * i + 6, 20);  
        }
        /*
        for (int i = 0; i < 20; i++) {  
            int x1 = random.nextInt(width);  
            int y1 = random.nextInt(height);  
            g.drawOval(x1, y1, 2, 2);  
        }
        */
        g.setColor(c);  
        g.dispose();  
  
        req.getSession().setAttribute("safecode", sRand);  
        ImageIO.write(image, "JPEG", os);  
    }  
  
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  
            throws ServletException, IOException {  
        doGet(req, resp);  
    }
}
