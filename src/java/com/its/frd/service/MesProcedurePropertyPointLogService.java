package com.its.frd.service;

import com.its.frd.entity.MesProcedurePropertyPointLog;

public interface MesProcedurePropertyPointLogService extends BaseService<MesProcedurePropertyPointLog> {
	public MesProcedurePropertyPointLog findLastedLogByPropertyId(long id);
	public MesProcedurePropertyPointLog findLastedLogByPointId(long id);
}
