package com.example.auctionapp.core;

/**
 * 项目常量
 */
public final class ProjectConstant {
    public static final String BASE_PACKAGE = "com.example.auctionapp";//生成代码所在的基础包名称，可根据自己公司的项目修改（注意：这个配置修改之后需要手工修改src目录项目默认的包路径，使其保持一致，不然会找不到类）

    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".model";//生成的Model所在包
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";//生成的Mapper所在包
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";//生成的Service所在包
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";//生成的ServiceImpl所在包
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".web";//生成的Controller所在包

    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".core.Mapper";//Mapper插件基础接口的完全限定名

    public static final int SENDSMS_TYPE_1 = 1;//验证码类型 注册
    public static final int SENDSMS_TYPE_2 = 2;//验证码类型 忘记密码
    public static final int PASSWORD_LENGHT = 32;//密码长度  登录

    public static final String STOCK_URL = "http://dt.jctytech.com/stock.php?u=16609360863&limit=100"; //16609360863
    public static final String STOCK_KLINE_FIVE_PARAMS = "&type=kline&line=min,5&symbol=";
    public static final String STOCK_KLINE_DAY_PARAMS = "&type=kline&line=day&symbol=";
    public static final int STOCK_SUSPENSION = -100;
    // 分时行情
    public static final String STOCK_TREND_QUTATION_PARAMS = "&type=trend&symbol=";

    public static final String AUCTION_LOGIN_USER_ARRAY_KEY = "AUCTION_LOGIN_USER_ARRAY_KEY"; // USER
    public static final String AUCTION_LOGIN_TOKEN_KEY = "AUCTION_LOGIN_TOKEN_KEY"; // TOKEN
    public static final String AUCTION_LOGIN_USER_ID_KEY = "AUCTION_LOGIN_USER_ID_KEY"; // USERID

    public static final String REGISTER_SMS = "registerSMS";//注册验证码缓存
    public static final String PASSWORD_SMS = "passwordSMS";//忘记验证码缓存
    public static final String OTHERSET_SMS = "otherSMS";//其他验证码缓存
    public static final String SMS_ERROR_CODE = "error_code";//验证码错误标签

    //支付渠道
    public static final String PAY_CHANNEL_ALIPAY   = "alipay"; //阿里支付
    public static final String PAY_CHANNEL_WXPAY    = "wxpay";  //微信支付
    public static final String PAY_CHANNEL_BALANCE  = "balance";//余额支付
    //账户类型
    public static final int ACCOUNT_TYPE_CUSTOMER   = 1; //客户账户
    public static final int ACCOUNT_YYPE_AGENT      = 2; //代理账户
    public static final int ACCOUNT_YYPE_ORGANIZER  = 3; //组织者账户
    //拍品状态
    public static final int ACCOUNT_GOOODS_STATUS_NOTSTARTED   = 0; //未上拍
    public static final int ACCOUNT_GOOODS_STATUS_DOING   = 1; //正在拍卖
    public static final int ACCOUNT_GOOODS_STATUS_END   = 2; //已拍卖
    //订单编号前缀
    public static final String CUSTOMER_RECHARGE = "CR";
    public static final String GOODS_AUCTION = "GA";
    public static final String GOODS_ORDER = "GO";
    public static final String SHARE_PROFIT = "SP";
    public static final String SHARE_ORDER = "SO";
    public static final String BOND_RETURN = "BR";
    public static final String WITHDRAW = "WD";

    //优惠券发放类型
    public static final int COUPONS_SENDTYPE_NEWREGISTER = 1; //新用户注册
    public static final int COUPONS_SENDTYPE_CUSTORMERINVITE = 2; //客户邀请好友
    public static final int COUPONS_SENDTYPE_BEINVITEDBYCUSTOMER = 3; //被客户邀请
    public static final int COUPONS_SENDTYPE_AUCTIONGOODS = 4; //拍卖
    public static final int COUPONS_SENDTYPE_LOTTERYINVITE = 5; //抽奖邀请
    public static final int COUPONS_SENDTYPE_BEINVITELOTTERY = 6; //被抽奖邀请

    public static final int PLATFORM_ANDROID = 1;  // android 平台类型
    public static final int PLATFORM_IOS = 2;   // ios 平台类型
    public static final int PLATFORM_RAPE = 1;   // 强制更新

    public static final String DICTIONARYS_KEY = "DICTIONARYS_KEY"; //数据字典

    public static final String REDIS_CHANNEL_ADMIN_NOTIFY = "ADMIN_NOTIFY";//redis通知渠道之后台通知

    //时间运算单位60s
    public static final int TIME_UNIT = 60;

    //状态码
    public static final int RECHARGEANDWITHDRAWAL_CODE_200 = 200;//status状态值
    public static final int RECHARGEANDWITHDRAWAL_CODE_0 = 0;//status状态值

    //客户websocket sessionId 前缀
    public static String WEBSOKET_CUSTOMER_SESSION_KEY = "WEBSOCKET_CUSTOMER_KEY";

    // 编码
    public static String CHARSET = "UTF-8";

    // 易宝 支付
    public static String YEEPAY_GETWAY_URL = "https://api.beecloud.cn/2/rest/bill";
    public static String YEEPAY_APP_ID = "901d16d2-571c-4bde-995c-c963a66160a0";
    public static String YEEPAY_APP_SECRET = "03be2892-adcb-4535-b154-2735ac4efa8f";
    public static String YEEPAY_MASTER_SECRET = "60432701-f3de-4632-b42c-8d6a130b825d";
    public static String YEEPAY_APP_CHANNEL = "BC_GATEWAY";

    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmRumDUTLNvd9gRpQNjY6G6kApw4+JYLTyGlag5PSJNBUhNPE8bWjGxzgWE1HM1WyRIb7Vw+52MpSYh/9tHm2uMgDIzyyFnb13Q8QsUreINnbvK96145F7m0LCjQx6xPp4buPMEjbE5b6G5hh+BHjUClsG+35bYNFSN05A5NUhyH05qeC2ytkoOuxExhc4kfTY+DduwEU8nz5e1rHfQQZ9isIBoN5g1Jf7Jd8vXb+uavPhp2nC8rr/LwuFNaSURQB9Y9tLxGN0yQEiTYw7z6Q5puT4qWdUqR+GRrPRFoV7qUEK8QBjC96//hWtwFlfy+Zpa+QCKt92vkVbW6wxZitGQIDAQAB";
    public static final String APP_ID = "2019062765776030";
    public static final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCE491l/4ldQv3HiM9VyLK3XMzUMS/qdFtD+81A7LylPhmxMfm0ANaPTgP6wfCiU7g77J+FHidPtoVBAgt32RX9bM5RcqjWkk2LQtetqpc+ExeAr9qWoxCqS7gIMdM5bxe291QxPmeVVbEFAfWiWc2bLVHbRyT7OJ12bEUelzq9J2Eo4m6C/H6gSRIVs9+LpzOYnZK64YLOLEuVyy3ws4ZyBh2kqi1fqgYY8WOVTkeuJHuP2KN/iD2J1MVWsWiaC76Jd900Nh66EsKUJzk325oDCgDAMTZ+meG8XpKxURI7GSoOmRvmCoB6VOrdj0gaddbv4QX9i1Mar5E2c/5Z7nZzAgMBAAECggEAUCEJ1b/wtxB278LZqhM7V/8X7va9urRwd5y0VeqpkUXRvhuxQk2jEoPrOF+K1oZ+t3tlcKIO+CzJSrqN6bU3YjkiatAlaESNh2RFoo0TZzWh3VGsD1rB6L2BeUd54s7O0N9vn8XONaiW9bsJvtxBcskv63WJogWwRMDiKKDtpTHeo9YC5M03Ql9iOkAwahBYb6PQqYJkkF7hl5GmlQqyCkF+dsGjbs8ZldCDCUF2/iiRZdlJzjhHwW+T5NuY3N7zUgIBqSTfJDsumYz6U83yNG8zZVRxiLGYzIaeypBMF1/NfdqI/xgso/UuGauK2KHuhIGngY/qBqn8kaZpqaKmAQKBgQDmNMmcGzTmyGGcAN45WB84YefEKKQ/6F0BeVpwwLgbc9WlZLnMWwvrRFExqy3irHudmiNv0IXVHJX65BS6Bs+skxwIqa92WKuv3HtT8Y92PpK4Un29AOa6mk8kQmUZqPuGmkiSl4kW/u4xMtWN+Skyg6DP8QKftuAvOw4VGXaxcwKBgQCTx6vrEA6r/KGq3kqsjgbs4/SpxdgwBYzWC1fAY8EYDXLVoBGVyxFtd6SnKLJLpzUAGOtD0U34V6NjlxF8czhw7Ul+FpaXojPERJUCQnQ9Z+INOHh1de571cUeG8yRGKFccTlsSwg2cHKgGSsoTdoArSlZcc/T7R9ZMyz8JgPnAQKBgQC8BfGFZ1NWC+Rtnufivm8BhjXeqbM933PXeF6+KSo2ge3GIPcASnz5s9r9Tmsz8N0mZHb9JLuqzKZTPWcQae/FbIJBMRb2+oWAJf01Gt+qHIGJuxaoAMxzLQXm/7PDuZl23xzXXp3CAoZZ+MMkKpXThF8ceCVEfgGtkfZ2lU20sQKBgA86yqrk8r4iwP58/jEeSfRr4bEFZKw+Ri9g5+A555J1AFqnrmhDjz460Esx1H4bc0jZSJWqomBvl7URwomSzkRFEAfgiBCEdNVLRBgBhT020YMPSMrCJpu4giICN2jUovyF5qcUbHo/yHBbZ6k1rLvZHKPNKN/sVgi5T2+E824BAoGAcpxkcLESRGPyOCPtghAJ6N1ULWRFlnAFYP6wzS40i4GQkXiaoaVqx2hZmRwZEJfBm7SjsyH5kUX7Sf5bJWKu4z7/6ar7jvhrExCRVHjfA6vGq1DIM62/zzEGymswuO0qqcds0R5MF5Qh+Szb9krk42qT5ScV4Sgv1N7o1rCZxE8=";
    //public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkxtCsiIFScb+L3KmxqYd9CQk3ssF2HPvuz92Or//he6nOJzYkksaUBQmSunr+jjGQLd1BprhpPUHfwTusukaT5k/p1DmT1JXZoAPkzjgjQvg6C4rT2pMahqNTXK2F5ROVEholgMhhCpUaxLvGQ/OtW06IKplFpvfUOUHSoLtbkWz2X0GLHyARubTMO3d7OR6+wr6/jIVxoPFEpi5EPVgLzzbdDvtkaQAF9PWCqrxTP//txmyi1N2WLhJp6xB1twCUATH/ydReh0TxU5ntwS8SxA00yzO6bHTq8fSJCcLkmsZD0yNsdqJUoD9gRUq9dZnRokKGZugPltGTCUr4Il4PwIDAQAB";
    //public static final String APP_ID = "2019022563325233";
    //public static final String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDWJsOHTTW1zNPoYU6fWt9K6jElUCUdB462FE5eBTEUd8uKDP+P/hvLSET6gUFxcPcq8a/DOzs6wKRCcLxDcm8W/W264KVcPoZzP3QyUb8tdIhYaDOdwP7iJzVVuy1qdT8FGCaDApEiimMB8Oi/3drTLK5V9lKxtoJUCeLPqnCuyczlM06QX7LoipRrIoOCHnODAKwRmUxytKJljl4IPj1OpIKvDrgo5OXbwXoogtOsxW4FDDEQ+5HuAAY7uua4nkQRZvxVOrr+O8Ekxl20UGOBcnY+bH3IkXDr4z2QKYf6vHztoufpBVppTmkxAnHn84ycAj3PIM65Ayo1TFCTJmgNAgMBAAECggEABO8jtixbPnMXttT3IMjROEKpGzj7ge24pg1VuOjZtqUD9YxTHxg1v7tMi5EK1vhJWZ3z9MWamcHyhu1eCRfL43W3X4TpinSOWH1vyfEEPKHguli8R7g4pzFME0uawkctSorB6GbYvTj/Cv5n/nO4JQF0itqAEUQpQJJxvWMp8Xz9/xXIQkK7dXS+EU+l/L+dvR11wEa8CuLbnxWPbirEOKl6M/erxtQ0Sdn6QML+rLMG7+mnrsQvOhe2ipsztNjQPb3UnF3y56ilbVXQdOMC7ykDRLlkkL5sA9odSU5GzqhrB4kuIbsv+obFwNB6lo1bXNhO+dKijhubU0REXToRaQKBgQD9DRV9fXpr6w7W6s1Y5vSvADWEetvBmv+6WI9OExDadRx9eL+zgapOJUFYyZR+1QAHw0gP1GS6A43wbqgzy32+7ANNzMJJhxYBH0JH4EotbQx9AxiscyOMRQmh/99QDkzJ3rE8dNBn0ivfy8RExgzJ9TuUil8+r9Gh4uuFI+OA1wKBgQDYpaHTx3QTFIGV0hjuLxDuxWNAYsepFZq2N4Toq+d4pbRK0PJ+onwu7hA39p/DF+gL53jsm+QnNj0VEZv4bYwpKckBO77BNsy6mXr3MVwG62EnY+dvUYg2UmYhQbL5CWOLu9XsD/Tpe+G58/QFST5BbjTj5+qGwUhYtBmm5oqtuwKBgQDagkTPUt22eIgc3y4omW4p+0U7O5eta3ltYPGF2oLQERZj5jOq/smfpx3xXEek6aqYCXGdcar00QLs3bDS4OGZTlVgBfKy6ToACA0E4dFLvoFtJZJVvyqwAiFaSQd9Kov2ORzUdo5LSDnoixzUwivi7KbhEkdCazHa14bGNgJKiwKBgQDTzdrQeYb5wGqQVr8Y3droFQJbyDb7D6wZyV2fO7EpOpdxLaZmN+TA3O3uSKcYzR6HxN1sNVxxGY3OM1ERJ8iH1do14nsz2qd9JuAZG6ClAyoQoJN7OXK56QUALoMTBiLUUedqyo7eVcbxTZecM2cyANSkm++lnwFj92+8xFeAUQKBgCeHruuEDPRgAbyT35bG2QtICXQrfjyDi9bmz1b/l7qH67+tsg+PnGe8XNqp/PQ8kejlKgcZouJODIBsnyQIU8Onk4tlRcYK9uHHmXalAr79QgI96rWtphmBeqUczikBo762bgJjhQysTSxeVHXgP21B7mH3BD5pjz5pPUCAkzO9Psw==";

    //微信支付
    public static final String WECHAT_APPID = "wx61e1fe2b50d02ff1";
    public static final String WECHAT_KAY ="a1b2dfb40daedbe24e8923707873593e";
    public static final String MCH_ID = "1540826341";
    public static final String NOTIFY_MSG_SUCCESS = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    public static final String NOTIFY_MSG_FAIL = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[NO]]></return_msg></xml>";

    //沙箱测试配置
    //public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAglh6ujAEIBvU2PYi3h1ti6fCoHlTYld+0FmMuvYqVfwSbJURMKv4YZGdXNi/EidGmGUqkWJev1v7RnBMNobiHHdCjPjTGGkZtmrJSfb/YPPvbXjeOiLZ5Fd70ZHjslJgDI/6cV4UJH+zEE9+VCI8Pn9252eBuy+1XEG5F3hV6xEyjCSj6GZ7T8Zvurxk7k82HmR9KT8cZ400+Qi/uuIPsMwklKNyScLOairGoHSR8NQakhOWBhqZkiXdgKWM+9ARa/kdwRGp7tB9uOZ5d7Ry//VGvFli6LeTlqRZa4NOyffCvFs8iJdkl9MnW4CyfejTodLxHCbgTMa8RPZMF22liQIDAQAB";
    //public static final String APP_ID = "2016093000635321";
    //public static final String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDQMEGa8DZEdJvCb5sAI/g4iMkFQv0+9mt3b3Ppf58eJJ41svOS12ONSaEkamGVD/+3PK4dW5POLfO+Ge3ynRuFBfx8pibMNL4vZFVC3kvZ7yk+/6BwUqcUl+FQLAlEtH73u70hFL2n4xVLlAGirsSZ41TtTKXsESBDSyEBtPyFjjmQzqbEXniErSHfHCCrcSLboBw0fNi4PQS57BIRPTX6mXt3c8BfQYVbrbJQTJS0FlEcsP86C+kEtsE8pFf9yBxZ2aC9QreykdO4/GTsYeOXVL0FcPzu56DZpFEwMRsLVoF+CyN0L7yUrUfEcJ+23+WslGFEdmeTgQlsMavFZ+nZAgMBAAECggEAHYOp2VhSqTPK3S7LT6LJzuH9daSAmIuAwsbnLOQt360bTMuY4mlssJwOn0YBnYtOGmEc4gaYlrNHCd1NMYb6XspxeA5qE6sqhzd3Kuqu4C+arla5HRNqom3BwIbtrXLVSTOxZV42YPW9kCBkdvbF4ncrFBU4tJXc+A1PmZlZ72jv4psK1SpHVzh/362Bd7O2KtB4YTkMbCWxfp6jLbFRdczjrGSOjz4r6npxR1/7xILvSFUCrAhkGW9ScQad5hrLyC/YrNlUF4FS+Yuz9V3p7NluGmum3NX/8P9sWrmwwc0FCPc6D7+h5F5e7ZYwgyrkuikLXc3sVH/qWvamZXDEAQKBgQDq48hew02vmT4t8unpeA60MRnS8OES1bo4Ejc50GpSgRJhpKotI25irYTIWK1C3ez/MJVANhyRGqJbBrlUTVbEagmmh2T8DtRriqrj5ZAohHA5pEVTEFnieucTgn2C4xj7SHw4iEegulkOXtOB83R0191cG9mZfzPm3BhE6EHMgQKBgQDi5iUhELaBZ4n68aQsT7RhUNH4bjHIxcT3IlDp4N5fywWA5LfqJKHPbFMT2ovOzkoPLiVq1o1xKIEhA5qIVvUNdcKsPgWaxdb2ougD4GK1n+3wFRoaqP5OVmFlL6ueO6To4P09xpR+YmGUWjQNRsioRGVPMv4CnL1dVoMdZ1NRWQKBgQDor4FcrqiUnYAFUK/7whz+z+0/E7RDTkpNJW5j7VyGIqqs48xZLtI5Gp+BQKdSqDoQinYvx+sAq5vOBJkvM/YSWOD6gQG+2IoBhrrHTDBEkv4BfAPYx0fjndCveQMQ2Bt1QlccQZSDWlbCb5mjECN7ZCQ7DLPA5UvrFHlOOa6FAQKBgQCJTGyBQqS9T1LuN9R5P83N6WL1KnETpaDXTruZmonVe3ucEEq982a6pjjrVZ6uCKRF5U2dBbFXHQRuXsje4YetmmmvYmRv0HGQkulElz4D23/WfF5sbYBdFOYgnaUCveK/TI3RGN34UD3t0FuZAREgySpor1mHaSMpXNVAMKr5GQKBgQDQFrKhTYopnIamJuN2ORc0ICDSHPe4woH9/Qz8sxOXeyp2FR0IuZC0Tp8BzkA/z7zGjBRjV8SYzYg58aZDS6tgSs9caQiaET2pTs1rpXqUe6SWuQrYGlPKUl3oO7RcUP9K8OwRrbwhNpOfkcIv5EjojkrbNgAy1chTQKX3XnrPsw==";

    //支付宝沙箱公钥
    public static String ALIPAY_PUBLIC_KEY_DEV = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqJlAFF19fyOYleDxc2bAZJx0klampb8ZLK/YDE8j+KIcyKaPtV0vA6lepaJwvBQjlLnEwrvdtXBCPwgDK8uc+3bD9mq4eH2lHSqfDOh6faaZoJ8Eg7e1GTq0cgdz5frQXGrH2fP1XWns8I3YmlSO/XJYDaBW76LJGRo/t9Yp6Opb7taNy9bZyVhPsfDmQaGfBKDDNaTQChkIvY2P+wUgs30fWC+OmFllv6tBt7Y+iqxMQx8/wFwalxWYukaAHEzReGx/x8EIEmhnAcqXP6r4WBE8DXJOay+GaNjfWgRK4m/1ruN2fk5w5Cp5Ht+jdZ61GmKJpkoERB06i8cViEwxeQIDAQAB";
    public static final String APP_ID_DEV = "2016093000632381";
    public static final String APP_PRIVATE_KEY_DEV = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQComUAUXX1/I5iV4PFzZsBknHSSVqalvxksr9gMTyP4ohzIpo+1XS8DqV6lonC8FCOUucTCu921cEI/CAMry5z7dsP2arh4faUdKp8M6Hp9ppmgnwSDt7UZOrRyB3Pl+tBcasfZ8/VdaezwjdiaVI79clgNoFbvoskZGj+31ino6lvu1o3L1tnJWE+x8OZBoZ8EoMM1pNAKGQi9jY/7BSCzfR9YL46YWWW/q0G3tj6KrExDHz/AXBqXFZi6RoAcTNF4bH/HwQgSaGcBypc/qvhYETwNck5rL4Zo2N9aBErib/Wu43Z+TnDkKnke36N1nrUaYommSgREHTqLxxWITDF5AgMBAAECggEBAI0ABYhCm8btZRnLkwJ5J2O9OPx9vUcWp54ovZ2lviacpUfljIAYG06Loh0R2ghhK1She/2EEipAZRbDw6sn0PS44bA+lmImaiUXVQb9khttbkPXWXYy+CDOaMSImRP4C7OOle8gSM7D3KVda42+NAugSiWzN6GX2WjF/A3p7QjdQzDOJxxYJK/pLB9peSDv0pOtRYUrEgzre8CDrQq8hcXJpHVOWWxvKc8YgXupIiSveS0F4dPhTBX/J7RRx7FIoXnLkzLKJJ/LIKUFaGp2Wt3NRql4gLMdqYXbqoaYQRiYNfoUz2a/nkvOL75/F8yuQNajhB/JzF33SxUrtLtCF4ECgYEA1qxawbjdkckPir+wFCtS2EncJxswALY/jWswYjp/ifT+ReA+QbZZsgtELaVuSaLSqMHNo5rodKg3lOWmRpbDv99mK7C2B8/rOkk4VYOqiMu1uqH/waV9HgGaNBZy2PcXaQ7T1mVFPBWt2kY2TDP4dabMR6nS4irVP660GIsuxIkCgYEAyQ44hRSxAEIFcdr2RJ34aCFVhPUoT6D8F/hKeqK12qo6JDERvEIbP78/J5gsz6Ny1fYLrhCAtmQ4F5JZoXSIRp0Wim2RO3KtrIMvKOaI5mP3+oxBD/UIjp8qwEWWQeBkOm2AQQRfVcHBt5y6G4XoYPleumbbPuP05WiYeXlhqXECgYAjSb+9ensJnOhuIWZ7WkfLABEtaH2ykELQJ1+TthQDBH/L0rnJKYWDC/EQI9KGTjqcvQblrvApRQGl+z4i+mOBJeLwqaKylA4Rzblx7SvaQspDvJ+DIIX5qb/EcjA81BlIDsTpyEniP5oNeF8ylZRxGpMX8KWWwf298yTtp+tvKQKBgACsx4vK0f1KO3JyZbk9PoQ6+GqF2CjMRYxfDQD0Yt/lCmTstT6IikbggXnEjQH1boFBXjCrZYM3Ouj5WMut9HcoN9cnHwGwFN0rY5EjlPoRA9gXeHBxRiIUbrROh36ZHj0D+RY+yYMkL15KbGWDQZBxxduiVMIVSdErVLezrJxBAoGANrnAQ8y15gSz7LdYWp1OLBBFZ3jRQbH6VeD8L/PPjdTDz6TTRtGvCfjGaWyR5PK0okzyvxNDwdqdPtUaL36Enkv6ZFZljS320vrdOD8nLfouBQb4hYmPpgtYD5ZvkX/4/6/30TrxRVXYAMwUqN3xAAY5oukCpVcSEXECbmaO3so=";


    //获取首页随机拍品的键值集合
    public static String AUCTION_GOODS_ID_RANDOM = "AUCTION_GOODS_ID_RANDOM";
}
