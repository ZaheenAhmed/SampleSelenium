package SamplePages;

import base.Base;

public class HomePage extends Base {

    //Locators
    private String bestSellers = "(//div[contains(@class,'loyalty-offer-overlay')])[1]";
    private String skinCare = "(//a[contains(@class,'menu-ref__link')])[3]";
    private String makeup = "(//a[contains(@class,'menu-ref__link')])[4]";
    private String promoButton = " (//a[contains(.,'SHOP NOW')])[1]";
    private String setsAndGiftsTopMenu = "//*[@id=\"node-30\"]/div/div/div[6]";
    private String shopAllSetsAndGifts = "//*[@id=\"node-137512\"]/div/ul/li[1]/a";


    //Page Methods
    public void clickBestSellers (){
        click(bestSellers);
    }

    public void clickSkinCare () {
        click(skinCare);
    }

    public void clickMakeup () {
        click(makeup);
    }

    public void clickPromoButton () {
        waitForPageToLoad();
        click(promoButton);
        waitForPageToLoad();

    }

    public void clickAllSetsAndGifts() {
        waitForPageToLoad();
        mouseOverElement(setsAndGiftsTopMenu);
        click(shopAllSetsAndGifts);
        waitForAngularRequestsToFinish();
    }
}
