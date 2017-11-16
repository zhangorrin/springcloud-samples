package com.orrin.sca.common.service.file.service;

import com.orrin.sca.common.service.file.factory.ConnectionPoolFactory;
import org.csource.fastdfs.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author orrin.zhang on 2017/8/14.
 */
@Service("sastDSFService")
public class FastDSFService {

	@Autowired
	private ConnectionPoolFactory connectionPoolFactory;

	/**
	 * 上传
	 * 用于二进制文件的上传，便于统一调用接口
	 * @param fileByte	二进制文件
	 * @param fileName	文件名称
	 * @return
	 * @throws Exception
	 */
	public String upload(byte[] fileByte, String fileName) throws Exception{
		if(fileByte==null){
			throw new Exception("fileByte is null");
		}
		if(StringUtils.isEmpty(fileName)){
			throw new Exception("fileName is empty");
		}
		try {
			return connectionPoolFactory.getClient().upload_file1(fileByte, fileName,null);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	/**
	 * 下载
	 * 返回二进制流 供调用
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public byte[] download(String fileId) throws Exception{
		if(StringUtils.isEmpty(fileId)){
			throw new Exception("fileId is null");
		}
		byte[] fileByte = connectionPoolFactory.getClient().download_file1(fileId);
		return fileByte;
	}
	/**
	 * 删除接口
	 * @param fileId  服务器上的存储的文件id
	 * @return
	 * @throws Exception
	 */
	public int delete(String fileId) throws Exception{
		return connectionPoolFactory.getClient().delete_file1(fileId);
	}
	/**
	 *
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public FileInfo queryFileInfo(String fileId) throws Exception{
		return connectionPoolFactory.getClient().query_file_info1(fileId);
	}
}
