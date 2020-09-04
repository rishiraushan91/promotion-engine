package com.promotion.engine.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PromotionTypes
{
    ADDER("adder"),
    MULTIPLIER("multiplier"),
    ALL("all"),
    NONE("none");

    private final String type;

    PromotionTypes(String promotionType)
    {
        this.type = promotionType;
    }

    @JsonValue
    public String getType()
    {
        return type;
    }
}
