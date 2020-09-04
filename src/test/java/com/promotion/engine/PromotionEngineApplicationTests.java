package com.promotion.engine;

import com.promotion.engine.model.SKUUnit;
import com.promotion.engine.service.CheckoutEngineService;
import com.promotion.engine.utils.PromotionEngineUtils;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static com.promotion.engine.utils.PromotionEngineTestUtils.getInputSkuUnit;
import static com.promotion.engine.utils.PromotionEngineTestUtils.getSkuUnits;
import static org.testng.Assert.assertEquals;

public class PromotionEngineApplicationTests
{
    private static List<SKUUnit> skuUnits;

    @Test
    public void contextLoads()
    {
    }

    @Test(description = "test checkout engine with multiplier type promotion")
    public void testCheckoutEngineWithMultiplierPromotion() throws IOException
    {
        skuUnits = getSkuUnits("sku_unit2.json");
        CheckoutEngineService checkoutEngine = new CheckoutEngineService();
        long actual = checkoutEngine.skuCheckoutPrice(skuUnits, "all");

        printPrettySkuCheckoutDetails("sku_unit2.json", actual);

        assertEquals(actual, 370);

    }

    @Test(description = "test checkout engine with adder type promotion")
    public void testCheckoutEngineWithAdderPromotion()
    {

    }

    @Test(description = "test checkout engine with all type promotion")
    public void testCheckoutEngineWithAllPromotion()
    {

    }

    @Test(description = "test checkout engine with no promotion")
    public void testCheckoutEngineWithNonePromotion()
    {

    }

    private void printPrettySkuCheckoutDetails(String inputSkuUnitFile, long checkoutPrice)
    {
        String inputSkuUnit = getInputSkuUnit(inputSkuUnitFile);
        System.out.println("******* Promotions ******");
        System.out.println(new JSONObject(PromotionEngineUtils.PROMOTIONS).toString(2));
        System.out.println("\n******* SKU Price ******");
        System.out.println(new JSONObject(PromotionEngineUtils.SKU_PRICE).toString(2));
        System.out.println("\n******* SKU Input ******");
        System.out.println(new JSONObject(inputSkuUnit).toString(2));
        System.out.println("\n***** Checkout Promotion Price ****");
        System.out.println("*****Total:\t" + checkoutPrice + " *****");
    }

}
