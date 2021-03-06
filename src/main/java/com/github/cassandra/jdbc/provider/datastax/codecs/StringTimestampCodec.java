/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package com.github.cassandra.jdbc.provider.datastax.codecs;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import org.joda.time.Instant;

import java.nio.ByteBuffer;

import static com.datastax.driver.core.ParseUtils.*;

public class StringTimestampCodec extends TypeCodec<String> {
    public static final StringTimestampCodec instance = new StringTimestampCodec();

    private StringTimestampCodec() {
        super(DataType.timestamp(), String.class);
    }

    @Override
    public ByteBuffer serialize(String value, ProtocolVersion protocolVersion) {
        if (value == null) {
            return null;
        }

        if (value.indexOf(' ') == 10 && value.indexOf('Z') < 0) {
            StringBuilder builder = new StringBuilder(value).append('Z');
            builder.setCharAt(10, 'T');
            value = builder.toString();
        }

        return bigint().serializeNoBoxing(Instant.parse(value).getMillis(), protocolVersion);
    }

    @Override
    public String deserialize(ByteBuffer bytes, ProtocolVersion protocolVersion) {
        if (bytes == null || bytes.remaining() == 0)
            return null;
        long millis = bigint().deserializeNoBoxing(bytes, protocolVersion);
        return new Instant(millis).toString();
    }

    @Override
    public String format(String value) {
        if (value == null)
            return "NULL";
        return quote(value);
    }

    @Override
    public String parse(String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("NULL"))
            return null;
        // strip enclosing single quotes, if any
        if (isQuoted(value))
            value = unquote(value);

        return value;
    }
}
