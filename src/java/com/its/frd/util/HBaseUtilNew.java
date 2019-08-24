package com.its.frd.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.hadoop.hbase.client.Result;

public class HBaseUtilNew {
	public static List<Result> getDatasFromHbase(String tableName,
            String family, final List<String> rowKeys,
            final List<String> filterColumn, boolean isContiansRowkeys,
            boolean isContainsList) {
		List<Result> dataQueue = new ArrayList<Result>();
        if (rowKeys == null || rowKeys.size() <= 0) {
            return dataQueue;
        }

        final int maxRowKeySize = 1000;
        ExecutorService pool = Executors.newFixedThreadPool(10);
        int loopSize = rowKeys.size() % maxRowKeySize == 0 ? rowKeys.size()
                / maxRowKeySize : rowKeys.size() / maxRowKeySize + 1;

        ArrayList<Future<List<Result>>> results = new ArrayList<Future<List<Result>>>();
        for (int loop = 0; loop < loopSize; loop++) {
            int end = (loop + 1) * maxRowKeySize > rowKeys.size() ? rowKeys
                    .size() : (loop + 1) * maxRowKeySize;
            List<String> partRowKeys = rowKeys.subList(loop * maxRowKeySize,
                    end);

            HbaseDataGetter hbaseDataGetter = new HbaseDataGetter(tableName,
                    family, partRowKeys, filterColumn, isContiansRowkeys,
                    isContainsList);
            Future<List<Result>> result = pool.submit(hbaseDataGetter);
            results.add(result);
        }

        
        try {
            for (Future<List<Result>> rs : results) {
                List<Result> rd = rs.get();
                dataQueue.addAll(rd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
        return dataQueue;
    }
}
