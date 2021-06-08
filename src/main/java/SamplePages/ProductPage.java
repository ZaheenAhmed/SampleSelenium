package SamplePages;

import base.Base;

public class ProductPage extends Base {

    //Locators

    private String addToBag = "(//button[@title='Add To Bag'])[1]";
    private String goToBag = "//span[contains(@class,'cart-icon')]";


    //Page Methods
    public void clickAddToBag (){
        click(addToBag);
    }

    public void clickGoToBag () {
        click(goToBag);
    }

}
