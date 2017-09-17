package benchmark;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import lxctools.FixedCacheMap;
import lxctools.Log;
import sigmod14.Sigmod14Indexing;
import sigmod14.Sigmod14Query;
import structures.Graph;
import utils.Print;
import conf.MyConfiguration;

public class Test_Sigmod14_temp {

	public static void main(String[] args) throws IOException {
		int topk = 20;
		int maxId = 100;
		String[] datas = {"delicious_delicious-ti_delicious-ti.txt"};
		int[] sizes = {38289740};
//		String[] datas = {"youtube-groupmemberships.txt"};
//		int[] sizes = {124325};
		String base = MyConfiguration.realdata;  // folder of reald dataset.
		String testBase = MyConfiguration.basePath+"/Test_Sigmod14_efficiency";
		
		Log log= new Log(testBase+"/sigmod14_delicious_efficiency.log");
		log.info("COMPUTING FOR REAL DATASET USING SIGMOD14! TEST EFFICIENCY!  " );
		log.info("maxId = "+maxId+"\tIndex: P = "+ Sigmod14Indexing.P+"\t Q = "+ Sigmod14Indexing.Q +"\t T = "+ Sigmod14Indexing.T );
		for (int i = 0; i < datas.length; i++){
			MemoryUsage heapMemoryUsage =ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
			MyConfiguration.biGraphPath = base +"/"+datas[i];
			MyConfiguration.totalCount = sizes[i];
			System.out.println(datas[i]+" size: "+sizes[i]+" ...");
			log.info(datas[i]+" size: "+sizes[i]+" ...");
			Graph g = new Graph(MyConfiguration.biGraphPath,MyConfiguration.totalCount);
			Sigmod14Query sigQuery = new Sigmod14Query(g);

			String indexPath = testBase+"/index_"+datas[i];
			sigQuery.loadIndex(indexPath);
			log.info("index loaded!  current heapmemory usage: "+(heapMemoryUsage.getUsed()/1024.0/1024)+" MB");
			
			
			String gammaPath = testBase+"/gamma_"+datas[i];
			sigQuery.loadGamma(gammaPath);
			log.info("gamma loaded!  current heapmemory usage: "+(heapMemoryUsage.getUsed()/1024.0/1024)+" MB");
			
			FixedCacheMap[] results = sigQuery.batchQueryTopK(maxId);
			String outPath = testBase+"/sigmod14_"+datas[i];
			Print.printByOrder(results, outPath , topk);
			System.out.println(datas[i]+" : sigmod14 output and computing done!");
			log.info(datas[i]+"sigmod14 output and computing done!  current heapmemory usage: " + (heapMemoryUsage.getUsed()/1024.0/1024)+" MB");
			
		}
		log.close();
	}

}
