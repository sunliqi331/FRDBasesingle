package com.its.common.entity;

import java.io.Serializable;

public interface Idable<T extends Serializable> {
	public T getId();

	public void setId(T id);
}
