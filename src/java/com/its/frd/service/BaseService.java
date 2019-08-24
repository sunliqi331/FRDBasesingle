package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;

public interface BaseService<T> {
	public List<T> findPage(Page page);
	public List<T> findPage(Specification<T> specification,Page page);
	public T saveOrUpdate(T t);
}
