package cn.timebusker.mapper;

import cn.timebusker.po.City;
import cn.timebusker.po.Country;

import java.util.List;

public interface CityMapper {
	List<City> getCitys();

	List<City> getCountrycity(String countryname);//获取某国家城市列表

	Country getCitysbyCountry(String countryname);
}