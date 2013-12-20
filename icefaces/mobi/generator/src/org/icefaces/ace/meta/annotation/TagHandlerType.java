/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.ace.meta.annotation;

public enum TagHandlerType {

	ATTRIBUTE_HANDLER,
	BEHAVIOR_HANDLER,
	COMPONENT_HANDLER,
	COMPOSITE_FACELET_HANDLER,
	CONVERTER_HANDLER,
	DELEGATING_META_TAG_HANDLER,
	FACELET_HANDLER,
	FACELETS_ATTACHED_OBJECT_HANDLER,
	FACET_HANDLER,
	META_TAG_HANDLER,
	TAG_HANDLER,
	TEXT_HANDLER,
	VALIDATOR_HANDLER,
	UNSET;
	
	public static final TagHandlerType DEFAULT = TagHandlerType.TAG_HANDLER;
}
