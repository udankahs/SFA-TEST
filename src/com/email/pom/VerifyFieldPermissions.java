package com.email.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import com.lib.ExcelLib;

/* Owner 			: Udanka H S
 * Email Id			: udanka.hs@cognizant.com
 * Department 		: QEA CRM
 * Organization		: Cognizant Technology Solutions
 */

public class VerifyFieldPermissions
{
	private WebDriver driver;

	String field = null;
	String srcReadAccess = null;
	String srcWriteAccess = null;

	public VerifyFieldPermissions(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	public void validateFieldPermissions(String Obj, String baselinepath) {

		String xpath = "//td[@colspan='1']//a[text()='" + Obj + "']";
		if (driver.findElements(By.xpath(xpath)).size() > 0)
		{
			driver.findElement(By.xpath("//td[@colspan='1']//a[text()='" + Obj + "']")).click();
			int numOfFields = ExcelLib.getRowCountofColumn(baselinepath, Obj, 2);

			Reporter.log(
					"<table><tr bgcolor='#D7D9D4'><th><b>STATUS </b></th><th><b> FIELD </b></th><th><b> READ ACCESS SOURCE (Excel) </b></th><th><b> READ ACCESS TARGET (Application) </b></th><th><b> EDIT ACCESS SOURCE (Excel)</b></th><th><b> EDIT ACCESS TARGET (Application) </b></th></tr>",
					true);

			for (int a = 2; a < numOfFields; a++)
			{
				field = ExcelLib.getCellValue(baselinepath, Obj, a, 2);
				srcReadAccess = ExcelLib.getCellValue(baselinepath, Obj, a, 3);
				srcWriteAccess = ExcelLib.getCellValue(baselinepath, Obj, a, 4);

				if (driver.findElements(By.xpath("//h3[text()='Field Permissions']/../..//td[text()='" + field + "']")).size() > 0)
				{
					boolean ReadAccess = driver.findElement(By.xpath("//h3[text()='Field Permissions']/../..//td[text()='" + field + "']/../td[2]/input")).isSelected();
					boolean writeAccess = driver.findElement(By.xpath("//h3[text()='Field Permissions']/../..//td[text()='" + field + "']/../td[3]/input")).isSelected();

					String tarReadAccess = String.valueOf(ReadAccess);
					String tarWriteAccess = String.valueOf(writeAccess);

					if (srcReadAccess.equalsIgnoreCase(tarReadAccess) && srcWriteAccess.equalsIgnoreCase(tarWriteAccess))
					{
						Reporter.log("<tr><th><b><font color = 'green'> PASS </b></th> <th><b> " + field + " </b></th><th><b>" + srcReadAccess + "</b></th><th><b>" + tarReadAccess + "</b></th><th><b>"
								+ srcWriteAccess + "</b></th><th><b>" + tarWriteAccess + "</b></th></tr>", true);
					} else
					{
						Reporter.log("<tr><th><b><font color = 'red'> FAIL </b></th> <th><b> " + field + " </b></th><th><b>" + srcReadAccess + "</b></th><th><b>" + tarReadAccess + "</b></th><th><b>"
								+ srcWriteAccess + "</b></th><th><b>" + tarWriteAccess + "</b></th></tr>", true);
					}
				} else
				{
					Reporter.log("<tr><th><b><font color = 'red'>FAIL </b></th> <th><b>" + field + "  </b></th><th colspan=\"4\"><b> Field not found in the application!!</b></th></tr>", true);
				}
			}
			Reporter.log("</table></br>", true);
			driver.findElement(By.xpath("//div[@class='pc_breadcrumbAlign']/a[text()='Object Settings']")).click();
		} else
		{
			Reporter.log("<table><tr><th><b><font color = 'red'>ERROR </b></th> <th><b>Object not found in the application!!</b></th></tr></table></br>", true);
		}
	}
	
	public void copyFieldPermissions(String Obj, String baselinepath) {

		String xpath = "//td[@colspan='1']//a[text()='" + Obj + "']";
		if (driver.findElements(By.xpath(xpath)).size() > 0)
		{
			driver.findElement(By.xpath("//td[@colspan='1']//a[text()='" + Obj + "']")).click();
			if (driver.findElements(By.xpath("//h3[text()='Field Permissions']")).size() > 0)
			{

				int recTypeCount = driver.findElements(By.xpath("//h3[text()='Field Permissions']/../..//td[@colspan='1']/../../tr")).size();
				for (int x = 1; x <= recTypeCount; x++)
				{
					try
					{
						String appfieldName = driver.findElement(By.xpath("//h3[text()='Field Permissions']/../..//td[@colspan='1']/../../tr[" + x + "]/td[1]")).getText();
						Boolean appReadAccess = driver.findElement(By.xpath("//h3[text()='Field Permissions']/../..//td[@colspan='1']/../../tr[" + x + "]/td[2]//input")).isSelected();
						Boolean appWriteAccess = driver.findElement(By.xpath("//h3[text()='Field Permissions']/../..//td[@colspan='1']/../../tr[" + x + "]/td[3]//input")).isSelected();
						
						String appStringReadAccess = String.valueOf(appReadAccess);
						String appStringWriteAccess = String.valueOf(appWriteAccess);
						try
						{
							ExcelLib.writeExcel(baselinepath, Obj, x + 1, 2, appfieldName);
							ExcelLib.writeExcel(baselinepath, Obj, x + 1, 3, appStringReadAccess);
							ExcelLib.writeExcel(baselinepath, Obj, x + 1, 4, appStringWriteAccess);

							Reporter.log("<table><tr><th><b> Sucessfully updated the record with Field " + appfieldName + "!!! </b></th></tr></table>", true);
						} catch (Exception e)
						{
							Reporter.log(
									"<table><tr><th><b> ERROR: Couldn't update the record with Field : " + appfieldName + ". Make sure baseline sheet is not open and has correct naming conventions. </b></th></tr></table>",
									true);
						}
					} catch (NoSuchElementException e)
					{
						
					}
				}

			}
			driver.findElement(By.xpath("//div[@class='pc_breadcrumbAlign']/a[text()='Object Settings']")).click();
		}
		else
		{
			Reporter.log("<table><tr><th><b><font color = 'red'>ERROR </b></th><th><b>Object not found in the application. </b></th></tr></table></br>", true);
		}
	}
}
