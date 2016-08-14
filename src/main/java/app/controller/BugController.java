package app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import app.domain.Bug;
import app.service.BugService;
import app.util.Convert;

@Controller
@RequestMapping("/bugs")
public class BugController {

    private static Logger log = LoggerFactory.getLogger(BugController.class);

    @Autowired
    BugService bugService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView index(@RequestParam(value = "project_id", required = true) Long project_id,
	    @RequestParam(value = "player_id", required = true) String player_id,
	    @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
	    @RequestParam(value = "page_size", required = false, defaultValue = "10") Long page_size,
	    @RequestParam(value = "is_resolve", required = false, defaultValue = "0") Integer is_resolve,
	    HttpServletRequest request, HttpServletResponse response) {

	ModelAndView mav = new ModelAndView();
	mav.setView(Convert.toView(request));
	mav.addObject("action", "GET bugs");
	if (project_id == null || player_id == null) {
	    mav.addObject("error_message", "project_id或player_id 参数为空!");
	    response.setStatus(301);
	    return mav;
	}
	String sql_status = " AND b.ProgressStatusID " + (is_resolve == 0 ? "NOT IN (258,262)" : "IN (258,262)");
	String sql_player_id = "AND d2.FieldLongText7='" + player_id + "'";
	String sql_project_id = " AND b.projectid= " + project_id;
	// 统计总数量
	StringBuffer s = new StringBuffer(
		"select count(*) as total_count FROM Bug b,BugCustomFieldsData2 d2 WHERE (d2.ProjectID=b.ProjectID AND d2.BugID=b.BugID)");
	s.append(sql_project_id);
	s.append(sql_status);
	s.append(sql_player_id);
	log.info("total_count \n" + s);
	// total_count
	long total_count = bugService.jdbcTemplate.queryForObject(s.toString(), Long.class);
	long total_page = total_count % page_size > 0 ? total_count / page_size + 1 : total_count / page_size;
	mav.addObject("page", page);// 页数
	mav.addObject("page_size", page_size); // 页量
	mav.addObject("total_page", total_page);// 总页数
	mav.addObject("total_count", total_count);// 总数
	if (total_count <= 0) {
	    response.setStatus(404);
	    mav.addObject("error_message", "总数为0");
	    mav.addObject("bugs", new ArrayList<>());
	    return mav;
	}
	if (total_page < page) {
	    response.setStatus(404);
	    mav.addObject("error_message", "当前页数超出总页数");
	    mav.addObject("bugs", new ArrayList<>());
	    return mav;
	}
	// page
	long offset = page_size * (page - 1);
	long max = total_count > offset ? page_size : total_count % page_size;
	s = new StringBuffer();
	s.append(
		"SELECT b.ProjectID as project_id,b.BugID as bug_id,b.ProgressStatusID as status,b.ProblemDescription as description,d3.FieldInteger3 as read_status FROM Bug b,BugCustomFieldsData3 d3");
	s.append(" WHERE (b.ProjectID=d3.ProjectID AND b.BugID=d3.BugID)");
	s.append(" AND b.BugID IN(SELECT top " + max + " b.bugid FROM Bug b WHERE b.bugid NOT IN");
	s.append(" (SELECT top " + offset + " b.bugid FROM Bug b,BugCustomFieldsData2 d2");
	s.append(" WHERE (b.ProjectID=d2.ProjectID AND b.BugID=d2.BugID)");
	s.append(sql_project_id);
	s.append(sql_status);
	s.append(sql_player_id);
	s.append(" ORDER BY b.BugID DESC) ");
	s.append(" ORDER BY b.BugID DESC) ");
	s.append(" ORDER BY b.BugID DESC");
	log.info("page sql \n" + s);
	List<Map<String, Object>> maps = bugService.jdbcTemplate.queryForList(s.toString());
	for (Map<String, Object> map : maps) {
	    map.put("read_status", Convert.toInt(map.get("read_status"), 0));
	}
	mav.addObject("bugs", maps);

	// 阅读数量
	s = new StringBuffer();
	s.append("SELECT count(*) as unread_count FROM Bug b,BugCustomFieldsData2 d2,BugCustomFieldsData3 d3 ");
	s.append(" WHERE (b.ProjectID=d2.ProjectID AND b.BugID=d2.BugID) ");
	s.append(" AND (b.ProjectID=d3.ProjectID AND b.BugID=d3.BugID)");
	s.append(sql_project_id);
	s.append(sql_status);
	s.append(sql_player_id);
	s.append(" AND (d3.FieldInteger3=0 OR d3.FieldInteger3 IS NULL)");// 未读
	log.info("unread_count\n" + s);
	long unread_count = bugService.jdbcTemplate.queryForObject(s.toString(), Long.class);
	mav.addObject("unread_count", unread_count);

	return mav;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView show(@PathVariable("id") Long bug_id,
	    @RequestParam(value = "project_id", required = true) Long project_id, HttpServletRequest request,
	    HttpServletResponse response) {
	ModelAndView mav = new ModelAndView();
	mav.setView(Convert.toView(request));
	mav.addObject("action", "GET bugs/{id}");

	if (project_id == null || bug_id == null) {
	    mav.addObject("error_message", "project_id或bug_id参数为空!");
	    response.setStatus(301);
	    return mav;
	}
	Bug bug = bugService.getAndConvert(project_id, bug_id);
	if (bug == null) {
	    mav.addObject("error_message", "bug not found");
	    response.setStatus(404);
	    return mav;
	}
	mav.addObject("bug", bug);

	return mav;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView create(@ModelAttribute Bug bug, HttpServletRequest request, HttpServletResponse response) {

	ModelAndView mav = new ModelAndView();
	mav.setView(Convert.toView(request));
	mav.addObject("action", "POST bugs");

	if (bug.project_id == null) {
	    response.setStatus(301);
	    mav.addObject("error_message", "project_id参数为空!");
	    return mav;
	}
	// bug.setIssue_type(1);
	// bug.setVip_level(2);
	// bug.setSource(1);
	// bug.setServer_name("SERVER+NAME");
	// bug.setRole_name("ROLE_NAME");
	// bug.setRefund_type(1);
	// bug.setReply_content("回复内容");
	// bug.setPlayer_id("player_id");
	// bug.setDescription("描述数据字段");
	bugService.save(bug);
	mav.addObject("bug", bug);
	return mav;
    }

    @RequestMapping(value = "/config", method = { RequestMethod.GET })
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView eval(@RequestParam("project_id") Long project_id, HttpServletRequest request,
	    HttpServletResponse response) {

	ModelAndView mav = new ModelAndView();
	mav.setView(Convert.toView(request));
	mav.addObject("action", "bugs/config");

	if (project_id == null) {
	    response.setStatus(301);
	    mav.addObject("error_message", "project_id为空!");
	    return mav;
	}

	// ID,NAME
	String sql = " SELECT FieldLookupValueID as id,LookupValueName as name FROM LanguageCfgLookupValues where ProjectID=? and LanguageID=? and FieldID=?";
	mav.addObject("source_types", bugService.jdbcTemplate.queryForList(sql, project_id, 2, 21));
	mav.addObject("issue_types", bugService.jdbcTemplate
		.queryForList(sql + " AND FieldLookupValueID IN (6,9,12,10)", project_id, 2, 120));
	mav.addObject("issue_status", bugService.jdbcTemplate.queryForList(sql, project_id, 2, 601));
	mav.addObject("game_types", bugService.jdbcTemplate.queryForList(sql, project_id, 2, 64));

	// game names
	sql = "SELECT ldid,choicename from CustomerFieldListValue where CustomFieldID=2006 and ProjectID=?";
	mav.addObject("game_names", bugService.jdbcTemplate.queryForList(sql, project_id));

	return mav;
    }

    @RequestMapping(value = "/{id}/eval", method = { RequestMethod.PUT, RequestMethod.GET, RequestMethod.POST })
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView eval(@PathVariable("id") Long bug_id, @ModelAttribute Bug bug, HttpServletRequest request,
	    HttpServletResponse response) {

	ModelAndView mav = new ModelAndView();
	mav.setView(Convert.toView(request));
	mav.addObject("action", "PUT bugs/{id}/eval");

	if (bug.project_id == null) {
	    response.setStatus(301);
	    mav.addObject("error_message", "project_id为空!");
	    return mav;
	}
	bug.bug_id = bug_id;
	bugService.eval(bug);
	return mav;
    }

}
