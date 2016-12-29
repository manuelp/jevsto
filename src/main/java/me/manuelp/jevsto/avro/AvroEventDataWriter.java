package me.manuelp.jevsto.avro;

import fj.F;
import me.manuelp.jevsto.EventDataWriter;
import me.manuelp.jevsto.dataTypes.EventData;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroEventDataWriter<V> implements EventDataWriter<V> {
  private final Schema              schema;
  private       F<V, GenericRecord> serialize;

  public AvroEventDataWriter(Schema schema, F<V, GenericRecord> serialize) {
    this.schema = schema;
    this.serialize = serialize;
  }

  @Override
  public EventData f(final V x) {
    ByteArrayOutputStream         out    = null;
    DataFileWriter<GenericRecord> writer = null;
    try {
      out = new ByteArrayOutputStream();
      writer = new DataFileWriter<>(new GenericDatumWriter<GenericRecord>(schema));

      writer.create(schema, out);
      writer.append(serialize.f(x));
      writer.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (out != null) try {
        out.close();
      } catch (IOException e) {
      }
      if (writer != null) try {
        writer.close();
      } catch (IOException e) {
      }
    }

    return EventData.eventData(out.toByteArray());
  }
}
