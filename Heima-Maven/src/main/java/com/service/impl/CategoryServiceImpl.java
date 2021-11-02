package com.service.impl;

import com.domain.Category;
import com.mapper.CategoryMapper;
import com.mapper.UserMapper;
import com.service.CategoryService;
import com.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public List<Category> findAll() {
        // 为了方便获取数据，第一次从数据库查询出来数据之后，将数据储存在redis中，后面直接从redis中获取
        // 而且为了能够将cid和cname都存入redis中，需要用到zrangeWithScores方法
        Jedis jedis = JedisUtils.getJedis();
        // 获取数据,第一次没有，所以获取不到
        Set<Tuple> tuples = jedis.zrangeWithScores("category", 0, -1);
        // 声明一个集合，用于储存键值对
        List<Category> cs = null;
        if (tuples==null || tuples.size()==0){
            // 表示redis中没有数据，是第一次查询
            System.out.println("第一次，从数据库中查询");
            cs = categoryMapper.findAll();
            // 将集合中的数据存入到redis中，按照cid的顺序
            for (Category category:cs){
                jedis.zadd("category",Integer.parseInt(category.getCid()),category.getCname());
            }
        }else {
            // 表示不是第一次，从获取数据，cid和cname，存到对象中，再一一添加到集合中
            System.out.println("不是第一次，从redis缓存中获取");
            cs = new ArrayList<Category>();
            for (Tuple tuple:tuples){
                Category category = new Category();

                category.setCid(Integer.toString((int)tuple.getScore()));
                category.setCname(tuple.getElement());
                cs.add(category);
            }
        }
        //List<Category> categoryList = categoryMapper.findAll();
        return cs;
    }
}
