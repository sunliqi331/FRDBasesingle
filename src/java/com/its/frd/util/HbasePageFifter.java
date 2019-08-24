package com.its.frd.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;

public class HbasePageFifter {
    public static Configuration configuration;
    private static HTable hTable;
    private static String startRow = null;
    private static List list = null;


    public static List getLast(int pageNum, int pageSize){
        getPage(pageNum-1,pageSize);
        return null;
    }
    /**
     * 取得下一页 这个类是接着getPage来用
     * @param pageSize 分页的大小
     * @return  返回分页数据
     */
    public static List getNext(int pageSize) throws Exception{
        Filter filter = new PageFilter(pageSize +1);
        Scan scan = new Scan();
        scan.setFilter(filter);
        scan.setStartRow(startRow.getBytes());
        ResultScanner result = hTable.getScanner(scan);
        Iterator iterator = result.iterator();
        list = new ArrayList<>();
        int count = 0;
        for(Result r:result){
            count++;
            if (count==pageSize + 1) {
                startRow = new String(r.getRow());

                scan.setStartRow(startRow.getBytes());
                System.out.println("startRow" + startRow);
                break;
            }else{
                list.add(r);
            }
            startRow = new String(r.getRow());
            System.out.println(startRow);
            //把 r的所有的列都取出来     key-value age-20
            System.out.println(count);
        }
        return list;

    }
    // pageNum = 3 pageSize = 10
    public static void getPage(int pageNum, int pageSize) {
        // int pageNow = 0;
        // TODO 这个filter到底是干嘛的？
        Filter page = new PageFilter(pageSize + 1);
        int totalSize = pageNum * pageSize;
        Scan scan = new Scan();
        scan.setFilter(page);
        //pageNum = 3   需要扫描3页
        for (int i = 0; i < pageNum; i++) {

            try {
                ResultScanner rs = hTable.getScanner(scan);
                int count = 0;
                for (Result r : rs) {
                    count++;
                    if (count==pageSize + 1) {
                        startRow = new String(r.getRow());

                        scan.setStartRow(startRow.getBytes());
                        System.out.println("startRow" + startRow);
                        break;
                    }
                    startRow = new String(r.getRow());
                    System.out.println(startRow);
                    //把 r的所有的列都取出来     key-value age-20
                    for (KeyValue keyValue : r.list()) {
                        System.out.println("列："
                                + new String(keyValue.getQualifier()) + "====值:"
                                + new String(keyValue.getValue()));
                    }
                    System.out.println(count);


                }
                if (count < pageSize) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}