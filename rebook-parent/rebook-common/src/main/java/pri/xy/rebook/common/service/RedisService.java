package pri.xy.rebook.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {
	
	/**
	 * 说明: 在工具类中需要将自动注入的属性添加 
	 * @Autowired(required=false)注解.添加该属性后,只有在
	 * 程序被调用时,对象才会自动注入
	 * 
	 * 原因:由于工具类可以被很多程序进行引用
	 * 某些项目不需要使用缓存,该对象不会实例化(不会添加redis的配置文件),
	 * 则启动必然报错
	 * 总结:工具类代码中依赖注入添加required=false
	 */
	/*@Autowired(required=false) //调用时才会自动注入
	private StringRedisTemplate redisTemplate;
	
	public void set(String key,String value){
		ValueOperations<String,String> 
		operations = redisTemplate.opsForValue();
		operations.set(key, value);
	}
	
	public String get(String key){
		ValueOperations<String,String> 
		operations = redisTemplate.opsForValue();
		
		System.out.println("单台的高级测试成功!!!!");
		return operations.get(key);
	}*/
	
	
	
	//实现分片的redis操作
	/*@Autowired
	private ShardedJedisPool shardedJedisPool;
	
	
	public void set(String key,String value){
		ShardedJedis jedis = shardedJedisPool.getResource();
		
		jedis.set(key, value);
		shardedJedisPool.returnResource(jedis);
	}
	
	public String get(String key){
		ShardedJedis jedis = shardedJedisPool.getResource();
		String json = jedis.get(key);
		shardedJedisPool.returnResource(jedis);
		System.out.println("分片的操作完成!!!!");
		return json;
	}*/
	
	
	//实现哨兵的操作
	@Autowired(required=false)
	public JedisSentinelPool jedisSentinelPool;
	
	public void set(String key,String value){
		
		Jedis jedis  = jedisSentinelPool.getResource();
		jedis.set(key, value);
		
		jedisSentinelPool.returnResourceObject(jedis);
	}
	
	public String get(String key){
		Jedis jedis = jedisSentinelPool.getResource();
		String json = jedis.get(key);
		jedisSentinelPool.returnResource(jedis);
		System.out.println("哨兵的配置成功!!!");
		return json;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
