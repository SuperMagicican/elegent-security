package cn.elegent.security.token.controller;

import cn.elegent.security.common.base.AuthenticateResult;
import cn.elegent.security.common.base.TokenDetails;
import cn.elegent.security.common.base.UserAuth;

import cn.elegent.security.token.core.AuthenticationManager;
import cn.elegent.security.token.core.SmsCodeService;
import cn.elegent.security.token.core.VerCodeService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;


    /**
     * 生成令牌
     * param userAuth
     * return
     */
    @PostMapping("/login")
    public AuthenticateResult createToken(@RequestBody UserAuth userAuth){
        return  authenticationManager.authenticate(userAuth);
    }

    /**
     * 刷新令牌
     * @param tokenDetails
     * @return
     */
    @PostMapping("/refresh/{type}")
    public TokenDetails refreshToken(@RequestBody TokenDetails tokenDetails,@PathVariable("type") String type){
        return authenticationManager.refresh(tokenDetails.getRefreshToken(),type);
    }

    @Autowired
    private DefaultKaptcha kaptcha;

    @Autowired
    private VerCodeService verCodeService;

    /**
     * 获取图片验证码
     * @param httpServletRequest
     * @param httpServletResponse
     */
    @GetMapping("/user/imageCode/{clientToken}")
    public void getImageCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable String clientToken) throws IOException {
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        String createText = kaptcha.createText();//生成随机字母+数字(4位)
        BufferedImage challenge = kaptcha.createImage(createText);//根据文本构建图片
        ImageIO.write(challenge, "jpg", jpegOutputStream);
        byte[] captchaChallengeAsJpeg  = jpegOutputStream.toByteArray();
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
        //保存验证码
        verCodeService.saveCode( clientToken, createText );
    }


    @Autowired
    private SmsCodeService smsCodeService;

    /**
     * 获取短信验证码
     */
    @GetMapping("/user/code/{mobile}")
    public String getSmsCode( @PathVariable String mobile) throws IOException {

        String regex = "^1[3-9]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mobile);

        if(!matcher.matches()){
            return "手机号格式非法!";
        }

        //随机生成数字
        StringBuilder sbCode = new StringBuilder();
        Stream
                .generate(()-> new Random().nextInt(10))
                .limit(5)
                .forEach(x-> sbCode.append(x));
        //保存验证码
        verCodeService.saveCode( mobile, sbCode.toString() );
        //发送验证码
        boolean b = smsCodeService.sendSmsCode(mobile, sbCode.toString());
        if(b){
            return "ok";
        }else{
            return "短信发送失败！";
        }

    }


}
