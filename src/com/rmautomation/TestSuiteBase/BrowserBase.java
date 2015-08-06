package com.rmautomation.TestSuiteBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.rmautomation.xls.read.Xls_Reader;

public class BrowserBase {	

	//public boolean BrowseralreadyLoaded=false;
	public static Properties Param = null;
	public static WebDriver driver=null;
	public static WebDriver ExistingchromeBrowser;
	public static WebDriver ExistingmozillaBrowser;
	public static WebDriver ExistingIEBrowser;		
	public static String filename =  System.getProperty("user.dir")+"\\TestData\\MyExcel.xls";    //FILE PATH		
	Xls_Reader excel;
	//Method to Read from Param.properties file


	public void loadWebBrowserfromPropertyFile(String browser_type) throws IOException{
		//		Param = new Properties();
		//		FileInputStream fip = new FileInputStream(System.getProperty("user.dir")+"//src//com//rmautomation//config//config.properties");
		//		Param.load(fip);
		//		String browser_type = Param.getProperty("testBrowser");
		checkExistingBrowser(browser_type);		
	}

	//Method to Read from the Excel
	public void loadWebBrowserfromExcelFile() throws IOException{		

		excel = new Xls_Reader(filename, "BrowserDetails");			//SHEETNAME
		//String browser_type = excel.getCellData("BrowserType",1); 		//COLUMN NAME
		//checkExistingBrowser(browser_type);
	}

	public void checkExistingBrowser(String browser_type){
		//Check If any previous WebDriver Browser Instance Is exist then run new test In that existing WebDriver Browser Instance.
		if(browser_type.equalsIgnoreCase("Mozilla") && ExistingmozillaBrowser!=null){
			driver = ExistingmozillaBrowser;
			return ;
		}
		else if(browser_type.equalsIgnoreCase("chrome") && ExistingchromeBrowser!=null){
			driver = ExistingchromeBrowser;
			return ;
		}
		else if(browser_type.equalsIgnoreCase("IE") && ExistingIEBrowser!=null){
			driver = ExistingIEBrowser;
			return ;
		}		

		if(browser_type.equalsIgnoreCase("Mozilla")){
			driver = new FirefoxDriver();
			ExistingmozillaBrowser=driver;				
		}
		else if(browser_type.equalsIgnoreCase("Chrome")){
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//BrowserDrivers//chromedriver.exe");
			driver = new ChromeDriver();
			ExistingchromeBrowser=driver;				
		}
		else if(browser_type.equalsIgnoreCase("IE")){
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"//BrowserDrivers//IEDriverServer.exe");
			DesiredCapabilities cap=DesiredCapabilities.internetExplorer();
			cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(cap);
			ExistingIEBrowser=driver;								
		}			
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();	
	}

	public void closeWebBrowser(){
		driver.close();
		//null browser Instance when closing.
		ExistingchromeBrowser=null;
		ExistingmozillaBrowser=null;
		ExistingIEBrowser=null;
	}
}
