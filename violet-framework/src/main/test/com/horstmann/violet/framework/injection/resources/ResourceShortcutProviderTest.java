package com.horstmann.violet.framework.injection.resources;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Created by piter on 30.01.16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResourceShortcutProviderTest
{
    @Test
    public void test1GetInstance() throws Exception
    {
        Assert.assertNotNull(ResourceShortcutProvider.getInstance());
    }

    @Test
    public void test2AddShortcut() throws Exception
    {
        ResourceShortcutProvider.getInstance().addShortcut("New", "Ctrl-N");
        Assert.assertEquals(ResourceShortcutProvider.getInstance().getAllShortcuts().size(), 1);

        String entityTestOne = ResourceShortcutProvider.getInstance().getAllShortcuts().get("New");
        Assert.assertEquals(entityTestOne, "Ctrl-N");
    }

    @Test
    public void test3AddOnlyOneElementOfThisSameKey() throws Exception
    {
        ResourceShortcutProvider.getInstance().addShortcut("New", "Ctrl-A");
        Assert.assertEquals(ResourceShortcutProvider.getInstance().getAllShortcuts().size(), 1);
        String entityTestTwo = ResourceShortcutProvider.getInstance().getAllShortcuts().get("New");

        Assert.assertNotEquals(entityTestTwo, "Ctrl-A");
        Assert.assertEquals(entityTestTwo, "Ctrl-N");
    }

    @Test
    public void test4AddDiffrentElement() throws Exception
    {
        ResourceShortcutProvider.getInstance().addShortcut("Edit", "Ctrl-E");
        Assert.assertEquals(ResourceShortcutProvider.getInstance().getAllShortcuts().size(), 2);
        String entityTestThree = ResourceShortcutProvider.getInstance().getAllShortcuts().get("Edit");

        Assert.assertEquals(entityTestThree, "Ctrl-E");
    }
}