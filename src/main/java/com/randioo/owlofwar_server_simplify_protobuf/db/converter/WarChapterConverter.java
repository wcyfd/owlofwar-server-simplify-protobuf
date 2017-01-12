package com.randioo.owlofwar_server_simplify_protobuf.db.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.WarChapter;
import com.randioo.randioo_server_base.db.converter.ResultConverter;

public class WarChapterConverter implements ResultConverter<WarChapter> {

	public WarChapter convert(ResultSet rs) throws SQLException {
		WarChapter warBuild = new WarChapter();
		
		warBuild.setRoleId(rs.getInt("roleId"));
		warBuild.setChapterId(rs.getInt("chapterId"));
		warBuild.setAward(rs.getInt("award"));
		
		return warBuild;
	}
}
