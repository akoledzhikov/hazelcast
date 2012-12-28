/*
 * Copyright (c) 2008-2012, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.nio.serialization;

/**
 * @mdogan 5/17/12
 */
final class SerializationConstants {

    private static byte ID = 0;

    static final byte SERIALIZER_TYPE_DATA = ID--;

    static final byte SERIALIZER_TYPE_OBJECT = ID--;

    static final byte SERIALIZER_TYPE_STRING = ID--;

    static final byte SERIALIZER_TYPE_LONG = ID--;

    static final byte SERIALIZER_TYPE_INTEGER = ID--;

    static final byte SERIALIZER_TYPE_BYTE_ARRAY = ID--;

    static final byte SERIALIZER_TYPE_BOOLEAN = ID--;

    static final byte SERIALIZER_TYPE_EXTERNALIZABLE = ID--;

    static final byte SERIALIZER_TYPE_CLASS = ID--;

    static final byte SERIALIZER_TYPE_DATE = ID--;

    static final byte SERIALIZER_TYPE_BIG_INTEGER = ID--;


    private SerializationConstants() {}
}