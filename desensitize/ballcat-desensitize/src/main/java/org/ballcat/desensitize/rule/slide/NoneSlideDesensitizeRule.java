/*
 * Copyright 2023-2024 the original author or authors.
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

package org.ballcat.desensitize.rule.slide;

import org.ballcat.desensitize.annotation.SimpleDesensitize;

/**
 * 无脱敏规则，用于搭配注解使用，当注解的规则类为 NoneSlideDesensitizeRule 时，使用注解的其他属性进行脱敏。
 *
 * @see SimpleDesensitize
 * @author Hccake
 * @since 2.0.0
 */
public class NoneSlideDesensitizeRule implements SlideDesensitizeRule {

	@Override
	public int leftPlainTextLen() {
		throw new UnsupportedOperationException("不支持的操作，请使用其他规则或指定属性");
	}

	@Override
	public int rightPlainTextLen() {
		throw new UnsupportedOperationException("不支持的操作，请使用其他规则或指定属性");
	}

	@Override
	public String maskString() {
		throw new UnsupportedOperationException("不支持的操作，请使用其他规则或指定属性");
	}

	@Override
	public boolean reverse() {
		throw new UnsupportedOperationException("不支持的操作，请使用其他规则或指定属性");
	}

}
