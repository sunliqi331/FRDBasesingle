package com.its.frd.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.chat.service.ChatService;
import com.its.common.entity.main.User;
import com.its.common.exception.ServiceException;
import com.its.common.service.UserService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.ChatMessage;
import com.its.frd.entity.Friendgroup;
import com.its.frd.entity.Friends;
import com.its.frd.entity.Group;
import com.its.frd.entity.Usercompanys;
import com.its.frd.service.ChatMessageService;
import com.its.frd.service.FriendsService;
import com.its.frd.service.GroupService;
import com.its.frd.service.UsercompanysService;

@Controller
@RequestMapping("/friends")
public class FriendsController {
	private final String PAGEPRE = "friends/";
	@Resource
	private FriendsService fdServ;
	@Resource
	private ChatService chatService;
	
	@Resource
	private ChatMessageService chatMessageService;
	
	@Resource
	private GroupService groupService;
	@Resource
	private UserService userServ;
	@Resource
	private UsercompanysService usercompanysService;

	@RequiresPermissions("FriendList:view")
	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String friendPage(){
		return PAGEPRE + "friendsList";
	}
	
	/**
	 * 查询用户数据访问UserController
	 */
	
	/**
	 * 分页查当前用户好友
	 * @param request
	 * @param page
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/friendPageData")
	@ResponseBody
	public String pageData(HttpServletRequest request, Page page) throws JsonProcessingException{
		Map<String, Object> map = new HashMap<String, Object>();
		SearchFilter[] filters = new SearchFilter[1];
		filters[0] = new SearchFilter("userid",SearchFilter.Operator.EQ,SecurityUtils.getShiroUser().getId());
		Specification<Friends> specification = DynamicSpecifications.bySearchFilter(request, Friends.class,filters);
		List<Friends> friends = fdServ.findPage(specification, page);
		for(Friends fr : friends){
			String email = userServ.findById(fr.getFriendid()).getEmail();
			fr.setEmail(email);
		}
		for(Friends fd : friends){
			String phone =userServ.findById(fd.getFriendid()).getPhone();
			fd.setPhone(phone);
		}
		for(Friends fk : friends){
			String username =userServ.findById(fk.getFriendid()).getUsername();
			fk.setUsername(username);
		}
		map.put("page", page);
		map.put("friends", friends);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(map);
	}

	/**
	 * 当前用户的群组
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping("/groupPageData")
	@ResponseBody
	public Map<String,Object> pageGroupData(HttpServletRequest request, Page page){
		Map<String, Object> map = new HashMap<String, Object>();
		SearchFilter[] filters = new SearchFilter[1];
		filters[0] = new SearchFilter("userid",SearchFilter.Operator.EQ,SecurityUtils.getShiroUser().getId());
		
		Specification<Friendgroup> specification = DynamicSpecifications.bySearchFilter(request, Friendgroup.class,filters);
		
		List<Group> groups = fdServ.findGroupPage(specification, page);
		for(Group gr : groups){
			String username = userServ.findById(gr.getCreateUserId()).getRealname();
			gr.setCreateUserName(username);
		}
		map.put("page", page);
		map.put("groups", groups);
		return map;
	}
	
	@RequiresPermissions("FriendList:save")
	@RequestMapping(value="/addfriend",method = RequestMethod.GET)
	public String createSubSys() {
	     return PAGEPRE + "addFriend";
	}
	
	@RequiresPermissions("group:save")
	@RequestMapping(value="/addgroup",method = RequestMethod.GET)
	public ModelAndView addgroup(String pagename,Long groupId) {
		ModelAndView modelAndView = new ModelAndView(PAGEPRE + "addGroup");
		modelAndView.addObject("friendsList", fdServ.findMyFriends(SecurityUtils.getShiroUser().getUser().getId()));
		Group group = new Group();
		if(pagename.equals("addfriends")){
			List<User> members = fdServ.findGroupMembers(groupId);
			List<Long> selectedIds = new ArrayList<Long>();
			for(User user : members){
				selectedIds.add(user.getId());
			}
			group = groupService.findOne(groupId);
			modelAndView.addObject("selectedIds", selectedIds);
		}
		return modelAndView.addObject("group", group);
	}
	
	@RequiresPermissions("FriendList:save")
	@RequestMapping("/addFriends")
	@ResponseBody
	public String addFriend(Friends fd){
		String msg = "操作成功!";
		try {
			fdServ.saveFriend(fd);
		} catch (ServiceException e) {
			msg = "操作失败!";
		}
		return AjaxObject.newOk(msg).toString();
	}
	
	@RequestMapping("/updateFriendsRemainingNum")
	@ResponseBody
	public String updateFriendsRemainingNum(Friends fd){
		try {
			chatService.updateFriendsRemainingNum(fd);
		} catch (ServiceException e) {
			//msg = "操作失败!";
		}
		return "";
	}
	
	@RequiresPermissions("group:save")
	@RequestMapping("/addGroup")
	@ResponseBody
	public String addGroup(Group group){
		try {
			groupService.addGroup(group);
		} catch (ServiceException e) {
			return AjaxObject.newError("操作失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("添加成功!").toString();
	}
	
	@RequiresPermissions("FriendList:delete")
	@RequestMapping("/delFriendById/{id}")
	@ResponseBody
	public String delFriendById(@PathVariable Long id){
		String msg = "删除成功!";
		try {
			Friends fd = new Friends();
			fd.setId(id);
			fdServ.deleteFriends(fd);
		} catch (ServiceException e) {
			msg = "操作失败!";
		}
		return AjaxObject.newOk(msg).toString();
	}
	
	@RequiresPermissions("group:delete")
	@RequestMapping("/quitGroupById/{id}")
	@ResponseBody
	public String delGroupById(@PathVariable Long id){
		String msg = "退出成功!";
		try {
			groupService.deleteGroupRelationship(id);
		} catch (ServiceException e) {
			msg = "操作失败!";
		}
		return AjaxObject.newOk(msg).toString();
	}
	
	@RequiresPermissions("FriendList:view") 
    @RequestMapping(value="/tree_list", method={RequestMethod.GET, RequestMethod.POST})
    public String tree_list(Map<String, Object> map) {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null)
			return "error/403";
        return PAGEPRE + "tree_list";
    }
    @RequiresPermissions("group:view")
    @RequestMapping(value="/group", method={RequestMethod.GET, RequestMethod.POST})
    public String group(Map<String, Object> map) {
    	Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null)
			return "error/403";
    	return PAGEPRE + "group_list";
    }
    @RequestMapping(value="/toAcceptPage", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView toAcceptPage() {
    	ModelAndView modelAndView = new ModelAndView(PAGEPRE + "acceptPage");
    	List<ChatMessage> list = chatMessageService.findChatMessageByCurrentUserAndReadStatus(SecurityUtils.getShiroUser().getUser().getId(),
    			ChatMessage.MESSAGE_READ_NO, ChatMessage.MESSAGE_TYPE_INVITE,ChatMessage.CHAT_TYPE_FRIEND);
    	return modelAndView.addObject("chatMessages", list);
    }
    @RequestMapping(value="/chat", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView chat(User user,HttpServletRequest request) {
    	Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null)
			return new ModelAndView("error/403");
    	user = SecurityUtils.getShiroUser().getUser();
//        return PAGEPRE + "friend";
        return new ModelAndView(PAGEPRE + "friend").addObject("user", user);
    }
    
    /**
	 * accept invitation ; 
	 * @param chatMessage
	 * @param accept 0 approve -1 unapprove
	 * @throws Exception
	 */
	@RequestMapping("/acceptInvitation") 
	@ResponseBody
    public String acceptInvitation(HttpServletRequest request) throws Exception {
		String[] friendIds = request.getParameterValues("friendIds");
		List<ChatMessage> list = chatMessageService.findChatMessageByCurrentUserAndReadStatus(SecurityUtils.getShiroUser().getUser().getId(),
    			ChatMessage.MESSAGE_READ_NO, ChatMessage.MESSAGE_TYPE_INVITE,ChatMessage.CHAT_TYPE_FRIEND);
		for(ChatMessage chatMessage : list){
			if(friendIds == null){
				chatMessage.setIsRead(ChatMessage.MESSAGE_READ_YES);
				chatMessageService.saveOrUpdateChatMessage(chatMessage);
				return  AjaxObject.newOk("操作成功").toString();
			}
			if(Arrays.asList(friendIds).contains(String.valueOf(chatMessage.getFriendid()))){
				chatService.updateInvitationMessage(chatMessage, 0);
			}else{
				chatService.updateInvitationMessage(chatMessage, -1);
			}
		}
		return AjaxObject.newOk("操作成功").toString();
    }
    
    @RequestMapping(value="/tree", method={RequestMethod.GET, RequestMethod.POST})
    public String tree(Map<String, Object> map,Page page,HttpServletRequest request) {
        SearchFilter[] filters = new SearchFilter[1];
        filters[0] = new SearchFilter("userid",SearchFilter.Operator.EQ,SecurityUtils.getShiroUser().getId());
        
        Specification<Friends> specification = DynamicSpecifications.bySearchFilter(request, Friends.class,filters);
        List<Friends> fd = fdServ.findPage(specification, page);
        map.put("friend", fd);
        return PAGEPRE + "tree";
    }
    
    //添加好友中显示的用户
    @RequestMapping("/data2")
	@ResponseBody
	public String data2(User user,HttpServletRequest request,Page page) throws JsonProcessingException {
		Map<String,Object> map = new HashMap<String,Object>();
		List<Friends> friend = fdServ.findMyFriends(SecurityUtils.getShiroUser().getUser().getId());
		List<Long> ids = new ArrayList<>();
		for(Friends fr : friend) {
			ids.add(fr.getUserid());
		}
		List<Usercompanys> usercompanys = usercompanysService.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
		List<Long> sameCompanyUserIds = new ArrayList<>();
		for(Usercompanys usercompany : usercompanys) {
			sameCompanyUserIds.add(usercompany.getUserid());
		}
		
		List<SearchFilter> filters = new ArrayList<SearchFilter>();
		filters.add(new SearchFilter("registerstate", Operator.EQ, "1"));
		filters.add(new SearchFilter("id", Operator.NOTEQ, SecurityUtils.getShiroUser().getUser().getId()));
		filters.add(new SearchFilter("id", Operator.IN, sameCompanyUserIds.toArray()));
		if (!(user.getEmail()==null||user.getEmail().isEmpty())) {
			filters.add(new SearchFilter("email", Operator.LIKE, user.getEmail()));
		}
		if (!(user.getRealname()==null||user.getRealname().isEmpty())) {
			filters.add(new SearchFilter("realname", Operator.LIKE, user.getRealname()));
		}
		if (!(user.getUsername()==null||user.getUsername().isEmpty())) {
			filters.add(new SearchFilter("username", Operator.LIKE, user.getUsername()));
		}
		if (!(user.getPhone()==null||user.getPhone().isEmpty())) {
			filters.add(new SearchFilter("phone", Operator.LIKE, user.getPhone()));
		}
		
		
		Specification<User> specification = DynamicSpecifications.bySearchFilter(request, User.class, filters);
//		Specification<User> specification = DynamicSpecifications.bySearchFilter(request, User.class
//				,new SearchFilter("email", Operator.LIKE, email)
//				,new SearchFilter("registerstate", Operator.EQ, "1")
//				,new SearchFilter("id", Operator.NOTEQ, SecurityUtils.getShiroUser().getUser().getId()));
		
		List<User> users = userServ.findByExample(specification, page);
		/*List<Long> uids = new ArrayList<>();
		for(User u : users) {
			uids.add(u.getId());
		}
		for(int i=uids.size()-1;i>=0;i--){
			if(ids.contains(uids.get(i)))
				uids.remove(i);
		}
		List<User> user1 = new ArrayList<>();
		if(uids.size() > 0){
		Specification<User> specification1 = DynamicSpecifications.bySearchFilter(request, User.class
					,new SearchFilter("id", Operator.IN,uids.toArray()));
			user1 = userServ.findByExample(specification1, page);
		}*/
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("user", users);
		return mapper.writeValueAsString(map);
	}
}
