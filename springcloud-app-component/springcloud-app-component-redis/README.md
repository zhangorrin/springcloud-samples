# 工作流使用方法
- - -
## 1. 配置
在application.yml当中配置相关参数
```
spring:
  redis:
    host: localhost
    port: 6379
    database: 5
```

## 2. 使用方法

### 2.1 原生使用方法
```java
        @Autowired
    	private RedisTemplate redisTemplate;
    
    	@Resource(name= "redisTemplate")
    	private ValueOperations<String, String> opsForValue;
    
    	@Resource(name= "redisTemplate")
    	private HashOperations<String,String,String> opsForHash;
    
    	@Resource(name= "redisTemplate")
    	private ListOperations<String, String> opsForList;
    
    	@Resource(name= "redisTemplate")
    	private SetOperations<String,String> opsForSet;
    
    	@Resource(name= "redisTemplate")
    	private ZSetOperations<String,String> opsForZSet;
    
    	@Test
    	public void test() throws Exception {
    
    		CreditTokenCurrentUser creditTokenCurrentUser = new CreditTokenCurrentUser();
    
    		creditTokenCurrentUser.setCompanyName("测试redis");
    		creditTokenCurrentUser.setComId("C88888");
    
    		String invoice3String = JSON.toJSONString(creditTokenCurrentUser);
    
    		Boolean putIfAbsent = opsForHash.putIfAbsent("WETRANSN:INVOICE:CID", "WE60000000CU", invoice3String);
    
    		System.out.println(putIfAbsent);
    		String invoice = opsForHash.get("WETRANSN:INVOICE:CID", "WE60000000CU");
    
    /*
    
    		System.out.println("*****************:"+redisTemplate.getExpire("WETRANSN:INVOICE:CID"));
    
    		redisTemplate.expire("WETRANSN:INVOICE:CID",1800, TimeUnit.SECONDS);
    
    		Thread.sleep(1000*5);
    
    		System.out.println("*****************:"+redisTemplate.getExpire("WETRANSN:INVOICE:CID"));
    */
    
    
    		CreditTokenCurrentUser parseObject = JSON.parseObject(invoice, CreditTokenCurrentUser.class);
    		System.out.println(parseObject.getCompanyName());
    
    		creditTokenCurrentUser.setCompanyName("测试redis2");
    		creditTokenCurrentUser.setComId("C888882");
    
    
    		Long leftPush = opsForList.leftPush("WETRANSN:TRUSER:TRID", invoice3String);
    		Long leftPush2 = opsForList.rightPush("WETRANSN:TRUSER:TRID", JSON.toJSONString(creditTokenCurrentUser));
    
    
    
    	}
    
    
    	@Test
    	public void testOpsForSet() throws Exception {
    		CreditTokenCurrentUser creditTokenCurrentUser = new CreditTokenCurrentUser();
    
    		creditTokenCurrentUser.setCompanyName("测试redis01");
    		creditTokenCurrentUser.setComId("C01");
    
    		String sysLanString = JSON.toJSONString(creditTokenCurrentUser);
    
    		CreditTokenCurrentUser creditTokenCurrentUser1 = new CreditTokenCurrentUser();
    
    		creditTokenCurrentUser1.setCompanyName("测试redis01");
    		creditTokenCurrentUser1.setComId("C01");
    
    		String sysLan1String = JSON.toJSONString(creditTokenCurrentUser1);
    
    
    		CreditTokenCurrentUser creditTokenCurrentUser2 = new CreditTokenCurrentUser();
    
    		creditTokenCurrentUser2.setCompanyName("测试redis02");
    		creditTokenCurrentUser2.setComId("C02");
    
    		String sysLan2String = JSON.toJSONString(creditTokenCurrentUser2);
    
    
    		CreditTokenCurrentUser creditTokenCurrentUser3 = new CreditTokenCurrentUser();
    
    		creditTokenCurrentUser3.setCompanyName("测试redis03");
    		creditTokenCurrentUser3.setComId("C03");
    
    		String sysLan3String = JSON.toJSONString(creditTokenCurrentUser3);
    
    
    
    
    		//如果不存在这个字符串，将字符串存入set集合，返回存入元素的个数；如果存在这个字符串就不操作，返回0；
    		Long add = opsForSet.add("WETRANSN:SYSLAN2", sysLan3String);
    		Long addmore = opsForSet.add("WETRANSN:SYSLAN", sysLanString, sysLan1String, sysLan2String,sysLan3String);
    
    		System.out.println(add);
    		System.out.println(addmore);
    
    		//列出key为"WETRANSN:SYSLAN"的所有set集合
    		Set<String> members = opsForSet.members("WETRANSN:SYSLAN");
    		System.out.println(members);
    
    		//随机取key为"WETRANSN:SYSLAN"的一个set元素
    		String randomMember = opsForSet.randomMember("WETRANSN:SYSLAN");
    		System.out.println(randomMember);
    
    		//随机取N次key为"WETRANSN:SYSLAN"，组成一个list集合，可以重复取出
    		List<String> randomMembers = opsForSet.randomMembers("WETRANSN:SYSLAN", 3);
    		System.out.println(randomMembers);
    		//随机取N次key为"WETRANSN:SYSLAN"，组成一个set集合，不可以重复取出
    		Set<String> distinctRandomMembers = opsForSet.distinctRandomMembers("WETRANSN:SYSLAN", 2);
    		System.out.println(distinctRandomMembers);
    
    
    		//sysLan1String字符串是否是key为"WETRANSN:SYSLAN"set集合的元素
    		Boolean member = opsForSet.isMember("WETRANSN:SYSLAN", sysLan1String);
    		System.out.println(member);
    		//将字符串sysLanString从key为"WETRANSN:SYSLAN"的集合，移动到key为"WETRANSN:SYSLAN2"的集合,返回是否移动成功
    //      Boolean move = opsForSet.move("WETRANSN:SYSLAN", sysLanString, "WETRANSN:SYSLAN2");
    //      System.out.println(move);
    		//删除key为"WETRANSN:SYSLAN"的元素sysLan1String、sysLan2String的字符串，返回删除的个数
    //      Long remove = opsForSet.remove("WETRANSN:SYSLAN", sysLan1String, sysLan2String);
    //      System.out.println(remove);
    		//从左侧依次删除元素
    //      String pop = opsForSet.pop("WETRANSN:SYSLAN");
    //      System.out.println(pop);
    		//返回集合长度
    		Long size = opsForSet.size("WETRANSN:SYSLAN");
    		System.out.println(size);
    
    		//difference(K key, K otherKey);
    		//比较key与otherKey的set集合，返回与otherKey集合不一样的set集合
    		Set<String> difference = opsForSet.difference("WETRANSN:SYSLAN","WETRANSN:SYSLAN2");
    		System.out.println(difference);
    
    		Set<String> difference2 = opsForSet.difference("WETRANSN:SYSLAN2","WETRANSN:SYSLAN");
    		System.out.println(difference2);
    
    		//differenceAndStore(K key, K otherKey, K destKey);
    		//比较key与otherKey的set集合，取出与otherKey的set集合不一样的set集合，并存入destKey的set集合中，返回存入个数
    		Long differenceAndStore = opsForSet.differenceAndStore("WETRANSN:SYSLAN","WETRANSN:SYSLAN2", "WETRANSN:SYSLAN3");
    		System.out.println(differenceAndStore);
    
    		//intersect(K key, K otherKey);
    		//比较key与otherKey的set集合，取出二者交集，返回set交集合
    		Set<String> intersect = opsForSet.intersect("WETRANSN:SYSLAN","WETRANSN:SYSLAN2");
    		System.out.println(intersect);
    
    		Set<String> intersect2 = opsForSet.intersect("WETRANSN:SYSLAN2","WETRANSN:SYSLAN");
    		System.out.println(intersect2);
    
    
    		//intersectAndStore(K key, K otherKey, K destKey);
    		//比较key与otherKey的set集合，取出二者交集，并存入destKey集合，返回null
    		Long intersectAndStore = opsForSet.intersectAndStore("WETRANSN:SYSLAN","WETRANSN:SYSLAN3", "WETRANSN:SYSLAN4");
    		System.out.println(intersectAndStore);
    
    		//union(K key, K otherKey)
    		//比较key与otherKey的set集合，取出二者并集，返回set并集合
    		Set<String> union = opsForSet.union("WETRANSN:SYSLAN","WETRANSN:SYSLAN2");
    		System.out.println(union);
    
    		//unionAndStore(K key, K otherKey, K destKey)
    		//比较key与otherKey的set集合，取出二者并集，并存入destKey集合，返回destKey集合个数
    		Long unionAndStore = opsForSet.unionAndStore("WETRANSN:SYSLAN", "WETRANSN:SYSLAN2", "WETRANSN:SYSLAN5");
    		System.out.println(unionAndStore);
    	}
```
缓存注解:
使用@EnableCaching(mode = AdviceMode.ASPECTJ),同时引入RedissonCacheConfig配置

### 2.2 Redisson使用方法

[详细使用方法](https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95)
