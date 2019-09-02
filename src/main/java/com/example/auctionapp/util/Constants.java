package com.example.auctionapp.util;

public class Constants {


	/**
	 * 优惠券发送类型
	 * @author zhuqiuyou
	 *
	 */
	public enum CouponSendType {

		sendType1(1, "注册"),
		sendType2(2, "邀请好友"),
		sendType3(3, "被客户邀请"),
		sendType4(4, "拍卖"),
        sendType5(5, "邀请抽奖"),
        sendType6(6, "被邀请抽奖");

		private int type;
		private String desc;

		CouponSendType(int type, String desc) {
			this.type = type;
			this.desc = desc;
		}


		public int getType() {
			return type;
		}

		public String getDesc() {
			return desc;
		}


		public static CouponSendType findByType(int type) {
			for (CouponSendType value : CouponSendType.values()) {
				if (value.type == type) {
					return value;
				}
			}
			return null;
		}
	}

}
