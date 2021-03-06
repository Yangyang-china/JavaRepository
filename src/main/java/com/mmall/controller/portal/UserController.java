package com.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private IUserService iUserService;
	//登录接口
    @RequestMapping(value="login.do",method=RequestMethod.POST)
    @ResponseBody
	public ServerResponse<User> login(String username,String password,HttpSession session) {
		ServerResponse<User> response=iUserService.login(username, password);
		if(response.isSuccess()) {
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}
    
    //登出接口
    
    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
    	session.removeAttribute(Const.CURRENT_USER);
    	return ServerResponse.createBySuccess();
    }
    
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String>  register(User user){
    	return iUserService.register(user);
    }
    
    //校验接口
    @RequestMapping(value = "checkValid.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String>  checkValid(String str,String type){
    	return iUserService.checkValid(str,type);
    }
    //获取用户信息
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User>  getUserInfo(HttpSession session){
    	User user =(User)session.getAttribute(Const.CURRENT_USER);
    	if(user != null) {
    		return ServerResponse.createBySuccess(user);
    	}
    	return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
    }
    
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User>  forgetGetQuestion(String username){
    	return iUserService.selectQuestion(username);
    }
    
    //校验问题答案是否正确
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
    	return iUserService.checkAnswer(username, question, answer);
    }
    
    @RequestMapping(value = "forget_Reset_password.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
    	return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }
    
    //登录状态下重置密码
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
    	User user=(User) session.getAttribute(Const.CURRENT_USER);
    	if(user == null) {
    		return ServerResponse.createByErrorMessage("用户未登录");
    	}
    	return  iUserService.resetPassword(passwordOld, passwordNew, user);
    }
    
    //更新个人信息
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session,User user){
    	User currentUser=(User) session.getAttribute(Const.CURRENT_USER);
    	if(currentUser == null) {
    		return ServerResponse.createByErrorMessage("用户未登录");
    	}
    	user.setId(currentUser.getId());
    	user.setUsername(currentUser.getUsername());
    	ServerResponse<User> response =iUserService.updateInformation(user);
    	if(response.isSuccess()) {
    		session.setAttribute(Const.CURRENT_USER, response.getData());
    	}
    	return response;
    }
    
    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }
    
}

