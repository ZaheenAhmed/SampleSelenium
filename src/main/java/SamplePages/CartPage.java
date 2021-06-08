package SamplePages;

import base.Base;

public class CartPage extends Base {

    //Locators

    private String total = "(//span[contains(@class,'wrapper')])[2]";


    //Page Methods
    public String readTotal (){
        String totalAmount = getText(total);
        return totalAmount;
    }

}
