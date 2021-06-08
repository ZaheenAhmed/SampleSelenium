package tests;

import SamplePages.CartPage;
import SamplePages.SetsAndGifts;
import SamplePages.HomePage;
import SamplePages.ProductPage;
import base.Base;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class SampleTest extends Base {

    @Test
    public void AddToBagTest(){

        SoftAssert softAssert = new SoftAssert();
        String expectedTotalAmount = "$200.00";

        HomePage homePage = new HomePage();
        SetsAndGifts setsAndGifts = new SetsAndGifts();
        ProductPage productPage = new ProductPage();
        CartPage cartPage = new CartPage();

        homePage.clickAllSetsAndGifts();
        setsAndGifts.clickFirstItem();
        productPage.clickAddToBag();
        productPage.clickGoToBag();
        String actualTotalAmount = cartPage.readTotal();

        softAssert.assertEquals(actualTotalAmount, expectedTotalAmount, "Total amounts do not match");

        softAssert.assertAll();


    }





}
