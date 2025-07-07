/*
 * Copyright 2017 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.thake.avro4k.compiler;

import com.github.avrokotlin.avro4k.Avro;
import kotlinx.serialization.KSerializer;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.specific.test.FullRecordV1;
import org.apache.avro.specific.test.FullRecordV2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestGeneratedCode {

    private final static SpecificData MODEL = new SpecificData();
    private final static Schema V1S = Avro.Default.schema(FullRecordV1.Companion.serializer());
    private final static Schema V2S = Avro.Default.schema(FullRecordV2.Companion.serializer());

    @BeforeEach
    public void setUp() {
        MODEL.setCustomCoders(true);
    }

    @Test
    public void withoutSchemaMigration() throws IOException {
        FullRecordV1 src = new FullRecordV1(true, 87231, 731L, 54.2832F, 38.321, "Hi there", null);
        KSerializer<FullRecordV1> serializer = FullRecordV1.Companion.serializer();
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] output = Avro.Default.encodeToByteArray(serializer, src);
        FullRecordV1 dst = Avro.Default.decodeFromByteArray(serializer, output);
        Assertions.assertEquals(src, dst);
    }

    @Test
    public void withSchemaMigration() throws IOException {
        FullRecordV2 src = new FullRecordV2(true, 731, 87231, 38L, 54.2832F, "Hi there", "Hello, world!");
        KSerializer<FullRecordV2> serializerV2 = FullRecordV2.Companion.serializer();
        byte[] output = Avro.Default.encodeToByteArray(serializerV2, src);

        KSerializer<FullRecordV1> serializerV1 = FullRecordV1.Companion.serializer();

        FullRecordV1 dst = Avro.Default.decodeFromByteArray(V2S, serializerV1, output);

        FullRecordV1 expected = new FullRecordV1(true, 87231, 731L, 54.2832F, 38.0, null, "Hello, world!");
        Assertions.assertEquals(expected, dst);
    }
}
