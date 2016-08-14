package app.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Bug implements Serializable {

    public Long project_id; // ProjectID
    public Long bug_id; // BugID

    // Bug
    // IssueTypeID : 提问类型
    // description : 问题描述
    // DateClosed : 创建提单时间
    // DateAssigned : 受理时间
    // IssueFinishDate : 完成时间
    // DateModified : 更新时间
    // ProblemDescription : 问题描述
    // ProgressStatusID : 提单状态
    public Integer issue_type; // 提问类型
    public String description; // 问题描述
    public Long create_time;// 创建提单时间
    public Long close_time;// 提单关闭时间
    public Long update_time;// 更新时间
    public Long assigned_time; // 受理时间
    public Long finish_time; // 完成时间
    public String rating_time; // 评价时间
    public Integer status;// 提单状态.

    // BugCustomFieldsData1
    // FieldShortText5 : 游戏ID
    // FieldShortText6 : 游戏昵称
    // FieldLongText1 : 商户订单号
    // FieldLongText4 : 下载渠道
    // FieldLongText5 : 手机品牌
    // FieldInteger4 : 来源
    // FieldInteger10 : 充值方式
    // FieldDouble4 : 充值金额
    public Integer game_id;// 游戏ID
    public String game_name;// 游戏名称
    public String merchant_order_number; // 商户订单号
    // 2016.8.12
    public String download_channel;// 下载渠道
    // 2016.8.12
    public String phone_brand; // 手机品牌
    public Integer source; // 提单来源
    public Integer refund_type; // 充值方式
    public Double recharge_money; // 充值金额

    // BugCustomFieldsData2
    // FieldInteger9 : VIP等级
    // FieldShortText2 : 角色名
    // FieldShortText3 : 服务器
    // FieldShortText4 : 联系电话
    // FieldLongText5 : 扣费手机号码
    // FieldLongText7 : 乐逗ID
    // FieldMemo2 : 截图
    public Integer vip_level; // vip等级
    public String role_name;// 角色名
    public String server_name;// 服务器
    public String tel; // 联系电话
    // 2016.8.12
    public String charge_phone;// 扣费手机
    public String player_id; // 玩家id
    public String image; // 截图

    // BugCustomFieldsData3
    // FieldShortText1 : 满意度评分
    // FieldShortText2 : 客服态度评分
    // FieldShortText3 : 服务效率评分
    // FieldShortText4 : 发生时间
    // FieldLongText2 : 充值时间
    // FieldMemo1 : 客服回复内容
    // FieldInteger3 : 阅读状态
    public String customer_service_rating; // 客服态度评分
    public String satisfaction_rating; /// 满意度评分
    public String efficiency_rating;// 效率评分
    public String issue_time; // 发生时间
    public String recharge_time; // 充值时间
    public String reply_content; // 回复内容
    public Integer read_status;// 阅读状态

    // BugCustomFieldsData4
    // FieldInteger2 : 兑换码获得来源
    // FieldDate4 : 兑换码获得时间
    public Long redemption_time; // 兑换码获取时间
    public Integer redemption_source; // 兑换码来源

    public Long getProject_id() {
	return project_id;
    }

    public void setProject_id(Long project_id) {
	this.project_id = project_id;
    }

    public Long getBug_id() {
	return bug_id;
    }

    public void setBug_id(Long bug_id) {
	this.bug_id = bug_id;
    }

    public Integer getIssue_type() {
	return issue_type;
    }

    public void setIssue_type(Integer issue_type) {
	this.issue_type = issue_type;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Long getCreate_time() {
	return create_time;
    }

    public void setCreate_time(Long create_time) {
	this.create_time = create_time;
    }

    public Long getClose_time() {
	return close_time;
    }

    public void setClose_time(Long close_time) {
	this.close_time = close_time;
    }

    public Long getUpdate_time() {
	return update_time;
    }

    public void setUpdate_time(Long update_time) {
	this.update_time = update_time;
    }

    public Long getAssigned_time() {
	return assigned_time;
    }

    public void setAssigned_time(Long assigned_time) {
	this.assigned_time = assigned_time;
    }

    public Long getFinish_time() {
	return finish_time;
    }

    public void setFinish_time(Long finish_time) {
	this.finish_time = finish_time;
    }

    public String getRating_time() {
	return rating_time;
    }

    public void setRating_time(String rating_time) {
	this.rating_time = rating_time;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Integer getGame_id() {
	return game_id;
    }

    public void setGame_id(Integer game_id) {
	this.game_id = game_id;
    }

    public String getGame_name() {
	return game_name;
    }

    public void setGame_name(String game_name) {
	this.game_name = game_name;
    }

    public String getMerchant_order_number() {
	return merchant_order_number;
    }

    public void setMerchant_order_number(String merchant_order_number) {
	this.merchant_order_number = merchant_order_number;
    }

    public String getDownload_channel() {
	return download_channel;
    }

    public void setDownload_channel(String download_channel) {
	this.download_channel = download_channel;
    }

    public String getPhone_brand() {
	return phone_brand;
    }

    public void setPhone_brand(String phone_brand) {
	this.phone_brand = phone_brand;
    }

    public Integer getSource() {
	return source;
    }

    public void setSource(Integer source) {
	this.source = source;
    }

    public Integer getRefund_type() {
	return refund_type;
    }

    public void setRefund_type(Integer refund_type) {
	this.refund_type = refund_type;
    }

    public Double getRecharge_money() {
	return recharge_money;
    }

    public void setRecharge_money(Double recharge_money) {
	this.recharge_money = recharge_money;
    }

    public Integer getVip_level() {
	return vip_level;
    }

    public void setVip_level(Integer vip_level) {
	this.vip_level = vip_level;
    }

    public String getRole_name() {
	return role_name;
    }

    public void setRole_name(String role_name) {
	this.role_name = role_name;
    }

    public String getServer_name() {
	return server_name;
    }

    public void setServer_name(String server_name) {
	this.server_name = server_name;
    }

    public String getTel() {
	return tel;
    }

    public void setTel(String tel) {
	this.tel = tel;
    }

    public String getCharge_phone() {
	return charge_phone;
    }

    public void setCharge_phone(String charge_phone) {
	this.charge_phone = charge_phone;
    }

    public String getPlayer_id() {
	return player_id;
    }

    public void setPlayer_id(String player_id) {
	this.player_id = player_id;
    }

    public String getImage() {
	return image;
    }

    public void setImage(String image) {
	this.image = image;
    }

    public String getCustomer_service_rating() {
	return customer_service_rating;
    }

    public void setCustomer_service_rating(String customer_service_rating) {
	this.customer_service_rating = customer_service_rating;
    }

    public String getSatisfaction_rating() {
	return satisfaction_rating;
    }

    public void setSatisfaction_rating(String satisfaction_rating) {
	this.satisfaction_rating = satisfaction_rating;
    }

    public String getEfficiency_rating() {
	return efficiency_rating;
    }

    public void setEfficiency_rating(String efficiency_rating) {
	this.efficiency_rating = efficiency_rating;
    }

    public String getIssue_time() {
	return issue_time;
    }

    public void setIssue_time(String issue_time) {
	this.issue_time = issue_time;
    }

    public String getRecharge_time() {
	return recharge_time;
    }

    public void setRecharge_time(String recharge_time) {
	this.recharge_time = recharge_time;
    }

    public String getReply_content() {
	return reply_content;
    }

    public void setReply_content(String reply_content) {
	this.reply_content = reply_content;
    }

    public Integer getRead_status() {
	return read_status;
    }

    public void setRead_status(Integer read_status) {
	this.read_status = read_status;
    }

    public Long getRedemption_time() {
	return redemption_time;
    }

    public void setRedemption_time(Long redemption_time) {
	this.redemption_time = redemption_time;
    }

    public Integer getRedemption_source() {
	return redemption_source;
    }

    public void setRedemption_source(Integer redemption_source) {
	this.redemption_source = redemption_source;
    }

}
