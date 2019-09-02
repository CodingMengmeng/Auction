package com.example.auctionapp.util;

public class SdkConstants {

    public static final String EQUAL = "=";

    public static final String AMPERSAND = "&";

    /*------------------------------报文接口定义------------------------------*/
    /*------------------------------请求参数定义------------------------------*/
    //----------通用----------
    //商户号
    public static final String req_param_mch_no = "mch_no";
    //随机字符串
    public static final String req_param_random_str = "random_str";
    //签名
    public static final String req_param_sign = "sign";
    //用户终端IP
    public static final String req_param_customer_ip = "customer_ip";
    //金额
    public static final String req_param_amount = "amount";
    //币种
    public static final String req_param_coin = "coin";
    //用户卡号
    public static final String req_param_card_no = "card_no";
    //异步通知地址
    public static final String req_param_notify_url = "notify_url";

    //----------支付----------
    //商品描述
    public static final String req_param_goods_desc = "goods_desc";
    //商品详情
    public static final String req_param_goods_detail = "goods_detail";
    //小鲸库支付订单No
    public static final String req_param_pay_no = "pay_no";
    //商户支付订单No
    public static final String req_param_out_pay_no = "out_pay_no";
    //交易过期时间
    public static final String req_param_timeout_minute = "timeout_minute";
    //支付方式(App, Wap, ...)
    public static final String req_param_pay_type = "pay_type";

    //----------退款----------
    //小鲸库退款订单No
    public static final String req_param_refund_no = "refund_no";
    //商户退款订单No
    public static final String req_param_out_refund_no = "out_refund_no";
    //退款金额
    public static final String req_param_refund_amount = "refund_amount";
    //退款描述
    public static final String req_param_refund_desc = "refund_desc";

    //----------代付----------
    //小鲸库代付订单No
    public static final String req_param_merchant_pay_no = "merchant_pay_no";
    //商户代付订单No
    public static final String req_param_out_merchant_pay_no = "out_merchant_pay_no";
    //代付描述
    public static final String req_param_merchant_pay_desc = "merchant_pay_desc";
    //批量代付任务号
    public static final String req_param_out_merchant_pay_task_no = "out_merchant_pay_task_no";
    //系统代付任务号
    public static final String req_param_merchant_pay_task_no = "merchant_pay_task_no";
    //批量代付描述
    public static final String req_param_merchant_pay_task_desc = "merchant_pay_task_desc";
    //批量代付csv md5
    public static final String req_param_merchant_pay_csv_md5 = "merchant_pay_csv_md5";

    //----------代扣----------
    //代扣订单No
    public static final String req_param_approved_pay_no = "approved_pay_no";
    //商户代扣订单No
    public static final String req_param_out_approved_pay_no = "out_approved_pay_no";
    //代扣 代扣描述
    public static final String req_param_approved_pay_desc = "approved_pay_desc";

    //----------授权通用----------
    //授权认证方式(App, Wap, ...)
    public static final String req_param_auth_type = "auth_type";
    //授权申请流水号
    public static final String req_param_out_apply_no = "out_apply_no";

    //----------代扣授权----------
    //代扣授权 单笔上限
    public static final String req_param_per_limit_btc = "per_limit_btc";
    //代扣授权 单日上限
    public static final String req_param_daily_limit_btc = "daily_limit_btc";
    //代扣授权 单月上限
    public static final String req_param_monthly_limit_btc = "monthly_limit_btc";

    //----------用户绑定----------
    //用户手机
    public static final String req_param_customer_mobile = "customer_mobile";

    /*------------------------------应答参数定义------------------------------*/
    //----------通用----------
    //返回状态码
    public static final String res_param_return_code = "return_code";
    //返回信息
    public static final String res_param_return_msg = "return_msg";
    //业务结果
    public static final String res_param_result_code = "result_code";
    //随机字符串
    public static final String res_param_random_str = "random_str";
    //错误代码
    public static final String res_param_err_code = "err_code";
    //错误消息
    public static final String res_param_err_msg = "err_msg";
    //商户号
    public static final String res_param_mch_no = "mch_no";
    //签名
    public static final String res_param_sign = "sign";
    //金额
    public static final String res_param_amount = "amount";
    //币种
    public static final String res_param_coin = "coin";

    //----------支付----------
    //支付方式
    public static final String res_param_pay_type = "pay_type";
    //支付跳转链接
    public static final String res_param_pay_url = "pay_url";
    //支付订单号
    public static final String res_param_pay_no = "pay_no";
    //商户支付订单号
    public static final String res_param_out_pay_no = "out_pay_no";
    //支付状态
    public static final String res_param_pay_state = "pay_state";
    //支付时间
    public static final String res_param_pay_date = "pay_date";
    //付款人
    public static final String res_param_payer_acc = "payer_acc";

    //----------退款----------
    //退款订单号
    public static final String res_param_refund_no = "refund_no";
    //商户退款订单号
    public static final String res_param_out_refund_no = "out_refund_no";
    //订单总金额
    public static final String res_param_total_amount = "total_amount";
    //退款金额
    public static final String res_param_refund_amount = "refund_amount";
    //退款状态
    public static final String res_param_refund_state = "refund_state";
    //退款时间
    public static final String res_param_refund_date = "refund_date";
    //收款人
    public static final String res_param_payee_acc = "payee_acc";

    //----------代付----------
    //代付订单号
    public static final String res_param_merchant_pay_no = "merchant_pay_no";
    //商户代付订单号
    public static final String res_param_out_merchant_pay_no = "out_merchant_pay_no";
    //代付状态
    public static final String res_param_merchant_pay_state = "merchant_pay_state";
    //代付时间
    public static final String res_param_merchant_pay_date = "merchant_pay_date";

    //----------代付----------
    //批量代付任务号
    public static final String res_param_merchant_pay_task_no = "merchant_pay_task_no";
    //商户批量代付任务号
    public static final String res_param_out_merchant_pay_task_no = "out_merchant_pay_task_no";
    //批量代付总数
    public static final String res_param_merchant_pay_total = "merchant_pay_total";
    //批量代付成功数
    public static final String res_param_merchant_pay_successes = "merchant_pay_successes";
    //批量代付失败数
    public static final String res_param_merchant_pay_failures = "merchant_pay_failures";
    //代付时间
    public static final String res_param_merchant_pay_task_date = "merchant_pay_task_date";
    //回执csv文件md5
    public static final String res_param_merchant_pay_csv_md5 = "merchant_pay_csv_md5";
    //回执csv文件过期时间
    public static final String res_param_merchant_pay_csv_expire_date = "merchant_pay_csv_expire_date";


    //----------代扣----------
    //代扣订单号
    public static final String res_param_approved_pay_no = "approved_pay_no";
    //商户代扣订单号
    public static final String res_param_out_approved_pay_no = "out_approved_pay_no";
    //代扣状态
    public static final String res_param_approved_pay_state = "approved_pay_state";
    //代扣时间
    public static final String res_param_approved_pay_date = "approved_pay_date";

    //----------申请通用----------
    //申请业务流水号
    public static final String res_param_apply_no = "apply_no";
    //商户申请业务流水号
    public static final String res_param_out_apply_no = "out_apply_no";
    //申请状态
    public static final String res_param_apply_state = "apply_state";
    //认证方式
    public static final String res_param_auth_type = "auth_type";
    //认证url
    public static final String res_param_auth_url = "auth_url";
    //用户卡号
    public static final String res_param_card_no = "card_no";

    //----------代扣授权申请结果----------
    //单笔限额
    public static final String res_param_per_limit_btc = "per_limit_btc";
    //每日限额
    public static final String res_param_daily_limit_btc = "daily_limit_btc";
    //每月限额
    public static final String res_param_monthly_limit_btc = "monthly_limit_btc";

    //----------绑定用户申请结果----------
    //用户手机
    public static final String res_param_customer_mobile = "customer_mobile";
    //是否已授权账户查询
    public static final String res_param_is_account_approved = "is_account_approved";
    //是否已授权代扣
    public static final String res_param_is_pay_approved = "is_pay_approved";

    //-----------申请跳转认证服务器----------
    public static final String res_param_redirect_url = "redirect_url";

    public static final String res_param_accounts = "accounts";

    //-----------打开小鲸库钱包-------------
    public static final String res_param_wallet_url = "wallet_url";
}
