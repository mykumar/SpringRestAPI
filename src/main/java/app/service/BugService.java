package app.service;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.domain.Bug;
import app.jdbc.JdbcTemplate;
import app.util.Convert;

@Service
@Transactional
public class BugService {

    private static Logger log = LoggerFactory.getLogger(BugService.class);

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Transactional
    public void save(Bug bug) {
	log.info(".save");
	Long ProjectID = bug.project_id;
	// generate.BugID
	String sql = "SELECT (MAX(BugID)+1) as BugID FROM bug WHERE ProjectID=" + ProjectID;
	Long BugID = jdbcTemplate.queryForObject(sql, Long.class);
	log.info("ProjectID " + ProjectID + " new BugID:" + BugID);
	// DateCreated : 创建时间
	Date date = new Date();
	bug.create_time = Convert.toSeconds(date);
	String ProblemID = BugID + "";// ProblemID 问题id
	// insert.bug
	sql = "INSERT INTO bug(ProjectID,BugID,ProblemID,IssueTypeID,DateCreated,ProblemDescription,ProgressStatusID) VALUES (?,?,?,?,?,?,?)";
	Object[] args = { ProjectID, BugID, ProblemID, bug.issue_type, date, bug.description, bug.status };
	int i = jdbcTemplate.update(sql, args);
	if (i > 0) {
	    log.info(sql);
	    log.info("insert Bug successful..");
	}

	// insert.BugCustomFieldsData1
	// FieldLongText1 : 商户订单号
	// FieldInteger4 : 来源
	// FieldInteger10 : 充值方式
	// FieldDouble4 : 充值金额
	sql = "INSERT INTO BugCustomFieldsData1(ProjectID,BugID,FieldLongText1,FieldInteger4,FieldInteger10,FieldDouble4) VALUES (?,?,?,?,?,?)";
	Object[] data1_args = { ProjectID, BugID, bug.merchant_order_number, bug.source, bug.refund_type,
		bug.recharge_money };
	i = jdbcTemplate.update(sql, data1_args);
	if (i > 0) {
	    log.info(sql);
	    log.info("insert BugCustomFieldsData1 successful..");
	}

	// insert.BugCustomFieldsData2
	// FieldInteger9 : VIP等级
	// FieldShortText2 : 角色名
	// FieldShortText3 : 服务器
	// FieldShortText4 : 联系电话
	// FieldLongText7 : 乐逗ID
	// FieldMemo2 : 截图
	sql = "INSERT INTO BugCustomFieldsData2(ProjectID,BugID,FieldInteger9,FieldShortText2,FieldShortText3,FieldShortText4,FieldLongText7,FieldMemo2) VALUES (?,?,?,?,?,?,?,?)";
	Object[] data2_args = { ProjectID, BugID, bug.vip_level, bug.role_name, bug.server_name, bug.tel, bug.player_id,
		bug.image };
	i = jdbcTemplate.update(sql, data2_args);
	if (i > 0) {
	    log.info(sql);
	    log.info("insert BugCustomFieldsData2 successful..");
	}

	// insert.BugCustomFieldsData3
	// FieldLongText2 : 充值时间
	// FieldShortText4 : 发生时间
	// FieldInteger1 : 游戏ID(下拉选项)
	// FieldInteger3 : 阅读状态

	bug.read_status = 0;// 阅读状态默认为0
	sql = "INSERT INTO BugCustomFieldsData3(ProjectID,BugID,FieldLongText2,FieldShortText4,FieldInteger1,FieldInteger3) VALUES (?,?,?,?,?,?)";
	Object[] data3_args = { ProjectID, BugID, bug.recharge_time, bug.issue_time, bug.game_id, bug.read_status };
	i = jdbcTemplate.update(sql, data3_args);
	if (i > 0) {
	    log.info(sql);
	    log.info("insert BugCustomFieldsData3 successful..");
	}

	// insert.BugCustomFieldsData4
	// FieldInteger9 : 公众号问题类型
	// FieldInteger2 : 兑换码获得来源
	// FieldDate4 : 兑换码获得时间
	if (bug.redemption_time != null) {
	    Date fieldDate4 = Convert.toDate(bug.redemption_time);
	    sql = "INSERT INTO BugCustomFieldsData4(ProjectID,BugID,FieldInteger2,FieldDate4)VALUES(?,?,?,?)";
	    Object[] data4_args = { ProjectID, BugID, bug.redemption_source, fieldDate4 };
	    i = jdbcTemplate.update(sql, data4_args);
	    if (i > 0) {
		log.info(sql);
		log.info("insert BugCustomFieldsData4 successful..");
	    }
	}

	bug.bug_id = BugID;
    }

    @Transactional
    public void eval(Bug bug) {
	// FieldShortText1 : 满意度评分
	// FieldShortText2 : 客服态度评分
	// FieldShortText3 : 服务效率评分
	String sql = "UPDATE BugCustomFieldsData3 SET FieldShortText1=?,FieldShortText2=?,FieldShortText3=? WHERE ProjectID=? And BugID=?";
	Object[] args = { bug.satisfaction_rating, bug.customer_service_rating, bug.efficiency_rating, bug.project_id,
		bug.bug_id };
	int i = jdbcTemplate.update(sql, args);
	if (i > 0) {
	    bug.status = 262; // 已关闭
	    // DateModified : 更新时间
	    // DateClosed : 关闭时间
	    sql = "UPDATE Bug SET ProgressStatusID=?,DateClosed=?,DateModified=? WHERE ProjectID=? And BugID=?";
	    Date date = new Date();
	    Object[] args2 = { bug.status, date, date, bug.project_id, bug.bug_id };
	    i = jdbcTemplate.update(sql, args2);

	    bug.close_time = Convert.toSeconds(date);
	    bug.update_time = bug.close_time;
	}
    }

    public Bug getAndConvert(Long project_id, Long bug_id) {
	Object[] args = { project_id, bug_id };
	// bug
	String sql = "SELECT * FROM bug WHERE ProjectID=? And BugID=? ";
	Map<String, Object> map = jdbcTemplate.queryForMap(sql, args);
	if (map == null) {
	    return null;
	}
	Bug bug = new Bug();
	bug.project_id = project_id;
	bug.bug_id = bug_id;
	// Bug
	// IssueTypeID : 提问类型
	// ProblemDescription : 问题描述
	// DateClosed : 创建提单时间
	// DateAssigned : 受理时间
	// IssueFinishDate : 完成时间
	// DateModified : 更新时间
	// ProblemDescription : 问题描述
	// ProgressStatusID : 提单状态
	bug.issue_type = (Integer) map.get("IssueTypeID");
	bug.create_time = Convert.toSeconds(map.get("DateCreated"));
	bug.close_time = Convert.toSeconds(map.get("DateClosed"));
	bug.assigned_time = Convert.toSeconds(map.get("DateAssigned"));
	bug.finish_time = Convert.toSeconds(map.get("IssueFinishDate"));
	bug.update_time = Convert.toSeconds(map.get("DateModified"));
	bug.description = (String) map.get("ProblemDescription");
	bug.status = (Integer) map.get("ProgressStatusID");
	map = null;
	// BugCustomFieldsData1
	// FieldLongText1 : 商户订单号
	// FieldLongText4 : 下载渠道
	// FieldLongText5 : 手机品牌
	// FieldInteger4 : 来源
	// FieldInteger10 : 充值方式
	// FieldDouble4 : 充值金额
	sql = "SELECT * FROM BugCustomFieldsData1 WHERE ProjectID=? And BugID=? ";
	map = jdbcTemplate.queryForMap(sql, args);
	if (map != null) {
	    bug.merchant_order_number = (String) (map.get("FieldLongText1"));
	    bug.download_channel = (String) (map.get("FieldLongText4"));
	    bug.charge_phone = (String) (map.get("FieldLongText5"));
	    bug.source = (Integer) (map.get("FieldInteger4"));
	    bug.refund_type = (Integer) (map.get("FieldInteger10"));
	    bug.recharge_money = (Double) (map.get("FieldDouble4"));
	    map = null;
	}
	// BugCustomFieldsData2
	// FieldInteger9 : VIP等级
	// FieldShortText2 : 角色名
	// FieldShortText3 : 服务器
	// FieldShortText4 : 联系电话
	// FieldLongText5 : 扣费手机号码
	// FieldLongText7 : 乐逗ID
	// FieldMemo2 : 截图
	sql = "SELECT * FROM BugCustomFieldsData2 WHERE ProjectID=? And BugID=? ";
	map = jdbcTemplate.queryForMap(sql, args);
	if (map != null) {
	    bug.vip_level = (Integer) map.get("FieldInteger9");
	    bug.role_name = (String) (map.get("FieldShortText2"));
	    bug.server_name = (String) (map.get("FieldShortText3"));
	    bug.tel = (String) (map.get("FieldShortText4"));
	    bug.charge_phone = (String) (map.get("FieldLongText5"));
	    bug.player_id = (String) (map.get("FieldLongText7"));
	    bug.image = (String) (map.get("FieldMemo2"));
	    map = null;
	}
	// BugCustomFieldsData3
	// FieldShortText1 : 满意度评分
	// FieldShortText2 : 客服态度评分
	// FieldShortText3 : 服务效率评分
	// FieldShortText4 : 发生时间
	// FieldLongText2 : 充值时间
	// FieldMemo1 : 客服回复内容
	// FieldInteger1 : 游戏ID
	// FieldInteger3 : 阅读状态
	sql = "SELECT * FROM BugCustomFieldsData3 WHERE ProjectID=? And BugID=? ";
	map = jdbcTemplate.queryForMap(sql, args);
	if (map != null) {
	    bug.satisfaction_rating = (String) (map.get("FieldShortText1"));
	    bug.customer_service_rating = (String) (map.get("FieldShortText2"));
	    bug.efficiency_rating = (String) (map.get("FieldShortText3"));
	    bug.issue_time = (String) (map.get("FieldShortText4"));
	    bug.recharge_time = (String) (map.get("FieldLongText2"));
	    bug.reply_content = (String) (map.get("FieldMemo1"));
	    bug.game_id = (Integer) (map.get("FieldInteger1"));
	    // 阅读状态
	    bug.read_status = Convert.toInt(map.get("FieldInteger3"), 0);
	    if (bug.read_status != 1) {// 如果是未读状态，则改成已读
		sql = "UPDATE BugCustomFieldsData3 SET FieldInteger3=1 WHERE ProjectID=? And BugID=? ";
		int i = jdbcTemplate.update(sql, args);
		bug.read_status = i > 0 ? 1 : 0;
	    }
	    // 游戏名称
	    if (bug.game_id != null) {
		sql = "SELECT choicename from CustomerFieldListValue where CustomFieldID=2006 and ProjectID=? AND LDID=?";
		map = jdbcTemplate.queryForMap(sql, bug.project_id, bug.game_id);
		if (map != null) {
		    bug.game_name = (String) (map.get("choicename"));
		} else {
		    bug.game_name = "未找到游戏ID(" + bug.game_id + ")名称";
		}
	    }
	    map = null;
	}
	// BugCustomFieldsData4
	// FieldInteger2 : 兑换码获得来源
	// FieldDate4 : 兑换码获得时间
	sql = "SELECT * FROM BugCustomFieldsData4 WHERE ProjectID=? And BugID=? ";
	map = jdbcTemplate.queryForMap(sql, args);
	if (map != null) {
	    bug.redemption_source = (Integer) (map.get("FieldInteger2"));
	    bug.redemption_time = Convert.toSeconds(map.get("FieldDate4"));
	    map = null;
	}

	return bug;
    }

}
