package oil.utils;


import oil.model.Role;
import oil.model.User;
import oil.service.RoleService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by  waiter on 18-10-23  下午10:23.
 *
 * @author waiter
 */
@Component
public class ExcelUtils {

  @Autowired
  private RoleService roleService;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;


  public List<User> getList(InputStream file) throws IOException, InvalidFormatException {
    List<User> users = new LinkedList<>();

      XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file);

    XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
    XSSFRow titleCell = xssfSheet.getRow(0);
    System.out.println(getValue(titleCell.getCell(0)));

    for (int i = 2; i <= xssfSheet.getLastRowNum() ; i++) {
      XSSFRow row = xssfSheet.getRow(i);
      User member = new User();


      member.setUserName(getStringValue(xssfSheet, row.getCell(0)));
      if ("".equals(member.getUsername())) {
        continue;
      }
      member.setNickName(getStringValue(xssfSheet, row.getCell(1)));

      member.setSex(getStringValue(xssfSheet, row.getCell(2)));
      String stringValue = getStringValue(xssfSheet, row.getCell(3));
      if ("".equals(stringValue)) {
        stringValue = member.getUsername() + "@s.upc.edu.cn";
      }
      member.setEmail(stringValue);
      Role roleUser = roleService.getRole("ROLE_USER");
      LinkedList<Role> objects = new LinkedList<>();
      objects.add(roleUser);
      member.setAuthorities(objects);
      member.setNonLocked(true);
      member.setNonExpired(true);
      member.setPassWord(bCryptPasswordEncoder.encode(member.getUsername()));
      users.add(member);

    }

    return users;
  }

  private String getStringValue(XSSFSheet xssfSheet, Cell cell) {
    String value = "";
    if (cell == null) {
      return value;
    }
    if (isMergedRegion(xssfSheet, cell.getRowIndex(), cell.getColumnIndex())) {
      value = getMergedCellValue(xssfSheet, cell);
    } else {
      value = getValue(cell);
    }
    return value;
  }

  /**
   * 读合并单元格值
   *
   * @param sheet 表格
   * @param cell  cell
   * @return value
   */
  private String getMergedCellValue(XSSFSheet sheet, Cell cell) {
    int firstC;
    int lastC;
    int firstR;
    int lastR;
    String value = "";
    List<CellRangeAddress> listCombineCell = sheet.getMergedRegions();
    for (CellRangeAddress ca : listCombineCell) {
      // 获得合并单元格的起始行, 结束行, 起始列, 结束列
      firstC = ca.getFirstColumn();
      lastC = ca.getLastColumn();
      firstR = ca.getFirstRow();
      lastR = ca.getLastRow();
      if (cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR) {
        if (cell.getColumnIndex() >= firstC && cell.getColumnIndex() <= lastC) {
          // 获取合并单元格左上角单元格值
          XSSFRow fRow = sheet.getRow(firstR);
          XSSFCell fCell = fRow.getCell(firstC);
          value = getValue(fCell);
        }
      }
    }
    return value;
  }

  private String getValue(Cell xssfRow) {
    if (xssfRow != null) {
//            if (xssfRow != null) {
//                xssfRow.setCellType(xssfRow.CELL_TYPE_STRING);
//            }
      if (xssfRow.getCellType() == CellType.BOOLEAN) {
        return String.valueOf(xssfRow.getBooleanCellValue());
      } else if (xssfRow.getCellType() == CellType.NUMERIC) {
        String result = "";
        if (xssfRow.getCellStyle().getDataFormat() == 22) {
          // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          double value = xssfRow.getNumericCellValue();
          Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
          result = sdf.format(date);
        } else {
          double value = xssfRow.getNumericCellValue();
          CellStyle style = xssfRow.getCellStyle();
          DecimalFormat format = new DecimalFormat();
          String temp = style.getDataFormatString();
          // 单元格设置成常规
          if (temp.equals("General")) {
            format.applyPattern("#");
          }
          result = format.format(value);
        }
        return result;
      } else {
        return String.valueOf(xssfRow.getStringCellValue());
      }
    } else {
      return "0";
    }
  }


  /**
   * 判断指定的单元格是否是合并单元格
   *
   * @param sheet
   * @param row    行下标
   * @param column 列下标
   * @return
   */
  private boolean isMergedRegion(Sheet sheet, int row, int column) {
    int sheetMergeCount = sheet.getNumMergedRegions();
    for (int i = 0; i < sheetMergeCount; i++) {
      CellRangeAddress range = sheet.getMergedRegion(i);
      int firstColumn = range.getFirstColumn();
      int lastColumn = range.getLastColumn();
      int firstRow = range.getFirstRow();
      int lastRow = range.getLastRow();
      if (row >= firstRow && row <= lastRow) {
        if (column >= firstColumn && column <= lastColumn) {
          return true;
        }
      }
    }
    return false;
  }
}
