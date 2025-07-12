package com.example.demo.dao;
import com.example.demo.model.GroupBuy;
import com.example.demo.model.Meetup;
import org.apache.ibatis.annotations.*;
import java.util.List;
@Mapper
public interface XiaoyuanDao {
	
	@Select("select * from meetup where region=#{region} and campus=#{campus} and is_delete=0 order by c_time desc limit #{length},10 ")
	public List<Meetup>getMeetupByRegionCampus(int length, String region, String campus);
	
	@Select("select * from meetup where region=#{region} and is_delete=0 order by c_time desc limit #{length},10")
	public List<Meetup>getMeetupByRegion(int length, String region);
	
	@Select("select * from meetup where region=#{region} order by c_time desc ")
	public List<Meetup>getAllMeetupByRegion(String region);
	
	@Select("select * from meetup where region=#{region} and campus in (#{campus},'0') and is_delete=0 order by c_time desc limit #{length},10 ")
	public List<Meetup>getMeetupByRegionCampusAndAll(int length, String region, String campus);
	
	@Select("select * from meetup where pax<limitation and region=#{region} and is_delete=0 order by c_time desc limit #{length},10 ")
	public List<Meetup>getAvailableMeetupByRegionXiaoyuan(int length, String region);
	
	@Select("select * from meetup where pax<limitation and region=#{region} and campus=#{campus} and is_delete=0 order by c_time desc limit #{length},10 ")
	public List<Meetup>getAvailableMeetupXiaoyuan(int length, String region, String campus);
	
	@Select("select * from meetup where group_id=#{group_id}")
	public List<Meetup> getMeetupByIdXiaoyuan(String group_id);
	
	@Select("select * from meetup where category=#{category} and is_delete=0 order by c_time desc")
	public List<Meetup> getMeetupByCategoryXiaoyuan(String category);
	
	@Select("select * from meetup where region=#{region} and category=#{category} and is_delete=0 order by c_time desc limit #{length},10 ")
	public List<Meetup> getMeetupByRegionCategoryXiaoyuan(String category, int length, String region);
	
	@Select("select * from meetup where region=#{region} and campus=#{campus} and category=#{category} and is_delete=0 order by c_time desc limit #{length},10 ")
	public List<Meetup> getMeetupByRegionCampusCategoryXiaoyuan(String category, int length, String region, String campus);
	
	@Select("select * from meetup where region=#{region} and campus in (#{campus},'0') and category=#{category} and is_delete=0 order by c_time desc limit #{length},10 ")
	public List<Meetup> getMeetupByRegionCampusAllCategoryXiaoyuan(String category, int length, String region, String campus);
	
	@Select("select * from meetup where pax<limitation and region=#{region} and campus=#{campus} and category=#{category} and is_delete=0 order by c_time desc")
	public List<Meetup> getAvailableMeetupByCategoryXiaoyuan(String category);
	
	@Select("select * from meetup where release_openid=#{openid} and is_delete=0 order by c_time desc")
	public List<Meetup> getMeetupByReleaseOpenidXiaoyuan(String openid);
	
	@Select("select * from meetup where join_openid like '#{openid}' and is_delete=0 order by c_time desc")
	public List<Meetup> getMeetupByJoinOpenidXiaoyuan(String openid);
	
	@Insert("insert into meetup (group_id, category, description, limitation, pax, "
			+ "release_openid, is_delete, due_datetime, c_time, region, campus) "
			+" values (#{group_id}, #{category}, #{description}, #{limitation}, #{pax}, "
			+ " #{release_openid}, #{is_delete}, #{due_datetime}, NOW(), #{region}, #{campus})")
	int addMeetup(Meetup meetup);
	
	@Update("update meetup set is_delete=1 where group_id=#{group_id}")
	int deleteMeetupByGroupId(String group_id);
	
	@Update("update meetup set is_delete=0 where group_id=#{group_id}")
	int restoreMeetupByGroupId(String group_id);
	
	@Update("update meetup set is_delete=1 where id=#{id}")
	int deleteMeetupById(int id);
	
	@Update("update meetup set is_delete=0 where id=#{id}")
	int restoreMeetupById(int id);
	
	@Update("update meetup set pax=pax-1, join_openid=#{openid} where group_id=#{group_id}")
	int updateMeetupJoiner(String group_id, String openid);
	
	@Update("update meetup set pax=pax+1, join_openid = case when join_openid is null then #{openid} "
			+ "when join_openid='' then #{openid} else concat(join_openid,',',#{openid}) end "
			+ "where group_id=#{group_id}")
	int addMeetupJoiner(String group_id, String openid);
	
	@Select("select * from groupbuy where region=#{region} and campus=#{campus} and is_delete=0 order by c_time desc")
	public List<GroupBuy> getAllGroupBuyByRegionCampus(String region, String campus);
	
	@Select("select * from groupbuy where region=#{region} and campus=#{campus} and is_delete=0 order by c_time desc limit #{length},10 ")
	public List<GroupBuy> getGroupBuyByRegionCampus(int length, String region, String campus);
	
	@Select("select * from groupbuy where region=#{region} and campus in (#{campus},'0') and is_delete=0 order by c_time desc limit #{length},10 ")
	public List<GroupBuy> getGroupBuyByRegionCampusAndAll(int length, String region, String campus);
	
	@Select("select * from groupbuy where id=#{id} ")
	public List<GroupBuy> getGroupBuyByIdXiaoyuan(String id);
	
	@Update("update groupbuy set is_delete=1 where id=#{id}")
	int deleteGroupBuyById(int id);
	
	@Insert("insert into groupbuy (name, ori_price, current_price, discount, qr_pic"
			+ ", poster_pic, is_delete, c_time, region, campus) "
			+ " values (#{name}, #{ori_price}, #{current_price}, #{discount}, #{qr_pic}, "
			+ " #{poster_pic}, 0, NOW(), #{region}, #{campus})")
	int addGroupBuy(GroupBuy groupbuy);
	
	
}
