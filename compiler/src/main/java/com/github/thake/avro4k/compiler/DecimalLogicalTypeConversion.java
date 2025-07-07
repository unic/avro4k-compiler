package com.github.thake.avro4k.compiler;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

public class DecimalLogicalTypeConversion extends SerializableLogicalTypeConversion {

    public DecimalLogicalTypeConversion() {
        super("decimal", "java.math.BigDecimal", "com.github.avrokotlin.avro4k.serializer.BigDecimalSerializer");
    }

    @Override public String getSerializationAnnotation(Schema schema) {
        LogicalTypes.Decimal logicalType = (LogicalTypes.Decimal) schema.getLogicalType();
        return "@AvroDecimal(scale=" + logicalType.getScale() + ",precision=" + logicalType.getPrecision() + ") "
                + super.getSerializationAnnotation(schema);
    }

    @Override public String getKotlinDefaultString(Schema schema, Object defaultValue) {
        return null;
    }
}
