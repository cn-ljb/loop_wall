package com.ljb.loopwall.simple;

/**
 * Created by L on 2017/4/12.
 */

class ModeItem {

    private String phone;
    private String time;
    private String prize;

    public ModeItem(String phone, String time, String prize) {
        this.phone = phone;
        this.time = time;
        this.prize = prize;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }
}
