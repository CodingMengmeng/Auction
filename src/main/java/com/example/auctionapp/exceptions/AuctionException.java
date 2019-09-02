package com.example.auctionapp.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * 拍品异常信息定义
 *
 */
@Slf4j
public class AuctionException extends BizException {

	private static final long serialVersionUID = 1L;


	public static final AuctionException AUCTION_GOODS_NOT_EXIT = new AuctionException(60001, "手慢了，宝贝被其他人抢走啦~");


	public AuctionException() {
	}

	public AuctionException(int code, String msgFormat, Object... args) {
		super(code, msgFormat, args);
	}

	public AuctionException(int code, String msg) {
		super(code, msg);
	}

	/**
	 * 实例化异常
	 * 
	 * @param msgFormat
	 * @param args
	 * @return
	 */
    @Override
	public AuctionException newInstance(String msgFormat, Object... args) {
		return new AuctionException(this.code, msgFormat, args);
	}

	public AuctionException print() {
		log.info("==>AuctionBizException, code:" + this.code + ", msg:" + this.msg);
		return new AuctionException(this.code, this.msg);
	}
}
