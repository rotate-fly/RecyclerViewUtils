package com.hzs.github.drag;




import java.io.Serializable;

/**
 *Created by hzs on 20180929
 */
public class Channel implements Serializable {

    private String channelId;
    private String channelName;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }


}
