package com.nian.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nian.dao.RedPacketDao;
import com.nian.pojo.RedPacket;
import com.nian.service.RedPacketService;

@Service
public class RedPacketServiceImpl implements RedPacketService {
	
	@Autowired
	private RedPacketDao  redPacketDao = null;

	/*
	 * ����������ע��@Transactional���ó����ܹ������������У��Ա�֤���ݵ�һ���ԣ�������õ��Ƕ����ύ�ĸ��뼶��
	 * �����ø��߼�����Ҫ�ǿ��ǵ��������ܣ������ڴ�����Ϊ����Ĭ�ϵ�REQUIRED��û�����񴴽��������������á�
	 * @see com.nian.service.RedPacketService#getRedPacket(java.lang.Long)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public RedPacket getRedPacket(Long id) {
		return redPacketDao.getRedPacket(id);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public int decreaseRedPacket(Long id) {
		return redPacketDao.decreaseRedPacket(id);
	}

}