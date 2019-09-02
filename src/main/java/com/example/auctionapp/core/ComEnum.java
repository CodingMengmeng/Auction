package com.example.auctionapp.core;


public class ComEnum{
    public enum childChannel {

        CHILD_CHANNEL_TYPE_APP("1","app"),
        CHILD_CHANNEL_TYPE_H5("2","h5"),
        CHILD_CHANNEL_TYPE_MP("3","mp");

        String type;
        String name;

        childChannel(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public static String getChannel(String type){
            switch (type){
                case "1":
                    return CHILD_CHANNEL_TYPE_APP.getName();
                case "2":
                    return CHILD_CHANNEL_TYPE_H5.getName();
                case "3":
                    return CHILD_CHANNEL_TYPE_MP.getName();
            }
            return null;
        }
    }



}


