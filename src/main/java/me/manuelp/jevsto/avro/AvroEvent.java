package me.manuelp.jevsto.avro;

import static me.manuelp.jevsto.avro.AvroRead.readSchemaFromResource;

import fj.F;
import java.io.IOException;
import me.manuelp.jevsto.dataTypes.AggregateType;
import me.manuelp.jevsto.dataTypes.EventDescriptor;
import me.manuelp.jevsto.dataTypes.EventType;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

public class AvroEvent {
  public static <T> EventDescriptor<T> eventDescriptor(AggregateType aggregateType, EventType eventType,
      String resource, F<GenericRecord, F<T, GenericRecord>> serialize, F<GenericRecord, T> deserialize) {
    AvroEventDataReader<T> reader = avroEventDataReader(resource, deserialize);
    AvroEventDataWriter<T> writer = avroEventDataWriter(resource, serialize);
    return EventDescriptor.eventDescriptor(aggregateType, eventType, reader, writer);
  }

  public static <T> AvroEventDataReader<T> avroEventDataReader(String resource, F<GenericRecord, T> deserialize) {
    final Schema schema;
    try {
      schema = readSchemaFromResource(resource);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new AvroEventDataReader<T>(schema, deserialize);
  }

  public static <T> AvroEventDataWriter<T> avroEventDataWriter(String resource,
      F<GenericRecord, F<T, GenericRecord>> serialize) {
    final Schema schema;
    try {
      schema = readSchemaFromResource(resource);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new AvroEventDataWriter<T>(schema, serialize.f(new GenericData.Record(schema)));
  }
}
