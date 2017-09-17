package giraph;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.giraph.worker.WorkerContext;
import org.apache.hadoop.fs.FSDataOutputStream;

public class SingleWalkWorkContext extends WorkerContext{
	private static Map<Integer, FSDataOutputStream> pid2OutStream =CombineBatchSingleWalkVertexReuse.pid2OutStream;

	
	@Override
	public void preApplication() throws InstantiationException,
			IllegalAccessException {
		
	}
 
	@Override
	public void postApplication() {
		
		for (Entry<Integer, FSDataOutputStream> entry : pid2OutStream.entrySet()){
			try {
				entry.getValue().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // close all hdfs outstream.
		}
	}

	@Override
	public void preSuperstep() {
		// TODO Auto-generated method stub
		//initialize hdfs file path, vertex.filepath
	}

	@Override
	public void postSuperstep() {
	}

}
