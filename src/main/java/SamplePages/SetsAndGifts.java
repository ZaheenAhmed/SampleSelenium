package SamplePages;

import base.Base;

public class SetsAndGifts extends Base {

    //Locators
    private String firstItem = "(//img[contains(@src,'0.jpg')])[1]";
    private String secondItem = "(//img[contains(@src,'0.jpg')])[2]";
    private String thirdItem = "(//img[contains(@src,'0.jpg')])[3]";

    //Page Methods
    public void clickFirstItem () {
        waitForPageToLoad();
        waitForAngularRequestsToFinish();
        click(firstItem);
    }

    public void clickSecondItem () {
        click(secondItem);
    }

    public void clickThirdItem(){
        click(thirdItem);
    }
}
