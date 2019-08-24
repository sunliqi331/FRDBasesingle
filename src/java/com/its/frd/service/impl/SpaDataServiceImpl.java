package com.its.frd.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.annotation.Resource;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.its.frd.dao.SpcDataDao;
import com.its.frd.entity.MesProduction;
import com.its.frd.entity.SpcData;
import com.its.frd.service.SpaDataService;
@Service
public class SpaDataServiceImpl implements SpaDataService {
    
    @Resource
    private SpcDataDao spcDao;

    @Override
    public List<SpcData> findNewSpcData() {
        return spcDao.findNewSpcData();
    }

    @Override
    public SortedMap<String, Map<String, String>> makeMapByHbaseData(List<Result> datas) {
        SortedMap<String, Map<String, String>> rsMap = Maps.newTreeMap();
        Map<String, String> rowMap = null;
        for (Result rs : datas) {
            rowMap = getRowByResultForOutPut(rs);
            rsMap.put(rowMap.get("rowKey"), rowMap);
        }
        return rsMap;
    }

    /**
     * 获取同一个rowkey下的记录集合
     * 
     * @param result
     *            结果集
     * @return
     */
    private Map<String, String> getRowByResultForOutPut(Result result) {
        if (result == null) {
            return null;
        }
        Map<String, String> cellMap = new HashMap<String, String>();
        int index = 0;
        String status = "OK";
        for (Cell cell : result.listCells()) {
            String qf = Bytes.toString(cell.getQualifierArray(),
            cell.getQualifierOffset(), cell.getQualifierLength());
            if(0 == index) {
                String rowkey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                cellMap.put("rowkey", rowkey);
                // 工厂
                cellMap.put("productSn", rowkey.split("_")[6]);
                cellMap.put("timestamp", String.valueOf(cell.getTimestamp()).substring(0, 10));
                cellMap.put("pointId", qf.split("_")[1]);
            }
            if("NG".equals(qf.split("_")[4])) {
                status = "NG";
                break;
            }
            index++;
        }
        cellMap.put("status", status);
        return cellMap;
    }   

}
