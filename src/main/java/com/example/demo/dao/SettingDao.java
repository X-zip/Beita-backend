package com.example.demo.dao;
import com.example.demo.model.Avatar;
import com.example.demo.model.NickName;
import org.apache.ibatis.annotations.*;
import java.util.List;
@Mapper
public interface SettingDao {
	
	@Select("select id, nickname,campus from nickname where campus=#{campus}")
	public List<NickName> getallNickname(String campus);
	
	@Delete("delete from nickname where campus=#{campus} and id=#{id}")
	public int deleteNickName(String campus, int id);
	
	@Insert("insert into nickname(campus, nickname) values (#{campus}, #{nickname})")
	public int addNickName(String campus, String nickname);
	
	@Select("select id, avatar,campus from avatar where campus=#{campus}")
	public List<Avatar> getallAvatar(String campus);
	
	@Delete("delete from avatar where campus=#{campus} and id=#{id}")
	public int deleteAvatar(String campus, int id);
	
	@Insert("insert into avatar(campus, avatar) values (#{campus}, #{avatar})")
	public int addAvatar(String campus, String avatar);
}
