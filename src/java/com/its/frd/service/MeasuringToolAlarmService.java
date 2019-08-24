package com.its.frd.service;


import com.its.frd.entity.MeasuringToolAlarm;

import java.util.List;


public interface MeasuringToolAlarmService extends BaseService<MeasuringToolAlarm> {

    List<MeasuringToolAlarm> getDataWithMesProductProcedureIdsAndLine(List<Long> mesProductProcedureIds, Long line);

    List<MeasuringToolAlarm> findByMeasuringToolId(Long id);

    void deleteByMeasuringToolId(Long id);

    void deleteAll();

    List<MeasuringToolAlarm> getDataWithByLine(Long line);

    void deleteAllBySpcsite(Integer spcsite);
}
