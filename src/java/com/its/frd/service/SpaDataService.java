package com.its.frd.service;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.hadoop.hbase.client.Result;

import com.its.frd.entity.SpcData;


public interface SpaDataService {
    List<SpcData> findNewSpcData();

    SortedMap<String, Map<String, String>> makeMapByHbaseData(List<Result> datas);
}
