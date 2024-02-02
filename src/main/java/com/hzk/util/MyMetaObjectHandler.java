package com.hzk.util;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 自定义元数据对象处理器
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    //插入的时候，执行的
    @Override
    public void insertFill(MetaObject metaObject) {
        Long currentId = BaseContext.getCurrentId();
        metaObject.setValue("createTime",new Date());
        metaObject.setValue("updateTime",new Date());
        metaObject.setValue("createUser",currentId);
        metaObject.setValue("updateUser",currentId);

    }
    //更新的时候执行的
    @Override
    public void updateFill(MetaObject metaObject) {
        Long currentId = BaseContext.getCurrentId();
        metaObject.setValue("updateTime",new Date());
        metaObject.setValue("updateUser",currentId);
    }
}
