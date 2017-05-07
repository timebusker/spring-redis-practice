package cn.timebusker.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.timebusker.mapper.ActorMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

import cn.timebusker.po.Actor;
import cn.timebusker.poi.WriteExcel;
import cn.timebusker.service.ActorService;
@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=5)
@Service("actorservice")
public class ActorServiceImpl implements ActorService{
	@Autowired
	public ActorMapper actorMapper;
	
	@Cacheable("getActorByid")   //建立缓存  
	public Actor getActorByid(short id) {
		Actor a=actorMapper.getactorbyid(id);
		System.out.println("getActorByid方法执行，不走缓存");
		return a;
	}
	
	@CacheEvict(value = {"getActorByid","getpageActors"}, allEntries = true)  //清理缓存
	public void updateactor(Actor a) {
		actorMapper.updateActorbyid(a);
	}

	@Cacheable("getpageActors") 
	public List<Actor> getpageActors(int pagenum, int pagesize) {
		PageHelper.startPage(pagenum,pagesize);  
		List<Actor> l=actorMapper.getAllactors();
		System.out.println("执行方法体getpageActors，不走缓存");
		return l;
	}

	public int getactornum() {
		List<Actor> l=actorMapper.getAllactors();
		return l.size();
	}

	@CacheEvict(value = {"actorlist"}, allEntries = true)  //清理缓存
	public void addactor(Actor a) {
		actorMapper.insertActor(a);
	}

	@CacheEvict(value = {"getActorByid","actorlist"}, allEntries = true)  //清理缓存
	public void delete(short id) {
		actorMapper.delete(id);
	}

	public InputStream getInputStream() throws Exception {
		String[] title=new String[]{"id","first_name","last_name","last_update"};
		List<Actor> plist=actorMapper.getAllactors();
		List<Object[]>  dataList = new ArrayList<Object[]>();  
		for(int i=0;i<plist.size();i++){
			Object[] obj=new Object[4];
			obj[0]=plist.get(i).getId();
			obj[1]=plist.get(i).getFirst_name();
			obj[2]=plist.get(i).getLast_name();
			obj[3]=plist.get(i).getLast_update();
			dataList.add(obj);
		}
		WriteExcel ex = new WriteExcel(title, dataList);  
		InputStream in;
		in = ex.export();
		return in;
	}

}
