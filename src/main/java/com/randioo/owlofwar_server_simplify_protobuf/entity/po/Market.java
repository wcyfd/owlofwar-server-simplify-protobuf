package com.randioo.owlofwar_server_simplify_protobuf.entity.po;

import java.util.HashMap;
import java.util.Map;

import com.randioo.randioo_server_base.utils.db.Saveable;

/**
 * 商城
 * 
 * @author wcy 2016年6月7日
 *
 */
public class Market implements Saveable {
	/** 玩家id */
	private int roleId;
	/** 刷新时间 */
	private int refreshTime;
	/** 刷新次数 */
	private int refreshCount;
	/** 刷新物品表 */
	private Map<Integer, MarketItem> marketItemMap = new HashMap<>();
	/**是否显示动画*/
	private boolean showAnimation;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		setChange(true);
		this.roleId = roleId;
	}

	public int getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(int refreshTime) {
		this.refreshTime = refreshTime;
	}

	public Map<Integer, MarketItem> getMarketItemMap() {
		return marketItemMap;
	}

	public int getRefreshCount() {
		return refreshCount;
	}

	public void setRefreshCount(int refreshCount) {
		this.refreshCount = refreshCount;
	}
	
	public boolean isShowAnimation() {
		return showAnimation;
	}
	
	public void setShowAnimation(boolean showAnimation) {
		this.showAnimation = showAnimation;
	}

	@Override
	public boolean checkChange() {
		return change;
	}

	/** 是否改变 */
	private boolean change;

	@Override
	public void setChange(boolean change) {
		this.change = change;
	}

	@Override
	public boolean isChange() {
		if (!change)
			change = checkChange();
		return change;
	}


}
