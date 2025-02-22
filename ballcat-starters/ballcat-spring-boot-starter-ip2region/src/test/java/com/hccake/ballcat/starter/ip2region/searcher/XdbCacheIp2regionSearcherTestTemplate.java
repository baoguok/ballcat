package com.hccake.ballcat.starter.ip2region.searcher;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

/**
 * XDB文件缓存测试查询IP
 *
 * @author lishangbu
 * @date 2022/10/16
 */
@ActiveProfiles("xdb")
public class XdbCacheIp2regionSearcherTestTemplate extends Ip2regionSearcherTestTemplate {

	@Test
	public void testIpSearch() {
		super.testIpSearch();
	}

}
