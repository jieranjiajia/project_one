package org.study.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;

/**
 * 使用easypoi导出excel的Demo
 * 
 * @author ou_qu_sheng
 *
 */
public class PoiExcelDemo {
	public void eportExcel() {
		List<?> list = initData();
		int len = list.size();
		int ind = len / 10000;
		List<?> subList = null;
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		int i = 0;
		Workbook workbook = null;
		Map<String, Object> dataMap = null;
		try {
			for (; i < ind; i++) {
				subList = list.subList(i * 10000, (i + 1) * 10000);
				dataMap = new HashMap<String, Object>();
				dataMap.put("title", new ExportParams("student", "sheet" + 0, ExcelType.XSSF));
				dataMap.put("entity", ExcelModel.class);
				dataMap.put("data", subList);
				dataList.add(dataMap);
			}
			workbook = ExcelExportUtil.exportExcel(dataList, null);
			FileOutputStream out = new FileOutputStream(new File("f://demo.xlsx"));
			workbook.write(out);
			/*
			 * if(sub > 0 ) { subList = list.subList(len-sub, len); Workbook
			 * workbook = ExcelExportUtil.exportBigExcel(params,
			 * ExcelModel.class, subList); FileOutputStream out = new
			 * FileOutputStream(new File("f://demo"+i+".xlsx"));
			 * workbook.write(out); }
			 */
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<ExcelModel> initData() {
		ExcelModel model = null;
		List<ExcelModel> list = new ArrayList<ExcelModel>(20000);
		for (int i = 0; i < 20000; i++) {
			model = new ExcelModel(i, "abc阿达阿德法阿的大大阿打发" + i, "" + (i % 2));
			list.add(model);
		}
		return list;
	}

	public static void main(String[] args) {
		System.out.println("-----");
		long start = System.currentTimeMillis();
		PoiExcelDemo demo = new PoiExcelDemo();
		demo.eportExcel();
		long end = System.currentTimeMillis();
		double time = (double) (end - start) / 1000;
		System.out.println("共经历了" + time + "秒");
		System.exit(0);
	}
}
