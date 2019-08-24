/**
 * 系统数据导出Excel
 */
package com.its.frd.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.RED;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileGenerator {

	private final int SPLIT_COUNT = 30000; //Excel每个工作簿的行数

	private List<String> fieldName = null; //excel标题数据集

	private List<List<String>> fieldData = null; //excel数据内容	

	private HSSFWorkbook workBook = null;
	
	private XSSFWorkbook _workBook = null;
	/**
	 * 构造器
	 * @param fieldName 结果集的字段名
	 * @param data
	 */
	public ExcelFileGenerator(List<String> fieldName, List<List<String>> fieldData) {

		this.fieldName = fieldName;
		this.fieldData = fieldData;
	}

	/**
	 * 创建HSSFWorkbook对象
	 * @return HSSFWorkbook
	 */
	public XSSFWorkbook createWorkbook() {
		_workBook = new XSSFWorkbook();
		//workBook = new HSSFWorkbook();//创建一个工作薄对象
		int rows = fieldData.size();//总的记录数
		int sheetNum = 0;           //指定sheet的页数

		if (rows % SPLIT_COUNT == 0) {
			sheetNum = rows / SPLIT_COUNT;
		} else {
			sheetNum = rows / SPLIT_COUNT + 1;
		}

		for (int i = 1; i <= sheetNum; i++) {//循环2个sheet的值
			XSSFSheet _sheet = _workBook.createSheet("Page " + i);//使用workbook对象创建sheet对象
			//HSSFSheet sheet = workBook.createSheet("Page " + i);//使用workbook对象创建sheet对象
			XSSFRow _headRow = _sheet.createRow((short) 0); //创建行，0表示第一行（本例是excel的标题）
			//HSSFRow headRow = sheet.createRow((short) 0); //创建行，0表示第一行（本例是excel的标题）
			for (int j = 0; j < fieldName.size(); j++) {//循环excel的标题
				XSSFCell _cell = _headRow.createCell( j);//使用行对象创建列对象，0表示第1列
				//HSSFCell cell = headRow.createCell( j);//使用行对象创建列对象，0表示第1列
				/**************对标题添加样式begin********************/
				
				//设置列的宽度/
				_sheet.setColumnWidth(j, 6000);
				//sheet.setColumnWidth(j, 6000);
				XSSFCellStyle _cellStyle = _workBook.createCellStyle();//创建列的样式对象
				//HSSFCellStyle cellStyle = workBook.createCellStyle();//创建列的样式对象
				XSSFFont _font = _workBook.createFont();//创建字体对象
				//HSSFFont font = workBook.createFont();//创建字体对象
				//字体加粗
				_font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
				//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				//字体颜色变红
				//_font.setColor(new XSSFColor(java.awt.Color.RED));
				//font.setColor(HSSFColor.RED.index);
				//如果font中存在设置后的字体，并放置到cellStyle对象中，此时该单元格中就具有了样式字体
				_cellStyle.setFont(_font);
				//cellStyle.setFont(font);
				
				/**************对标题添加样式end********************/
				
				//添加样式
				_cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				if(fieldName.get(j) != null){
					//将创建好的样式放置到对应的单元格中
					_cell.setCellStyle(_cellStyle);
					//cell.setCellStyle(cellStyle);
					_cell.setCellValue((String) fieldName.get(j));//为标题中的单元格设置值
					//cell.setCellValue((String) fieldName.get(j));//为标题中的单元格设置值
				}else{
					_cell.setCellValue("-");
					//cell.setCellValue("-");
				}
			}
			//分页处理excel的数据，遍历所有的结果
			for (int k = 0; k < (rows < SPLIT_COUNT ? rows : SPLIT_COUNT); k++) {
				if (((i - 1) * SPLIT_COUNT + k) >= rows)//如果数据超出总的记录数的时候，就退出循环
					break;
				XSSFRow _row = _sheet.createRow((short) (k + 1));//创建1行
				//HSSFRow row = sheet.createRow((short) (k + 1));//创建1行
				//分页处理，获取每页的结果集，并将数据内容放入excel单元格
				ArrayList<String> rowList = (ArrayList<String>) fieldData.get((i - 1) * SPLIT_COUNT + k);
				for (int n = 0; n < rowList.size(); n++) {//遍历某一行的结果
					XSSFCell _cell = _row.createCell( n);//使用行创建列对象
					//HSSFCell cell = row.createCell( n);//使用行创建列对象
					if(rowList.get(n) != null){
						_cell.setCellValue((String) rowList.get(n).toString());
						//cell.setCellValue((String) rowList.get(n).toString());
					}else{
						_cell.setCellValue("");
						//cell.setCellValue("");
					}
				}
			}
		}
		return _workBook;
	}

	public void expordExcel(OutputStream os) throws IOException{
		_workBook = createWorkbook();
		_workBook.write(os);//将excel中的数据写到输出流中，用于文件的输出
		os.close();
	}

}
