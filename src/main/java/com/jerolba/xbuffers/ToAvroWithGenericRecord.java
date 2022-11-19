package com.jerolba.xbuffers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Stream;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.EnumSymbol;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;

import com.google.common.base.Stopwatch;
import com.jerolba.xbuffers.SampleDataFactory.Org;
import com.jerolba.xbuffers.SampleDataFactory.Type;

public class ToAvroWithGenericRecord {

    public static void main(String[] args) throws IOException {
        run(new SampleDataFactory().getOrganizations(400_000));
    }

    public static void run(List<Org> organizations) throws IOException {
        Stats stats = new Stats("Serialize Avro");
        for (int i = 0; i < 10; i++) {
            Stopwatch sw = Stopwatch.createStarted();
            try (OutputStream os = new FileOutputStream("/tmp/organizations.avro")) {
                serialize(organizations, os);
            }
            stats.add(sw);
        }
        stats.printStat();
    }

    public static void serialize(List<Org> organizations, OutputStream os) throws IOException {

        Schema attrSchema = SchemaBuilder.record("Attribute")
                .fields()
                .requiredString("id")
                .requiredInt("quantity")
                .requiredInt("amount")
                .requiredInt("size")
                .requiredDouble("percent")
                .requiredBoolean("active")
                .endRecord();
        var enumSymbols = Stream.of(Type.values()).map(Type::name).toArray(String[]::new);
        Schema orgsSchema = SchemaBuilder.record("Organizations")
                .fields()
                .requiredString("name")
                .requiredString("category")
                .requiredString("country")
                .name("organizationType").type().enumeration("organizationType").symbols(enumSymbols).noDefault()
                .name("attributes").type().array().items(attrSchema).noDefault()
                .endRecord();
        var typeField = orgsSchema.getField("organizationType").schema();
        EnumMap<Type, EnumSymbol> enums = new EnumMap<>(Type.class);
        enums.put(Type.BAR, new EnumSymbol(typeField, Type.BAR));
        enums.put(Type.BAZ, new EnumSymbol(typeField, Type.BAZ));
        enums.put(Type.FOO, new EnumSymbol(typeField, Type.FOO));

        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(orgsSchema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);
        dataFileWriter.create(orgsSchema, os);
        for (var org : organizations) {
            List<GenericRecord> attrs = new ArrayList<>();
            for (var attr : org.attributes()) {
                GenericRecord attrRecord = new GenericData.Record(attrSchema);
                attrRecord.put("id", attr.id());
                attrRecord.put("quantity", attr.quantity());
                attrRecord.put("amount", attr.amount());
                attrRecord.put("size", attr.size());
                attrRecord.put("percent", attr.percent());
                attrRecord.put("active", attr.active());
                attrs.add(attrRecord);
            }
            GenericRecord orgRecord = new GenericData.Record(orgsSchema);
            orgRecord.put("name", org.name());
            orgRecord.put("category", org.category());
            orgRecord.put("country", org.country());
            orgRecord.put("organizationType", enums.get(org.type()));
            orgRecord.put("attributes", attrs);
            dataFileWriter.append(orgRecord);
        }
        dataFileWriter.close();
    }

}
