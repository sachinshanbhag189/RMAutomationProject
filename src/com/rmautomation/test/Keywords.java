package com.rmautomation.test;

//Static Imports
import static com.rmautomation.test.DriverScript.APP_LOGS;
import static com.rmautomation.test.DriverScript.CONFIG;
import static com.rmautomation.test.DriverScript.OR;
import static com.rmautomation.test.DriverScript.dbutility;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.rmautomation.TestSuiteBase.BrowserBase;

public class Keywords extends BrowserBase {

	/************************GENERIC KEYWORDS WILL GO HERE********************************/

	public String openBrowser(String object,String data,String data1) throws IOException{		
		APP_LOGS.debug("Opening browser");
		loadWebBrowserfromPropertyFile(data);
		return Constants.KEYWORD_PASS;
	}

	public String navigate(String object,String data,String data1){		
		APP_LOGS.debug("Navigating to URL");
		try{
			driver.navigate().to(data);  //Hits  the given url
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to navigate";
		}
		return Constants.KEYWORD_PASS;
	}

	public String verifyUserLoggedIn(String object,String data,String data1)
	{
		try{
			if((driver.findElement(By.xpath(OR.getProperty("rm_logged_in_user_name").replace("<USERNAME>",CONFIG.getProperty(object))))).isDisplayed())
			{
				APP_LOGS.debug("User is already logged in");
				return Constants.KEYWORD_PASS;
			}
			else
			{
				APP_LOGS.debug("User not logged in");
				return Constants.KEYWORD_FAIL;
			}
		}
		catch(Exception e)
		{
			return Constants.KEYWORD_FAIL;
		}
	}

	public String click(String object,String data,String data1){
		APP_LOGS.debug("Clicking on any element");
		try{
			driver.findElement(By.xpath(OR.getProperty(object))).click();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Not able to click"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public String clickLink(String object,String data,String data1){
		APP_LOGS.debug("Clicking on link ");
		try{
			driver.findElement(By.xpath(OR.getProperty(object))).click();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to click on link"+e.getMessage();
		}

		return Constants.KEYWORD_PASS;
	}

	public String clickLink_linkText(String object,String data,String data1){
		APP_LOGS.debug("Clicking on link ");
		try{
			driver.findElement(By.partialLinkText(OR.getProperty(object))).click();
			return Constants.KEYWORD_PASS;
		}
		catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to find the Link Text.\n"+e.getMessage();	
		}
	}

	public String verify_element_displayed(String object, String data, String data1){
		wait_until_invisibility_of_ajax_loader(null,null,null);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try{
			if(driver.findElement(By.xpath(OR.getProperty(object).replace("<RECORDNAME>", data))).isDisplayed())
			{
				return Constants.KEYWORD_PASS;
			}
			else{
				return Constants.KEYWORD_FAIL+" -- The element is not displayed on the page";
			}
		}
		catch(NoSuchElementException e){
			return Constants.KEYWORD_FAIL+" -- Not able to find the element"+e.getMessage();
		}
	}

	public  String verifyLinkText(String object,String data,String data1){
		APP_LOGS.debug("Verifying link Text");
		try{
			String actual=driver.findElement(By.xpath(OR.getProperty(object))).getText();
			String expected=data;

			if(actual.equals(expected))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL+" -- Link text not verified";

		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Link text not verified"+e.getMessage();

		}
	}

	public  String clickButton(String object,String data,String data1){
		APP_LOGS.debug("Clicking on Button");
		try{
			driver.findElement(By.xpath(OR.getProperty(object))).click();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to click on Button"+e.getMessage();
		}


		return Constants.KEYWORD_PASS;
	}

	public  String verifyButtonText(String object,String data,String data1){
		APP_LOGS.debug("Verifying the button text");
		try{
			String actual=driver.findElement(By.xpath(OR.getProperty(object))).getText();
			String expected=data;

			if(actual.equals(expected))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL+" -- Button text not verified "+actual+" -- "+expected;
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Object not found "+e.getMessage();
		}

	}

	public  String selectList(String object,String data,String data1){
		APP_LOGS.debug("Selecting from list");
		try{
			if(!data.equals(Constants.RANDOM_VALUE)){
				driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
			}else{
				// logic to find a random value in list
				WebElement droplist= driver.findElement(By.xpath(OR.getProperty(object))); 
				List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
				Random num = new Random();
				int index=num.nextInt(droplist_cotents.size());
				String selectedVal=droplist_cotents.get(index).getText();

				driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(selectedVal);
			}
		}catch(Exception e){
			return Constants.KEYWORD_FAIL +" - Could not select from list. "+ e.getMessage();	

		}

		return Constants.KEYWORD_PASS;	
	}

	public String verifyAllListElements(String object,String data,String data1){
		APP_LOGS.debug("Verifying the selection of the list");
		try{	
			WebElement droplist= driver.findElement(By.xpath(OR.getProperty(object))); 
			List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));

			// extract the expected values from OR. properties
			String temp=data;
			String allElements[]=temp.split(",");
			// check if size of array == size if list
			if(allElements.length != droplist_cotents.size())
				return Constants.KEYWORD_FAIL +"- size of lists do not match";	

			for(int i=0;i<droplist_cotents.size();i++){
				if(!allElements[i].equals(droplist_cotents.get(i).getText())){
					return Constants.KEYWORD_FAIL +"- Element not found - "+allElements[i];
				}
			}
		}catch(Exception e){
			return Constants.KEYWORD_FAIL +" - Could not select from list "+ e.getMessage();	

		}


		return Constants.KEYWORD_PASS;	
	}

	public  String verifyListSelection(String object,String data,String data1){
		APP_LOGS.debug("Verifying all the list elements");
		try{
			String expectedVal=data;
			//System.out.println(driver.findElement(By.xpath(OR.getProperty(object))).getText());
			WebElement droplist= driver.findElement(By.xpath(OR.getProperty(object))); 
			List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
			String actualVal=null;
			for(int i=0;i<droplist_cotents.size();i++){
				String selected_status=droplist_cotents.get(i).getAttribute("selected");
				if(selected_status!=null)
					actualVal = droplist_cotents.get(i).getText();			
			}

			if(!actualVal.equals(expectedVal))
				return Constants.KEYWORD_FAIL + "Value not in list - "+expectedVal;

		}catch(Exception e){
			return Constants.KEYWORD_FAIL +" - Could not find list. "+ e.getMessage();	

		}
		return Constants.KEYWORD_PASS;	

	}

	public  String selectRadio(String object,String data,String data1){
		APP_LOGS.debug("Selecting a radio button");
		try{
			String temp[]=object.split(Constants.DATA_SPLIT);
			driver.findElement(By.xpath(OR.getProperty(temp[0])+data+OR.getProperty(temp[1]))).click();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL +"- Not able to find radio button";	

		}

		return Constants.KEYWORD_PASS;	

	}

	public  String verifyRadioSelected(String object,String data,String data1){
		APP_LOGS.debug("Verify Radio Selected");
		try{
			String temp[]=object.split(Constants.DATA_SPLIT);
			String checked=driver.findElement(By.xpath(OR.getProperty(temp[0])+data+OR.getProperty(temp[1]))).getAttribute("checked");
			if(checked==null)
				return Constants.KEYWORD_FAIL+"- Radio not selected";	


		}catch(Exception e){
			return Constants.KEYWORD_FAIL +"- Not able to find radio button";	

		}

		return Constants.KEYWORD_PASS;	

	}

	public  String checkCheckBox(String object,String data,String data1){
		APP_LOGS.debug("Checking checkbox");
		try{
			// true or null
			String checked=driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("checked");
			if(checked==null)// checkbox is unchecked
				driver.findElement(By.xpath(OR.getProperty(object))).click();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" - Could not find checkbox";
		}
		return Constants.KEYWORD_PASS;

	}

	public String unCheckCheckBox(String object,String data,String data1){
		APP_LOGS.debug("Unchecking checkBox");
		try{
			String checked=driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("checked");
			if(checked!=null)
				driver.findElement(By.xpath(OR.getProperty(object))).click();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" - Could not find checkbox";
		}
		return Constants.KEYWORD_PASS;

	}

	public  String verifyCheckBoxSelected(String object,String data,String data1){
		APP_LOGS.debug("Verifying checkbox selected");
		try{
			String checked=driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("checked");
			if(checked!=null)
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL + " - Not selected";

		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" - Could not find checkbox";

		}


	}

	public String verifyText(String object,String data,String data1){
		APP_LOGS.debug("Verifying the text");
		try{
			String actual=driver.findElement(By.xpath(OR.getProperty(object))).getText();
			String expected=data;

			if(actual.equals(expected))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL+" -- text not verified "+actual+" -- "+expected;
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Object not found "+e.getMessage();
		}
	}

	public  String writeInInput(String object,String data,String data1){
		APP_LOGS.debug("Writing in text box");

		try{
			driver.findElement(By.xpath(OR.getProperty(object))).clear();
			driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Unable to write in the text box - "+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public  String verifyTextinInput(String object,String data,String data1){
		APP_LOGS.debug("Verifying the text in input box");
		try{
			String actual = driver.findElement(By.xpath(OR.getProperty(object).replace("<RECORDNAME>", data))).getAttribute("value");
			String expected=data1;

			if(actual.equals(expected)){
				return Constants.KEYWORD_PASS;
			}else{
				return Constants.KEYWORD_FAIL+" Not matching ";
			}

		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Unable to find input box "+e.getMessage();
		}
	}

	public  String verifyNumberinInput(String object,String data,int data1){
		APP_LOGS.debug("Verifying the number entered in input box");
		try{
			String actual = driver.findElement(By.xpath(OR.getProperty(object).replace("<RECORDNAME>", data))).getAttribute("value");
			int expected=data1;

			if(Integer.parseInt(actual)==expected){
				return Constants.KEYWORD_PASS;
			}else{
				return Constants.KEYWORD_FAIL+" -- Not matching ";
			}

		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Unable to find input box "+e.getMessage();
		}
	}

	public  String clickImage(){
		APP_LOGS.debug("Clicking the image");

		return Constants.KEYWORD_PASS;
	}

	public  String verifyFileName(){
		APP_LOGS.debug("Verifying image filename");

		return Constants.KEYWORD_PASS;
	}

	public  String verifyTitle(String object,String data,String data1){
		APP_LOGS.debug("Verifying title");
		try{
			String actualTitle= driver.getTitle();
			String expectedTitle=data;
			if(actualTitle.equals(expectedTitle))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL+" -- Title not verified "+expectedTitle+" -- "+actualTitle;
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Error in retrieving title";
		}		
	}

	public String exist(String object,String data,String data1){
		APP_LOGS.debug("Checking existance of element");
		try{
			driver.findElement(By.xpath(OR.getProperty(object)));
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Object doest not exist";
		}
		return Constants.KEYWORD_PASS;
	}

	public  String synchronize(String object,String data,String data1){
		APP_LOGS.debug("Waiting for page to load");
		((JavascriptExecutor) driver).executeScript(
				"function pageloadingtime()"+
						"{"+
						"return 'Page has completely loaded'"+
						"}"+
				"return (window.onload=pageloadingtime());");

		return Constants.KEYWORD_PASS;
	}

	public  String waitForElementVisibilityinWebTable(String object,String data,String data1){
		wait_until_invisibility_of_ajax_loader(null,null,null);
		APP_LOGS.debug("Waiting for an element to be visible");
		int start=0;
		int time=(int)Double.parseDouble(data1);
		try{
			while(time == start){
				if(driver.findElements(By.xpath(OR.getProperty(object).replace("<RECORDNAME>", data))).size() == 0){
					Thread.sleep(10000L);
					start++;
				}else{
					break;
				}
			}
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+"Unable to wait for element visibilty. \n"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public  String waitForElementInVisibilityinWebTable(String object,String data,String data1){
		APP_LOGS.debug("Waiting for an element to be invisible");
		int start=0;
		int time=(int)Double.parseDouble(data1);
		try{
			while(time == start){				
				if(driver.findElements(By.xpath(OR.getProperty(object).replace("<RECORDNAME>", data))).size()!= 0){
					Thread.sleep(1000L);
					start++;
				}else{
					break;
				}
			}
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+"Unable to wait for element invisibilty. \n"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public  String closeBrowser(String object,String data,String data1){
		APP_LOGS.debug("Closing the browser");
		try{
			driver.close();
			//null browser Instance when closing.
			ExistingchromeBrowser=null;
			ExistingmozillaBrowser=null;
			ExistingIEBrowser=null;
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+"Unable to close browser. Check if its open. \n"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;

	}

	public String pause(String object,String data,String data1) throws NumberFormatException, InterruptedException{
		long time = (long)Double.parseDouble(object);
		Thread.sleep(time*1000L);
		return Constants.KEYWORD_PASS;
	}

	public void captureScreenshot(String filename, String keyword_execution_result) throws IOException{
		// take screen shots
		if(keyword_execution_result.startsWith(Constants.KEYWORD_PASS) && CONFIG.getProperty("screenshot_everystep").equals("Y")){
			captureScreenTool(filename, keyword_execution_result);

		}else if (keyword_execution_result.startsWith(Constants.KEYWORD_FAIL) && CONFIG.getProperty("screenshot_error").equals("Y") ){
			captureScreenTool(filename, keyword_execution_result.split(":")[0]);
		}
	}

	public void captureScreenTool(String filename, String keyword_execution_result) throws IOException{

		DateFormat dateformat_for_foldername = new SimpleDateFormat("dd-MMM-yyyy");    //e.g - 26-APRIL-1986

		//String screenshot_destination = System.getProperty("user.dir")+"\\Snapshots\\WebPageErrors\\"+dateformat_for_foldername.format(new Date());
		//Below line will also work fine
		String screenshot_destination= "Snapshots/"+dateformat_for_foldername.format(new Date());

		new File(screenshot_destination).mkdirs(); 	//To create (if not already present), the above said folder to store screenshots

		File srcfile  = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);  // Here driver is type casted to TakesScreenshot

		DateFormat dateformat_for_filename = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");

		String destFile = filename+"_"+ dateformat_for_filename.format(new Date())+"_"+(keyword_execution_result.replaceAll("[^a-zA-Z0-9_]", ""))+".jpeg";   //method name comes from calling method
		System.out.println(destFile);
		FileUtils.copyFile(srcfile, new File(screenshot_destination + "\\" + destFile));   //move from Temporary location to screenshot destination with valid name
		//having date and time in it.
	}

	public String verifyPageNavigation(String object,String data,String data1){
		APP_LOGS.debug("Verifying whether the navigation was successful");

		try{
			if(data1!=null){
				WebDriverWait wait = new WebDriverWait(driver, 50);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(data1)));
			}
			else{
				APP_LOGS.debug("Test to check that the user is not taken to another page");
			}

			String actualcurrentURL = driver.getCurrentUrl();
			String partofexpectedURL=data;
			if(actualcurrentURL.contains(partofexpectedURL))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL+" -- User not taken to the expected page. Actual url = "+actualcurrentURL;
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Error in retrieving the URL.\n"+e.getMessage();
		}	
	}


	/************************APPLICATION SPECIFIC KEYWORDS WILL GO HERE********************************/


	/**Login to RM Application**/
	public String loginToRMApplication(String object,String data,String data1)
	{
		if(verifyUserLoggedIn(object, data, data1).contains("FAIL"))
		{
			try
			{
				APP_LOGS.debug("Logging in to the Application");
				driver.get(CONFIG.getProperty("testsiteBaseURL"));
				driver.findElement(By.xpath(OR.getProperty("rm_username"))).clear();
				driver.findElement(By.xpath(OR.getProperty("rm_username"))).sendKeys(CONFIG.getProperty("Username"));
				driver.findElement(By.xpath(OR.getProperty("rm_password"))).clear();
				driver.findElement(By.xpath(OR.getProperty("rm_password"))).sendKeys(CONFIG.getProperty("Password"));
				driver.findElement(By.xpath(OR.getProperty("rm_login_button"))).click();	
				Thread.sleep(20*1000L);
				return Constants.KEYWORD_PASS;
				
			}
			catch(Exception e){
				return Constants.KEYWORD_FAIL+" -- "+e.getMessage();
			}
		}
		else
		{
			return Constants.KEYWORD_PASS;
		}
	}

	public String verifyErrorMessageOnLogin(String object,String data,String data1){
		APP_LOGS.debug("Verifying Error Messages Login");
		String error_combine = null;
		try{
			WebElement exx =   driver.findElement(By.xpath(OR.getProperty(object)));

			List<WebElement> allRows  = exx.findElements(By.tagName("li"));

			if(allRows.size()==1)
			{
				String  first_error = driver.findElement(By.xpath(OR.getProperty("rm_login_error_list"))).getText();
				if(first_error.contains("Validation Error: Value is required.")) {				
					error_combine = first_error;
				}
				else{ 
					return Constants.KEYWORD_FAIL+": Error message not matching.\nActual message : \n"+first_error;
				}
			}
			else if(allRows.size()==2)
			{
				String first_error = driver.findElement(By.xpath(OR.getProperty("rm_login_username_error"))).getText();
				String second_error = driver.findElement(By.xpath(OR.getProperty("rm_login_password_error"))).getText();	
				if(first_error.contains("Validation Error: Value is required.") && second_error.contains("Validation Error: Value is required."))
				{
					error_combine = first_error+"\n"+second_error;		
				}
				else 
					return Constants.KEYWORD_FAIL+": Error message not matching.\nActual message : \n"+first_error+"\n"+second_error;
			}
			return Constants.KEYWORD_PASS;
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+": Not able to find the error messages.\n"+e.getMessage();
		}	
	}

	public String clickLanguageLink(String object,String data,String data1){
		APP_LOGS.debug("Clicking on Language link ");
		try{
			driver.findElement(By.xpath("//a[contains(@id,'switch"+data.substring(0, 3)+"')]")).click();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to click on the Langauage <"+data+"> link.\n"+e.getMessage();
		}

		return Constants.KEYWORD_PASS;
	}

	public String logoutApplication(String object,String data,String data1){
		APP_LOGS.debug("Logging out of the Application");
		try{
			((JavascriptExecutor)driver).executeScript("arguments[0].setAttribute('style', 'display: block;');",driver.findElement(By.xpath(OR.getProperty("rm_logout_menu_container"))));
			driver.findElement(By.xpath(OR.getProperty("rm_logout_link"))).click();
		}
		catch(Exception e){
			return Constants.KEYWORD_FAIL+": -- Not able to find the Logout Link on this Page. Check the chain of Execution. \n"+e.getMessage();
		}					
		return Constants.KEYWORD_PASS;

	}

	public String verify_department_count(String object,String data,String data1) throws SQLException{
		int count = 0;
		int db_count =0;
		try{
			wait_until_invisibility_of_ajax_loader(null, null,null);
			
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			
			WebElement data_web_table = driver.findElement(By.xpath(OR.getProperty(object)));

			List<WebElement> allRows = data_web_table.findElements(By.tagName("tr"));			

			db_count = dbutility.get_count_query(data);

			String table_name = data.substring(data.lastIndexOf(" ")+1);  //Gets the last word from the query - table name.

			if(allRows.size()==db_count){
				APP_LOGS.debug("The "+table_name+" count in the Database = "+db_count+" and the count on the Web Page = "+db_count+". The count is matching.");
				return Constants.KEYWORD_PASS;
			}
			else{
				return Constants.KEYWORD_FAIL+" -- The "+table_name+" count in the Database ("+db_count+") and that on the web page ("+allRows.size()+") are not matching";
			}
		}
		catch(NoSuchElementException e){

			return Constants.KEYWORD_FAIL+": -- Not able to find the WebTable or rows inside the WebTable. \n"+e.getMessage();
		}
		catch(SQLException e){
			return Constants.KEYWORD_FAIL+": -- Not able to connect to the Database. \n"+e.getMessage();
		}
		catch(Exception e){
			return Constants.KEYWORD_FAIL+": -- There was some generic Exception.\n"+e.getMessage();
		}
	}

	public String delete_record_if_already_present(String object,String data,String data1) throws SQLException{
		try{
			String result_of_data_found_in_db_table  = verify_data_found_into_database(object,data,data1);
			String result_of_data_found_in_web_table  = verify_data_found_in_webtable(object,data,data1);

			if(result_of_data_found_in_db_table=="PASS" && result_of_data_found_in_web_table == "PASS")
			{				
				//Need to find a way to get some generic method instead of the line below.  - 27th July,15
				if(confirm_delete_record("rm_manage_department_delete_department_link",data,data1).equals("PASS")){
					return Constants.KEYWORD_PASS;
				}
				else
				{
					APP_LOGS.debug("Could not delete the record");
					return Constants.KEYWORD_FAIL+" -- Could not delete the record.";
				}			
			}		
			else if(result_of_data_found_in_db_table.contains("FAIL: -- The data was not added") &&  result_of_data_found_in_web_table.contains("FAIL"))
			{
				return Constants.KEYWORD_PASS;
			}
			else if(result_of_data_found_in_db_table.contains("FAIL: -- The record is not present") &&  result_of_data_found_in_web_table.contains("FAIL"))
			{
				return Constants.KEYWORD_PASS;
			}
			else
			{
				return Constants.KEYWORD_FAIL+" -- Could not verify that the record is already present in (database & webtable) or not\n. Found in db -"+result_of_data_found_in_db_table+" Found in webtable- "+result_of_data_found_in_web_table;
			}
		}
		catch(Exception e){
			APP_LOGS.debug("FAIL - : -- There was some generic Exception in deleting the record.\n"+e.getMessage());
			return Constants.KEYWORD_FAIL+": -- There was some generic Exception in deleting the record.\n"+e.getMessage();
		}
		finally{
			APP_LOGS.debug("delete_record_if_already_present method finished");
		}
	}

	/**Code to get the table name from a prepared statement. Splits the query by 'FROM' and then removes space and finally takes the first word which is obviously the table name*/	
	public String getTableName_from_query(String query)
	{
		String temp_array[] = OR.getProperty(query).split("FROM");			
		String split_second_part =  temp_array[1].substring(temp_array[1].indexOf(" ")+1);			
		int i = split_second_part.indexOf(' ');
		String table_name = split_second_part.substring(0, i);
		return table_name;
	}


	public String verify_data_found_into_database(String object,String data,String data1) throws SQLException{
		APP_LOGS.debug("Verifying that the record is present in the db table or not");
		try{
			wait_until_invisibility_of_ajax_loader(null,null,null);
			
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			
			Thread.sleep(1000L);
			
			int result = dbutility.run_simple_prepared_query_for_count(OR.getProperty(object), data);
			
			System.out.println("------------------------------The result of data in db is "+result);
			APP_LOGS.debug("The result of data in db after adding the data is "+result);

			String table_name =  getTableName_from_query(object);  //Call to get table name from the query.

			if(result==1){
				APP_LOGS.debug("The record is present in the db table - "+table_name);
				return Constants.KEYWORD_PASS;
			}
			else if(result==0)
			{
				APP_LOGS.debug("The record is not present in the db table - "+table_name);
				return Constants.KEYWORD_FAIL+": -- The record is not present in the db table -"+table_name;
			}
			else{
				APP_LOGS.debug("The Prepared Statement was not executed successfully - "+table_name);
				return Constants.KEYWORD_FAIL+": -- The Prepared Statement was not executed successfully - "+table_name;
			}
		}
		catch(SQLException e){
			return Constants.KEYWORD_FAIL+": -- Not able to connect to the Database. \n"+e.getMessage();
		}
		catch(Exception e){
			return Constants.KEYWORD_FAIL+": -- There was some generic Exception.\n"+e.getMessage();
		}
	}

	public String verify_data_found_in_webtable(String object,String data,String data1){
		APP_LOGS.debug("Verifying that the record is present in the web table or not");
		try{
			wait_until_invisibility_of_ajax_loader(null,null,null);

			try{
				driver.findElement(By.xpath(data1.replace("<RECORDNAME>", data)));
				return Constants.KEYWORD_PASS;
			}
			catch(NoSuchElementException e){
				return Constants.KEYWORD_FAIL;
			}
		}
		catch(Exception e){
			return Constants.KEYWORD_FAIL+": -- There was some generic Exception.\n"+e.getMessage();
		}
	}

	public String verify_data_deleted_from_database(String object,String data,String data1) throws SQLException{
		APP_LOGS.debug("Verifying that the record is deleted from the db table or not");
		try{
			wait_until_invisibility_of_ajax_loader(null,null,null);
			
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			
			Thread.sleep(10000L);
			
			int  result = dbutility.run_simple_prepared_query_for_count(OR.getProperty(object), data);
			
			System.out.println("Verify data deleted -------------------------"+result);

			String table_name =  getTableName_from_query(object);  //Call to get table name from the query.

			if(result==0){
				APP_LOGS.debug("The record was also found to be deleted from the db table - "+table_name);
				return Constants.KEYWORD_PASS;
			}
			else if(result==1)
			{
				APP_LOGS.debug("The record was not deleted from the db table - "+table_name);
				return Constants.KEYWORD_FAIL+": -- The record was not deleted from the db table -"+table_name;
			}
			else{
				APP_LOGS.debug("The Prepared Statement was not executed successfully - "+table_name);
				return Constants.KEYWORD_FAIL+": -- The Prepared Statement was not executed successfully - "+table_name;
			}
		}
		catch(SQLException e){
			return Constants.KEYWORD_FAIL+": -- Not able to connect to the Database. \n"+e.getMessage();
		}
		catch(Exception e){
			return Constants.KEYWORD_FAIL+": -- There was some generic Exception.\n"+e.getMessage();
		}
	}

	public String wait_until_invisibility_of_ajax_loader(String object,String data,String data1){
		try{
			APP_LOGS.debug("Waiting for invisibilty of Ajax Loader");
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(OR.getProperty("rm_ajax_loader"))));
			APP_LOGS.debug("Ajax Loader gone");
			return Constants.KEYWORD_PASS;
		}
		catch(NoSuchElementException e){
			APP_LOGS.debug("Ajax Loader not found.  "+e.getMessage());
			return Constants.KEYWORD_FAIL+": Not able to find the ajax loader.\n"+e.getMessage();
		}		
	}

	public String wait_until_visibility_of_ajax_loader(String object,String data,String data1){
		try{

			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("rm_ajax_loader"))));

			return Constants.KEYWORD_PASS;
		}
		catch(NoSuchElementException e){
			APP_LOGS.debug("Ajax Loader not found.  "+e.getMessage());
			return Constants.KEYWORD_FAIL+": Not able to find the ajax loader.\n"+e.getMessage();
		}		
	}

	public String verify_growlError_for_blank_deparment(String object,String data,String data1){
		APP_LOGS.debug("Verifying Error Messages on Page");
		String error_msg = null;
		//We can also add condition to check if the growl error is present or not . If not then we can execute the javascript here to enable the growl error.later disable it.

		try{

			wait_until_invisibility_of_ajax_loader(null,null,null);
			error_msg = driver.findElement(By.xpath(OR.getProperty("rm_manage_department_dept_name_growl_error"))).getText();

			if((CONFIG.getProperty("Language").equals("Norwegian")) && error_msg.equals(OR.getProperty("rm_manage_department_dept_name_growl_error_text_Nor")))
			{
				return Constants.KEYWORD_PASS;
			}
			else if ((CONFIG.getProperty("Language").equals("English")) && error_msg.equals(OR.getProperty("rm_manage_department_dept_name_growl_error_text_Eng")))
			{
				return Constants.KEYWORD_PASS;
			}
			else if((CONFIG.getProperty("Language").equals("Swedish")) && error_msg.equals(OR.getProperty("rm_manage_department_dept_name_growl_error_text_Swe")))
			{
				return Constants.KEYWORD_PASS;
			}
			else{
				return Constants.KEYWORD_FAIL+": Error message for the said Language -'"
						+CONFIG.getProperty("Language")+"' is not matching.\nActual message : "+error_msg+"\nExpected message : "
						+OR.getProperty("rm_manage_department_dept_name_growl_error_text_"+(CONFIG.getProperty("Language").substring(0, 3)));
			}			
		}
		catch(Exception e){
			return Constants.KEYWORD_FAIL+": Not able to find the Error message.\n"+e.getMessage();
		}
	}

	public String confirm_delete_record(String object,String data,String data1){		
		try{
			driver.findElement(By.xpath(OR.getProperty(object).replace("<RECORDNAME>", data))).click();
			driver.findElement(By.xpath(OR.getProperty("rm_manage_department_confirm_dialog_yes"))).click();
			wait_until_invisibility_of_ajax_loader(null, null,null);
			APP_LOGS.debug("The record successfully deleted");
			return Constants.KEYWORD_PASS;			
		}
		catch(NoSuchElementException e){
			APP_LOGS.debug("The Delete Link was not found on the page"+e.getMessage());
			return Constants.KEYWORD_FAIL+": -- Not able to find the Delete Link for the record - "+data+".\n"+e.getMessage();					
		}		
	}

	public String edit_record_in_web_table(String object,String data,String data1){		
		try{
			driver.findElement(By.xpath(OR.getProperty(object).replace("<RECORDNAME>", data))).click();
			wait_until_invisibility_of_ajax_loader(null, null,null);
			APP_LOGS.debug("The Edit link was clicked successfully.");
			return Constants.KEYWORD_PASS;			
		}
		catch(NoSuchElementException e){
			APP_LOGS.debug("The Edit Link was not found on the page"+e.getMessage());
			return Constants.KEYWORD_FAIL+": -- Not able to find the Edit Link for the record - "+data+".\n"+e.getMessage();					
		}		
	}

	public  String writeInInput_editable_textbox_webtable(String object,String data,String data1){
		APP_LOGS.debug("Writing in editable text box in the Webtable");
		String result_verify_input_entered;
		try{			
			wait_until_invisibility_of_ajax_loader(null,null,null);

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			WebElement temp_element = driver.findElement(By.xpath(OR.getProperty(object).replace("<RECORDNAME>", data)));
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

			temp_element.clear();
			String result_verify_input_cleared = verifyTextinInput(object,data,"");	

			if(OR.getProperty(object).contains("Percent")){
				((JavascriptExecutor)driver).executeScript("arguments[0].value='"+(int)Double.parseDouble(data1)+"';",temp_element);   // Need to check for IE and Chrome
				result_verify_input_entered = verifyNumberinInput(object,data,(int)Double.parseDouble(data1));
			}
			else{
				((JavascriptExecutor)driver).executeScript("arguments[0].value='"+data1+"';",temp_element);   // Need to check for IE and Chrome
				result_verify_input_entered = verifyTextinInput(object,data,data1);
			}
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			if(result_verify_input_cleared.equals("PASS") && result_verify_input_entered.equals("PASS")){
				return Constants.KEYWORD_PASS;
			}
			else
				return Constants.KEYWORD_FAIL+" Unable to verify the text entered in the editable text box in the Webtable - ";
		}catch(NoSuchElementException e){
			return Constants.KEYWORD_FAIL+" Unable to find the element(s) on the page - "+e.getMessage();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Unable to write in the editable text box in the Webtable - "+e.getMessage();
		}
	}

	public String save_edited_record(String object, String data, String data1){
		try{
			driver.findElement(By.xpath(OR.getProperty(object).replace("<RECORDNAME>", data))).click();
			wait_until_invisibility_of_ajax_loader(null, null,null);
			APP_LOGS.debug("The record was saved successfully.");
			return Constants.KEYWORD_PASS;			
		}
		catch(NoSuchElementException e){
			APP_LOGS.debug("The Accept Link was not found on the page"+e.getMessage());
			return Constants.KEYWORD_FAIL+": -- Not able to find the Accept Link for the record -  \n"+e.getMessage();					
		}	
	}	

	public String write_in_filter_textbox(String object,String data,String data1){
		try{
			driver.findElement(By.xpath(OR.getProperty(object).replace("<COLUMNNAME>", OR.getProperty(data+CONFIG.getProperty("Language").toLowerCase())))).sendKeys("Sachin");
			return Constants.KEYWORD_PASS;
		}
		catch(NoSuchElementException e){
			return Constants.KEYWORD_FAIL+" -- Not able to find the element to write into.";
		}
	}
}