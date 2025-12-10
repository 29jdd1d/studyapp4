package com.kaoyan.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaoyan.common.constant.CommonConstants;
import com.kaoyan.common.exception.BusinessException;
import com.kaoyan.common.result.ResultCode;
import com.kaoyan.common.utils.JwtUtil;
import com.kaoyan.user.dto.UserUpdateRequest;
import com.kaoyan.user.dto.WxLoginRequest;
import com.kaoyan.user.entity.User;
import com.kaoyan.user.mapper.UserMapper;
import com.kaoyan.user.service.UserService;
import com.kaoyan.user.vo.LoginVO;
import com.kaoyan.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserMapper userMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Value("${wechat.appid}")
    private String appid;
    
    @Value("${wechat.secret}")
    private String secret;
    
    @Value("${wechat.login-url}")
    private String loginUrl;
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO wxLogin(WxLoginRequest request) {
        // 1. 调用微信接口获取 openid 和 session_key
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                loginUrl, appid, secret, request.getCode());
        
        String response = HttpUtil.get(url);
        log.info("微信登录响应: {}", response);
        
        JSONObject jsonObject = JSON.parseObject(response);
        
        if (jsonObject.containsKey("errcode")) {
            log.error("微信登录失败: {}", jsonObject.getString("errmsg"));
            throw new BusinessException(ResultCode.WX_LOGIN_ERROR);
        }
        
        String openid = jsonObject.getString("openid");
        String sessionKey = jsonObject.getString("session_key");
        String unionid = jsonObject.getString("unionid");
        
        // 2. 查询用户是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenid, openid);
        User user = userMapper.selectOne(queryWrapper);
        
        boolean isNewUser = false;
        
        // 3. 不存在则创建新用户
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setUnionid(unionid);
            user.setNickname(StringUtils.hasText(request.getNickname()) ? 
                    request.getNickname() : "考研er_" + System.currentTimeMillis());
            user.setAvatar(request.getAvatar());
            user.setVipStatus(0);
            user.setTotalStudyTime(0);
            user.setContinuousDays(0);
            userMapper.insert(user);
            isNewUser = true;
        } else {
            // 更新用户信息
            if (StringUtils.hasText(request.getNickname())) {
                user.setNickname(request.getNickname());
            }
            if (StringUtils.hasText(request.getAvatar())) {
                user.setAvatar(request.getAvatar());
            }
            userMapper.updateById(user);
        }
        
        // 4. 生成 JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put(CommonConstants.JWT_USER_ID, user.getId());
        String token = JwtUtil.generateToken(user.getId().toString(), claims, jwtExpiration, jwtSecret);
        
        // 5. 缓存用户信息到 Redis
        String redisKey = CommonConstants.REDIS_USER_PREFIX + user.getId();
        redisTemplate.opsForValue().set(redisKey, user, CommonConstants.CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
        
        // 6. 构建响应
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        return new LoginVO(token, userVO, isNewUser);
    }
    
    @Override
    public UserVO getUserInfo(Long userId) {
        // 先从缓存获取
        String redisKey = CommonConstants.REDIS_USER_PREFIX + userId;
        User user = (User) redisTemplate.opsForValue().get(redisKey);
        
        if (user == null) {
            // 缓存未命中，从数据库查询
            user = userMapper.selectById(userId);
            if (user == null) {
                throw new BusinessException(ResultCode.USER_NOT_EXIST);
            }
            // 更新缓存
            redisTemplate.opsForValue().set(redisKey, user, CommonConstants.CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
        }
        
        return BeanUtil.copyProperties(user, UserVO.class);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserInfo(Long userId, UserUpdateRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        
        // 更新用户信息
        BeanUtil.copyProperties(request, user, "id", "openid", "unionid");
        userMapper.updateById(user);
        
        // 删除缓存
        String redisKey = CommonConstants.REDIS_USER_PREFIX + userId;
        redisTemplate.delete(redisKey);
        
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addStudyTime(Long userId, Integer minutes) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        
        user.setTotalStudyTime(user.getTotalStudyTime() + minutes);
        userMapper.updateById(user);
        
        // 删除缓存
        String redisKey = CommonConstants.REDIS_USER_PREFIX + userId;
        redisTemplate.delete(redisKey);
        
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer checkIn(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        
        // 检查今天是否已签到
        String checkInKey = "studyapp4:checkin:" + userId + ":" + LocalDate.now();
        Boolean hasCheckedIn = redisTemplate.hasKey(checkInKey);
        
        if (Boolean.TRUE.equals(hasCheckedIn)) {
            return user.getContinuousDays();
        }
        
        // 检查昨天是否签到，判断是否连续
        String yesterdayKey = "studyapp4:checkin:" + userId + ":" + LocalDate.now().minusDays(1);
        Boolean checkedInYesterday = redisTemplate.hasKey(yesterdayKey);
        
        int continuousDays;
        if (Boolean.TRUE.equals(checkedInYesterday)) {
            continuousDays = user.getContinuousDays() + 1;
        } else {
            continuousDays = 1;
        }
        
        user.setContinuousDays(continuousDays);
        userMapper.updateById(user);
        
        // 记录今天签到
        redisTemplate.opsForValue().set(checkInKey, "1", 2, TimeUnit.DAYS);
        
        // 删除缓存
        String redisKey = CommonConstants.REDIS_USER_PREFIX + userId;
        redisTemplate.delete(redisKey);
        
        return continuousDays;
    }
}
