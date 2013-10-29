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

package org.icefaces.ace.generator.artifacts;

import org.icefaces.ace.generator.context.MetaContext;
import org.icefaces.ace.generator.context.TagHandlerContext;
import org.icefaces.ace.generator.context.ComponentContext;

public abstract class Artifact {
    private MetaContext metaContext;

    public Artifact(MetaContext metaContext) {
        this.metaContext = metaContext;
    }

    public MetaContext getMetaContext() {
        return metaContext;
    }

	public String getName() {
		return this.getClass().getSimpleName();
	}
	
	public abstract void build();
}
