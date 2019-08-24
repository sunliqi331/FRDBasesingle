package com.its.frd.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

public class HbaseDataGetter implements Callable<List<Result>> {
	private static final Logger logger = LoggerFactory.getLogger(HbaseDataGetter.class);
	private static HbaseTemplate htemplate = null;//SpringBeanUtils.getBean("htemplate",HbaseTemplate.class);
    private String tableName;
    private String family;
    private List<String> rowKeys;
    private List<String> filterColumn;
    private boolean isContiansRowkeys;
    private boolean isContainsList;

    public HbaseDataGetter(String tableName, String family,
            List<String> rowKeys, List<String> filterColumn,
            boolean isContiansRowkeys, boolean isContainsList) {
        this.tableName = tableName;
        this.family = family;
        this.rowKeys = rowKeys;
        this.filterColumn = filterColumn;
        this.isContiansRowkeys = isContiansRowkeys;
        this.isContainsList = isContainsList;
    }

    public HbaseDataGetter(List<String> rowKeys, List<String> filterColumn,
            boolean isContiansRowkeys, boolean isContainsList) {
        this.rowKeys = rowKeys;
        this.filterColumn = filterColumn;
        this.isContiansRowkeys = isContiansRowkeys;
        this.isContainsList = isContainsList;
    }

    @Override
    public List<Result> call() throws Exception {
        Result[] rs = getDatasFromHbase(tableName, family, rowKeys,
                filterColumn);
        List<Result> listData = new ArrayList<Result>();
        for (Result r : rs) {
            listData.add(r);
            // WifiInfoVO data = assembleData(r, filterColumn,
            // isContiansRowkeys,
            // isContainsList);
            // listData.add(data);
        }
        return listData;
    }
    private Result[] getDatasFromHbase(String tableName, String family,
            List<String> rowKeys, List<String> filterColumn) {
        Result[] rs = null;
        HTableInterface hTableInterface = createTable(tableName);
        List<Get> listGets = new ArrayList<Get>();
        for (String rk : rowKeys) {
        	if(null == rk || rk.length() == 0)
        		continue;
            Get get = new Get(Bytes.toBytes(rk));
            if (filterColumn != null) {
                for (String column : filterColumn) {
                    get.addColumn(family.getBytes(), column.getBytes());
                }
            }
            listGets.add(get);
        }
        try {
            rs = hTableInterface.get(listGets);
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                listGets.clear();
                hTableInterface.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }

	private HTableInterface createTable(String tableName) {
		//HbaseUtil.initConfiguration();
		HConnection connection = null;
		HTableInterface attachmentsTable = null;
		try {
			if (connection == null) {
				connection = HConnectionManager.createConnection(htemplate.getConfiguration());
			}
			if (attachmentsTable == null) {
				attachmentsTable = connection.getTable(tableName);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			String errMsg = "Error retrievinging connection and access to dangerousEventsTable";
			logger.error(errMsg, e);
			throw new RuntimeException(errMsg, e);
		}
		return attachmentsTable;
	}
}

/**/