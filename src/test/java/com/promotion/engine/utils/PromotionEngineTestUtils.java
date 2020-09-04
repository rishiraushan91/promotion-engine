package com.promotion.engine.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promotion.engine.model.SKUUnit;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PromotionEngineTestUtils
{
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static List<SKUUnit> getSkuUnits(String inputUnitFile) throws IOException
    {
        String inputSkuUnit = getInputSkuUnit(inputUnitFile);

        JsonNode actualObject = OBJECT_MAPPER.readTree(inputSkuUnit);

        JsonNode skuPrices = actualObject.get("sku_unit");

        return OBJECT_MAPPER.convertValue(skuPrices, new TypeReference<List<SKUUnit>>()
        {
        });
    }

    public static String getInputSkuUnit(String inputUnitFile)
    {
        String inputSkuUnit;
        try
        {
            inputSkuUnit = IOUtils.toString(new ClassPathResource("input_files/" + inputUnitFile).getInputStream(),
                    UTF_8);
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Error while initializing the promotions and sku unit price", e);
        }
        return inputSkuUnit;
    }
}
