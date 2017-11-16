package com.orrin.sca.common.service.file.factory;

import com.orrin.sca.common.service.file.properties.FastDFSPoolProperties;
import com.orrin.sca.common.service.file.properties.FastDFSProperties;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.csource.fastdfs.StorageClient1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author orrin.zhang on 2017/8/14.
 */
public class ConnectionPoolFactory {

	private final static Logger logger = LoggerFactory.getLogger(ConnectionPoolFactory.class);

	private GenericObjectPool<StorageClient1> pool;

	private FastDFSPoolProperties poolProperties;

	private FastDFSProperties properties;

	public ConnectionPoolFactory(FastDFSProperties properties, FastDFSPoolProperties poolProperties) {
		this.properties = properties;
		this.poolProperties = poolProperties;

		pool = new GenericObjectPool<>(new ConnectionFactory(properties));
		setConfig();
	}


	public StorageClient1 getClient() throws Exception {
		return pool.borrowObject();
	}

	public void releaseConnection(StorageClient1 client) {
		pool.returnObject(client);

	}

	private void setConfig() {
		pool.setMaxTotal(this.poolProperties.maxTotal);
		pool.setMaxIdle(this.poolProperties.maxIdle);
		pool.setMinIdle(this.poolProperties.minIdle);
		pool.setTestOnBorrow(this.poolProperties.testOnBorrow);
		pool.setMaxWaitMillis(this.poolProperties.maxWait);
	}
}
