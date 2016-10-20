package cn.thinkbear.app.grabredpacket.vo;

/**
 *
 * @author ThinkBear
 */

public class Chat {
    private String chatName;//聊天标题
    private int number;//人数
    private boolean isGroup;//是否群聊

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }
}
