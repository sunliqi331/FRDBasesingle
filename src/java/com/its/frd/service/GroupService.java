package com.its.frd.service;

import com.its.frd.entity.Group;

public interface GroupService {

	public void addGroup(Group group);
	
	public void deleteGroupRelationship(Long id);

	public Group findOne(Long groupId);
}
