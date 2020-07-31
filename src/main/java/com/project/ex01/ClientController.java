package com.project.ex01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;

import kakao.KakaoLoginBO;
import naver.NaverLoginBO;
import service.ClientService;
import vo.ClientVO;

@Controller
public class ClientController {
		
	private NaverLoginBO naverLoginBO;
	private String apiResult = null;
		
	@Autowired
	ClientService service;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	GoogleConnectionFactory googleConnectionFactory;
	
	@Autowired
	OAuth2Parameters googleOAuth2Parameters;
	
	@Autowired
	private void setNaverLoginBO(NaverLoginBO naverLoginBO) {
		this.naverLoginBO = naverLoginBO;
	}
	
	@RequestMapping(value = "catloginf")
	public ModelAndView catloginf(ModelAndView mv, HttpSession session) {
		OAuth2Operations oauthOperations = googleConnectionFactory.getOAuthOperations();
		String googleurl = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);
		String naverurl = naverLoginBO.getAuthorizationUrl(session);
		String kakaourl = KakaoLoginBO.getAuthorizationUrl(session);
		
		mv.addObject("google_url", googleurl);
		mv.addObject("naver_url", naverurl);
		mv.addObject("kakao_url", kakaourl);
		mv.setViewName("cat/login/LoginForm");
		return mv;
	}
	
	@RequestMapping(value = "kakaologin", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
	public ModelAndView kakaologin(ModelAndView mv, @RequestParam String code, HttpSession session) throws ClientProtocolException, IOException {
		String email = null;
		String name = null;
		String id = null;
		
		JsonNode node = KakaoLoginBO.getAccessToken(code);
		JsonNode accessToken = node.get("access_token");
		JsonNode userInfo = KakaoLoginBO.getkakaoUserInfo(accessToken);
		
		System.out.println("userInfo => "+userInfo);
		
		JsonNode properties = userInfo.path("properties");
		JsonNode kakao_account = userInfo.path("kakao_account");
		
		System.out.println(properties);
		System.out.println(kakao_account);
		
		id = "kakao"+userInfo.path("id").asText();
		email = kakao_account.path("email").asText();
		name = properties.path("nickname").asText();
		System.out.println("id = "+id);
		System.out.println("email = "+email);
		System.out.println("name = "+name);
		
		ClientVO cv = new ClientVO();
		cv.setId(id);
		cv = service.selectOne(cv);

		if(cv != null) {
			session.setAttribute("logID", id);
			mv.setViewName("redirect: catmain");
		}else {
			mv.addObject("social_type", "kakao");
			mv.addObject("social_name", name);
			mv.addObject("social_email", email);
			mv.addObject("social_id", id);
			mv.setViewName("cat/join/JoinForm");
		}
				
		return mv;
	}
	
	@RequestMapping(value = "naverlogin", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView naverlogin(ModelAndView mv, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException, ParseException {
		OAuth2AccessToken oauthToken = naverLoginBO.getAccessToken(session, code, state);
		apiResult = naverLoginBO.getUserProfile(oauthToken);
		
		System.out.println(apiResult);
		
		String id = null;
		String name = null;
		String email = null;
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(apiResult);
		JSONObject jsonObj = (JSONObject) obj;
		
		JSONObject response_obj = (JSONObject)jsonObj.get("response");
		System.out.println(response_obj);
		
		if(response_obj.get("id") != null && response_obj.get("name") != null && response_obj.get("email") != null) {
			id = "naver"+(String)response_obj.get("id");
			name = (String)response_obj.get("name");
			email = (String)response_obj.get("email");
			
			ClientVO cv = new ClientVO();
			cv.setId(id);
			cv = service.selectOne(cv);

			if(cv != null) {
				session.setAttribute("logID", id);
				mv.setViewName("redirect: catmain");
			}else {
				mv.addObject("social_type", "naver");
				mv.addObject("social_name", name);
				mv.addObject("social_email", email);
				mv.addObject("social_id", id);
				mv.setViewName("cat/join/JoinForm");
			}
		}else {
			String reprompt = naverLoginBO.getAuthorizationUrl(session)+"&auth_type=reprompt";
			
			mv.setViewName("redirect: "+reprompt);
		}
		
		return mv;
	}
	
	@RequestMapping(value = "googlelogin", method = RequestMethod.GET)
	public ModelAndView googlelogin(HttpServletRequest request, ModelAndView mv, @RequestParam String code) throws MalformedURLException, IOException {
		System.out.println("googlecallback");
		
		String name = null;
		String email = null;
		String sub = null;
		
		OAuth2Operations oauthOperations = googleConnectionFactory.getOAuthOperations();
		AccessGrant accessGrant = oauthOperations.exchangeForAccess(code, googleOAuth2Parameters.getRedirectUri(), null);
		
		String accessToken = accessGrant.getAccessToken();
		Long expireTime = accessGrant.getExpireTime();
		
		if(expireTime != null && expireTime < System.currentTimeMillis()) {
			accessToken = accessGrant.getRefreshToken();
		}
				
		BufferedReader in = new BufferedReader(new InputStreamReader(
                ((HttpURLConnection) (new URL("https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken.trim()))
                .openConnection()).getInputStream(), Charset.forName("UTF-8")));
		
		StringBuffer b = new StringBuffer();
		String inputLine;
		while((inputLine = in.readLine()) != null) {
			b.append(inputLine+"\n");
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String,String> tokenPayload = objectMapper.readValue(b.toString(), objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
		
		if(tokenPayload.get("name") != null && tokenPayload.get("email") != null && tokenPayload.get("sub") != null) {
			name = tokenPayload.get("name");
			email = tokenPayload.get("email");
			sub = "google"+tokenPayload.get("sub");
		}
		
		ClientVO cv = new ClientVO();
		cv.setId(sub);
		cv = service.selectOne(cv);

		if(cv != null) {
			request.getSession().setAttribute("logID", sub);
			mv.setViewName("redirect: catmain");
		}else {
			mv.addObject("social_type", "google");
			mv.addObject("social_name", name);
			mv.addObject("social_email", email);
			mv.addObject("social_id", sub);
			mv.setViewName("cat/join/JoinForm");
		}
		
		return mv;
	}
			
	@RequestMapping(value = "termsuse")
	public ModelAndView termsuse(ModelAndView mv) {
		mv.setViewName("cat/join/TermsofUse");
		return mv;
	}
	@RequestMapping(value = "termsprivacy")
	public ModelAndView termsprivacy(ModelAndView mv) {
		mv.setViewName("cat/join/TermsofPrivacy");
		return mv;
	}
	@RequestMapping(value = "termslocation")
	public ModelAndView termslocation(ModelAndView mv) {
		mv.setViewName("cat/join/TermsofLocationInformation");
		return mv;
	}
	@RequestMapping(value = "logout")
	public ModelAndView logout(HttpServletRequest request, ModelAndView mv) {
		String id = (String)request.getSession().getAttribute("logID");
		if(id != null) {
			request.getSession().invalidate();
			mv.addObject("result", true);
		}else {
			mv.addObject("result", false);
		}
		mv.setViewName("jsonView");
		return mv;
	}
	@RequestMapping(value = "clientInfo")
	public ModelAndView clientInfo(HttpServletRequest request, ModelAndView mv, String code, ClientVO cv) {
		String id = (String)request.getSession().getAttribute("logID");
		cv.setId(id);
		cv = service.selectOne(cv);
		mv.addObject("cv", cv);
		if(code.equals("json")) {
			mv.setViewName("jsonView");
		}else {
			mv.setViewName("cat/login/Myinfo");
		}
		return mv;
	}
	@RequestMapping(value="delete")
	public ModelAndView delete(ModelAndView mv, HttpServletRequest request, ClientVO cv) {
		String id = "";
		HttpSession session = request.getSession(false);
		id = (String)session.getAttribute("logID");
		cv.setId(id);
		System.out.println(cv);
		if(service.delete(cv) > 0) {
			session.invalidate();
			mv.addObject("code",0);
		}else {
			mv.addObject("code",1);
		}
		mv.setViewName("jsonView");
		return mv;
	} // delete
	@RequestMapping(value = "updatef")
	public ModelAndView updatef(HttpServletRequest request, ModelAndView mv) {
		ClientVO cv = new ClientVO();
		String id = (String)request.getSession().getAttribute("logID");
		cv.setId(id);
		cv = service.selectOne(cv);
		mv.addObject("cv", cv);
		mv.setViewName("cat/login/MyinfoUpdate");
		return mv;
	}
	@RequestMapping(value = "update")
	public ModelAndView update(HttpServletRequest request, ModelAndView mv, ClientVO cv) {
		if(service.update(cv) > 0) {
			mv.addObject("code", 0);
		}else {
			mv.addObject("code", 1);
		}
		mv.setViewName("jsonView");
		return mv;
	}
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request, ModelAndView mv, ClientVO cv) {
		String password = cv.getPassword();
		cv = service.selectOne(cv);
		if(cv != null) {
			if(passwordEncoder.matches(password, cv.getPassword())) {
				request.getSession().setAttribute("logID", cv.getId());
				mv.addObject("code", 0);
			}else {
				mv.addObject("code", 1);
			}
		}else {
			mv.addObject("code", 2);
		}
		mv.setViewName("jsonView");
		return mv;
	}
	@RequestMapping(value = {"/","/home"})
	public ModelAndView home(ModelAndView mv) {
		mv.setViewName("Homepage");
		return mv;
	}
	@RequestMapping(value = "dogloginf")
	public ModelAndView dogloginf(ModelAndView mv) {
		mv.setViewName("dog/login/LoginForm");
		return mv;
	}
	
	@RequestMapping(value = "dogmain")
	public ModelAndView dogmain(ModelAndView mv) {
		mv.setViewName("dog/Dogmain");
		return mv;
	}
	@RequestMapping(value = "catmain")
	public ModelAndView catmain(ModelAndView mv) {
		mv.setViewName("cat/Catmain");
		return mv;
	}
	@RequestMapping(value="JoinTerms")
	public ModelAndView JoinTerms(ModelAndView mv) {
		mv.setViewName("cat/join/JoinTerms");
		return mv;
	}
	@RequestMapping(value="JoinForm")
	public ModelAndView JoinForm(ModelAndView mv) {
		mv.setViewName("cat/join/JoinForm");
		return mv;
	}
	@RequestMapping(value="join")
	public ModelAndView join(ModelAndView mv, ClientVO cv) {
		cv.setPassword(passwordEncoder.encode(cv.getPassword()));
		if (service.insert(cv) > 0) {
			mv.addObject("result",true);
		}else {
			mv.addObject("result",false);
		}
		mv.setViewName("jsonView");
		return mv;
	}
	@RequestMapping(value="selectOne",method=RequestMethod.POST)
	public ModelAndView selectOne(ModelAndView mv, ClientVO cv) {
		cv = service.selectOne(cv);
		if(cv!=null) {
			mv.addObject("result",false);
		}else {
			mv.addObject("result",true);
		}
		mv.setViewName("jsonView");
		return mv;
	}
	@RequestMapping(value = "juso")
	public ModelAndView juso(ModelAndView mv) {
		mv.setViewName("popup/jusoPopup");
		return mv;
	}
} // class