package com.randioo.owlofwar_server_simplify_protobuf.module.role.service;

import com.randioo.owlofwar_server_simplify_protobuf.db.dao.RoleDao;
import com.randioo.randioo_server_base.module.BaseService;


public class RoleServiceImpl extends BaseService implements RoleService {

	private RoleDao roleDao;

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	
	@Override
	public void init() {
		roleDao.serverStartInit();
	}
//
//	private LoginService loginService;
//
//	public void setLoginService(LoginService loginService) {
//		this.loginService = loginService;
//	}
//
//	
//
//	@Override
//	public void addFood(Role role, int value) {
//		byte method = 0;
//		this.addFood(role, value, method);
//	}
//
//	// @Override
//	// public void addFood(Role role, int value, byte method) {
//	// int originTotal = role.getFood();
//	// int total = role.getFood() + value;
//	// if (total < 0) {
//	// if (value >= 0) {
//	// total = Integer.MAX_VALUE;
//	// } else {
//	// total = 0;
//	// }
//	// }
//	// role.setFood(total);
//	// this.checkCapacity(role, total, IncomeConstant.ITEM_TYPE_FOOD);
//	// total = role.getFood();
//	// value = total - originTotal;
//	// IoSession ioSession = SessionCache.getSessionById(role.getRoleId());
//	// Message message = new Message();
//	// message.setType(RoleConstant.ADD_FOOD_ROLE);
//	// message.putInt(value);
//	// annalsService.recordIncome(role, IncomeConstant.ITEM_TYPE_FOOD, value);
//	// if (ioSession != null) {
//	// ioSession.write(message);
//	// }
//	// }
//
//	@Override
//	public void addFood(Role role, int value, byte method) {
//		int originTotal = role.getFood();
//		int total = role.getFood() + value;
//		if (total < 0) {
//			if (value >= 0) {
//				total = Integer.MAX_VALUE;
//			} else {
//				total = 0;
//			}
//		}
//
//		if (method == 0) {// 正常
//			// 如果还没有到达容量上限，则加入 
//			if (value > 0) {
//				if (!this.checkCapacity(role, total, IncomeConstant.ITEM_TYPE_FOOD)) {
//					role.setFood(total);
//					this.repairCapacity(role, IncomeConstant.ITEM_TYPE_FOOD);
//					total = role.getFood();
//					value = total - originTotal;
//				} else {
//					value = 0;
//				}
//			} else {
//				role.setFood(total);
//				value = total - originTotal;
//			}
//
//		} else if (method == 1) {// 金币
//			role.setFood(total);
//			value = total - originTotal;
//		} else if (method == 2) {// GM
//			role.setFood(total);
//			value = total - originTotal;
//		}
//		IoSession ioSession = SessionCache.getSessionById(role.getRoleId());
//		Message message = new Message();
//		message.setType(RoleConstant.ADD_FOOD_ROLE);
//		message.putInt(value);
//		if (ioSession != null) {
//			ioSession.write(message);
//		}
//		annalsService.recordIncome(role, IncomeConstant.ITEM_TYPE_FOOD, value);
//	}
//
//	@Override
//	public void addMoney(Role role, int value) {
//		byte method = 0;
//		this.addMoney(role, value, method);
//
//	}
//
//	// @Override
//	// public void addMoney(Role role, int value, byte payMethod) {
//	// int originTotal = role.getMoney();
//	// int total = role.getMoney() + value;
//	// if (total < 0) {
//	// if (value >= 0) {
//	// total = Integer.MAX_VALUE;
//	// } else {
//	// total = 0;
//	// }
//	// }
//	// role.setMoney(total);
//	// this.checkCapacity(role, IncomeConstant.ITEM_TYPE_MONEY);
//	// total = role.getMoney();
//	// value = total - originTotal;
//	// IoSession ioSession = SessionCache.getSessionById(role.getRoleId());
//	// Message message = new Message();
//	// message.setType(RoleConstant.ADD_MONEY_ROLE);
//	// message.putInt(value);
//	// annalsService.recordIncome(role, IncomeConstant.ITEM_TYPE_MONEY, value);
//	// if (ioSession != null) {
//	// ioSession.write(message);
//	// }
//	// }
//
//	@Override
//	public void addMoney(Role role, int value, byte method) {
//		int originTotal = role.getMoney();
//		int total = role.getMoney() + value;
//		if (total < 0) {
//			if (value >= 0) {
//				total = Integer.MAX_VALUE;
//			} else {
//				total = 0;
//			}
//		}
//
//		if (method == 0) {// 正常
//			// 如果还没有到达容量上限，则加入
//			if (value > 0) {
//				if (!this.checkCapacity(role, total, IncomeConstant.ITEM_TYPE_MONEY)) {
//					role.setMoney(total);
//					this.repairCapacity(role, IncomeConstant.ITEM_TYPE_MONEY);
//					total = role.getMoney();
//					value = total - originTotal;
//				} else {
//					value = 0;
//				}
//			} else {
//				role.setMoney(total);
//				value = total - originTotal;
//			}
//
//		} else if (method == 1) {// 金币
//			role.setMoney(total);
//			value = total - originTotal;
//		} else if (method == 2) {// GM
//			role.setMoney(total);
//			value = total - originTotal;
//		}
//		IoSession ioSession = SessionCache.getSessionById(role.getRoleId());
//		Message message = new Message();
//		message.setType(RoleConstant.ADD_MONEY_ROLE);
//		message.putInt(value);
//		if (ioSession != null) {
//			ioSession.write(message);
//		}
//		annalsService.recordIncome(role, IncomeConstant.ITEM_TYPE_MONEY, value);
//	}
//
//	@Override
//	public void addGold(Role role, int value) {
//		int originTotal = role.getGold();
//		int total = role.getGold() + value;
//		if (total < 0) {
//			if (value >= 0) {
//				total = Integer.MAX_VALUE;
//			} else {
//				total = 0;
//			}
//		}
//		value = total - originTotal;
//		role.setGold(total);
//		IoSession ioSession = SessionCache.getSessionById(role.getRoleId());
//		Message message = new Message();
//		message.setType(RoleConstant.ADD_GOLD_ROLE);
//		message.putInt(value);
//		annalsService.recordIncome(role, IncomeConstant.ITEM_TYPE_GOLD, value);
//		if (ioSession != null) {
//			ioSession.write(message);
//		}
//	}
//
//	@Override
//	public Role getRoleById(int roleId) {
//		Role role = RoleCache.getRoleById(roleId);
//		if (role == null) {
//			role = roleDao.getRoleById(roleId);
//			if (role == null) {
//				return role;
//			}
//			loginService.loginRoleModuleDataInit(role);
//		}
//		return role;
//	}
//
//	@Override
//	public Role getRoleByAccount(String account) {
//		Role role = RoleCache.getRoleByAccount(account);
//		if (role == null) {
//			role = roleDao.getRoleByAccount(account);
//			if (role == null)
//				return role;
//			loginService.loginRoleModuleDataInit(role);
//		}
//
//		return role;
//	}
//
//	private boolean checkCapacity(Role role, int total, byte resType) {
//		Income income = role.getIncome();
//		int lv = income.getBuildLvMap().get(IncomeConstant.BUILD_TYPE_STORE);
//		BuildConfig buildConfig = BuildConfigCache.getBuildLvMapByBuildType(IncomeConstant.BUILD_TYPE_STORE).get(lv);
//		BuildAttributeStore element = (BuildAttributeStore) buildConfig.getAttributeMap().get(
//				IncomeConstant.ATTRIBUTE_STORE);
//		Map<Byte, Integer> storeMap = element.getStoreMap();
//
//		int store = storeMap.get(resType);
//		if (resType == IncomeConstant.ITEM_TYPE_MONEY) {
//			if (total > store) {
//				return true;
//			}
//		} else if (resType == IncomeConstant.ITEM_TYPE_FOOD) {
//			if (total > store) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	private void repairCapacity(Role role, byte resType) {
//		Income income = role.getIncome();
//		int lv = income.getBuildLvMap().get(IncomeConstant.BUILD_TYPE_STORE);
//		BuildConfig buildConfig = BuildConfigCache.getBuildLvMapByBuildType(IncomeConstant.BUILD_TYPE_STORE).get(lv);
//		BuildAttributeStore element = (BuildAttributeStore) buildConfig.getAttributeMap().get(
//				IncomeConstant.ATTRIBUTE_STORE);
//		Map<Byte, Integer> storeMap = element.getStoreMap();
//
//		int store = storeMap.get(resType);
//		if (resType == IncomeConstant.ITEM_TYPE_MONEY) {
//			if (role.getMoney() > store) {
//				role.setMoney(store);
//			}
//		} else if (resType == IncomeConstant.ITEM_TYPE_FOOD) {
//			if (role.getFood() > store) {
//				role.setFood(store);
//			}
//		}
//	}
//
//	@Override
//	public Message getRoleInfo(int roleId) {
//		Message message = new Message();
//		message.setType(RoleConstant.GET_ROLE_INFO);
//		Role targetRole = this.getRoleById(roleId);
//		Income income = targetRole.getIncome();
//		int palaceLv = income.getBuildLvMap().get(IncomeConstant.BUILD_TYPE_PALACE);
//
//		String roleName = targetRole.getName();
//		int legionId = targetRole.getLegionId();
//		Legion legion = LegionCache.getLegionById(legionId);
//		int legionIconId = 0;
//
//		War targetRoleWar = targetRole.getWar();
//
//		String legionName = "";
//		byte legionDuty = LegionConstant.NO_MEMBER;
//		int heroCount = 0;
//		int point = targetRole.getPoint();
//		int warLevel = targetRoleWar.getMaxLevel();
//		int winCount = targetRole.getWarWinCount();
//		int useCardListId = targetRole.getUseCardsId();
//		CardList cardList = targetRole.getCardListMap().get(useCardListId);
//		int size = cardList.getList().size() + 1;
//		int mainId = cardList.getMainId();
//		Map<Integer, Card> cardMap = targetRole.getCardMap();
//		byte lv = cardMap.get(mainId).getLv();
//
//		List<Integer> cards = cardList.getList();
//
//		if (legion != null) {
//			legionName = legion.getName();
//			legionDuty = legionService.identification(targetRole);
//			legionIconId = legion.getIconId();
//		}
//		for (Card card : cardMap.values()) {
//			int cardId = card.getCardId();
//			CardConfig config = CardConfigCache.getConfigById(cardId);
//			if (config.getType() == CardConstant.CARD_HERO) {
//				heroCount++;
//			}
//		}
//		message.putString(roleName);
//		message.putInt(legionId);
//		message.putString(legionName);
//		message.putInt(legionIconId);
//		message.put(legionDuty);
//		message.putInt(heroCount);
//		message.putInt(point);
//		message.putInt(warLevel);
//		message.putInt(winCount);
//		message.putInt(palaceLv);
//		message.putInt(size);
//		{
//			message.putInt(mainId);
//			message.put(lv);
//			message.put((byte) 1);
//			for (Integer cardId : cards) {
//				message.putInt(cardId);
//				Card card = targetRole.getCardMap().get(cardId);
//				message.put(card.getLv());
//				message.put((byte) 0);
//			}
//		}
//		return message;
//	}
//
//	@Override
//	public byte checkNameIllege(String name) {
//		if (name.length() > RoleConstant.NAME_MAX_LENGTH || name.length() < RoleConstant.NAME_MIN_LENGTH) {
//			return 1;
//		}
//
//		if (SensitiveWordDictionary.containsSensitiveWord(name)) {
//			return 2;
//		}
//
//		return 0;
//	}
//
//	@Override
//	public Message showRenameInfo(Role role) {
//		Message message = new Message();
//		message.setType(RoleConstant.SHOW_RENAME_INFO);
//
//		int renameNeedGold = 0;
//		if (role.getRenameCount() != 0) {
//			renameNeedGold = RoleConstant.RENAME_NEED_GOLD;
//		}
//		message.putInt(renameNeedGold);
//		return message;
//	}
//
//	@Override
//	public Message rename(Role role, String newName) {
//		Message message = new Message();
//		message.setType(RoleConstant.RENAME);
//
//		String name = role.getName();
//
//		// 判断名字是否和谐
//		byte newNameCheck = this.checkNameIllege(newName);
//		if (newNameCheck != 0) {
//			if (newNameCheck == 1) {
//				message.putShort(ErrorCode.NAME_TOO_LONG);
//				return message;
//			} else if (newNameCheck == 2) {
//				message.putShort(ErrorCode.NAME_NOT_ILLEGEL);
//				return message;
//			}
//		}
//
//		if (RoleCache.getNameSet().contains(newName)) {
//			message.putShort(ErrorCode.NAME_IS_AREADY_HAS);
//			return message;
//		}
//
//		int renameNeedGold = 0;
//		if (role.getRenameCount() != 0) {
//			renameNeedGold = RoleConstant.RENAME_NEED_GOLD;
//		}
//
//		if (role.getGold() < renameNeedGold) {
//			message.putShort(ErrorCode.NO_GOLD);
//			return message;
//		}
//
//		this.addGold(role, -renameNeedGold);
//
//		role.setName(newName);
//		role.setRenameCount(role.getRenameCount() + 1);
//		RoleCache.getNameSet().remove(name);
//		RoleCache.getNameSet().add(newName);
//
//		message.putShort(ErrorCode.SUCCESS);
//		return message;
//	}
//
//	@Override
//	public Message changeReceiveMessage(Role role, byte receive) {
//		Message message = new Message();
//		message.setType(RoleConstant.CHANGE_RECEIVE_MESSAGE);
//
//		if (receive != RoleConstant.RECEIVE_MESSAGE && receive != RoleConstant.REJECT_MESSAGE) {
//			receive = RoleConstant.RECEIVE_MESSAGE;
//		}
//		role.setReceive(receive);
//		message.putShort(ErrorCode.SUCCESS);
//
//		return message;
//	}
}
