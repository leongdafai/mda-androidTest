package utils;

import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;

public class ExcelUtil {

    /**
     * 读取 Excel，返回 Object[][]（跳过表头）
     */
    public static Object[][] readExcel(String filePath, String sheetName) {
        try (InputStream is = ExcelUtil.class.getClassLoader()
                .getResourceAsStream(filePath)) {
            if (is == null) {
                throw new IllegalArgumentException("找不到文件: " + filePath);
            }

            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheet(sheetName);

            int lastRow = sheet.getLastRowNum();       // 最后一行索引
            int lastCol = sheet.getRow(0).getLastCellNum();  // 最后一列索引

            // ★ 跳过表头（第 0 行），所以数据行数 = lastRow
            Object[][] data = new Object[lastRow][lastCol];

            for (int i = 1; i <= lastRow; i++) {        // 从第 1 行开始（跳过表头）
                Row row = sheet.getRow(i);
                for (int j = 0; j < lastCol; j++) {
                    Cell cell = row.getCell(j);
                    data[i - 1][j] = cell == null ? "" : cell.toString().trim();
                }
            }

            workbook.close();
            return data;

        } catch (Exception e) {
            throw new RuntimeException("读取 Excel 失败: " + filePath, e);
        }
    }
}