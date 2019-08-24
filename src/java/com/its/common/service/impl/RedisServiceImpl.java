package com.its.common.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.its.common.service.RedisService;

import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 封装redis 缓存服务器服务接口
 * 
 * 
 */
@Service(value = "redisService")
public class RedisServiceImpl implements RedisService {

	@Resource(name="redisTemplate")
	private RedisTemplate<String,Object> redisTemplate;
	
	
	@Resource(name="redisTemplate")
	private HashOperations<String, String, Object> hashOperations;
	

    private ListOperations<String, Object> listOperations;

	private static String redisCode = "utf-8";

	@Override
	public long del(String... keys) {
		// TODO Auto-generated method stub
		for(String key : keys){
			redisTemplate.delete(key);
		}
		return 0;
	}

	@Override
	public void set(byte[] key, byte[] value, long liveTime) {
	}

	@Override
	public void set(String key, String value, long liveTime) {

	}

	@Override
	public void set(String key, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void set(byte[] key, byte[] value) {
		// TODO Auto-generated method stub

	}
	@Override
	public String get(String key) {
		return redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					return new String(connection.get(key.getBytes()), redisCode);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return "";
			}
		});

	}

	@Override
	public Set keys(String pattern) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String flushDB() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long dbSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String ping() {
		try {
            return redisTemplate.getConnectionFactory().getConnection().ping();
        } catch (Exception e) {
            System.out.println("RedisServiceImpl" + e.getMessage());
            return null;
        }
	}

	@Override
	public Map<String,Object> getHash(String key) {
		Map<String, Object> map = null;
        try {
            map = hashOperations.entries(key);
        } catch (Exception e) {
            System.out.println("RedisServiceImpl" + e.getMessage());
        }
		return map;
	}

	@Override
	public void setHash(String key, Map<String,Object> map) {
		try {
            hashOperations.putAll(key, map);
        } catch (Exception e) {
            System.out.println("RedisServiceImpl" + e.getMessage());
        }
	}

	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		list.add("aa");
		list.add("bb");
		System.out.println(StringUtils.join(list, ","));
	}

	@Override
	public List<Object> getHash(String key, List<String> fields) {
		try {
            return hashOperations.multiGet(key, fields);
        } catch (Exception e) {
            System.out.println("RedisServiceImpl" + e.getMessage());
            return null;
        }
	}

	@Override
	public Object getHash(String key, Object field) {
		try {
            return hashOperations.get(key, field);
        } catch (Exception e) {
            System.out.println("RedisServiceImpl" + e.getMessage());
            return null;
        }
	}

	@Override
	public void setHash(String key, List<String> list) {
		try {
            for(int i = 0; i < list.size(); i++){
            	listOperations.set(key, i, list.get(i));
            }
        } catch (Exception e) {
            System.out.println("RedisServiceImpl" + e.getMessage());
        }
	}
	@Override
	public boolean checkConnection(){
		try {
			redisTemplate.getConnectionFactory().getConnection();
		} catch (RedisConnectionFailureException e) {
			return false;
		} catch (JedisConnectionException e1) {
			return false;
		}
		return true;
	}

    @Override
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}
