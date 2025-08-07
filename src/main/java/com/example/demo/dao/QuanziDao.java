package com.example.demo.dao;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.model.VerifyUser;
import com.example.demo.model.VerifyUserIdentity;
import com.example.demo.model.WXTemplate;
import com.example.demo.model.Like;
import com.example.demo.model.Member;
import com.example.demo.model.PMChat;
import com.example.demo.model.PMChatDetail;
import com.example.demo.model.PMFriendList;
import com.example.demo.model.PMFriendMsgList;
import com.example.demo.model.PMStatus;
import com.example.demo.model.QR;
import com.example.demo.model.RadioGroupCategory;
import com.example.demo.model.Rider;
import com.example.demo.model.Secret;
import com.example.demo.model.Suggestion;
import com.example.demo.model.Switch;
import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.BitRank;
import com.example.demo.model.BlackList;
import com.example.demo.model.Campus;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentAll;

import org.apache.ibatis.annotations.*;
import java.util.List;
@Mapper
public interface QuanziDao {
	
	@Select("select * from task_quanzi  where is_delete=0 order by c_time desc limit #{length},10 ")
	public List<Task>getallTask(int length);
	
	@Select("select * from task_quanzi  where region=#{region} and is_delete=0 "
			+ " and DATEDIFF(Now(),replace(substring(c_time,1,10),'/','-'))<=3 "
			+ " order by (watchNum+commentNum*10+likeNum*10) desc limit #{length} ")
	public List<Task>getHotTaskByRegion(String region, int length);
	
	@Select("select * from task_quanzi  where region=#{region} and campusGroup=#{campus} and is_delete=0 "
			+ " and DATEDIFF(Now(),replace(substring(c_time,1,10),'/','-'))<=3 "
			+ " order by (watchNum+commentNum*10+likeNum*10) desc limit #{length} ")
	public List<Task>getHotTaskByRegionCampus(String region, String campus, int length);
	
	@Select("select * from  task_quanzi   where  openid= #{openid} and is_delete=0 order by c_time desc limit #{length},10" )
    public List<Task>gettaskbyOpenId(String openid,int length);
	
	@Select("select * from  task_quanzi   where  id= #{ID} and is_delete=0 ")
    public List<Task>gettaskbyId(int ID);
	
	// add campus=0
	@Select("select * from  task_quanzi   where  (title like CONCAT('%',#{search},'%') or content like CONCAT('%',#{search},'%')) and is_delete=0 and campusGroup in (#{campus},'0') order by c_time desc limit #{length},10")
    public List<Task>gettaskbySearch(String search,int length,String campus);
	
	@Select("select * from  task_quanzi   where  (title like CONCAT('%',#{search},'%') or content like CONCAT('%',#{search},'%')) and is_delete=0 and region = #{region} order by c_time desc limit #{length},10")
    public List<Task>gettaskbySearchRegion(String search,int length,String region);
	
	@Select("select * from  task_quanzi   where  radioGroup= #{radioGroup} and is_delete=0 order by c_time desc limit #{length},10")
    public List<Task>gettaskbyRadio(String radioGroup,int length);
	
	@Select({"<script>",
			"select * from task_quanzi  where radioGroup in ",
			"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
			"#{item}",
			"</foreach>",
			"and is_delete=0 and campusGroup in (#{campus}) and region=#{region} order by c_time desc limit #{length},10",
			"</script>"})
    public List<Task>gettaskbyRadioSecond(@Param("radioGroup") List<String> radioGroup,int length,String campus,String region);
	
	@Select({"<script>",
		"select * from task_quanzi  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and region=#{region} order by c_time desc limit #{length},10",
		"</script>"})
	public List<Task>gettaskbyRadioSecondCampus(@Param("radioGroup") List<String> radioGroup,int length,String campus,String region);
	
	@Select({"<script>",
		"select * from task_quanzi  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and campusGroup in (#{campus}) and region=#{region} order by c_time desc limit #{length},10",
		"</script>"})
    public List<Task>gettaskbyCtime(@Param("radioGroup") List<String> radioGroup,int length,String campus,String region);
	
	@Select({"<script>",
		"select * from task_quanzi  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and region=#{region} order by c_time desc limit #{length},10",
		"</script>"})
    public List<Task>gettaskbyCtimeCampus(@Param("radioGroup") List<String> radioGroup,int length,String campus,String region);
	
	@Select({"<script>",
		"select * from task_quanzi  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and campusGroup in (#{campus}) and region=#{region} order by comment_time desc limit #{length},10",
		"</script>"})
    public List<Task>gettaskbyComment(@Param("radioGroup") List<String> radioGroup,int length,String campus,String region);
	
	@Select({"<script>",
		"select * from task_quanzi  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and region=#{region} order by comment_time desc limit #{length},10",
		"</script>"})
    public List<Task>gettaskbyCommentCampus(@Param("radioGroup") List<String> radioGroup,int length,String campus,String region);
	
	@Select({"<script>",
		"select * from task_quanzi  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and campusGroup in (#{campus}) and region=#{region} and choose = 1 order by c_time desc limit #{length},10",
		"</script>"})
    public List<Task>gettaskbyChoose(@Param("radioGroup") List<String> radioGroup,int length,String campus,String region);
	
	@Select({"<script>",
		"select * from task_quanzi  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and region=#{region} and choose = 1 order by c_time desc limit #{length},10",
		"</script>"})
    public List<Task>gettaskbyChooseCampus(@Param("radioGroup") List<String> radioGroup,int length,String campus,String region);
	
	@Select({"<script>",
		"select * from task_quanzi  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and campusGroup in (#{campus}) and region=#{region} order by (watchNum+commentNum*10+likeNum*10)/(DATEDIFF(Now(),replace(substring(c_time,1,10),'/','-'))/7+1) desc limit #{length},10",
		"</script>"})
    public List<Task>gettaskbyHot(@Param("radioGroup") List<String> radioGroup,int length,String campus,String region);
	
	@Select({"<script>",
		"select * from task_quanzi  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and region=#{region} order by (watchNum+commentNum*10+likeNum*10)/(DATEDIFF(Now(),replace(substring(c_time,1,10),'/','-'))/7+1) desc limit #{length},10",
		"</script>"})
    public List<Task>gettaskbyHotCampus(@Param("radioGroup") List<String> radioGroup,int length,String campus,String region);
	
	@Select({"<script>",
		"select * from task_quanzi  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and campusGroup=#{campus} and c_time &lt; #{end} and c_time &gt; #{start}  and title like CONCAT('%',#{keyword},'%') order by c_time desc limit #{length},50",
		"</script>"})
    public List<Task>getAllTaskForWX(String keyword,int length,String campus,String start,String end,@Param("radioGroup") List<String> radioGroup);
	
	@Select({"<script>",
		"select * from task_quanzi  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and region=#{campus} and c_time &lt; #{end} and c_time &gt; #{start}  and title like CONCAT('%',#{keyword},'%') order by c_time desc limit #{length},50",
		"</script>"})
    public List<Task>getAllTaskForWXByRegion(String keyword,int length,String campus,String start,String end,@Param("radioGroup") List<String> radioGroup);
	
	@Select({"<script>",
		"select * from task  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and c_time &lt; #{end} and c_time &gt; #{start} and title like CONCAT('%',#{keyword},'%') order by c_time desc limit #{length},50",
		"</script>"})
    public List<Task>getAllTaskForWXBeita(String keyword,int length,String campus,String start,String end,@Param("radioGroup") List<String> radioGroup);
	
	@Select({"<script>",
		"select * from task_caicai  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and c_time &lt; #{end} and c_time &gt; #{start} and title like CONCAT('%',#{keyword},'%') order by c_time desc limit #{length},50",
		"</script>"})
    public List<Task>getAllTaskForWXSg(String keyword,int length,String campus,String start,String end,@Param("radioGroup") List<String> radioGroup);
	
	@Select({"<script>",
		"select * from task_quanzi  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and campusGroup=#{campus} and c_time &lt; #{end} and c_time &gt; #{start} and title like CONCAT('%',#{keyword},'%') order by (watchNum+commentNum*10+likeNum*10)/(DATEDIFF(Now(),replace(substring(c_time,1,10),'/','-'))/7+1) desc limit #{length},50",
		"</script>"})
    public List<Task>getAllTaskForWXByHot(String keyword,int length,String campus,String start,String end,@Param("radioGroup") List<String> radioGroup);
	
	@Select({"<script>",
		"select * from task_quanzi  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and region=#{campus} and c_time &lt; #{end} and c_time &gt; #{start} and title like CONCAT('%',#{keyword},'%') order by (watchNum+commentNum*10+likeNum*10)/(DATEDIFF(Now(),replace(substring(c_time,1,10),'/','-'))/7+1) desc limit #{length},50",
		"</script>"})
    public List<Task>getAllTaskForWXByHotByRegion(String keyword,int length,String campus,String start,String end,@Param("radioGroup") List<String> radioGroup);
	
	@Select({"<script>",
		"select * from task  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and c_time &lt; #{end} and c_time &gt; #{start} and title like CONCAT('%',#{keyword},'%') order by (watchNum+commentNum*10+likeNum*10)/(DATEDIFF(Now(),replace(substring(c_time,1,10),'/','-'))/7+1) desc limit #{length},50",
		"</script>"})
    public List<Task>getAllTaskForWXByHotBeita(String keyword,int length,String campus,String start,String end,@Param("radioGroup") List<String> radioGroup);
	
	@Select({"<script>",
		"select * from task_caicai  where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and c_time &lt; #{end} and c_time &gt; #{start} and title like CONCAT('%',#{keyword},'%') order by (watchNum+commentNum*10+likeNum*10)/(DATEDIFF(Now(),replace(substring(c_time,1,10),'/','-'))/7+1) desc limit #{length},50",
		"</script>"})
    public List<Task>getAllTaskForWXByHotSg(String keyword,int length,String campus,String start,String end,@Param("radioGroup") List<String> radioGroup);
	
	@Select({"<script>",
		"select * from task_quanzi where campusGroup=#{campus} order by c_time desc limit #{length},50",
		"</script>"})
    public List<Task>taskManagement(int length,String campus);
	
	@Select({"<script>",
		"select * from task_quanzi where region=#{campus} order by c_time desc limit #{length},50",
		"</script>"})
    public List<Task>taskManagementByRegion(int length,String campus);
	
	@Select({"<script>",
		"select * from task_caicai order by c_time desc limit #{length},50",
		"</script>"})
    public List<Task>taskManagementSg(int length);
	
	@Select({"<script>",
		"select * from task order by c_time desc limit #{length},50",
		"</script>"})
    public List<Task>taskManagementBeita(int length);
	
	
	@Insert("insert into task_quanzi  (content,price,title,wechat,openid,avatar,campusGroup,"
			+ "commentNum,watchNum,likeNum,radioGroup,img,region,userName,c_time,comment_time,choose,cover,is_delete,ip) "
			+ "values (#{content},#{price},#{title},#{wechat},#{openid},#{avatar},#{campusGroup},"
			+ "#{commentNum},#{watchNum},#{likeNum},#{radioGroup},#{img},#{region},#{userName},#{c_time},#{c_time},0,#{cover},#{is_delete},#{ip})")
    int addTask(String content,String price,String title,String wechat,String openid,String avatar,
    		String campusGroup,int commentNum,int watchNum,int likeNum,String radioGroup,
    		String img,String region,String userName,String c_time,String cover,int is_delete,String ip);
	
	@Update("update  task_quanzi  set c_time =#{c_time},comment_time=#{c_time} where  id= #{Id}")
    public  int  upDateTask(@Param("c_time") String c_time,
                                @Param("Id") int Id);

	@Update("update  task_quanzi  set watchNum = watchNum+1  where  id= #{Id}")
    public  int  incWatch(@Param("Id") int Id);
	
	@Update("update  task_quanzi  set likeNum = likeNum+1  where  id= #{Id}")
    public  int  incLike(@Param("Id") int Id);
	
	@Update("update  comment_quanzi set like_num = like_num+1  where  id= #{Id}")
    public  int  incCommentLike(@Param("Id") int Id);
	
	@Update("update  comment_quanzi set like_num = like_num-1  where  id= #{Id}")
    public  int  decCommentLike(@Param("Id") int Id);
	
	@Update("update  task_quanzi  set commentNum = commentNum+1  where  id= #{Id}")
    public  int  incComment(@Param("Id") int Id);
	
	@Update("update  task_quanzi  set likeNum = likeNum-1  where  id= #{Id}")
    public  int  decLike(@Param("Id") int Id);
	
	@Update("update  task_quanzi  set comment_time=#{c_time} where  id= #{pk}")
    int  upDateTaskCommentTime(Comment comment);
	
	@Update("update  task_quanzi  set is_delete=1 where  id= #{Id}")
    int  deleteTask(@Param("Id")int Id);
	
	@Update("update  task_quanzi  set is_delete=0 where  id= #{Id}")
    int  recoverTask(@Param("Id")int Id);
	
	@Update("update  task_quanzi  set is_complaint=1 where  id= #{Id}")
    int  hideTask(@Param("Id")int Id);
	
	@Update("update  task_quanzi  set title=content ,is_complaint=0 where  id= #{Id}")
    int  recoverTaskHide(@Param("Id")int Id);
	
	@Update("update  task_quanzi  set c_time = REPLACE(DATE_ADD(c_time, INTERVAL 100 year), '-', '/') where  id= #{Id}")
    int  topTask(@Param("Id")int Id);
	
	@Update("update  task_quanzi  set c_time = REPLACE(DATE_SUB(c_time, INTERVAL 100 year), '-', '/') where  id= #{Id}")
    int  downTask(@Param("Id")int Id);
	
	@Insert("insert into like_quanzi (pk,openid) values (#{pk},#{openid})")
    int addLike(Like like);
	
	@Select("select * from  like_quanzi  where openid= #{openid} and pk=#{pk}")
    public List<Like>getlikeByPk(String openid,int pk);
	
	@Select("select * from  like_quanzi  where openid= #{openid} order by id desc limit #{length} ,10")
    public List<Like>getlikeByOpenid(String openid, int length);
	
	@Delete("delete from like_quanzi WHERE id = #{Id}")
    int  deleteLike(@Param("Id")int Id);
	
	@Insert("insert into comment_quanzi (pk,openid,applyTo,avatar,comment,userName,c_time,img,level,pid) values (#{pk},#{openid},#{applyTo},#{avatar},#{comment},#{userName},#{c_time},#{img},#{level},#{pid})")
    int addComment(Comment comment);
	
	@Select("select * from  comment_quanzi  where pk= #{pk} and level = 1 and is_delete = 0 limit #{length},5")
    public List<Comment>getCommentByLength(int pk, int length);
	
//	@Select("select * from (select * from comment_quanzi where pk= #{pk} and level = 1 and is_delete = 0 limit #{length},5) c1 LEFT JOIN (select * from  comment_quanzi where level = 2 and is_delete = 0) c2 on c2.pid= c1.id ")
//    public List<CommentAll>getCommentByLength(int pk, int length);
	
	@Select("select * from  comment_quanzi  where pk= #{pk} and level = 1 and is_delete = 0")
    public List<Comment>getCommentByPk(int pk);
	
	@Select("select * from  comment_quanzi  where pk= #{pk} and level = 1 and is_delete = 0 order by c_time desc limit #{length},5")
    public List<Comment>getCommentByReverse(int pk, int length);
	
//	@Select("select * from (select * from comment_quanzi where pk= #{pk} and level = 1 and is_delete = 0 order by c_time desc limit #{length},5) c1 LEFT JOIN (select * from  comment_quanzi where level = 2 and is_delete = 0) c2 on c2.pid= c1.id")
//    public List<CommentAll>getCommentByReverse(int pk, int length);
	
	@Select("select * from  comment_quanzi  where pk= #{pk} and level = 1 and is_delete = 0 order by like_num desc limit #{length},5")
    public List<Comment>getCommentByHot(int pk, int length);
	
//	@Select("select * from (select * from comment_quanzi where pk= #{pk} and level = 1 and is_delete = 0 order by like_num desc limit #{length},5) c1 LEFT JOIN (select * from  comment_quanzi where level = 2 and is_delete = 0) c2 on c2.pid= c1.id")
//    public List<CommentAll>getCommentByHot(int pk, int length);
	
	@Select("select * from  comment_quanzi  where pid= #{pid} and level = 2 and is_delete = 0")
    public List<Comment>getCommentByPid(int pid);
	
	@Select("select * from  comment_quanzi  where pid= #{pid} and level = 2 and is_delete = 0 limit 1")
    public List<Comment>getFirstCommentByPid(int pid);
	
	@Select({"<script>",
		"select * from  comment_quanzi  where pid in ",
		"<foreach collection='str' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and level = 2 and is_delete = 0",
		"</script>"
	})
    public List<Comment>getCommentByIdList(@Param("str") List<String> str);
	
	@Select("select * from  comment_quanzi  where openid= #{openid} and is_delete = 0 order by c_time desc limit #{length},10")
    public List<Comment>getCommentByOpenid(String openid,int length);
	@Select("select * from  comment_quanzi  where applyTo= #{applyTo} and is_delete = 0 order by c_time desc limit #{length},10")
    public List<Comment>getCommentByApplyto(String applyTo,int length);
	
	@Update("update comment_quanzi set is_delete=1 where  id= #{Id}")
    int  deleteComment(@Param("Id")int Id);
	
	@Select("select id,openid,au1,au2,au3,au4,au5,avatar,name from  member_quanzi  where openid= #{openid} and campus = #{campus}")
    public List<Member>getMember(String openid,String campus);
	
	@Select("select * from  member_quanzi where campus=#{campus}")
    public List<Member>getAllMember(String campus);
	
	@Select("select * from  member_quanzi")
    public List<Member>getAllMemberWX();
	
	@Delete("delete from `member_quanzi` WHERE id = #{Id}")
    int  deleteMember(@Param("Id")int Id);
	
	@Insert("insert into member_quanzi (openid,name,campus) values (#{openid},#{name},#{campus})")
    int addMember(String name,String openid,String campus);
	
	@Update("update  task_quanzi  set watchNum =#{watchNum}  where  id= #{Id}")
    public  int  upDateWatch(@Param("watchNum") int watchNum,
                                @Param("Id") int Id);
	
	@Update("update  task_quanzi  set choose =#{choose}  where  id= #{Id}")
    public  int  updateChoose(int Id,int choose, String campus);
	
	@Select("select * from  comment_quanzi order by c_time desc limit #{length},10")
    public List<Comment>getAllComment(int length);
	
	@Select("select id,imageUrl,navUrl,weight from  banner_quanzi where campus = #{campus} and weight>0 order by weight desc")
    public List<Banner>getBanner(String campus);
	
	@Select("select id,imageUrl,navUrl,weight from  banner_quanzi where region=#{region} and campus = #{campus} and weight>0 order by weight desc")
    public List<Banner>getBannerXY(String region, String campus);
	
	@Delete("delete from `banner_quanzi` WHERE id = #{Id}")
    int  deleteBanner(@Param("Id")int Id);
	
	@Insert("insert into banner_quanzi (imageUrl,navUrl,weight,campus,page) values (#{imgPath},#{url},#{weight},#{campus},#{page})")
    int addBanner(String page,String imgPath,String url,String campus,String weight);
	
	@Select("select * from  blacklist where openid = #{openid}")
    public List<BlackList>checkBlackList(String openid);
	
	@Select("select * from  blacklist where campus=#{campus} order by id desc limit #{length},10")
    public List<BlackList>getBlackList(int length, String campus);
	
	@Insert("insert into blacklist (openid, period, campus, start) values (#{openid}, #{period}, #{campus}, NOW())")
	int addBlacklistXiaoyuan(String openid, String period, String campus);
	
	@Select("select id,title,openid from  task_quanzi   where  title like CONCAT('%',#{search},'%') and campusGroup = #{campus} order by c_time desc")
    public List<Task>getOpenidbySearch(String search, String campus);
	
	@Select("select id,title,openid from  task_quanzi   where  title like CONCAT('%',#{search},'%') and region = #{campus} order by c_time desc")
    public List<Task>getOpenidbySearchByRegion(String search, String campus);
	
	@Select("select id,title,openid from  task_quanzi   where  openid = #{search} and campusGroup = #{campus} order by c_time desc")
    public List<Task>getAllOpenid(String search, String campus);
	
	
	@Select("select id,`comment`,openid from  `comment_quanzi`  where  `comment` like CONCAT('%',#{search},'%') and campusGroup = #{campus} order by c_time desc limit 0,20")
    public List<Comment>getOpenidBySearchComment(String search, String campus);
	
	@Select("select id,`comment`,openid from  `comment_quanzi`  where  `comment` like CONCAT('%',#{search},'%') and campusGroup = #{campus} order by c_time desc limit 0,20")
    public List<Comment>getOpenidBySearchCommentByRegion(String search, String campus);
	
	@Select("select id,`comment`,openid from  `comment_quanzi`  where  openid = #{search} order by c_time desc limit 0,20")
    public List<Comment>getOpenidBySearchAllComment(String search, String campus);
	
	@Insert("insert into `blacklist` (openid,period,description,start,campus) values (#{openid},#{period},#{description},NOW(),#{campus})")
    int addBlacklist(String openid,String period,String description,String campus);
	
	@Delete("delete from `blacklist` WHERE id = #{Id}")
    int  deleteBlacklist(@Param("Id")int Id);
	
	@Insert("insert into `suggestion` (task_id,content,openid,c_time) values (#{task_id},#{content},#{openid},NOW())")
    int addSuggestion(Suggestion suggestion);
	
	@Select("select distinct openid from  suggestion where task_id = #{id} limit 0,5")
    public List<Suggestion>getSuggestionByPk(int id);
	
	@Select("select * from  suggestion")
    public List<Suggestion>getSuggestion();
	
	@Select("select * from bitrank order by score desc limit #{length},10 ")
	public List<BitRank>getRankList(int length);
	
	@Select("select score from bitrank where openid = #{openid} ")
	public List<BitRank>getRankListByOpenid(String openid);
	
	@Insert("insert into `bitrank` (avatar,nickName,openid,score,c_time) values (#{avatar},#{nickName},#{openid},#{score},NOW())")
    int addRank(BitRank bitrank);
	
	@Update("update bitrank set score =#{score},avatar=#{avatar},nickName =#{nickName}  where openid= #{openid}")
    public  int  updateRank(BitRank bitrank);

	@Select("select * from  task_quanzi  where title like CONCAT('%',#{search},'%') and campusGroup = #{campus} order by c_time desc")
    public List<Task>getChooseBySearch(String search,String campus);
	
	@Select("select * from  task_quanzi  where title like CONCAT('%',#{search},'%') and region = #{campus} order by c_time desc")
    public List<Task>getChooseBySearchByRegion(String search,String campus);
	
	@Select("select * from accesscode_quanzi where c_time >= #{c_time} and id=#{campus}")
	public List<AccessCode> getCodeCtime(Long c_time,String campus);
	
	@Update("update accesscode_quanzi set accessCode =#{code},c_time=#{c_time} where id=#{campus}")
    public  int saveCode(String code,Long c_time,String campus);
	
	@Select("select * from secret where appid=#{appid}")
	public List<Secret> getSecret(String appid);
	
	@Select("select * from secret where id=#{campus}")
	public List<Secret> getSecretByCampus(String campus);
	
	@Select("select * from login where account=#{account} and password=#{password}")
	public List<User> getUser(String account,String password);
	
	@Select("select * from isverify where campus=#{campus}")
	public List<Switch> getSwitchStatus(String campus);
	
	@Insert("insert into `isverify` (campus,verify) values (#{campus},#{status})")
    int addSwitch(String campus, String status);
	
	@Update("update isverify set verify =#{status} where campus= #{campus}")
    public  int  updateSwitch(String campus, String status);
	
	@Select("select * from  qr where region=#{region} and campus = #{campus}")
    public List<QR>getQRList(String region, String campus);
	
	@Select("select * from  qr where campus = #{campus}")
    public List<QR>getQR(String campus);
	
	@Delete("delete from `qr` WHERE id = #{Id}")
    int  deleteQR(@Param("Id")int Id);
	
	@Insert("insert into qr (imageUrl,campus) values (#{imgPath},#{campus})")
    int addQR(String imgPath,String campus);
	
	@Select("select id, openid, pic, c_time, status, campus, email, region, phone, identity from user_verify where openid=#{openid}")
	public List<VerifyUserIdentity> getVerifyUserByOpenid(String openid);
	
	@Insert("insert into user_verify (openid, pic, status, c_time, email, campus) values (#{openid}, #{pic}, #{status}, NOW(), #{email}, #{campus})")
	public int addVerifyUser(VerifyUser verify_user);
	
	@Update("update user_verify set pic=#{pic},status=#{status},c_time=NOW(),email=#{email},region=#{region},campus=#{campus} where openid=#{openid}")
	public int updateVerifyUserVerifyInfo(VerifyUser verify_user);
	
	@Select("select id, openid, pic, c_time, status, campus, email, region, phone from user_verify where campus=#{campus} order by c_time limit #{length}, 50")
	public List<VerifyUser> getVerifyUserbyCampusLength(String campus, int length);
	
	@Select("select id, openid, pic, c_time, status, campus, email, region, phone from user_verify where campus=#{campus} and status=#{status} order by c_time limit #{length}, 50")
	public List<VerifyUser> getVerifyUserbyCampusLengthStatus(String campus, int length, int status);
	
	@Select("select id, openid, pic, c_time, status, campus, email, region, phone from user_verify where region=#{region} and campus=#{campus} and status=#{status} order by c_time limit #{length}, 50")
	public List<VerifyUser> getVerifyUserbyRegionCampusLengthStatus(String region, String campus, int length, int status);
	
	@Select("select id, openid, pic, c_time, status, campus, email, region, phone from user_verify where campus=#{campus} order by c_time desc")
	public List<VerifyUser> getVerifyUserbyCampus(String campus);
	
	@Select("select id, openid, pic, c_time, status, campus, email, region, phone from user_verify where email=#{email} order by c_time desc")
	public List<VerifyUser> getVerifyUserbyEmail(String email);
	
	@Select("select id, openid, pic, c_time, status, campus, email, region, phone from user_verify order by c_time desc")
	public List<VerifyUser> getVerifyUserAll();
	
	@Update("update user_verify set nickname=#{nickname}, avatar=#{avatar} where openid=#{openid}")
	public int udpateUserInfoByOpenid(String openid, String nickname, String avatar);
	
	@Insert("insert into user_verify (openid, nickname, avatar, status) values (#{openid}, #{nickname}, #{avatar}, #{status})")
	public int addUserInfoByOpenid(String openid, String nickname, String avatar, int status);
	
	@Update("update user_verify set phone=#{phone} where openid=#{openid}")
	public int udpateUserPhoneByOpenid(String openid, String phone);
	
	@Insert("insert into user_verify (openid, phone, status) values (#{openid}, #{phone}, #{status})")
	public int addUserPhoneByOpenid(String openid, String phone, int status);
	
	@Update("update user_verify set campus=#{campus} , region=#{region} where openid=#{openid}")
	public int updateCampusRegionByOpenid(String openid, String campus, String region);
	
	@Insert("insert into user_verify(openid, campus, region) values (#{openid}, #{campus}, #{region})")
	public int insertCampusRegionByOpenid(String openid, String campus, String region);
	
	@Update("update user_verify set status=#{status} where id=#{id}")
	public int updateVerifyUserStatusById(int id, int status);
	
	@Delete("delete from user_verify where id=#{id}")
	public int deleteVerifyUserById(int id);
	
	@Select("select * from PMChat where (openid=#{openid} or targetOpenid=#{openid} or openid=#{targetOpenid} or targetOpenid=#{targetOpenid}) and is_delete=0 and region=#{region}")
	public List<PMChat> getChatfromBothOpenid(String openid, String targetOpenid, String region, String campusGroup);
	
	@Select("select * from PMChat where (openid=#{openid} or targetOpenid=#{openid}) and is_delete=0 and region=#{region} and campusGroup=#{campusGroup}")
	public List<PMChat> getChatfromOneOpenid(String openid, String region, String campusGroup);
	
	@Insert("insert into PMChat(openid, targetOpenid, c_time, content, region, campusGroup) values (#{openid}, #{targetOpenid}, NOW(), #{content}, #{region}, #{campusGroup})")
	public int addChat(String openid, String targetOpenid, String content, String region, String campusGroup);
	
//	@Update("update PMStatus latest_content=#{content} and update_time=NOW() where ((openid1=#{openid} and openid2=#{targetOpenid} ) or (openid1=#{targetOpenid} and openid2=#{openid})) and region=#{region} and campusGroup=#{campusGroup}")
//	public int updateLatestContent(String openid, String targetOpenid, String c_time, String content, String region, String campusGroup);
	
	
	@Select("select * from PMStatus where (( sender=#{sender} and receiver=#{receiver} ) or (sender=#{receiver} and receiver=#{sender})) and region=#{region}")
	public List<PMStatus> getPMStatusByBothOpenid(String sender, String receiver, String region, String campusGroup);
	
	@Insert("insert into PMStatus (sender,receiver,c_time,status, region, campusGroup, update_time) values (#{sender},#{receiver},NOW(),0, #{region}, #{campusGroup}, NOW())")
	public int addPMStatusByBothOpenid(String sender, String receiver, String region, String campusGroup);
	
	@Update("update PMStatus set sender=#{sender}, receiver=#{receiver}, status=#{status}, c_time=NOW(), update_time=NOW() where ((sender=#{sender} and receiver=#{receiver}) or (sender=#{receiver} and receiver=#{sender})) and region=#{region}")
	public int udpatePMStatusReceiverByBothOpenid(String sender, String receiver, String region, String campusGroup, int status);
	
	@Update("update PMStatus set `status`=#{status}, `update_time`=NOW() "
			+ " where ((`sender`=#{openid1} and `receiver`=#{openid2}) or (`sender`=#{openid2} and `receiver`=#{openid1})) "
			+ " and `region`=#{region}")
	public int udpatePMStatusByBothOpenid(String openid1, String openid2,  String region, String campusGroup, int status);
	
	@Update("update PMChat set is_delete=1 where ((openid=#{openid1} and targetOpenid=#{openid2}) or (openid=#{openid2} and targetOpenid=#{openid1})) and region=#{region}")
	public int deletePMChatDeleteByBothOpenid(String openid1, String openid2,  String region, String campusGroup);
	
	@Select("select * from PMStatus where ((openid1=#{openid} or openid2=#{openid}) and (status=1 or (status=0 and receiver=#{openid}))) and region=#{region} ")
	public List<PMStatus> getVisiblePMUserListByOpenid(String openid, String region, String campusGroup);
	
	@Select("select * from PMStatus where ((openid1=#{openid} or openid2=#{openid}) and (status=1 or (status=0 and receiver=#{openid}))) and region=#{region} ")
	public List<PMStatus> getLatestUserMsgListByOpenid(String openid, String region, String campusGroup);
	
	
	@Select("select * from PMStatus where ((openid1=#{openid} or openid2=#{openid}) and status=0 and receiver=#{openid}) and region=#{region} order by c_time desc")
	public List<PMStatus> getPendingPMUserList(String openid, String region, String campusGroup);
	
	@Select("select c.*, u.*, tu.* "
			+ " from ( select * from PMChat "
			+ " where (openid=#{roomId} or targetOpenid=#{roomId}) "
			+ " and is_delete=0 and region=#{region} and campusGroup=#{campusGroup}) c"
			+ " left join (select openid, avatar, nickname from user_verify) u on c.openid=u.openid"
			+ " left join (select openid as targetOpenid, avatar as targetAvatar, nickname as targetNickname from user_verify) tu on c.targetOpenid=tu.targetOpenid"
			+ " order by c_time")
	public List<PMChatDetail> getPMChatByOpenid(String roomId, String region, String campusGroup);

	@Select("select c.*, u.*, tu.* "
			+ " from ( select * from PMChat "
			+ " where ((openid=#{openid1} and targetOpenid=#{openid2}) or (openid=#{openid2} and targetOpenid=#{openid1})) "
			+ " and is_delete=0 and region=#{region}) c"
			+ " left join (select openid, avatar, nickname from user_verify) u on c.openid=u.openid"
			+ " left join (select openid as targetOpenid, avatar as targetAvatar, nickname as targetNickname from user_verify) tu on c.targetOpenid=tu.targetOpenid"
			+ " order by c_time")
	public List<PMChatDetail> getPMChatByBothOpenid(String openid1, String openid2, String region, String campusGroup);
	
	@Select("select c.sender as openid, c.receiver as targetOpenid, c.region, c.campusGroup, c.c_time, u.*, tu.* "
			+ " from ( select * from PMStatus "
			+ " where ((sender=#{openid1} and receiver=#{openid2}) or (sender=#{openid2} and receiver=#{openid1})) "
			+ " and region=#{region}) c"
			+ " left join (select openid, avatar, nickname from user_verify) u on c.sender=u.openid"
			+ " left join (select openid as targetOpenid, avatar as targetAvatar, nickname as targetNickname from user_verify) tu on c.receiver=tu.targetOpenid"
			+ " order by c_time")
	public List<PMChatDetail> getPMChatStatusByBothOpenid(String openid1, String openid2, String region, String campusGroup);
	
	@Update("update PMStatus set status=#{status}, update_time=NOW() where id=#{id}")
	public int udpatePMStatusTimeById(int id, int status);
	
	@Select("SELECT a.openid as openid, a.f_openid, a.`status`, a.status_time, a.region, a.campusGroup, "
			+ "a.me_last_read_time, a.friend_last_read_time, u.avatar as f_avatar, u.nickname as f_nickname"
			+ " from "
			+ "(select #{openid}"
			+ "	, case when sender=#{openid} then receiver else sender end as f_openid"
			+ "	, `status`, update_time as status_time, region, campusGroup"
			+ "	, case when sender=#{openid} then sender_last_read_time else receiver_last_read_time end as me_last_read_time"
			+ "	, case when sender=#{openid} then receiver_last_read_time else sender_last_read_time end as friend_last_read_time"
			+ " from PMStatus"
			+ " where ((sender=#{openid} and `status`=1) or (receiver=#{openid} and `status`!=-1))"
			+ " and region={region}) a "
			+ " left join user_verify u on a.f_openid=u.openid")
	public List<PMFriendList> getPMFriendListByOpenid(String openid, String region, String campusGroup);
	
	@Select("select f.*, c.maxContentTime, p.content, case when c.maxContentTime is null or c.maxContentTime<status_time then status_time else maxContentTime end as update_time"
			+ " , case when c.maxContentTime<me_last_read_time and status_time<me_last_read_time then 1 else 0 end as is_read"
			+ " from "
			+ "( SELECT a.openid, a.f_openid, a.`status`, a.status_time, a.region, a.campusGroup, "
			+ " a.me_last_read_time, a.friend_last_read_time, u.avatar as f_avatar, u.nickname as f_nickname"
			+ " from "
			+ "(select #{openid} as openid, case when sender=#{openid} then receiver else sender end as f_openid"
			+ "	, `status`, update_time as status_time, region, campusGroup"
			+ " , case when sender=#{openid} then sender_last_read_time else receiver_last_read_time end as me_last_read_time"
			+ "	, case when sender=#{openid} then receiver_last_read_time else sender_last_read_time end as friend_last_read_time"
			+ " from PMStatus"
			+ " where ((sender=#{openid} and `status`=1) or (receiver=#{openid} and `status`!=-1))"
			+ " and region=#{region}) a"
			+ " left join user_verify u on a.f_openid=u.openid) f"
			+ " left join ( select case when openid=#{openid} then targetOpenid else openid end as f_openid, max(c_time) as maxContentTime from PMChat "
			+ "	where (openid=#{openid} or targetOpenid=#{openid}) and region=#{region}  and is_delete=0"
			+ " group by case when openid=#{openid} then targetOpenid else openid end ) c on c.f_openid=f.f_openid"
			+ " left join (select * from PMChat where is_delete=0 and (openid=#{openid} or targetOpenid=#{openid})) p "
			+ " on c.maxContentTime=p.c_time and ((p.openid=#{openid} and p.targetOpenid=c.f_openid) or (p.targetOpenid=#{openid} and p.openid=c.f_openid))"
			+ " order by maxContentTime desc")
	public List<PMFriendMsgList> getPMFriendMsgListByOpenid(String openid, String region, String campusGroup);
	
	@Update("update PMStatus set sender_last_read_time=case when sender=#{openid} then NOW() else sender_last_read_time end,"
			+ " receiver_last_read_time=case when receiver=#{openid} then NOW() else receiver_last_read_time end"
			+ " where ((sender=#{openid} and receiver=#{targetOpenid}) or (sender=#{targetOpenid} and receiver=#{openid}))"
			+ " and region=#{region}")
	public int updateLastReadTime(String openid, String targetOpenid, String region, String campusGroup);
	
	@Select("select * from rider where region=#{region} and is_verified=#{status} order by c_time limit #{length}, 50")
	public List<Rider> getPaotuiRiderbyRegionLengthStatus(String region, int length, int status);
	
	@Select("select * from rider where campusGroup=#{campus} and is_verified=#{status} order by c_time limit #{length}, 50")
	public List<Rider> getPaotuiRiderbyCampusLengthStatus(String campus, int length, int status);
	
	@Select("select * from rider where region=#{region} and campusGroup=#{campus} and is_verified=#{status} order by c_time limit #{length}, 50")
	public List<Rider> getPaotuiRiderbyRegionCampusLengthStatus(String region, String campus, int length, int status);
	
	@Update("update rider set is_verified=#{status} where id=#{id}")
	public int updatePaotuiUserStatusById(int id, int status);
	
	@Delete("delete from rider where id=#{id}")
	public int deletePaotuiUserById(int id);
	
	@Select("select * from template where region=#{region} and campusGroup=#{campusGroup} and name=#{name}")
	public List<WXTemplate> getWXTemplate(String region, String campusGroup, String name);
	
	@Select("select * from template where region=#{region} and name=#{name}")
	public List<WXTemplate> getWXTemplateByRegion(String region, String name);
	
	@Select("select * from campus")
	public List<Campus> getAllCampusList();
	
	@Select("select * from radioGroupCategory")
	List<RadioGroupCategory> getAllCategoryList();
	
	@Update("update user_verify set identity=#{identity} where openid=#{openid}")
	public int updateUserIdentityByOpenid(String openid, String identity);
	
	@Select({"<script>",
		"select id,openid,pic,c_time,status,campus,email,region,phone,identity from user_verify where openid in ",
		"<foreach collection='openidList' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"</script>"})
	public List<VerifyUserIdentity> getUserVerifyByOpenidList(List<String> openidList);
}
