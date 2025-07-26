package com.example.demo.dao;
import com.example.demo.model.Task;
import com.example.demo.model.Like;
import com.example.demo.model.Member;
import com.example.demo.model.Suggestion;
import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.BitRank;
import com.example.demo.model.BlackList;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentAll;

import org.apache.ibatis.annotations.*;
import java.util.List;
@Mapper
public interface TaskDao {

	@Select("SELECT COUNT(*) FROM task")
	public int getTaskCount();

	// 加载全部帖子，无论是否被删除
	@Select("select * from task order by c_time asc limit #{start},#{limit} ")
	public List<Task>getallTaskbyBatch(int start, int limit);
	@Select("select * from task where is_delete=0 order by c_time desc limit #{length},20 ")
	public List<Task>getallTask(int length);
	@Select("select * from task where is_delete=0 "
			+ " and DATEDIFF(Now(),replace(substring(c_time,1,10),'/','-'))<2 "
			+ " order by (watchNum+commentNum*10+likeNum*10) desc limit #{length} ")
	public List<Task>getHotTask(int length);
	
	@Select("select * from  task  where  openid= #{openid} and is_delete=0 order by c_time desc limit #{length},20" )
    public List<Task>gettaskbyOpenId(String openid,int length);
	
	@Select("select * from  task  where  id= #{ID} and is_delete=0 ")
    public List<Task>gettaskbyId(int ID);
	
	@Select("select * from  task  where  (title like CONCAT('%',#{search},'%') or content like CONCAT('%',#{search},'%')) and is_delete=0 order by c_time desc limit #{length},20")
    public List<Task>gettaskbySearch(String search,int length);
	
	@Select("select * from  task  where  radioGroup= #{radioGroup} and is_delete=0 order by c_time desc limit #{length},20")
    public List<Task>gettaskbyRadio(String radioGroup,int length);
	
	@Select({"<script>",
			"select * from task where radioGroup in ",
			"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
			"#{item}",
			"</foreach>",
			"and is_delete=0 order by c_time desc limit #{length},30",
			"</script>"})
    public List<Task>gettaskbyRadioSecond(@Param("radioGroup") List<String> radioGroup,int length);
	
	@Select({"<script>",
		"select * from task where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and campusGroup in ('1','2') order by c_time desc limit #{length},20",
		"</script>"})
    public List<Task>gettaskbyLX(@Param("radioGroup") List<String> radioGroup,int length);
	
	@Select({"<script>",
		"select * from task where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and campusGroup in ('0','2') order by c_time desc limit #{length},30",
		"</script>"})
    public List<Task>gettaskbyZGC(@Param("radioGroup") List<String> radioGroup,int length);
	
	@Select({"<script>",
		"select * from task where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and campusGroup in ('3','2') order by c_time desc limit #{length},30",
		"</script>"})
    public List<Task>gettaskbyZH(@Param("radioGroup") List<String> radioGroup,int length);
	
	@Select({"<script>",
		"select * from task where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 order by c_time desc limit #{length},20",
		"</script>"})
    public List<Task>gettaskbyCtime(@Param("radioGroup") List<String> radioGroup,int length);
	
	@Select({"<script>",
		"select * from task where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 order by comment_time desc limit #{length},10",
		"</script>"})
    public List<Task>gettaskbyComment(@Param("radioGroup") List<String> radioGroup,int length);
	
	@Select({"<script>",
		"select * from task where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 and choose = 1 order by c_time desc limit #{length},20",
		"</script>"})
    public List<Task>gettaskbyChoose(@Param("radioGroup") List<String> radioGroup,int length);
	
	@Select({"<script>",
		"select * from task where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and is_delete=0 order by (watchNum+commentNum*10+likeNum*10)/(DATEDIFF(Now(),replace(substring(c_time,1,10),'/','-'))/3+1) desc limit #{length},20",
		"</script>"})
    public List<Task>gettaskbyHot(@Param("radioGroup") List<String> radioGroup,int length);
	
	@Select({"<script>",
		"select * from task where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and campusGroup in (#{campus},'2') and is_delete=0 order by c_time desc limit #{length},20",
		"</script>"})
    public List<Task>gettaskbyCtimeCampus(@Param("radioGroup") List<String> radioGroup,int length, String campus);
	
	@Select({"<script>",
		"select * from task where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and campusGroup in (#{campus},'2') and is_delete=0 order by comment_time desc limit #{length},10",
		"</script>"})
    public List<Task>gettaskbyCommentCampus(@Param("radioGroup") List<String> radioGroup,int length, String campus);
	
	@Select({"<script>",
		"select * from task where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and campusGroup in (#{campus},'2') and is_delete=0 and choose = 1 order by c_time desc limit #{length},20",
		"</script>"})
    public List<Task>gettaskbyChooseCampus(@Param("radioGroup") List<String> radioGroup,int length, String campus);
	
	@Select({"<script>",
		"select * from task where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and campusGroup in (#{campus},'2') and is_delete=0 order by (watchNum+commentNum*10+likeNum*10)/(DATEDIFF(Now(),replace(substring(c_time,1,10),'/','-'))/3+1) desc limit #{length},20",
		"</script>"})
    public List<Task>gettaskbyHotCampus(@Param("radioGroup") List<String> radioGroup,int length, String campus);
	
	@Select({"<script>",
		"select * from task where radioGroup in ",
		"<foreach collection='radioGroup' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and campusGroup in (#{campus},'2') and is_delete=0 order by c_time desc limit #{length},30",
		"</script>"})
public List<Task>gettaskbyRadioSecondCampus(@Param("radioGroup") List<String> radioGroup,int length, String campus);

	
	@Select("select * from  (select *,locate(radioGroup,#{radioGroup}) as test from task ) as source where test!=0 and is_delete=0 order by c_time desc limit #{length},50")
    public List<Task>gettaskbyRadioSecondForWX(String radioGroup,int length);
	
	
	@Insert("insert into task (content,price,title,wechat,openid,avatar,campusGroup,"
			+ "commentNum,watchNum,likeNum,radioGroup,img,region,userName,c_time,comment_time,choose,cover,ip,is_delete) "
			+ "values (#{content},#{price},#{title},#{wechat},#{openid},#{avatar},#{campusGroup},"
			+ "#{commentNum},#{watchNum},#{likeNum},#{radioGroup},#{img},#{region},#{userName},#{c_time},#{c_time},0,#{cover},#{ip},#{is_delete})")
    int addTask(Task task);
	
	@Update("update  task set c_time =#{c_time},comment_time=#{c_time} where  id= #{Id}")
    public  int  upDateTask(@Param("c_time") String c_time,
                                @Param("Id") int Id);

	@Update("update  task set watchNum = watchNum+1  where  id= #{Id}")
    public  int  incWatch(@Param("Id") int Id);
	
	@Update("update  task set likeNum = likeNum+1  where  id= #{Id}")
    public  int  incLike(@Param("Id") int Id);
	
	@Update("update  comment set like_num = like_num+1  where  id= #{Id}")
    public  int  incCommentLike(@Param("Id") int Id);
	
	@Update("update  comment set like_num = like_num-1  where  id= #{Id}")
    public  int  decCommentLike(@Param("Id") int Id);
	
	@Update("update  task set commentNum = commentNum+1  where  id= #{Id}")
    public  int  incComment(@Param("Id") int Id);
	
	@Update("update  task set likeNum = likeNum-1  where  id= #{Id}")
    public  int  decLike(@Param("Id") int Id);
	
	@Update("update  task set comment_time=#{c_time} where  id= #{pk}")
    int  upDateTaskCommentTime(Comment comment);
	
	@Update("update  task set is_delete=1 where  id= #{Id}")
    int  deleteTask(@Param("Id")int Id);
	
	@Update("update  task  set is_delete=0 where  id= #{Id}")
    int  recoverTask(@Param("Id")int Id);
	
	@Update("update  task set is_complaint=1 where  id= #{Id}")
    int  hideTask(@Param("Id")int Id);
	
	@Update("update  task  set title=content ,is_complaint=0 where  id= #{Id}")
    int  recoverTaskHide(@Param("Id")int Id);
	
	@Update("update  task  set c_time = REPLACE(DATE_ADD(c_time, INTERVAL 100 year), '-', '/') where  id= #{Id}")
    int  topTask(@Param("Id")int Id);
	
	@Update("update  task  set c_time = REPLACE(DATE_SUB(c_time, INTERVAL 100 year), '-', '/') where  id= #{Id}")
    int  downTask(@Param("Id")int Id);
	
	@Insert("insert into `like` (pk,openid) values (#{pk},#{openid})")
    int addLike(Like like);
	
	@Select("select * from  `like`  where openid= #{openid} and pk=#{pk}")
    public List<Like>getlikeByPk(String openid,int pk);
	
	@Select("select * from  `like`  where openid= #{openid} order by id desc limit #{length} ,10")
    public List<Like>getlikeByOpenid(String openid, int length);
	
	@Delete("delete from `like` WHERE id = #{Id}")
    int  deleteLike(@Param("Id")int Id);
	
	@Insert("insert into comment (pk,openid,applyTo,avatar,comment,userName,c_time,img,level,pid) values (#{pk},#{openid},#{applyTo},#{avatar},#{comment},#{userName},#{c_time},#{img},#{level},#{pid})")
    int addComment(Comment comment);
	
	@Select("select * from  comment  where pk= #{pk} and level = 1 and is_delete = 0 limit #{length},5")
    public List<Comment>getCommentByLength(int pk, int length);
	
//	@Select("select * from (select * from comment where pk= #{pk} and level = 1 and is_delete = 0 limit #{length},5) c1 LEFT JOIN (select * from  comment where level = 2 and is_delete = 0) c2 on c2.pid= c1.id ")
//    public List<CommentAll>getCommentByLength(int pk, int length);
	
	@Select("select * from  comment  where pk= #{pk} and level = 1 and is_delete = 0")
    public List<Comment>getCommentByPk(int pk);
	
	@Select("select * from  comment  where pk= #{pk} and level = 1 and is_delete = 0 order by c_time desc limit #{length},5")
    public List<Comment>getCommentByReverse(int pk, int length);
	
//	@Select("select * from (select * from comment where pk= #{pk} and level = 1 and is_delete = 0 order by c_time desc limit #{length},5) c1 LEFT JOIN (select * from  comment where level = 2 and is_delete = 0) c2 on c2.pid= c1.id")
//    public List<CommentAll>getCommentByReverse(int pk, int length);
	
	@Select("select * from  comment  where pk= #{pk} and level = 1 and is_delete = 0 order by like_num desc limit #{length},5")
    public List<Comment>getCommentByHot(int pk, int length);
	
//	@Select("select * from (select * from comment where pk= #{pk} and level = 1 and is_delete = 0 order by like_num desc limit #{length},5) c1 LEFT JOIN (select * from  comment where level = 2 and is_delete = 0) c2 on c2.pid= c1.id")
//    public List<CommentAll>getCommentByHot(int pk, int length);
	
	@Select("select * from  comment  where pid= #{pid} and level = 2 and is_delete = 0")
    public List<Comment>getCommentByPid(int pid);
	
	@Select("select * from  comment  where pid= #{pid} and level = 2 and is_delete = 0 limit 1")
    public List<Comment>getFirstCommentByPid(int pid);
	
	@Select({"<script>",
		"select * from  comment  where pid in ",
		"<foreach collection='str' item='item' index='index' separator=',' open='(' close = ')'>",
		"#{item}",
		"</foreach>",
		"and level = 2 and is_delete = 0",
		"</script>"
	})
    public List<Comment>getCommentByIdList(@Param("str") List<String> str);
	
	@Select("select * from  comment  where openid= #{openid} and is_delete = 0 order by c_time desc limit #{length},10")
    public List<Comment>getCommentByOpenid(String openid,int length);
	@Select("select * from  comment  where applyTo= #{applyTo} and is_delete = 0 order by c_time desc limit #{length},10")
    public List<Comment>getCommentByApplyto(String applyTo,int length);
	
	@Update("update comment set is_delete=1 where  id= #{Id}")
    int  deleteComment(@Param("Id")int Id);
	
	@Select("select * from  member  where openid= #{openid}")
    public List<Member>getMember(String openid);
	
	@Select("select * from  member")
    public List<Member>getAllMember();
	
	@Insert("insert into member (openid,name) values (#{openid},#{name})")
    int addMember(Member member);
	
	@Update("update  task set watchNum =#{watchNum}  where  id= #{Id}")
    public  int  upDateWatch(@Param("watchNum") int watchNum,
                                @Param("Id") int Id);
	
	@Update("update  task set choose =#{choose}  where  id= #{Id}")
    public  int  updateChoose(int Id,int choose);
	
	@Select("select * from  comment order by c_time desc limit #{length},10")
    public List<Comment>getAllComment(int length);
	
	@Select("select * from  banner where weight>0 order by weight desc")
    public List<Banner>getBanner();
	
	@Select("select * from  banner2 where weight>0 order by weight desc")
    public List<Banner>getBanner2();
	
	@Delete("delete from `banner` WHERE id = #{Id}")
    int  deleteBanner(@Param("Id")int Id);
	
	@Insert("insert into banner (imageUrl,navUrl,weight) values (#{imgPath},#{url},#{weight})")
    int addBanner(String imgPath,String url,String weight);
	
	@Select("select * from  blacklist where openid = #{openid}")
    public List<BlackList>checkBlackList(String openid);
	
	@Select("select * from  blacklist order by id desc limit #{length},20")
    public List<BlackList>getBlackList(int length);
	
	@Select("select id,title,openid from  task  where  title like CONCAT('%',#{search},'%') order by c_time desc")
    public List<Task>getOpenidbySearch(String search);
	
	@Select("select id,title,openid from  task  where  openid = #{search} order by c_time desc")
    public List<Task>getAllOpenid(String search);
	
	
	@Select("select id,`comment`,openid from  `comment`  where  `comment` like CONCAT('%',#{search},'%') order by c_time desc limit 0,20")
    public List<Comment>getOpenidBySearchComment(String search);
	
	@Select("select id,`comment`,openid from  `comment`  where  openid = #{search} order by c_time desc limit 0,20")
    public List<Comment>getOpenidBySearchAllComment(String search);
	
	@Insert("insert into `blacklist` (openid,period,description,start) values (#{openid},#{period},#{description},NOW())")
    int addBlacklist(BlackList blacklist);
	
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

	@Select("select * from task where title like CONCAT('%',#{search},'%') order by c_time desc")
    public List<Task>getChooseBySearch(String search);
	
	@Select("select * from accesscode where c_time >= #{c_time} and id=1")
	public List<AccessCode> getCodeCtime(Long c_time);
	
	@Update("update accesscode set accessCode =#{code},c_time=#{c_time} where id=1")
    public  int saveCode(String code,Long c_time);
}
