package com.its.frd.controller;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.its.common.utils.XmlUtils;
import com.its.frd.entity.SpcData;
import com.its.frd.service.SpaDataService;
import com.its.frd.util.HbaseUtil;

@Controller
@RequestMapping("/spcdata")
public class SpaDataCotroller {

    @Autowired
    private SpaDataService spcDataService;

    /**
     * 获取最新的SPC生产数据，以XMLobj的形式上传到SHCD的MES服务器
     * 
     * @return SPC生产数据
     */
    @RequestMapping("/shcd")
    @ResponseBody
    public String list() {
        SortedMap<String, Map<String, String>> map = Maps.newTreeMap();
        List<SpcData> spcDataList = spcDataService.findNewSpcData();
        if (null != spcDataList) {
            SpcData spcNewDataUpdated = spcDataList.get(0);
            List<Result> datas = new HbaseUtil().getSpcDateByHbaseStopRow(516L, null, null, null, null, null, null, null,
                    Boolean.FALSE, "516_242_358_9_235_1521467731_BN0003844", "516_242_358_9_235_1521467990_BN0003887");
            System.out.println(datas.get(0));
        }

        return null;
//        return XmlUtils.parseXML(map);
    }

}
