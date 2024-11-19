/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.utils;

/**
 *
 * @author boscochuen
 */

public class Header {

    public static String getHeaderHtml(String logoPath, String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("<nav class=\"navbar navbar-expand-lg navbar-light bg-light\">");
        sb.append("<div class=\"container-fluid\">");
        sb.append("<a class=\"navbar-brand\" href=\"#\">");
        sb.append("<img src=\"").append(logoPath).append("\" alt=\"IPE Logo\" style=\"height: 40px;\">");  // 确保CSS控制了高度
        sb.append(title);
        sb.append("</a>");
        sb.append("</div>");
        sb.append("</nav>");
        return sb.toString();
    }
}
