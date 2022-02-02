package assignment01;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.AssertJUnit;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DDTestUsingJSON 
{
	public static WebDriver driver;
	public static ExtentReports extent;	

	
	@BeforeSuite
	public void setUp()
	{
		extent = new ExtentReports();
		ExtentSparkReporter spark = new ExtentSparkReporter("target/Spark/crossbrowsertesting_"+Helper.getCurrentDateTime()+".html");
		extent.attachReporter(spark);

	}

	@Parameters("browser")
	@BeforeClass



	public void setup1(String browser) throws Exception
	{




		if(browser.equalsIgnoreCase("chrome")){ //set path to chromedriver.exe
			//System.setProperty("webdriver.chrome.driver","C:\\Selenium\\chromedriver_win32\\chromedriver.exe"); //create chrome instance
			WebDriverManager.chromedriver().driverVersion("97").setup();
			driver = new ChromeDriver();
		}
		//Check if parameter passed as 'Edge'
		else if(browser.equalsIgnoreCase("Edge")){ //set path to Edge.exe
			//System.setProperty("webdriver.edge.driver","C:\\Selenium\\edgedriver_win64\\msedgedriver.exe"); //create Edge instance
			WebDriverManager.edgedriver().driverVersion("97").setup();
			driver = new EdgeDriver();
		}
		else{ //If no browser passed throw exception
			throw new Exception("browser is not correct");
		}
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
	}

	@Test(dataProvider="harr")
	void login(String data) throws InterruptedException
	{
		String users[] = data.split(",");
		driver.get("https://opensource-demo.orangehrmlive.com/");
		driver.findElement(By.id("txtUsername")).sendKeys(users[0]); //username
		driver.findElement(By.id("txtPassword")).sendKeys(users[1]); //password
		//Thread.sleep(2000);



		driver.findElement(By.id("btnLogin")).click();
		String act_url = driver.getCurrentUrl();
		String exp_url = "https://opensource-demo.orangehrmlive.com/index.php/dashboard";
		AssertJUnit.assertEquals(act_url, exp_url);



		ExtentTest test = extent.createTest("crossbrowsertesting");
		test.pass("Passed");
		test.log(Status.PASS, "Passed");
		//test.log(Status.FAIL, "fail");
		//test.fail("fail");

		// Exception e=null;
		// test.fail(e);
		//extent.createTest("Exception! <i class='fa fa-frown-o'></i>").fail(new RuntimeException("A runtime exception occurred!"));

	}
	
	@AfterClass
	void tearDown() throws InterruptedException
	{
	//Thread.sleep(2000);
	driver.close();
	}

	@AfterSuite
	public void tearDown1() {

	extent.flush();

	}
	@AfterMethod
	public void name(ITestResult result) {
	//System.out.println("in aftermethod of newTestngClass");

	long a = result.getEndMillis()-result.getStartMillis();

	System.out.println("Time taken to run test is :" +(a/1000)+"seconds");

	}
	@DataProvider(name="harr")
	public String[] readJSON() throws IOException, ParseException
	{
	JSONParser jsonParser = new JSONParser(); //object of jsonparser class(pass the data)
	FileReader reader = new FileReader(".\\jsonfiles\\testdata.json"); //load jsonparser class



	Object obj = jsonParser.parse(reader);



	JSONObject userloginsJsonobj = (JSONObject) obj;
	JSONArray userloginsArray = (JSONArray)userloginsJsonobj.get("userlogins");




	String arr[] = new String[userloginsArray.size()];



	for(int i=0;i<userloginsArray.size();i++)
	{
		JSONObject users = (JSONObject)userloginsArray.get(i);
		String user = (String) users.get("username");
		String pwd = (String) users.get("password");



		arr[i]=user+","+pwd;
		}



		return arr;
	}
}
