package com.email.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

/* Owner 			: Udanka H S
 * Email Id			: udanka.hs@cognizant.com
 * Department 		: QEA CRM
 * Organization		: Cognizant Technology Solutions
 */

public class GotoPermissionSet
{
	private WebDriver driver;

	@FindBy(id = "setupLink")
	private WebElement Setup;

	@FindBy(id = "userNavLabel")
	private WebElement UserName;

	@FindBy(xpath = "//div[@id='userNav-menuItems']/a[contains (text(), 'Setup')]")
	private WebElement Setup2;

	@FindBy(id = "globalHeaderNameMink")
	private WebElement communityUserNav;

	@FindBy(xpath = "//a[contains (text(), 'Setup')]")
	private WebElement Setup3;

	@FindBy(id = "Users_font")
	private WebElement Users;

	@FindBy(id = "PermSets_font")
	private WebElement PermissionSets;

	@FindBy(xpath = "//img[@class='selectArrow']")
	private WebElement Arrow;

	@FindBy(xpath = "//td[text()='200']")
	private WebElement TwoHundred;

	@FindBy(xpath = "//span[text()='Next']")
	private WebElement Next;

	@FindBy(xpath = "//a[contains(text(),'Object Settings')]")
	private WebElement ObjectSettings;

	public GotoPermissionSet(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	public void gotoFieldAaccebilty() {

		if (driver.findElements(By.id("setupLink")).size() > 0)
		{
			Setup.click();
		} else if (driver.findElements(By.id("userNavLabel")).size() > 0)
		{
			UserName.click();
			Setup2.click();
		} else
		{
			communityUserNav.click();
			Setup3.click();
		}

		Users.click();
		PermissionSets.click();
	}

	public boolean verifyPermissionVisibility(String PermSet) {
		boolean PermSetExists = false;

		if (driver.findElements(By.xpath("//a/span[text()='" + PermSet + "']")).size() > 0)
		{
			driver.findElement(By.xpath("//a/span[text()='" + PermSet + "']")).click();
			ObjectSettings.click();
			PermSetExists = true;
		}

		else
		{
			Arrow.click();
			TwoHundred.click();
			if (driver.findElements(By.xpath("//a/span[text()='" + PermSet + "']")).size() > 0)
			{
				driver.findElement(By.xpath("//a/span[text()='" + PermSet + "']")).click();
				ObjectSettings.click();
				PermSetExists = true;
			} else
			{
				try
				{
					Next.click();
					if (driver.findElements(By.xpath("//a/span[text()='" + PermSet + "']")).size() > 0)
					{
						driver.findElement(By.xpath("//a/span[text()='" + PermSet + "']")).click();
						ObjectSettings.click();
						PermSetExists = true;
					} else
					{
						Reporter.log("<table><tr><th><b><font color = 'red'>ERROR </b></th> <th><b>Permission Set : " + PermSet + " not found in the application!!!!</b></th></tr></table>", true);
						PermSetExists = false;
					}
				} catch (Exception e)
				{
					Reporter.log("<table><tr><th><b><font color = 'red'>ERROR </b></th> <th><b>Permission Set : " + PermSet + " not found in the application!!!!</b></th></tr></table>", true);
					PermSetExists = false;
				}
			}
		}
		return PermSetExists;
	}
}
