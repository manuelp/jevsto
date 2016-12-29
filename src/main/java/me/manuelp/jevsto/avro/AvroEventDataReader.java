package me.manuelp.jevsto.avro;

import fj.F;
import me.manuelp.jevsto.EventDataReader;
import me.manuelp.jevsto.dataTypes.EventData;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class AvroEventDataReader<V> implements EventDataReader<V> {
  private final Schema schema;
  private final F<GenericRecord, V> deserialize;

  public AvroEventDataReader(Schema schema, F<GenericRecord, V> deserialize) {
    this.schema = schema;
    this.deserialize = deserialize;
  }

  @Override
  public V f(final EventData x) {
    GenericDatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(schema);
    ByteArrayInputStream in = null;
    DataFileStream<GenericRecord> stream = null;

    try {
      in = new ByteArrayInputStream(x.getData());
      stream = new DataFileStream<GenericRecord>(in, reader);
      GenericRecord record = new GenericData.Record(schema);
      stream.next(record);
      return deserialize.f(record);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if(in != null) try {
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      if(stream != null) try {
        stream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
