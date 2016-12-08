package com.randioo.owlofwar_server_simplify_protobuf.entity.po;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.randioo.owlofwar_server_simplify_protobuf.cache.file.AutoNpcConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.entity.file.AutoNpcConfig;
import com.randioo.owlofwar_server_simplify_protobuf.formatter.ResourceFormatter;
import com.randioo.owlofwar_server_simplify_protobuf.utils.RandomUtils;

public class FightEventListenerAdapter implements FightEventListener {
	private Role role;

	private Set<Resource> resources = new HashSet<>();

	public FightEventListenerAdapter(Role role) {
		this.role = role;
	}

	
	@Override
	public String getAward() {
		return ResourceFormatter.formatResources(resources);
	}

	@Override
	public void setAward(String award) {
		if (award == null)
			return;
		Set<Resource> formatResources = ResourceFormatter.formatResouces(award);
		ResourceFormatter.combineResource(formatResources, resources);
	}

	public int getAIMapIdByPoint() {
		int point = getRole().getPoint();
		List<AutoNpcConfig> list = AutoNpcConfigCache.getAllAutoNpcConfigList();
		int mapsId = 0;
		for (AutoNpcConfig config : list) {
			if (point > config.getPoint())
				continue;

			int index = RandomUtils.getRandomNum(config.getNpcIdList().size());
			mapsId = config.getNpcIdList().get(index);
			return mapsId;

		}
		mapsId = 0;
		AutoNpcConfig config = list.get(list.size() - 1);
		int index = RandomUtils.getRandomNum(config.getNpcIdList().size());
		mapsId = config.getNpcIdList().get(index);

		return mapsId;
	}

	@Override
	public Role getRole() {
		return role;
	}

	@Override
	public byte getReturnType(Role role) {
		return 0;
	}


	@Override
	public int getAI() {
		return 0;
	}

	@Override
	public int getPalaceLv() {
		
		return 0;
	}

	@Override
	public void enterGame(Role role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void matchFighter(OwlofwarGame game, Role role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readyFight(OwlofwarGame game, Role role1, Role role2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startFight(OwlofwarGame game, Role role1, Role role2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterEnd(OwlofwarGame game, byte result) {
		// TODO Auto-generated method stub
		
	}
}
