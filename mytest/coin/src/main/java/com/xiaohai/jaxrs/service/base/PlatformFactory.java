package com.xiaohai.jaxrs.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaohai.constant.enums.Platform;
import com.xiaohai.jaxrs.service.platform.BinanceService;
import com.xiaohai.jaxrs.service.platform.BitzServcie;
import com.xiaohai.jaxrs.service.platform.HuobiService;

@Service
public class PlatformFactory {
	
	@Autowired
	private BitzServcie bitServcie;
	
	@Autowired
	private HuobiService huobiService;
	
	@Autowired
	private BinanceService binanceService;
	
	public PlatformService getPlatformService(String platform) {
		PlatformService platformService = null;
		
		Platform platformEnum = Platform.getPlatform(platform);
		switch (platformEnum) {
		case Bitz:
			platformService = (PlatformService) bitServcie;
			break;
			
		case Huobi:
			platformService = (PlatformService) huobiService;
			break;
			
		case Binance:
			platformService = (PlatformService) binanceService;
			break;

		default:
			break;
		}
		
		return platformService;
	}
	
}
