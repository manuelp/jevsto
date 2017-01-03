package me.manuelp.jevsto.avro;

import static fj.data.List.iterableList;

import fj.F;
import fj.data.List;
import java.io.IOException;
import java.io.InputStream;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;

public class AvroRead {
  public static Schema readSchemaFromResource(String resource) throws IOException {
    Schema.Parser parser = new Schema.Parser();
    InputStream schema = ClassLoader.getSystemResourceAsStream(resource);
    return parser.parse(schema);
  }

  public static String readString(GenericRecord r, String name) {
    return ((Utf8) r.get(name)).toString();
  }

  public static <T> F<GenericData.Array<T>, List<T>> avroArrayToList() {
    return new F<GenericData.Array<T>, List<T>>() {
      @Override
      public List<T> f(GenericData.Array<T> genericRecords) {
        return iterableList(genericRecords);
      }
    };
  }
}
