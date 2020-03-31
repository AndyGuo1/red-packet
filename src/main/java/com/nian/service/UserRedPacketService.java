package com.nian.service;

public interface UserRedPacketService {

    /**
     * �����������Ϣ.
     *
     * @param redPacketId ������
     * @param userId      ������û����
     * @return Ӱ���¼��
     */
    public int grapRedPacket(Long redPacketId, Long userId);

    /**
     * @param redPacketId
     * @param userId
     * @return Ӱ���¼��
     */
    public int grapRedPacketForVersion(Long redPacketId, Long userId);

    /**
     * ͨ��Redisʵ�������
     *
     * @param redPacketId ������
     * @param userId      �û����
     * @return 0-û�п�棬ʧ��
     * 1-�ɹ����Ҳ������һ�����
     * 2-�ɹ����������һ�����
     */
    Long grapRedPacketByRedis(Long redPacketId, Long userId);
}
