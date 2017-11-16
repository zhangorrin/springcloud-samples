package com.orrin.sca.common.service.file.factory;

import com.orrin.sca.common.service.file.properties.FastDFSProperties;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author orrin.zhang on 2017/8/14.
 */
public class ConnectionFactory extends BasePooledObjectFactory<StorageClient1> {

	private final static Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

	private FastDFSProperties properties;

	private TrackerGroup trackerGroup;

	private Map<StorageClient1,TrackerServer> storageClientTrackerServerMap = new HashMap<>();

	public ConnectionFactory(FastDFSProperties properties) {
		this.properties = properties;
		turnTrackerGroup();
		setToGlobal();
	}

	private void turnTrackerGroup(){
		InetSocketAddress[] tracker_servers_socket = new InetSocketAddress[properties.getTracker_server().size()];
		for (int i = 0; i < properties.getTracker_server().size(); i++) {
			String str = properties.getTracker_server().get(i);
			String[] parts = str.split("\\:", 2);
			if (parts.length != 2) {
				throw new RuntimeException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
			}

			tracker_servers_socket[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
		}

		trackerGroup = new TrackerGroup(tracker_servers_socket);
	}

	private void setToGlobal() {
		ClientGlobal.setG_connect_timeout(properties.getConnect_timeout());
		ClientGlobal.setG_network_timeout(properties.getNetwork_timeout());
		ClientGlobal.setG_charset(properties.getCharset());
		ClientGlobal.setG_tracker_http_port(properties.getHttp().getTracker_http_port());
		ClientGlobal.setG_anti_steal_token(properties.getHttp().getAnti_steal_token());
		ClientGlobal.setG_secret_key(properties.getHttp().getSecret_key());
		ClientGlobal.setG_tracker_group(trackerGroup);
	}

	@Override
	public StorageClient1 create() throws Exception {
		TrackerClient trackerClient = new TrackerClient(trackerGroup);
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageClient1 storageClient = new StorageClient1(trackerServer, null);
		storageClientTrackerServerMap.put(storageClient,trackerServer);

		return storageClient;
	}

	@Override
	public PooledObject<StorageClient1> wrap(StorageClient1 storageClient) {
		return new DefaultPooledObject<>(storageClient);
	}

	@Override
	public PooledObject<StorageClient1> makeObject() throws Exception {
		return wrap(create());
	}

	@Override
	public void destroyObject(PooledObject<StorageClient1> p) throws Exception {
		storageClientTrackerServerMap.get(p.getObject()).close();
	}
}
