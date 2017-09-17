package benchmark;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import lxctools.FixedCacheMap;
import lxctools.Log;
import sigmod14.Sigmod14Gamma;
import sigmod14.Sigmod14Indexing;
import sigmod14.Sigmod14Query;
import structures.Graph;
import utils.Print;
import conf.MyConfiguration;

/**
 * test efficiency of SIGMOD14.
 * @author luoxiongcai
 *
 */
public class Test_Sigmod14_maxId {

	public static void main(String[] args) throws IOException {
		int topk = 20;
		int[] maxIds = {20,40,60,80,100};
		String[] datas = {"bibsonomy-2ti.txt","citeulike-ti.txt"};
		int[] sizes = {972120,511463, 885046};
		String base = MyConfiguration.realdata;  // folder of reald dataset.
		String testBase = MyConfiguration.basePath+"/Test_Sigmod14_efficiency";
		
		Log log= new Log(testBase+"/sigmod14_realdata_maxId.log");
		log.info("COMPUTING FOR REAL DATASET USING SIGMOD14! TEST EFFICIENCY!  " );
		log.info("\tIndex: P = "+ Sigmod14Indexing.P+"\t Q = "+ Sigmod14Indexing.Q +"\t T = "+ Sigmod14Indexing.T );
		for (int i = 0; i < datas.length; i++){
			MyConfiguration.biGraphPath = base +"/"+datas[i];
			MyConfiguration.totalCount = sizes[i];
			System.out.println(datas[i]+" size: "+sizes[i]+" ...");
			log.info(datas[i]+" size: "+sizes[i]+" ...");
			Graph g = new Graph(MyConfiguration.biGraphPath,MyConfiguration.totalCount);
			Sigmod14Indexing sigIndex = new Sigmod14Indexing(g);
			Set<Integer>[] index = sigIndex.generateIndex();
			File findex = new File(testBase+"/index_"+datas[i]);
			sigIndex.outputIndex(findex.getAbsolutePath());
			log.info("index constrction done! size: ");
			
			Sigmod14Gamma sigGamma = new Sigmod14Gamma(g);
			double[][] gammas = sigGamma.computeGamma();
			File fgamma = new File(testBase+"/gamma_"+datas[i]);
			sigGamma.outputGamma(fgamma.getAbsolutePath());
			log.info("gamma computation done! total size of index and gamma : " +(findex.length()*1.0/1024+fgamma.length()*1.0/1024)+" KB" );
			
			Sigmod14Query sigQuery = new Sigmod14Query(g, index, gammas);
			for (int maxId: maxIds){
				log.info(datas[i]+" sigmod14, maxId = "+maxId);
				FixedCacheMap[] results = sigQuery.batchQueryTopK(maxId);
				String outPath = testBase+"/sigmod14_"+datas[i];
				Print.printByOrder(results, outPath , topk);
				System.out.println(datas[i]+" : sigmod14 output and computing done!");		
				log.info(datas[i]+" sigmod14 output and computing done!");
			}		
			sigIndex = null;
			sigGamma = null;
			sigQuery = null;
			g = null;
		}
		log.close();
	}

}
