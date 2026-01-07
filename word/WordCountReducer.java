import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class WordCountReducer
 extends Reducer<Text, IntWritable, Text, IntWritable> {

    public void reduce(Text k, Iterable<IntWritable> v, Context c)
            throws IOException, InterruptedException {

        int sum = 0;
        for (IntWritable i : v)
            sum += i.get();

        c.write(k, new IntWritable(sum));
    }
}