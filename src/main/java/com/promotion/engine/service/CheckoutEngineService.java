package com.promotion.engine.service;

import com.promotion.engine.model.AdderPromotion;
import com.promotion.engine.model.MultiplierPromotion;
import com.promotion.engine.model.SKUCheckoutPrice;
import com.promotion.engine.model.SKUPrice;
import com.promotion.engine.model.SKUUnit;
import com.promotion.engine.model.enums.PromotionTypes;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.promotion.engine.utils.PromotionEngineUtils.getAdderPromotions;
import static com.promotion.engine.utils.PromotionEngineUtils.getMultiplierPromotions;
import static com.promotion.engine.utils.PromotionEngineUtils.getSkuPrices;
import static com.promotion.engine.utils.PromotionEngineUtils.getSkuUnits;

@Slf4j
public class CheckoutEngineService
{
    public Long skuCheckoutPrice(List<SKUUnit> inputSkuUnits, String promotionType) throws IOException
    {
        return checkoutSku(inputSkuUnits, promotionType).stream().mapToLong(SKUCheckoutPrice::getPrice).sum();
    }

    public List<SKUCheckoutPrice> checkoutSku(List<SKUUnit> inputSkuUnits, String promotionType) throws IOException
    {
        List<SKUCheckoutPrice> skuCheckoutPrices = new ArrayList<>();

        LinkedHashMap<String, SKUUnit> inputSkuUnitMap =
                inputSkuUnits.stream().collect(Collectors.toMap(SKUUnit::getSku,
                        Function.identity(), (e1, e2) -> e1, LinkedHashMap::new));
        log.info("\nSKU Units: {}", inputSkuUnitMap);

        List<SKUPrice> skuPrices = getSkuPrices();
        LinkedHashMap<String, Long> skuPriceMap = skuPrices.stream().collect(Collectors.toMap(SKUPrice::getSku,
                SKUPrice::getPrice, (e1, e2) -> e1, LinkedHashMap::new));
        log.info("\nSKU Prices: {}", skuPriceMap);

        if (promotionType.equals(PromotionTypes.MULTIPLIER.getType())
                || promotionType.equals(PromotionTypes.ALL.getType()))
        {
            checkoutWithMultiplierPromotion(skuCheckoutPrices, inputSkuUnitMap, skuPriceMap);
        }

        if (promotionType.equals(PromotionTypes.ADDER.getType())
                || promotionType.equals(PromotionTypes.ALL.getType()))
        {
            checkoutWithAdderPromotion(skuCheckoutPrices, inputSkuUnitMap, skuPriceMap);

        }

        if (!inputSkuUnitMap.isEmpty())
        {
            skuCheckoutPrices.addAll(checkoutWithNormal(inputSkuUnitMap, skuPriceMap));
        }

        return skuCheckoutPrices;
    }

    private void checkoutWithAdderPromotion(List<SKUCheckoutPrice> skuCheckoutPrices,
                                            LinkedHashMap<String, SKUUnit> inputSkuUnitMap,
                                            LinkedHashMap<String, Long> skuPriceMap) throws IOException
    {
        List<AdderPromotion> promotions = getAdderPromotions();

        LinkedHashMap<List<String>, AdderPromotion> adderPromotionMap =
                promotions.stream().collect(Collectors.toMap(AdderPromotion::getSku, Function.identity(),
                        (e1, e2) -> e1, LinkedHashMap::new));
        log.info("\nAdder promotions: {}", adderPromotionMap);

        adderPromotionMap.forEach((key, value) ->
        {
            if (inputSkuUnitMap.containsKey(key.get(0)) && inputSkuUnitMap.containsKey(key.get(1)))
            {
                skuCheckoutPrices.add(applyAdderPromotion(key, value, inputSkuUnitMap, skuPriceMap));
                inputSkuUnitMap.get(key.get(0)).setCheckout(true);
                inputSkuUnitMap.get(key.get(1)).setCheckout(true);
            }

        });
    }

    private void checkoutWithMultiplierPromotion(List<SKUCheckoutPrice> skuCheckoutPrices,
                                                 LinkedHashMap<String, SKUUnit> inputSkuUnitMap,
                                                 LinkedHashMap<String, Long> skuPriceMap) throws IOException
    {
        List<MultiplierPromotion> promotions = getMultiplierPromotions();
        LinkedHashMap<String, MultiplierPromotion> multiplierPromotionMap =
                promotions.stream().collect(Collectors.toMap(MultiplierPromotion::getSku, Function.identity(),
                        (e1, e2) -> e1, LinkedHashMap::new));
        log.info("\nMultiplier promotions: {}", multiplierPromotionMap);

        multiplierPromotionMap.forEach((key, value) ->
        {
            if (inputSkuUnitMap.containsKey(key))
            {
                skuCheckoutPrices.add(applyMultiplierPromotion(key, value, inputSkuUnitMap, skuPriceMap));
                inputSkuUnitMap.get(key).setCheckout(true);
            }
        });
    }

    private SKUCheckoutPrice applyAdderPromotion(List<String> skuKeys, AdderPromotion promotion,
                                                 LinkedHashMap<String, SKUUnit> inputSkuUnitMap,
                                                 LinkedHashMap<String, Long> skuPriceMap)
    {
        long low, high;
        String moreKey;
        if (inputSkuUnitMap.get(skuKeys.get(0)).getUnit() < inputSkuUnitMap.get(skuKeys.get(1)).getUnit())
        {
            low = inputSkuUnitMap.get(skuKeys.get(0)).getUnit();
            high = inputSkuUnitMap.get(skuKeys.get(1)).getUnit();
            moreKey = skuKeys.get(1);
        }
        else if (inputSkuUnitMap.get(skuKeys.get(0)).getUnit() > inputSkuUnitMap.get(skuKeys.get(1)).getUnit())
        {
            low = inputSkuUnitMap.get(skuKeys.get(1)).getUnit();
            high = inputSkuUnitMap.get(skuKeys.get(0)).getUnit();
            moreKey = skuKeys.get(0);
        }
        else
        {
            low = high = inputSkuUnitMap.get(skuKeys.get(0)).getUnit();
            moreKey = skuKeys.get(1);
        }
        long times = low;
        long price = (times * promotion.getPrice()) + (high - times) * skuPriceMap.get(moreKey);

        return SKUCheckoutPrice.builder()
                .sku(moreKey)
                .unit(inputSkuUnitMap.get(moreKey).getUnit())
                .price(price)
                .build();
    }

    private SKUCheckoutPrice applyMultiplierPromotion(String sku, MultiplierPromotion promotion,
                                                      LinkedHashMap<String, SKUUnit> skuUnitMap,
                                                      LinkedHashMap<String, Long> skuPriceMap)
    {
        long unit = skuUnitMap.get(sku).getUnit();
        int times = (int) (unit / promotion.getUnit());
        long price = (times * promotion.getPrice()) + ((unit - (times * promotion.getUnit())) * skuPriceMap.get(sku));

        return SKUCheckoutPrice.builder()
                .sku(sku)
                .unit(unit)
                .price(price)
                .build();
    }

    private List<SKUCheckoutPrice> checkoutWithNormal(LinkedHashMap<String, SKUUnit> skuUnitMap,
                                                      LinkedHashMap<String, Long> skuPriceMap)
    {
        List<SKUCheckoutPrice> checkoutPrices = skuUnitMap.entrySet().stream().filter(e -> !e.getValue().isCheckout())
                .map(e -> SKUCheckoutPrice.builder()
                        .sku(e.getKey())
                        .unit(e.getValue().getUnit())
                        .price(e.getValue().getUnit() * skuPriceMap.get(e.getKey()))
                        .build())
                .collect(Collectors.toList());
        skuUnitMap.forEach((key, value) -> value.setCheckout(true));

        return checkoutPrices;
    }

    public static void main(String[] args) throws IOException
    {
        CheckoutEngineService engineService = new CheckoutEngineService();

        List<SKUCheckoutPrice> checkoutPrices = engineService.checkoutSku(getSkuUnits(), "all");
        log.info("checkoutPrices: {}", checkoutPrices);
        long totalPrice = checkoutPrices.stream().mapToLong(SKUCheckoutPrice::getPrice).sum();
        log.info("***************");
        log.info("Total Price: {}", totalPrice);

    }

}
