package com.iscas.biz.service.mysql.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.biz.mapper.UserMapper;
import com.iscas.biz.model.User;
import com.iscas.biz.service.mysql.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-01-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
