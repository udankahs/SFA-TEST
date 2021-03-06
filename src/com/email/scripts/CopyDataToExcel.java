package com.email.scripts;

import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.testng.Reporter;

import com.email.pom.GotoPermissionSet;
import com.email.pom.SFDCLogin;
import com.email.pom.VerifyFieldPermissions;
import com.email.pom.VerifyObjectSettings;
import com.email.pom.VerifyRecordTypeAssignments;
import com.lib.ExcelLib;

/* 
 * Owner 			: Udanka H S
 * Email Id			: udanka.hs@cognizant.com
 * Department 		: QEA CRM
 * Organization		: Cognizant Technology Solutions
 */

public class CopyDataToExcel extends SFASuperTestNG
{

	@Test
	public void getFields() throws UnsupportedEncodingException {
		SFDCLogin loginPage = new SFDCLogin(driver);
		GotoPermissionSet gotoPermissionSet = new GotoPermissionSet(driver);
		VerifyObjectSettings verifyObjectSettings = new VerifyObjectSettings(driver);
		VerifyRecordTypeAssignments verifyRecordTypeAssignments = new VerifyRecordTypeAssignments(driver);
		VerifyFieldPermissions verifyFieldPermissions = new VerifyFieldPermissions(driver);

		String JarPath = CopyDataToExcel.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String folderPath = JarPath.substring(0, JarPath.lastIndexOf("/") + 1);
		String decodedPath = URLDecoder.decode(folderPath, "UTF-8");

		String dataSheetPath = decodedPath + "Data Sheet/Data Sheet.xls";
		String sheetName = "Login";

		String uname = ExcelLib.getCellValue(dataSheetPath, sheetName, 1, 0);
		String password = ExcelLib.getCellValue(dataSheetPath, sheetName, 1, 1);
		String environment = ExcelLib.getCellValue(dataSheetPath, sheetName, 1, 4);
		String URL = "https://test.salesforce.com";
		if (environment.equalsIgnoreCase("Sandbox"))
		{
			URL = "https://test.salesforce.com";
		} else if (environment.equalsIgnoreCase("Production"))
		{
			URL = "https://login.salesforce.com";
		}

		Reporter.log("<html><head><style>table, th, td { border: 1px solid black; border-collapse: collapse;}</style></head><body>", true);
		loginPage.login(uname, password, URL);

		if (loginPage.verifyLogin())
		{
			int PrmSetCount = ExcelLib.getRowCountofColumn(dataSheetPath, sheetName, 2);
			for (int a = 1; a < PrmSetCount; a++)
			{
				String PrmSet = ExcelLib.getCellValue(dataSheetPath, sheetName, a, 2);

				Reporter.log("<table><tr bgcolor='#F9DFE3'><td>", true);
				Reporter.log("<table align = 'center' style='border:solid;width: 100%'><tr><th><b>PERMISSION SET : " + PrmSet + "</b></th></tr></table>", true);

				gotoPermissionSet.gotoFieldAaccebilty();
				if (gotoPermissionSet.verifyPermissionVisibility(PrmSet))
				{
					Reporter.log("</br><table><tr><th><b>Object Settings</b></th></tr></table>", true);
					//verifyObjectSettings.validateObjectSettings(decodedPath + "Baseline Data/Baseline Excel_" + PrmSet + ".xls");

					int ObjCount = ExcelLib.getRowCountofColumn(dataSheetPath, PrmSet, 0);
					
					try
					{
						verifyObjectSettings.copyObjectSettings(decodedPath + "Baseline Data/Baseline Excel_" + PrmSet + ".xls");
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					for (int b = 1; b <ObjCount; b++)
					{
						String Obj = ExcelLib.getCellValue(dataSheetPath, PrmSet, b, 0);

						Reporter.log("<table><tr bgcolor='#EEF9DF'><td>", true);
						Reporter.log("</br><table align='center' style='width: 100%'><tr><th><b>OBJECT : " + Obj + "</b></th></tr></table></br>", true);

						Reporter.log("</br><table><tr><th><b>Record Type Assignments</b></th></tr></table>", true);
						if(verifyRecordTypeAssignments.copyRecordTypeAssignments(Obj, decodedPath + "Baseline Data/Baseline Excel_" + PrmSet + ".xls"))
						{
							Reporter.log("</br><table><tr><th><b>Field Permissions</b></th></tr></table>", true);
							verifyFieldPermissions.copyFieldPermissions(Obj, decodedPath + "Baseline Data/Baseline Excel_" + PrmSet + ".xls");
						}
						Reporter.log("</td></tr></table></br>", true);
					}
				} else
				{
					// Reporter.log("<table><tr><th><b>ERROR : Permission Set
					// "+PrmSet+" not available in the
					// application!!</b></th></tr></table>", true);
				}
				Reporter.log("</td></tr></table></br>", true);
			}
		} else
		{
			Reporter.log("</br><table><tr><th><b>TEST STATUS :</b></th><td> FAILED -- Incorrect Username or Password </td></tr></table>", true);
		}
		Reporter.log("</body></html>", true);
	}
}
