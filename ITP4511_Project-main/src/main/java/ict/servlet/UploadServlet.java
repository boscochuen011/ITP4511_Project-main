/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet;

import ict.bean.EquipmentBean;
import ict.db.EquipmentDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/UploadServlet")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    private EquipmentDB equipmentDB;

    @Override
    public void init() throws ServletException {
        super.init();
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");

        equipmentDB = new EquipmentDB(dbUrl, dbUser, dbPassword);
    }

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Part filePart = request.getPart("file");
    if (filePart != null) {
        try (InputStream inputStream = filePart.getInputStream()) {
            List<EquipmentBean> equipments = parseExcelFile(inputStream);
            for (EquipmentBean equipment : equipments) {
                equipmentDB.addOrUpdateEquipment(equipment);
            }
            // 使用重定向到相同页面或通过Refresh header刷新当前页面
            response.setHeader("Refresh", "0; URL=" + request.getHeader("Referer"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("File upload failed: " + e.getMessage());
        }
    } else {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("File is missing");
    }
}

    private List<EquipmentBean> parseExcelFile(InputStream inputStream) throws IOException {
        List<EquipmentBean> equipments = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // Skip header row
                    continue;
                }

                EquipmentBean equipment = new EquipmentBean();
                equipment.setName(getCellValue(row, 0));
                equipment.setDescription(getCellValue(row, 1));
                equipment.setStatus(getCellValue(row, 2));
                equipment.setLocation(getCellValue(row, 3));
                equipment.setStaffOnly(Boolean.parseBoolean(getCellValue(row, 4)));

                equipments.add(equipment);
            }
        }

        return equipments;
    }

    private String getCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return cell.toString();
        }
    }
}
