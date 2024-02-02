package com.hzk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzk.entity.AddressBook;
import com.hzk.service.AddressBookService;
import com.hzk.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author 86136
* @description 针对表【address_book】的数据库操作Service实现
* @createDate 2024-01-15 21:21:43
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




