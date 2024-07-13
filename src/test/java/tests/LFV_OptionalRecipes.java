package tests;

import org.testng.annotations.Test;

import base.BaseTest;
import logger.Log;
import utils.CommonUtils;

public class LFV_OptionalRecipes extends BaseTest {
	
	CommonUtils commonUtils;
	
	@Test
	public void extractOptionalRecipes() throws InterruptedException
	{
		commonUtils = new CommonUtils(driver);
		commonUtils.doClickOnLink();
		Log.info("I am inside extractOptionalRecipes method");	

		
		
		
		
	}
	
}
