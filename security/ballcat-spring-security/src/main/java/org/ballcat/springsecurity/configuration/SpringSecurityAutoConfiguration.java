/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ballcat.springsecurity.configuration;

import org.ballcat.security.authorization.SecurityChecker;
import org.ballcat.security.configuration.BallcatSecurityAutoConfiguration;
import org.ballcat.springsecurity.authorization.SpringSecurityChecker;
import org.ballcat.springsecurity.component.CustomPermissionEvaluator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = BallcatSecurityAutoConfiguration.class)
public class SpringSecurityAutoConfiguration {

	/**
	 * 基于 spring security 的权限判断组件
	 * @return SpringSecurityChecker
	 */
	@Bean
	@ConditionalOnMissingBean
	public SecurityChecker springSecurityChecker() {
		return new SpringSecurityChecker();
	}

	/**
	 * 自定义的权限判断组件
	 * @return CustomPermissionEvaluator
	 */
	@Bean(name = "per")
	@ConditionalOnMissingBean(CustomPermissionEvaluator.class)
	public CustomPermissionEvaluator customPermissionEvaluator() {
		return new CustomPermissionEvaluator();
	}

}
