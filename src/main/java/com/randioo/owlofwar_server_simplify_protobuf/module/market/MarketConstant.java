package com.randioo.owlofwar_server_simplify_protobuf.module.market;

import java.util.HashMap;
import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.MarketItemDataBuyType;

public class MarketConstant {
	public enum MarketBuyType {
		MONEY, GOLD, RMB
	}
	public static Map<MarketBuyType,MarketItemDataBuyType> buyTypeMap = new HashMap<>();
	static{
		//设置游戏服务器中的枚举与序列化枚举的映射
		buyTypeMap.put(MarketBuyType.GOLD, MarketItemDataBuyType.GOLD);
		buyTypeMap.put(MarketBuyType.MONEY, MarketItemDataBuyType.MONEY);
		buyTypeMap.put(MarketBuyType.RMB, MarketItemDataBuyType.RMB);
	}
	/**商品栏的数量*/
	public static int MARKET_ITEM_COUNT = 4;	
	/**最大刷新次数*/
	public static int MAX_FREE_REFRESH_COUNT = 5;
}
