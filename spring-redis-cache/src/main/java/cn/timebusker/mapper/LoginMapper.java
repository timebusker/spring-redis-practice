package cn.timebusker.mapper;

import cn.timebusker.po.Staff;


public interface LoginMapper {
	Staff getpwdbyname(String name);
}