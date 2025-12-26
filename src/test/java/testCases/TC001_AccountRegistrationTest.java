package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class TC001_AccountRegistrationTest extends BaseClass
{
	@Test(groups = {"Regression" ,"Master"})
	public void Verify_Account_Registration() throws InterruptedException
	{
		logger.info("***** Starting TC001_AccountRegistrationTest  ****");
		logger.debug("This is a debug log message");
		try
		{
		
		HomePage hp = new HomePage(driver);
		hp.clickMyAccount();
		logger.info("Clicked on MyAccount Link.. ");
		hp.clickRegister();
		logger.info("Clicked on Register Link.. ");
		
		AccountRegistrationPage rp = new AccountRegistrationPage(driver);
		
		logger.info("Providing customer details...");
		rp.setFirstName(randomString().toUpperCase());
		rp.setLastName(randomString().toUpperCase());
		rp.setEmail(randomString()+"@hmail.com");
		rp.setTelephone(randomNumber());
		
		String password = randomAlphaNumeric();
		rp.setPassword(password);
		rp.setConfirmPassword(password);
		
		
		rp.setPrivacyPolicy();
		rp.clickContinue();
		logger.info("Validating expected message..");
		
		String confmsg = rp.getConfirmationMsg();
		if(confmsg.equals("Your Account Has Been Created!"))
		{
			logger.info("Test passed");
			Assert.assertTrue(true);
		}
		else
		{
			logger.error("Test failed: ");
			logger.debug("Debug logs");
			Assert.assertTrue(false);
		}
		} 
		catch (Exception e)
		{
			Assert.fail("Test failed: " + e.getMessage());
		} 
		finally 
		{
		logger.info("***** Finished TC001_AccountRegistrationTest *****");
		}
	}
	
	
}
