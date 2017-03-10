package org.study.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.export.ExcelExportServer;

/**
 * 使用easypoi导出excel的Demo
 * @author ou_qu_sheng
 *
 */
public class PoiExcelDemo {

	
	public void eportExcel() {
		Workbook workbook = new XSSFWorkbook();
		
		ExportParams params = new ExportParams();
		
		params.setType(ExcelType.XSSF);
		params.setSheetName("学生sheet");
		new ExcelExportServer().createSheet(workbook, params, ExcelModel.class, initData());
	
		try {
			FileOutputStream out = new FileOutputStream(new File("f://demo.xlsx"));
			
			workbook.write(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public List<ExcelModel> initData() {
		ExcelModel model = null;
		List<ExcelModel> list = new ArrayList<ExcelModel>(10000);
		for(int i=0; i<10000; i++) {
			model = new ExcelModel(i,"abc"+i,""+(i%2));
			list.add(model);
		}
		return list;
	}
	
	public static void main(String[] args) {
		PoiExcelDemo demo = new PoiExcelDemo();
		demo.eportExcel();
	}
}
