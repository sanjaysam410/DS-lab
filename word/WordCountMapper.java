import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMapper
 extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(LongWritable k, Text v, Context c)
            throws IOException, InterruptedException {

        for (String w : v.toString().split("\\s+")) {
            word.set(w);
            c.write(word, one);
        }
    }
}
