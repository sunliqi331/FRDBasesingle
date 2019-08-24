package com.its.frd.service;

import com.its.frd.entity.MeasuringTool;

import java.util.List;

public interface MeasuringToolService extends BaseService<MeasuringTool> {

    List<MeasuringTool> findAllByIsdelete(Integer isdelete);

    MeasuringTool findById(Long id);

    List<MeasuringTool> findBySnAndIsdelete(String sn, int i);

    void deleteAll();

    void deleteAllBySpcsite(Integer spcsite);
}
