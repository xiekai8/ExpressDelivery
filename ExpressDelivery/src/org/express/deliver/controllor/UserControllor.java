package org.express.deliver.controllor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.express.deliver.manager.IUserManager;
import org.express.deliver.pojo.User;
import org.express.deliver.sms.IDCardValidate;
import org.express.deliver.sms.IndustrySMS;
import org.express.deliver.util.CutImg;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户Controllor类
 * 
 * @author 吴文鑫
 * 
 */
@Controller
@RequestMapping("/user")
public class UserControllor {
	@Resource(name = "userManager")
	private IUserManager userManager;

	@RequestMapping("/login")
	/**
	 * 登录方法
	 * @param user
	 */
	public ModelAndView login(User user, HttpServletRequest request) {
		User user2 = userManager.login(user);
		ModelAndView modelAndView = null;
		// 视图解释器解析ModelAndVIew是，其中model本生就是一个Map的实现类的子类。

		if (user2 != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user2);
			modelAndView = new ModelAndView("redirect:/ui/jsp/main/frame.jsp");
			return modelAndView;
		} else {
			Map<String, Object> model = new HashMap<String, Object>();
			// 视图解析器将model中的每个元素都通过model.put(name, value);
			// 这样就可以在JSP页面中通过EL表达式来获取对应的值
			model.put("url", "index.jsp");
			model.put("message", "信息输入有误，请您核对后再次登录!");
			modelAndView = new ModelAndView("ui/jsp/commont/error", model);
			return modelAndView;
		}

	}

	@RequestMapping("/loginAndroid")
	@ResponseBody
	public String loginAndroid(User user) throws UnsupportedEncodingException {
		User user2 = userManager.loginAndroid(user);
		return "{\"id\":" + user2.getId() + "}";

	}

	/**
	 * 分页查询所有用户信息，显示在后台管理页面中
	 * 
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */

	@RequestMapping(value = "/UserListBybussiness", produces = "text/html;charset=UTF-8")
	public ModelAndView getUserListBybussiness(String keyword, int pageNo, int pageSize,
			String userType, String expressType)
	
			throws JsonGenerationException, JsonMappingException, IOException {
		userType="快递员";
		if (keyword == null) {
			keyword = "";
		}
		Map<String, Object> map = userManager.queryUserByPagingBybussiness(pageNo, pageSize, keyword, userType, expressType);
		int total = userManager.queryAllUserAcount();
		map.put("pageSize", pageSize);
		map.put("pageNo", pageNo);
		map.put("keyword", keyword);
		return new ModelAndView("ui/jsp/tablelist_manger/user/courierlist",
				"result", map);
	}

	/**
	 * 
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @param userType
	 * @param expressType
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/UserListByAdmin", produces = "text/html;charset=UTF-8")
	public ModelAndView getUserListByAdmin(String keyword, int pageNo, int pageSize,
			String userType, String expressType)
			throws JsonGenerationException, JsonMappingException, IOException {
		System.out.println(keyword);
		if (keyword == null) {
			keyword = "";
		}
		Map<String, Object> map = userManager.queryUserByPagingByAdmin(pageNo, pageSize, keyword);
		map.put("pageSize", pageSize);
		map.put("pageNo", pageNo);
		map.put("keyword", keyword);
		return new ModelAndView("ui/jsp/tablelist_manger/user/userlist",
				"result", map);
	}

	/**
	 * 注册账户之前查询所有用户名，以便判断注册时输入的用户名是否已存在
	 * 
	 * @return
	 */
	@RequestMapping("/preRegister")
	@ResponseBody
	public void preRegster(HttpServletRequest request) {
		final HttpSession session = request.getSession();// 取得sesion
		// 开启线程查询数据库的所有用户名
		Thread thread = new Thread() {
			@Override
			public void run() {
				List<String> userNameList = userManager.queryAllUserName();
				session.setAttribute("allUserList", userNameList);
			}
		};
		thread.start();
		// 取得sesion
		// 开启线程查询数据库的所有电话号码
		Thread thread1 = new Thread() {
			@Override
			public void run() {
				List<String> alluserTelephoneList = userManager
						.queryAllUserTelephone();
				session.setAttribute("alluserTelephoneList",
						alluserTelephoneList);
			}
		};
		thread1.start();
	}

	/**
	 * 验证用户名是否重复
	 * 
	 * @return 是否重复
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/userNameIsExist")
	@ResponseBody
	public String userNameIsExist(HttpServletRequest request, String userName) {
		List<String> userNameList = (List<String>) request.getSession()
				.getAttribute("allUserList");
		boolean flag = false;
		/**
		 * contains判断userNameList集合里面是否包含了userName
		 */
		if (userNameList.contains(userName)) {
			flag = true;
		}
		return flag + "";
	}

	/**
	 * 注册账户
	 * 
	 * @return
	 */
	@RequestMapping("/regster")
	public String regster(User user, HttpServletRequest request) {
		// 将当前时间设置为注册时间
		user.setRegDate(new Date());
		System.out.println(user.getUserName());
		user.setUserType("管理员");
		user.setImagePath("ui/userimg/defaultuserimage.png");
		userManager.addUser(user);
		HttpSession session = request.getSession();
		session.setAttribute("user", user);

		return "redirect:/ui/jsp/main/frame.jsp";
	}

	/**
	 * 安卓注册账户
	 * 
	 * @return
	 */
	@RequestMapping("/regsterAndroid")
	@ResponseBody
	public String regsterAndroid(User user) {
		// 将当前时间设置为注册时间
		user.setRegDate(new Date());
		user.setImagePath("ui/userimg/defaultuserimage.png");
		userManager.addUser(user);
		return "succss";
	}

	/**
	 * 安卓修改个人头像
	 * 
	 * @param user
	 * @param request
	 * @param
	 * @return
	 */
	@RequestMapping("/updateUserImgAndroid")
	public String updateUserImgAndroid(HttpServletRequest request,
			MultipartFile userImg) {
		// 设置图片路径
		System.out.println("________________________");
		HttpSession session = request.getSession();
		User user2 = (User) session.getAttribute("user");
		user2.setImagePath(uploadUserImgAndroid(userImg, request));
		session.setAttribute("user", user2);
		userManager.modifyUserInfo(user2);
		return "";
	}

	/**
	 * 安卓上传图片
	 * 
	 * @param userImg
	 * @param request
	 * @return 保存的图片路径
	 */
	public String uploadUserImgAndroid(MultipartFile file,
			HttpServletRequest request) {
		// 通过request获取项目实际运行目录,就可以将上传文件存放在项目目录了,不管项目部署在任何地方都可以
		// 图片保存路径
		String filePath = request.getSession().getServletContext()
				.getRealPath("/ui/userimg/1");
		// 获取图片原始名称
		String originalFilename = file.getOriginalFilename();
		// 以用户id加图片扩展名给图片命名
		String newFileName = ((new Date()).getTime())
				+ originalFilename.substring(originalFilename.lastIndexOf("."));
		String path = filePath + newFileName;
		File file1 = new File(path);
		if (file1.exists()) {
			file1.delete();
			file1 = new File(path);
		}
		try {
			file.transferTo(file1);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 截取图片路径
		path = path.substring(path.indexOf("ui"), path.length());
		// 替换路径中斜杠
		path = path.replace("\\", "/");
		return path;
	}

	/**
	 * 退出登录，返回登录页
	 * 
	 * @param request
	 *            获取网页参数
	 * @return
	 */
	@RequestMapping("/exitLogin")
	public String exitLogin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		return "redirect:/index.jsp";
	}

	/**
	 * 修改个人头像
	 * 
	 * @param user
	 * @param request
	 * @param
	 * @return
	 */
	@RequestMapping("/updateUserImg")
	public String updateUserImg(User user, HttpServletRequest request,
			MultipartFile userImg) {
		// 设置图片路径
		HttpSession session = request.getSession();
		User user2 = (User) session.getAttribute("user");
		user2.setImagePath(uploadUserImg(userImg, request));
		session.setAttribute("user", user2);
		userManager.modifyUserInfo(user2);
		return "redirect:/ui/jsp/tablelist_manger/user/userinfo.jsp";
	}

	/**
	 * 修改个人信息
	 * 
	 * @param user
	 * @param request
	 * @param
	 * @return
	 */
	@RequestMapping("/updateUserInfo")
	public String updateUserInfo(User user, HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user2 = (User) session.getAttribute("user");
		user.setId(user2.getId());
		user.setIdCard(user2.getIdCard());
		user.setImagePath(user2.getImagePath());
		user.setUserName(user2.getUserName());
		user.setPassword(user2.getPassword());
		user.setRegDate(user2.getRegDate());
		user.setUserType(user2.getUserType());
		user.setCredit(user2.getCredit());
		user.setIntegral(user2.getIntegral());
		userManager.modifyUserInfo(user);
		session.setAttribute("user", user);
		return "redirect:/ui/jsp/tablelist_manger/user/userinfo.jsp";
	}

	/**
	 * 上传图片
	 * 
	 * @param userImg
	 * @param request
	 * @return 保存的图片路径
	 */
	public String uploadUserImg(MultipartFile userImg,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		// 通过request获取项目实际运行目录,就可以将上传文件存放在项目目录了,不管项目部署在任何地方都可以
		// 图片保存路径
		String filePath = request.getSession().getServletContext()
				.getRealPath("/ui/userimg/1");
		// 获取图片原始名称
		String originalFilename = userImg.getOriginalFilename();
		// 以用户id加图片扩展名给图片命名
		String newFileName = user.getId()
				+ originalFilename.substring(originalFilename.lastIndexOf("."));
		String path = filePath + newFileName;
		File file = new File(path);
		if (file.exists()) {
			file.delete();
			file = new File(path);
		}
		try {
			userImg.transferTo(file);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 压缩图片1
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ImageIO.write(CutImg.ImageTransform(bi, 100, 100), "png", new File(
					path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 截取图片路径
		path = path.substring(path.indexOf("ui"), path.length());
		// 替换路径中斜杠
		path = path.replace("\\", "/");
		return path;
	}

	/**
	 * 通过短信发送验证码
	 * 
	 * @param phone
	 *            手机号
	 * @param valitCode
	 *            验证码
	 * @param time
	 *            有效时间
	 * @return 短信状态
	 */
	@RequestMapping("/getIndustrySMS")
	@ResponseBody
	public String getIndustrySMS(String phone, int time,
			HttpServletRequest request) {

		String valiCode = ((int) ((Math.random() * 9 + 1) * 100000)) + "";
		final HttpSession session = request.getSession();
		session.setAttribute("valiCode", valiCode);
		// 发送验证码
		//IndustrySMS.execute(phone, valiCode, time);
		System.out.println(valiCode);
		return valiCode;
	}

	/**
	 * 安卓端 通过短信发送验证码
	 * 
	 * @param phone
	 *            手机号
	 * @param valitCode
	 *            验证码
	 * @param time
	 *            有效时间
	 * @return 短信状态
	 */
	@RequestMapping("/getAndroidIndustrySMS")
	@ResponseBody
	public String getAndroidIndustrySMS(String phone) {

		String valiCode = ((int) ((Math.random() * 9 + 1) * 100000)) + "";
		// 发送验证码
		//IndustrySMS.execute(phone, valiCode, 5);
		System.out.println(valiCode);
		return valiCode;
	}

	/**
	 * 判断输入的验证码是否正确
	 * 
	 * @param userinputvaliCode
	 * @param request
	 * @return
	 */
	@RequestMapping("/useVerificationCodeIsCorrect")
	@ResponseBody
	public String useVerificationCodeIsCorrect(String userinputvaliCode,
			HttpServletRequest request) {
		boolean flag = false;
		String valicode = (String) request.getSession()
				.getAttribute("valiCode");
		if (valicode.equals(userinputvaliCode)) {
			flag = true;
		}
		return flag + "";
	}

	/**
	 * 找回密码前要输入电话号码，先查询数据库的所有用户电话，以此判断用户输入的号码是否存在
	 * 
	 * @return
	 */
	@RequestMapping("/preSeekPassword")
	public String preSeekPassword(HttpServletRequest request) {
		final HttpSession session = request.getSession();
		// 取得sesion
		// 开启线程查询数据库的所有电话号码
		Thread thread = new Thread() {
			@Override
			public void run() {
				List<String> alluserTelephoneList = userManager
						.queryAllUserTelephone();
				session.setAttribute("alluserTelephoneList",
						alluserTelephoneList);
			}
		};
		thread.start();
		return "ui/jsp/tablelist_manger/user/usergetvalicode";
	}

	/**
	 * 验证用户找回密码时输入的电话号码是否注册
	 * 
	 * @param request
	 * @param userTelephone
	 *            找回密码时输入的电话号码
	 * @return json 包含号码是否正确 以及号码
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/userTelephoneIsExist")
	@ResponseBody
	public String userTelephoneIsExist(HttpServletRequest request,
			String userTelephone) {

		List<String> alluserTelephoneList = (List<String>) request.getSession()
				.getAttribute("alluserTelephoneList");
		boolean flag = false;
		/**
		 * contains判断alluserTelephoneList集合里面是否包含了userTelephone
		 */
		if (alluserTelephoneList.contains(userTelephone)) {
			final HttpSession session = request.getSession();
			session.setAttribute("userTelephone", userTelephone);
			flag = true;
		}
		String json = "{\"flag\":\"" + flag + "\",\"phone\":\"" + userTelephone
				+ "\"}";
		return json;

	}

	/**
	 * 根据电话号码修改密码
	 * 
	 * @param user
	 * @return 登录页
	 */
	@RequestMapping("/modifyUserPassword")
	public String modifyUserPassword(HttpServletRequest request, User user) {
		user.setTelephone((String) (request.getSession()
				.getAttribute("userTelephone")));
		userManager.modifyUserPassword(user);
		return "redirect:/index.jsp";
	}

	/**
	 * 安卓端根据电话号码修改密码
	 * 
	 * @param user
	 * @return 成功更改信息
	 */
	@RequestMapping("/modifyAndroidUserPassword")
	@ResponseBody
	public String modifyAndroidUserPassword(User user) {
		userManager.modifyUserPassword(user);
		return "success";
	}

	/**
	 * 身份证实名认证
	 * 
	 * @param idCard
	 *            身份证号
	 * @param realName
	 *            真实姓名
	 * @return 验证结果
	 */
	@RequestMapping("/IdCard")
	@ResponseBody
	public boolean getIdCard(User user, HttpServletRequest request) {
		boolean flag = false;
		String result = IDCardValidate.getIdCard(user.getIdCard().trim(), user
				.getTrueName().trim());
		String tString = result.substring(result.indexOf("msg"),
				result.indexOf("result"));
		tString = tString.substring(tString.indexOf(":") + 2,
				tString.indexOf(",") - 1);
		User user2 = (User) request.getSession().getAttribute("user");
		if (tString.equals("ok")) {
			user2.setTrueName(user.getTrueName());
			user2.setIdCard(user.getIdCard());
			userManager.modifyUserInfo(user2);
			HttpSession session = request.getSession();
			session.setAttribute("user", user2);
			flag = true;
		}
		return flag;

	}

	/**
	 * 进行安卓端的实名认证 认证通过后将信息插入数据库
	 * 
	 * @param user
	 *            用户信息
	 * @return 是否认证通过
	 */
	@RequestMapping("/androidByIdcard")
	@ResponseBody
	public String androidByIdcard(User user) {
		String msg = "no";
		String result = IDCardValidate.getIdCard(user.getIdCard().trim(), user
				.getTrueName().trim());
		String tString = result.substring(result.indexOf("msg"),
				result.indexOf("result"));
		msg = tString.substring(tString.indexOf(":") + 2,
				tString.indexOf(",") - 1);
		System.err.println(msg);
		if (msg.equals("ok")) {
			User user2 = userManager.queryUserById(user.getId());
			user2.setTrueName(user.getTrueName());
			user2.setIdCard(user.getIdCard());
			user2.setGender(user.getGender());
			user2.setSchool(user.getSchool());
			user2.setIntegral(50);
			userManager.modifyUserInfo(user2);
		}
		return msg;
	}

	@RequestMapping(value = "/getUserbyIdForAndroid", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getUserbyIdForAndroid(String id) {
		User user = userManager.queryUserById(id);
		StringBuffer json = new StringBuffer();
		json.append("{");
		json.append("\"nickName\":\"" + user.getNickName() + "\",");
		json.append("\"userName\":\"" + user.getUserName() + "\",");
		json.append("\"integral\":\"" + user.getIntegral() + "\",");
		json.append("\"password\":\"" + user.getPassword() + "\",");
		json.append("\"idCard\":\"" + user.getIdCard() + "\",");
		json.append("\"gender\":\"" + user.getGender() + "\",");
		json.append("\"imagePath\":\"" + user.getImagePath() + "\",");
		json.append("\"school\":\"" + user.getSchool() + "\",");
		json.append("\"trueName\":\"" + user.getTrueName() + "\"}");
		return json.toString();
	}

	/**
	 * 拦截到404错误时，跳转到错误提示页
	 * 
	 * @return
	 */
	@RequestMapping("/error404")
	public ModelAndView error404() {
		Map<String, Object> model = new HashMap<String, Object>();
		// 视图解析器将model中的每个元素都通过model.put(name, value);
		// 这样就可以在JSP页面中通过EL表达式来获取对应的值
		model.put("url", "index.jsp");
		model.put("message", "哦豁，你访问的页面不存在！");
		ModelAndView modelAndView = new ModelAndView("ui/jsp/commont/error",
				model);
		return modelAndView;
	}

	/**
	 * 安卓端修改密码
	 * 
	 * @param u
	 * @return
	 */
	@RequestMapping("/updatePassword")
	@ResponseBody
	public String updatePassword(User u) {
		User user = userManager.queryUserById(u.getId());
		user.setPassword(u.getPassword());
		userManager.modifyUserInfo(user);
		return "success";
	}
	/**
	 * 修改积分
	 * @param id
	 * @param grade
	 * @return
	 */
	@RequestMapping("/updateGrade")
	@ResponseBody
	public String updateGrade(String id,int grade) {
		User user=userManager.queryUserById(id);
		user.setIntegral(grade);
		userManager.modifyUserInfo(user);
		return "";
	}
	@RequestMapping("/IsAndroidForRegster")
	@ResponseBody
	public String IsAndroidForRegster(String phoneId) {
		String string="success";
		boolean flag=userManager.IsAndroidForRegster(phoneId);
		if (!flag) {
			string="error";
		}
		return string;
	}
}
