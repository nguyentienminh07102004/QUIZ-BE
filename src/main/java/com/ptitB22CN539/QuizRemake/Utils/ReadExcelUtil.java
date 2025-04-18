package com.ptitB22CN539.QuizRemake.Utils;

import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReadExcelUtil<T> {
    public List<T> readExcel(MultipartFile file, Integer sheetIndex, Class<T> cla)
            throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook;
        if (Objects.requireNonNull(file.getOriginalFilename()).endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (file.getOriginalFilename().endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            throw new DataInvalidException(ExceptionVariable.FILE_TYPE_NOT_SUPPORT);
        }
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        Iterator<Row> rows = sheet.iterator();
        // đọc tiêu đề
        Row rowTitle = rows.next();
        Iterator<Cell> cells = rowTitle.cellIterator();
        Map<String, Integer> titleMapToIndex = new HashMap<>();
        while (cells.hasNext()) {
            Cell cell = cells.next();
            // tiêu đề luôn là string
            String title = String.join("", cell.getStringCellValue().strip().toLowerCase().split("\\s+"));
            titleMapToIndex.put(title, cell.getColumnIndex());
        }
        Map<Integer, Object> dataValues = new HashMap<>();
        //đọc data
        while (rows.hasNext()) {
            Row row = rows.next();
            Iterator<Cell> cellIterators = row.cellIterator();
            while (cellIterators.hasNext()) {
                Cell cell = cellIterators.next();
                CellType cellType = cell.getCellType();
                Object data = switch (cellType) {
                    case NUMERIC -> cell.getNumericCellValue();
                    case STRING -> StringUtils.hasText(cell.getStringCellValue()) ? cell.getStringCellValue() : null;
                    case BOOLEAN -> cell.getBooleanCellValue();
                    case FORMULA -> {
                        CellValue value = workbook.getCreationHelper().createFormulaEvaluator().evaluate(cell);
                        yield switch (value.getCellType()) {
                            case BOOLEAN -> value.getBooleanValue();
                            case STRING -> value.getStringValue();
                            case NUMERIC -> value.getNumberValue();
                            case FORMULA, _NONE, BLANK, ERROR -> null;
                        };
                    }
                    default -> null;
                };
                dataValues.put(cell.getColumnIndex(), data);
            }
        }
        T t = cla.getDeclaredConstructor().newInstance();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            String fieldName = field.getName().strip().toLowerCase();
            Integer index = titleMapToIndex.get(fieldName);
            Object data = dataValues.get(index);
            // set data
            if (type.equals(Integer.class)) {
                double doubleValue = Double.parseDouble(data.toString());
                field.set(t, (int) doubleValue);
            } else if (type.equals(Long.class)) {
                double doubleValue = Double.parseDouble(data.toString());
                field.set(t, (long) doubleValue);
            } else if (field.getType().isEnum()) {
                field.set(this, Enum.valueOf((Class<Enum>) field.getType(), String.valueOf(data)));
            } else field.set(t, data);
        }
        list.add(t);
        return list;
    }
}
