package com.example.auctionapp.vo;

public class DealConcluedVo {

    boolean isConclued;//是否拍中
    int concluedUserId;//拍中者id，拍中的话有值

    public DealConcluedVo(){}

    public DealConcluedVo(boolean isConclued, int concluedUserId) {
        this.isConclued = isConclued;
        this.concluedUserId = concluedUserId;
    }

    public boolean isConclued() {
        return isConclued;
    }

    public void setConclued(boolean conclued) {
        isConclued = conclued;
    }

    public int getConcluedUserId() {
        return concluedUserId;
    }

    public void setConcluedUserId(int concluedUserId) {
        this.concluedUserId = concluedUserId;
    }
}
