package com.promotion.engine.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promotion.engine.model.AdderPromotion;
import com.promotion.engine.model.MultiplierPromotion;
import com.promotion.engine.model.SKUPrice;
import com.promotion.engine.model.SKUUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static com.promotion.engine.utils.Constants.ADDER_SKU;
import static com.promotion.engine.utils.Constants.INPUT_SKU_UNIT_FILE;
import static com.promotion.engine.utils.Constants.MULTIPLIER_SKU;
import static com.promotion.engine.utils.Constants.PROMOTIONS_FILE;
import static com.promotion.engine.utils.Constants.SKU_UNIT_PRICE_FILE;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class PromotionEngineUtils
{
    public static final String PROMOTIONS;
    public static final String SKU_PRICE;
    public static final String INPUT_SKU_UNIT;
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static
    {
        try
        {
            PROMOTIONS = IOUtils.toString(new ClassPathResource(PROMOTIONS_FILE).getInputStream(), UTF_8);
            SKU_PRICE = IOUtils.toString(new ClassPathResource(SKU_UNIT_PRICE_FILE).getInputStream(), UTF_8);
            INPUT_SKU_UNIT = IOUtils.toString(new ClassPathResource(INPUT_SKU_UNIT_FILE).getInputStream(),
                    UTF_8);

        }
        catch (IOException e)
        {
            log.error("Error while initializing the promotions and sku unit price", e);
            throw new IllegalArgumentException("Error while initializing the promotions and sku unit price", e);
        }
    }

    public static List<MultiplierPromotion> getMultiplierPromotions() throws IOException
    {
        JsonNode actualObject = OBJECT_MAPPER.readTree(PROMOTIONS);

        JsonNode multiplierPromotions = actualObject.get(MULTIPLIER_SKU);

        return OBJECT_MAPPER.convertValue(multiplierPromotions, new TypeReference<List<MultiplierPromotion>>()
        {
        });
    }

    public static List<AdderPromotion> getAdderPromotions() throws IOException
    {
        JsonNode actualObject = OBJECT_MAPPER.readTree(PROMOTIONS);

        JsonNode adderPromotions = actualObject.get(ADDER_SKU);

        return OBJECT_MAPPER.convertValue(adderPromotions, new TypeReference<List<AdderPromotion>>()
        {
        });
    }

    public static List<SKUPrice> getSkuPrices() throws IOException
    {
        JsonNode actualObject = OBJECT_MAPPER.readTree(SKU_PRICE);

        JsonNode skuPrices = actualObject.get("sku_price");

        return OBJECT_MAPPER.convertValue(skuPrices, new TypeReference<List<SKUPrice>>()
        {
        });
    }

    public static List<SKUUnit> getSkuUnits() throws IOException
    {
        JsonNode actualObject = OBJECT_MAPPER.readTree(INPUT_SKU_UNIT);

        JsonNode skuPrices = actualObject.get("sku_unit");

        return OBJECT_MAPPER.convertValue(skuPrices, new TypeReference<List<SKUUnit>>()
        {
        });
    }
}
