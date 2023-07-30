package org.definedim.event;

public class DefinedIMEvent {
    private long timestamp;
    private long eventId;
    private boolean blocked;

    public DefinedIMEvent(long _t, long _id) {
        timestamp = _t;
        eventId = _id;
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    public final long getTimestamp() {
        return timestamp;
    }

    /**
     * 获取事件ID
     *
     * @return
     */
    public final long getEventId() {
        return eventId;
    }

    /**
     * 查询事件是否被阻塞
     * @return
     */
    public final boolean isBlocked() {
        return blocked;
    }

    /**
     * 阻塞该事件
     */
    public final void block() {
        blocked = true;
    }

}
