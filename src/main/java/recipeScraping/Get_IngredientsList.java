package recipeScraping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Get_IngredientsList {

	// Eliminate list
	public static ArrayList<String> get_EliminateList(String fileName, int columnNumber) throws IOException {
		ArrayList<String> eliminateList = new ArrayList<>();
		FileInputStream inputStream = new FileInputStream(new File(fileName));
		Workbook workbook = WorkbookFactory.create(inputStream);
		Sheet sheet = workbook.getSheetAt(0);

		int rowIndex = 0;
		for (Row row : sheet) {
			if (rowIndex >= 1) {
				Cell cell = row.getCell(columnNumber);
				if (cell != null && cell.getCellType() == CellType.STRING) {
					eliminateList.add(cell.getStringCellValue());
				}
			}
			rowIndex++;
		}

		workbook.close();
		inputStream.close();

		return eliminateList;
	}

	public static ArrayList<String> get_FoodCategory(String fileName, int columnNumber) throws IOException {
		ArrayList<String> eliminateList = new ArrayList<>();
		FileInputStream inputStream = new FileInputStream(new File(fileName));
		Workbook workbook = WorkbookFactory.create(inputStream);
		Sheet sheet = workbook.getSheetAt(3);

		int rowIndex = 0;
		for (Row row : sheet) {
			if (rowIndex >= 1) {
				Cell cell = row.getCell(columnNumber);
				if (cell != null && cell.getCellType() == CellType.STRING) {
					eliminateList.add(cell.getStringCellValue());
				}
			}
			rowIndex++;
		}

		workbook.close();
		inputStream.close();

		return eliminateList;
	}

	public static void main(String[] args) throws IOException {

		String fileName = System.getProperty("user.dir") + "\\Ingredients.xlsx";

		ArrayList<String> eliminateList_FV = get_EliminateList(fileName, 0); // read column 1
		System.out.println(eliminateList_FV);
		ArrayList<String> foodCategory = get_FoodCategory(fileName, 2); // read column 1
		System.out.println(foodCategory);

	}

	// Eliminate list LCHF
	public static ArrayList<String> get_EliminateList_LCHF(String fileName, int columnNumber) throws IOException {
		ArrayList<String> eliminateList = new ArrayList<>();
		FileInputStream inputStream = new FileInputStream(new File(fileName));
		Workbook workbook = WorkbookFactory.create(inputStream);
		Sheet sheet = workbook.getSheetAt(1);

		int rowIndex = 0;
		for (Row row : sheet) {
			if (rowIndex >= 1) {
				Cell cell = row.getCell(columnNumber);
				if (cell != null && cell.getCellType() == CellType.STRING) {
					eliminateList.add(cell.getStringCellValue());
				}
			}
			rowIndex++;
		}

		workbook.close();
		inputStream.close();

		return eliminateList;
	}

}
