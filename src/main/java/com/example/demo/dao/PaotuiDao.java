package com.example.demo.dao;
import com.example.demo.model.TaskPaotui;
//import com.example.demo.model.User;
import com.example.demo.model.WXTemplate;
import com.example.demo.model.Rider;
import com.example.demo.model.BlackList;
import com.example.demo.model.Secret;
import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.Profile;

import org.apache.ibatis.annotations.*;
import java.util.List;
@Mapper
public interface PaotuiDao {
	
//	@Select("select * from user where openid = #{openid} and is_blacklist=1")
//    public List<User>checkBlackList(String openid);
	
	@Insert("insert into task_paotui (order_id, category, address_from, address_to, due_date, due_time,"
			+" description, content, price, release_openid, img_url, list_datetime, update_datetime,"
			+ "release_phone, release_wechat,release_name,amount,weight,status, region, campusGroup, trade_number) "
			+"values(#{orderid}, #{category}, #{address_from}, #{address_to}, #{due_date}, "
			+"#{due_time}, #{description}, #{content}, #{price}, #{release_openid}, #{img_url}, NOW(), NOW(),"
			+ "#{release_phone}, #{release_wechat},#{release_name},#{amount},#{weight},#{status}, "
			+ "#{region}, #{campusGroup}, #{trade_number})")
	public int addTask(String orderid, String category, String address_from,
			String address_to, String due_date, String due_time, String description,
			String content, int price, String release_openid, String img_url,
			String release_phone, String release_wechat, String release_name, String amount, String weight, int status,
			String region, String campusGroup, String trade_number);
	
	@Select("select * from task_paotui where region=#{region} and campusGroup=#{campusGroup} and is_delete=0 and is_complaint=0 and status!=0 and status !=5 order by update_datetime desc limit #{length},10")
	public List<TaskPaotui>getallTask(int length, String region, String campusGroup);
	
	@Select("select * from task_paotui where region=#{region} and is_delete=0 and is_complaint=0 and `status`!=0 and `status`!=5 and `status`!=-2 order by update_datetime desc limit #{length},10")
	public List<TaskPaotui>getallTaskByRegion(int length, String region);
	
	
	@Select("select * from  task_paotui where order_id= #{order_id}" )
    public List<TaskPaotui>gettaskbyOrderId(String order_id);
	
	@Select("select * from  task_paotui where region=#{region} and campusGroup=#{campusGroup} and release_openid= #{openid} and is_complaint=0 order by update_datetime desc limit #{length},10" )
    public List<TaskPaotui>gettaskbyReleaseOpenId(String openid, int length, String region, String campusGroup);
	
	@Select("select * from  task_paotui where region=#{region} and release_openid= #{openid} and is_complaint=0 order by update_datetime desc limit #{length},10" )
    public List<TaskPaotui>gettaskbyReleaseOpenIdRegion(String openid, int length, String region);
	
	
	@Select("select * from  task_paotui where region=#{region} and campusGroup=#{campusGroup} and pickup_openid= #{openid} and is_complaint=0 order by update_datetime desc limit #{length},10" )
    public List<TaskPaotui>gettaskbyPickupOpenId(String openid, int length, String region, String campusGroup);
	
	@Select("select * from  task_paotui where region=#{region} and campusGroup=#{campusGroup} and pickup_openid= #{openid} and status in (2,3) order by update_datetime desc limit 1" )
    public List<TaskPaotui>getCurrentTaskbyPickupOpenId(String openid, String region, String campusGroup);
	
	@Select("select * from  task_paotui where region=#{region} and pickup_openid= #{openid} and status in (2,3) order by update_datetime desc limit 1" )
    public List<TaskPaotui>getCurrentTaskbyPickupOpenIdRegion(String openid, String region);
	
	@Select("select * from  task_paotui  where region=#{region} and campusGroup=#{campusGroup} and description like CONCAT('%',#{search},'%') and is_delete=0 and is_complaint=0 and status !=5 order by update_datetime desc limit #{length},10")
    public List<TaskPaotui>gettaskbySearch(String search, int length, String region, String campusGroup);
	
	@Select("select * from  task_paotui  where region=#{region} and description like CONCAT('%',#{search},'%') and is_delete=0 and is_complaint=0 and status !=5 order by update_datetime desc limit #{length},10")
    public List<TaskPaotui>gettaskbySearchRegion(String search, int length, String region);
	
	@Select("select * from  task_paotui   where region=#{region} and campusGroup=#{campusGroup} and category= #{category} and is_delete=0 and is_complaint=0 and status!=0 and status!=5 order by update_datetime desc limit #{length},10")
	public  List<TaskPaotui> gettaskbyCategory(String category, int length, String region, String campusGroup);
	
	@Select("select * from  task_paotui   where region=#{region} and category= #{category} and is_delete=0 and is_complaint=0 and status!=0 and status!=5 order by update_datetime desc limit #{length},10")
	public  List<TaskPaotui> gettaskbyCategoryRegion(String category, int length, String region);
	
	@Select("select * from  task_paotui   where region=#{region} and campusGroup=#{campusGroup} and  status=#{status} and is_delete=0 and is_complaint=0 order by update_datetime desc limit #{length},10")
	public List<TaskPaotui> gettaskbyStatus(String status, int length, String region, String campusGroup);
	
	@Select("select * from  task_paotui   where region=#{region} and  status=#{status} and is_delete=0 and is_complaint=0 order by update_datetime desc limit #{length},10")
	public List<TaskPaotui> gettaskbyStatusRegion(String status, int length, String region);
	
	@Select("select * from  task_paotui   where  region=#{region} and campusGroup=#{campusGroup} and category= #{category} and status= #{status} and is_delete=0 and is_complaint=0 order by update_datetime desc limit #{length},10")
	public  List<TaskPaotui> gettaskbyCategoryStatus(String category,String status, int length, String region, String campusGroup);
	
	@Select("select * from  task_paotui   where  region=#{region} and category= #{category} and status= #{status} and is_delete=0 and is_complaint=0 order by update_datetime desc limit #{length},10")
	public  List<TaskPaotui> gettaskbyCategoryStatusRegion(String category,String status, int length, String region);
	
	@Select("select * from  task_paotui   where region=#{region} and campusGroup=#{campusGroup} and  category= #{category} and release_openid= #{openid} and is_delete=0 and is_complaint=0 order by update_datetime desc limit #{length},10")
	public  List<TaskPaotui> gettaskbyReleaseOpenIdCategory(String category,String openid, int length, String region, String campusGroup);
	
	@Select("select * from  task_paotui   where region=#{region} and  category= #{category} and release_openid= #{openid} and is_delete=0 and is_complaint=0 order by update_datetime desc limit #{length},10")
	public  List<TaskPaotui> gettaskbyReleaseOpenIdCategoryRegion(String category,String openid, int length, String region);
	
	@Select("select * from  task_paotui   where region=#{region} and campusGroup=#{campusGroup} and  category= #{category} and pickup_openid= #{openid} and is_delete=0 and is_complaint=0 order by update_datetime desc limit #{length},10")
	public  List<TaskPaotui> gettaskbyPickupOpenIdCategory(String category,String openid, int length, String region, String campusGroup);
	
	@Select("select * from  task_paotui   where region=#{region} and  category= #{category} and pickup_openid= #{openid} and is_delete=0 and is_complaint=0 order by update_datetime desc limit #{length},10")
	public  List<TaskPaotui> gettaskbyPickupOpenIdCategoryRegion(String category,String openid, int length, String region);
	
	@Update("update task_paotui set address_from=#{address_from}, address_to=#{address_to}, due_date=#{due_date}, due_time=#{due_time}, description=#{description}, content=#{content}, img_url=#{img_url}, update_datetime=NOW() where  order_id= #{order_id}")
    public  int  modifyTask(@Param("order_id")String order_id,
    		@Param("address_from")String address_from,@Param("address_to")String address_to, 
    		@Param("due_date")String due_date, @Param("due_time")String due_time,
    		@Param("description")String description, @Param("content")String content, @Param("img_url")String img_url);
	
	@Update("update  task_paotui  set is_delete=1, status=5 where  order_id= #{order_id}")
    int deleteTask(@Param("order_id")String order_id);
	
	@Update("update  task_paotui  set status=5 where  order_id= #{order_id}")
    int expireTask(@Param("order_id")String order_id);
	
	@Select("select * from rider where openid=#{openid} or phone=#{phone} or wechat=#{wechat} or ic_number=#{ic_number}")
	public List<Rider> checkRider(String openid, String phone, String wechat, String ic_number);
	
	@Insert("insert into rider(openid, realname, phone, wechat, school, campus, ic_number, student_card_pic"
			+ ", ic_pic_front, ic_pic_back, region, campusGroup, c_time) "
			+"values (#{openid}, #{realname}, #{phone}, #{wechat}, #{school}, #{campus}, #{ic_number}, "
			+ " #{student_card_pic}, #{ic_pic_front}, #{ic_pic_back}, #{region}, #{campusGroup}, NOW())")
	public int addRider(String openid, String realname, String phone, String wechat, String school, String campus,String ic_number,
			String student_card_pic, String ic_pic_front, String ic_pic_back, String region, String campusGroup);
	
	@Insert("insert into user(openid,phone,c_time) values (#{openid}, #{phone}, NOW())")
	public int insertUserOpenidPhone(String openid, String phone);
	
//	@Select("select * from user where openid=#{openid}")
//	public List<User> getUserByOpenid(String openid);
	
	@Select("select * from rider where openid=#{openid}")
	public List<Rider> getRiderByOpenid(String openid);
	
	@Update("update task_paotui set pickup_openid=#{openid}, pickup_datetime=NOW(), status=2, update_datetime=NOW() where order_id=#{order_id}")
	public int updateOrderPickup(@Param("openid")String openid, @Param("order_id")String order_id);
	
	@Update("update task_paotui set status=4, update_datetime=NOW() where order_id=#{order_id}")
	public int orderComplete(String order_id);
	
	@Update("update task_paotui set status=3, complete_datetime=NOW(), update_datetime=NOW() where order_id=#{order_id}")
	public int orderCompleteByRider(String order_id);
	
//	@Select("select * from user limit #{length}, 10")
//	public List<User> getAllUser(int lenght);
	
	@Select("select * from rider limit #{length}, 10")
	public List<Rider> getAllRider(int lenght);
	
	@Update("update rider set is_verified=1 where openid=#{openid}")
	public int verifyRider(@Param("openid")String openid);
	
//	@Select("select * from accesscode where c_time >= #{c_time} and id=1")
//	public List<AccessCode> getCodeCtime(Long c_time);
	
	@Select("select * from accesscode where c_time >= #{c_time} and id=1")
	public List<AccessCode> getCodeCtime(Long c_time);
	
	@Select("select * from accesscode_quanzi where c_time >= #{c_time} and id=#{id}")
	public List<AccessCode> getCodeCtimeById(Long c_time, String id);
	
	@Update("update accesscode set accessCode =#{code},c_time=#{c_time} where id=1")
    public  int saveCode(String code,Long c_time);
	
	@Update("update accesscode set accessCode =#{code},c_time=#{c_time} where id=#{id}")
    public  int saveCodeById(String code,Long c_time, String id);
	
	@Select("select * from banner order by weight desc")
    public List<Banner>getBanner();
	
	@Update("update task_paotui set status=-2,update_datetime=NOW() where order_id=#{order_id} and status=0")
	public int updateTaskPendingPayStatus(String order_id);
	
	@Update("update task_paotui set status=1,update_datetime=NOW() where order_id=#{order_id}")
	public int updateTaskPayStatus(String openid, String order_id);
	
	@Update("update rider set income=income+#{price}, pickup_order_num=pickup_order_num+1 where openid=#{openid}")
	public int riderAddIncome(String openid, double price);
	
	@Update("update user set release_order_num=release_order_num+1 where openid=#{openid}")
	public int updateUserOrderNum(String openid);
	
	@Select("select u.openid, u.phone as phone_user,u.is_blacklist as is_blacklist_user, u.c_time as c_time_user,"
			+ "u.release_order_num, u.remark as remark_user, r.realname, r.phone as rider, r.wechat, r.school,"
			+ "r.campus, r.ic_number, r.student_card_pic, r.ic_pic_front, r.ic_pic_back, r.is_verified as is_verified_rider,"
			+ "r.is_blacklist as is_blacklist_rider, r.remark as remark_rider, r.income, r.pickup_order_num"
			+ " from user u left join rider r on u.openid=r.openid where u.openid=#{openid}")
	public List<Profile> getProfileByOpenid(String openid);
	
	@Update("update rider set busy=#{status} where openid=#{openid}")
	public int updateRiderBusyStatus(String openid, Integer status);
	
	@Select("select * from rider where is_verified=1 and is_blacklist=0")
	public List<Rider> getAllVerifiedRider();
	
	@Select("select * from template where region=#{region} and campusGroup=#{campusGroup} and name=#{name}")
	public List<WXTemplate> getWXTemplate(String region, String campusGroup, String name);
	
	@Select("select * from template where region=#{region} and name=#{name}")
	public List<WXTemplate> getWXTemplateByRegion(String region, String name);
	
	@Select("select * from  blacklist where openid = #{openid}")
    public List<BlackList>checkBlackList(String openid);
	
	@Select("select * from  blacklist where campus=#{campus} order by id desc limit #{length},10")
    public List<BlackList>getBlackList(int length, String campus);
	
	@Select("select * from secret where id=#{id}")
	public List<Secret> getSecretById(String id);
	
	@Update("update task_paotui set trade_number=#{trade_number} where order_id=#{order_id}")
	public int updateTradeNoByOrderId(String order_id, String trade_number);
	
}
