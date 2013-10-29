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

package org.icefaces.samples.showcase.example.compat.tree;

import java.io.Serializable;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.icesoft.faces.component.tree.IceUserObject;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.tree.title",
        description = "example.compat.tree.description",
        example = "/resources/examples/compat/tree/tree.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tree.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tree/tree.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TreeBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tree/TreeBean.java")
        }
)
@Menu(
	title = "menu.compat.tree.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.tree.subMenu.main",
                    isDefault = true,
                    exampleBeanName = TreeBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.tree.subMenu.events",
                    exampleBeanName = TreeEvents.BEAN_NAME),
            @MenuLink(title = "menu.compat.tree.subMenu.icons",
                    exampleBeanName = TreeIcons.BEAN_NAME),
            @MenuLink(title = "menu.compat.tree.subMenu.navigation",
                    exampleBeanName = TreeNavigation.BEAN_NAME),
            @MenuLink(title = "menu.compat.tree.subMenu.root",
                    exampleBeanName = TreeRoot.BEAN_NAME),
            @MenuLink(title = "menu.compat.tree.subMenu.dynamic",
                    exampleBeanName = TreeDynamic.BEAN_NAME)
})
@ManagedBean(name= TreeBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeBean extends ComponentExampleImpl<TreeBean> implements Serializable {
	
    public static final String BEAN_NAME = "tree";
    public static final String IMAGE_PATH = "/xmlhttp/css/rime/css-images/";
    public static final String IMAGE_ICON = "tree_document.gif";
    public static final String IMAGE_CLOSED = "tree_folder_closed.gif";
    public static final String IMAGE_OPEN = "tree_folder_open.gif";

    private static final Random randomizer = new Random(System.nanoTime());
    private DefaultTreeModel model;

    public TreeBean() 
    {
        super(TreeBean.class);
        model = generateRandomTree();
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public static DefaultTreeModel generateRandomTree() {
        return generateRandomTree(2, 3, 1, 5);
    }
    public void refreshTree(ActionEvent event) {
        model = generateRandomTree();
    }

    public static DefaultTreeModel generateRandomTree(int minFolder, int randFolder,
                              int minLeaf, int randLeaf) 
    {
        // Setup the root node
        DefaultMutableTreeNode rootNode = addNode(null, "Root", false);
        DefaultTreeModel toReturn = new DefaultTreeModel(rootNode);

        // Setup each folder and child
        DefaultMutableTreeNode folderNode = null;
        int numFolders = minFolder+randomizer.nextInt(randFolder);
        int numLeaf = -1;
        for (int i = 0; i < numFolders; i++) {
            folderNode = addNode(rootNode, "Folder " + (i+1), false);

            numLeaf = minLeaf+randomizer.nextInt(randLeaf);

            for (int j = 0; j < numLeaf; j++) {
                addNode(folderNode, "Item " + (i+1) + "-" + j, true);
            }
        }
        return toReturn;
    }

    public static DefaultMutableTreeNode addNodeToRoot(DefaultTreeModel model, String text, boolean isLeaf) {
        return addNode((DefaultMutableTreeNode)model.getRoot(), text, isLeaf);
    }

    public static DefaultMutableTreeNode addNode(DefaultMutableTreeNode parent, String text, boolean isLeaf) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
        IceUserObject userObject = new IceUserObject(node);
        node.setUserObject(userObject);

        // Customize our new node
        userObject.setLeaf(isLeaf);
        userObject.setText(text);
        userObject.setTooltip(text);
        node.setAllowsChildren(!isLeaf);

        // Set the image icons
        userObject.setLeafIcon(makeIcon(IMAGE_ICON));
        userObject.setBranchContractedIcon(makeIcon(IMAGE_CLOSED));
        userObject.setBranchExpandedIcon(makeIcon(IMAGE_OPEN));        

    // Expand if we're not a leaf
    if (!isLeaf) {
        userObject.setExpanded(true);
    }

    // Add the completed node to the parent
    if (parent != null) {
        parent.add(node);
    }

    return node;
    }

    public static String makeIcon(String image) {
        return IMAGE_PATH + image;
    }

    public DefaultTreeModel getModel() { return model; }
    public void setModel(DefaultTreeModel model) { this.model = model; }
}
