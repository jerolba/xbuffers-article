// automatically generated by the FlatBuffers compiler, do not modify

package com.jerolba.xbuffers.flat;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Organization extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_1_12_0(); }
  public static Organization getRootAsOrganization(ByteBuffer _bb) { return getRootAsOrganization(_bb, new Organization()); }
  public static Organization getRootAsOrganization(ByteBuffer _bb, Organization obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public Organization __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String name() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer nameAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer nameInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }
  public String category() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer categoryAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public ByteBuffer categoryInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 6, 1); }
  public byte type() { int o = __offset(8); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public String country() { int o = __offset(10); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer countryAsByteBuffer() { return __vector_as_bytebuffer(10, 1); }
  public ByteBuffer countryInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 10, 1); }
  public com.jerolba.xbuffers.flat.Attribute attributes(int j) { return attributes(new com.jerolba.xbuffers.flat.Attribute(), j); }
  public com.jerolba.xbuffers.flat.Attribute attributes(com.jerolba.xbuffers.flat.Attribute obj, int j) { int o = __offset(12); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int attributesLength() { int o = __offset(12); return o != 0 ? __vector_len(o) : 0; }
  public com.jerolba.xbuffers.flat.Attribute.Vector attributesVector() { return attributesVector(new com.jerolba.xbuffers.flat.Attribute.Vector()); }
  public com.jerolba.xbuffers.flat.Attribute.Vector attributesVector(com.jerolba.xbuffers.flat.Attribute.Vector obj) { int o = __offset(12); return o != 0 ? obj.__assign(__vector(o), 4, bb) : null; }

  public static int createOrganization(FlatBufferBuilder builder,
      int nameOffset,
      int categoryOffset,
      byte type,
      int countryOffset,
      int attributesOffset) {
    builder.startTable(5);
    Organization.addAttributes(builder, attributesOffset);
    Organization.addCountry(builder, countryOffset);
    Organization.addCategory(builder, categoryOffset);
    Organization.addName(builder, nameOffset);
    Organization.addType(builder, type);
    return Organization.endOrganization(builder);
  }

  public static void startOrganization(FlatBufferBuilder builder) { builder.startTable(5); }
  public static void addName(FlatBufferBuilder builder, int nameOffset) { builder.addOffset(0, nameOffset, 0); }
  public static void addCategory(FlatBufferBuilder builder, int categoryOffset) { builder.addOffset(1, categoryOffset, 0); }
  public static void addType(FlatBufferBuilder builder, byte type) { builder.addByte(2, type, 0); }
  public static void addCountry(FlatBufferBuilder builder, int countryOffset) { builder.addOffset(3, countryOffset, 0); }
  public static void addAttributes(FlatBufferBuilder builder, int attributesOffset) { builder.addOffset(4, attributesOffset, 0); }
  public static int createAttributesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startAttributesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endOrganization(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }

  public static final class Vector extends BaseVector {
    public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) { __reset(_vector, _element_size, _bb); return this; }

    public Organization get(int j) { return get(new Organization(), j); }
    public Organization get(Organization obj, int j) {  return obj.__assign(__indirect(__element(j), bb), bb); }
  }
}

