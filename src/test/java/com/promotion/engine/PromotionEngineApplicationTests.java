package com.promotion.engine;

import com.promotion.engine.model.SKUUnit;
import com.promotion.engine.model.enums.PromotionTypes;
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
        long actual = checkoutEngine.skuCheckoutPrice(skuUnits, PromotionTypes.MULTIPLIER.getType());

        printPrettySkuCheckoutDetails("sku_unit2.json", actual);

        assertEquals(actual, 370);

    }

    @Test(description = "test checkout engine with adder type promotion")
    public void testCheckoutEngineWithAdderPromotion() throws IOException
    {
        skuUnits = getSkuUnits("sku_unit3.json");
        CheckoutEngineService checkoutEngine = new CheckoutEngineService();
        long actual = checkoutEngine.skuCheckoutPrice(skuUnits, PromotionTypes.ADDER.getType());

        printPrettySkuCheckoutDetails("sku_unit3.json", actual);

        assertEquals(actual, 330);
    }

    @Test(description = "test checkout engine with all type promotion")
    public void testCheckoutEngineWithAllPromotion() throws IOException
    {
        skuUnits = getSkuUnits("sku_unit3.json");
        CheckoutEngineService checkoutEngine = new CheckoutEngineService();
        long actual = checkoutEngine.skuCheckoutPrice(skuUnits, PromotionTypes.ALL.getType());

        printPrettySkuCheckoutDetails("sku_unit3.json", actual);

        assertEquals(actual, 280);
    }

    @Test(description = "test checkout engine with no promotion")
    public void testCheckoutEngineWithNonePromotion() throws IOException
    {
        skuUnits = getSkuUnits("sku_unit1.json");
        CheckoutEngineService checkoutEngine = new CheckoutEngineService();
        long actual = checkoutEngine.skuCheckoutPrice(skuUnits, PromotionTypes.NONE.getType());

        printPrettySkuCheckoutDetails("sku_unit3.json", actual);

        assertEquals(actual, 100);
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
        System.out.println("***** Checkout Promotion Price ****");
        System.out.println("***** Total:\t" + checkoutPrice + " *****");
    }

}
